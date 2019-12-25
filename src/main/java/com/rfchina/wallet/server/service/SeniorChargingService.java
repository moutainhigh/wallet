package com.rfchina.wallet.server.service;

import com.rfchina.biztools.functional.DateIterator;
import com.rfchina.biztools.functional.MaxIdIterator;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.model.GatewayLog;
import com.rfchina.wallet.domain.model.GatewayLogCriteria;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.domain.model.StatChargingCriteria;
import com.rfchina.wallet.domain.model.StatChargingDetail;
import com.rfchina.wallet.domain.model.StatChargingDetailCriteria;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletOrderCriteria;
import com.rfchina.wallet.server.mapper.ext.BalanceTunnelDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.GatewayLogExtDao;
import com.rfchina.wallet.server.mapper.ext.StatChargingDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.StatChargingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.ChargingVo;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import com.rfchina.wallet.server.model.ext.SumOfFeeVo;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;
import com.rfchina.wallet.server.msic.EnumYunst.YunstMethodName;
import com.rfchina.wallet.server.msic.EnumYunst.YunstServiceName;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeniorChargingService {

	@Autowired
	private GatewayLogExtDao gatewayLogExtDao;

	@Autowired
	private StatChargingExtDao statChargingDao;

	@Autowired
	private StatChargingDetailExtDao statChargingDetailDao;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private BalanceTunnelDetailExtDao balanceTunnelDetailDao;

	@Autowired
	private CacheService cacheService;

	/**
	 * 计算报表
	 */
	public void doCharging(TunnelType tunnelType, Date date) {
		Date firstDay = DateUtil.getDate2(DateUtil.getFirstDayOfMonth(date));
		Date lastDay = DateUtil.getDate(DateUtil.getLastDayOfMonth(date));
		// 删除历史数据
		statChargingDao.deleteByTimeRange(TunnelType.YUNST.getValue(), firstDay, lastDay);
		// 个人验证次数
		long countOfPersonVerify = statChargingDetailDao
			.sumOfCountByMethod(tunnelType.getValue(), firstDay, lastDay,
				YunstServiceName.MEMBER.getValue(),
				Arrays.asList(YunstMethodName.PERSON_VERIFY.getValue()));
		// 公司验证次数
		long countOfCompanyVerify = statChargingDetailDao
			.sumOfCountByMethod(tunnelType.getValue(), firstDay, lastDay,
				YunstServiceName.MEMBER.getValue(),
				Arrays.asList(YunstMethodName.COMPANY_VERIFY.getValue()));
		// 提现次数
		long countOfWithdraw = statChargingDetailDao
			.sumOfCountByMethod(tunnelType.getValue(), firstDay, lastDay,
				YunstServiceName.ORDER_SERVICE.getValue(),
				Arrays.asList(YunstMethodName.WITHDRAW.getValue()));
		// 充值手续费
		SumOfFeeVo sumOfRecharge = statChargingDetailDao
			.sumOfFeeByMethod(tunnelType.getValue(), firstDay, lastDay,
				YunstServiceName.ORDER_SERVICE.getValue(),
				Arrays.asList(YunstMethodName.RECHARGE.getValue()));
		// 支付手续费
		SumOfFeeVo sumOfPay = statChargingDetailDao
			.sumOfFeeByMethod(tunnelType.getValue(), firstDay, lastDay,
				YunstServiceName.ORDER_SERVICE.getValue(),
				Arrays.asList(YunstMethodName.COLLECT.getValue(),
					YunstMethodName.REFUND.getValue()));
		StatCharging statCharging = StatCharging.builder()
			.tunnelType(tunnelType.getValue())
			.chargingDate(firstDay)
			.localPayFee(sumOfPay != null ? sumOfPay.getLocalTunnelFee() : null)
			.thirdPayFee(sumOfPay != null ? sumOfPay.getThirdTunnelFee() : null)
			.localRechargeFee(sumOfRecharge != null ? sumOfRecharge.getLocalTunnelFee() : null)
			.thirdRechargeFee(sumOfRecharge != null ? sumOfRecharge.getThirdTunnelFee() : null)
			.withdrawCount(countOfWithdraw)
			.personVerifyCount(countOfPersonVerify)
			.companyVerifyCount(countOfCompanyVerify)
			.build();
		statChargingDao.insertSelective(statCharging);
	}

	/**
	 * 提取数据
	 */
	public void doExtract(TunnelType tunnelType, Date date) {
		Date firstDay = DateUtil.getDate2(DateUtil.getFirstDayOfMonth(date));
		Date lastDay = DateUtil.getDate(DateUtil.getLastDayOfMonth(date));

		// 删除历史数据
		statChargingDetailDao.deleteByTimeRange(TunnelType.YUNST.getValue(), firstDay, lastDay);
		// 提取身份验证数据
		new MaxIdIterator<GatewayLog>()
			.apply((maxId) -> {
				GatewayLogCriteria example = new GatewayLogCriteria();
				example.setOrderByClause("id asc");
				example.createCriteria()
					.andIdGreaterThan(maxId)
					.andTunnelTypeEqualTo(tunnelType.getValue())
					.andInvokeTimeBetween(firstDay, lastDay)
					.andServiceNameEqualTo(YunstServiceName.MEMBER.getValue())
					.andMethodNameIn(Arrays.asList(YunstMethodName.PERSON_VERIFY.getValue(),
						YunstMethodName.COMPANY_VERIFY.getValue()));
				List<GatewayLog> gatewayLogs = gatewayLogExtDao
					.selectByExampleWithRowbounds(example, new RowBounds(0, 300));
				return gatewayLogs;
			}, (gatewayLog) -> {
				// 个人认证按调成功次数收费,企业认证按系统自动审核次数收费
				if (YunstMethodName.PERSON_VERIFY.getValue().equals(gatewayLog.getMethodName())
					||
					YunstMethodName.COMPANY_VERIFY.getValue().equals(gatewayLog.getMethodName())
						&& gatewayLog.getIsAuth() == (byte) 1) {
					StatChargingDetail detail = StatChargingDetail.builder()
						.tunnelType(tunnelType.getValue())
						.serviceName(gatewayLog.getServiceName())
						.methodName(gatewayLog.getMethodName())
						.tunnelCount(1L)
						.bizTime(gatewayLog.getInvokeTime())
						.build();
					statChargingDetailDao.insertSelective(detail);
				}
				return gatewayLog.getId();
			});

		// 提取订单数据
		new MaxIdIterator<WalletOrder>()
			.apply((maxId) -> {
				WalletOrderCriteria example = new WalletOrderCriteria();
				example.setOrderByClause("id asc");
				example.createCriteria()
					.andIdGreaterThan(maxId)
					.andTunnelTypeEqualTo(tunnelType.getValue())
					.andStatusEqualTo(OrderStatus.SUCC.getValue())
					.andTunnelSuccTimeBetween(firstDay, lastDay)
					.andTypeIn(Arrays.asList(OrderType.RECHARGE.getValue(),
						OrderType.WITHDRAWAL.getValue(), OrderType.COLLECT.getValue(),
						OrderType.REFUND.getValue()));
				List<WalletOrder> walletOrders = walletOrderDao
					.selectByExampleWithRowbounds(example, new RowBounds(0, 300));
				return walletOrders;
			}, (walletOrder) -> {
				ChargingVo chargingVo = BeanUtil.newInstance(walletOrder, ChargingVo.class);
				StatChargingDetail detail = StatChargingDetail.builder()
					.tunnelType(tunnelType.getValue())
					.serviceName(YunstServiceName.ORDER_SERVICE.getValue())
					.methodName(chargingVo.getMethodName())
					.orderNo(chargingVo.getOrderNo())
					.bizNo(chargingVo.getBizNo())
					.localTunnelFee(chargingVo.getTunnelFee())
					.tunnelCount(chargingVo.getTunnelCount())
					.bizTime(chargingVo.getTunnelSuccTime())
					.build();
				statChargingDetailDao.insertSelective(detail);
				return walletOrder.getId();
			});

		statChargingDetailDao.updateTunnelDetail(firstDay, lastDay);

	}


	public Pagination<StatCharging> queryCharging(Integer limit, Integer offset, Boolean stat) {
		StatChargingCriteria example = new StatChargingCriteria();
		example.setOrderByClause("id desc");
		example.createCriteria()
			.andDeletedEqualTo((byte) 0)
			.andChargingDateLessThan(DateUtil.getFirstDayOfMonth(new Date()));
		List<StatCharging> data = statChargingDao
			.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
		Long total = 0L;
		if (stat) {
			total = statChargingDao.countByExample(example);
		}
		return new Pagination.PaginationBuilder<StatCharging>()
			.total(total)
			.data(data)
			.offset(offset)
			.pageLimit(limit)
			.build();

	}

	public StatCharging queryChargingByCurrentMonth(){
		// 个人验证次数
		long countOfPersonVerify = cacheService.getStatisticsYunstVerify(YunstMethodName.PERSON_VERIFY.getValue());
		// 公司验证次数
		long countOfCompanyVerify = cacheService.getStatisticsYunstVerify(YunstMethodName.COMPANY_VERIFY.getValue());
		// 提现次数
		AtomicLong countOfWithdraw = new AtomicLong(0L);
		// 充值手续费
		SumOfFeeVo sumOfRecharge = new SumOfFeeVo();
		// 支付手续费
		SumOfFeeVo sumOfPay = new SumOfFeeVo();

		Date lastDay = new Date();
		Date firstDay = DateUtil.getFirstDayOfMonth(lastDay);
		new MaxIdIterator<WalletOrder>()
			.apply((maxId) -> {
				WalletOrderCriteria example = new WalletOrderCriteria();
				example.setOrderByClause("id asc");
				example.createCriteria()
					.andIdGreaterThan(maxId)
					.andTunnelTypeEqualTo(TunnelType.YUNST.getValue())
					.andStatusEqualTo(OrderStatus.SUCC.getValue())
					.andTunnelSuccTimeBetween(firstDay, lastDay)
					.andTypeIn(Arrays.asList(OrderType.RECHARGE.getValue(),
						OrderType.WITHDRAWAL.getValue(), OrderType.COLLECT.getValue(),
						OrderType.REFUND.getValue()));
				List<WalletOrder> walletOrders = walletOrderDao
					.selectByExampleWithRowbounds(example, new RowBounds(0, 300));
				return walletOrders;
			}, (walletOrder) -> {
				long tunnelFee = Optional.ofNullable(walletOrder.getTunnelFee()).orElse(0L);
				if (OrderType.RECHARGE.getValue().equals(walletOrder.getType())) {
					long sumOfRechargeTunnelFee = Optional.ofNullable(sumOfRecharge.getLocalTunnelFee()).orElse(0L);
					sumOfRecharge.setLocalTunnelFee(sumOfRechargeTunnelFee + tunnelFee);
				} else if (OrderType.WITHDRAWAL.getValue().equals(walletOrder.getType())) {
					countOfWithdraw.getAndIncrement();
				} else if (OrderType.COLLECT.getValue().equals(walletOrder.getType())) {
					long sumOfPayTunnelFee = Optional.ofNullable(sumOfPay.getLocalTunnelFee()).orElse(0L);
					sumOfPay.setLocalTunnelFee(sumOfPayTunnelFee + tunnelFee);
				}  else if (OrderType.REFUND.getValue().equals(walletOrder.getType())) {
					long sumOfPayTunnelFee = Optional.ofNullable(sumOfPay.getLocalTunnelFee()).orElse(0L);
					sumOfPay.setLocalTunnelFee(sumOfPayTunnelFee + tunnelFee);
				}

				return walletOrder.getId();
			});
		StatCharging statCharging = StatCharging.builder()
			.tunnelType(TunnelType.YUNST.getValue())
			.chargingDate(firstDay)
			.localPayFee(sumOfPay != null ? sumOfPay.getLocalTunnelFee() : null)
			.thirdPayFee(sumOfPay != null ? sumOfPay.getThirdTunnelFee() : null)
			.localRechargeFee(sumOfRecharge != null ? sumOfRecharge.getLocalTunnelFee() : null)
			.thirdRechargeFee(sumOfRecharge != null ? sumOfRecharge.getThirdTunnelFee() : null)
			.withdrawCount(countOfWithdraw.get())
			.personVerifyCount(countOfPersonVerify)
			.companyVerifyCount(countOfCompanyVerify)
			.build();

		return statCharging;
	}

	public StatCharging queryChargingByDate(Date date) {
		StatChargingCriteria example = new StatChargingCriteria();
		example.createCriteria()
			.andChargingDateEqualTo(date);
		List<StatCharging> data = statChargingDao.selectByExample(example);

		return (Objects.nonNull(data) && !data.isEmpty()) ? data.get(0) : new StatCharging();
	}

	public Pagination<StatChargingDetailVo> queryChargingDetail(Date startTime, Date endTime,
		Integer limit, Integer offset, Boolean stat, Boolean asc) {
		StatChargingDetailCriteria example = new StatChargingDetailCriteria();
		example.setOrderByClause(asc ? "id asc" : "id desc");
		example.createCriteria()
			.andBizTimeBetween(startTime, endTime)
			.andDeletedEqualTo((byte) 0);
		List<StatChargingDetail> data = statChargingDetailDao
			.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
		List<StatChargingDetailVo> list = data.stream()
			.map(item -> BeanUtil.newInstance(item, StatChargingDetailVo.class))
			.collect(Collectors.toList());
		Long total = 0L;
		if (stat) {
			total = statChargingDetailDao.countByExample(example);
		}
		return new Pagination.PaginationBuilder<StatChargingDetailVo>()
			.total(total)
			.data(list)
			.offset(offset)
			.pageLimit(limit)
			.build();
	}

	public void chargingRedo(Date startTime, Date endTime) {
		DateIterator dateIterator = new DateIterator(startTime, endTime);
		dateIterator.apply((theDay) -> {

			doExtract(TunnelType.YUNST, theDay);
			doCharging(TunnelType.YUNST, theDay);
		});
	}
}
