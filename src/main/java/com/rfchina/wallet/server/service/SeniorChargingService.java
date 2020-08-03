package com.rfchina.wallet.server.service;

import com.rfchina.biztools.functional.DateIterator;
import com.rfchina.biztools.functional.MaxIdIterator;
import com.rfchina.mch.sdk.model.ChargingConfig;
import com.rfchina.platform.biztool.excel.ExcelBean;
import com.rfchina.platform.biztool.excel.ExcelFactory;
import com.rfchina.platform.biztools.CacheHashMap;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.model.GatewayLog;
import com.rfchina.wallet.domain.model.GatewayLogCriteria;
import com.rfchina.wallet.domain.model.StatCharging;
import com.rfchina.wallet.domain.model.StatChargingCriteria;
import com.rfchina.wallet.domain.model.StatChargingDetail;
import com.rfchina.wallet.domain.model.StatChargingDetail.StatChargingDetailBuilder;
import com.rfchina.wallet.domain.model.StatChargingDetailCriteria;
import com.rfchina.wallet.domain.model.StatChargingDetailCriteria.Criteria;
import com.rfchina.wallet.domain.model.WalletCompany;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletOrderCriteria;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.server.mapper.ext.BalanceTunnelDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.GatewayLogExtDao;
import com.rfchina.wallet.server.mapper.ext.StatChargingDetailExtDao;
import com.rfchina.wallet.server.mapper.ext.StatChargingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCompanyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.model.ext.ChargingVo;
import com.rfchina.wallet.server.model.ext.StatChargingDetailVo;
import com.rfchina.wallet.server.model.ext.SumOfFeeVo;
import com.rfchina.wallet.server.msic.EnumWallet.FeeConfigKey;
import com.rfchina.wallet.server.msic.EnumYunst.YunstMethodName;
import com.rfchina.wallet.server.msic.EnumYunst.YunstServiceName;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler.YunstMemberType;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private WalletPersonExtDao walletPersonDao;

	@Autowired
	private WalletCompanyExtDao walletCompanyDao;

	@Autowired
	@Qualifier(value = "feeMap")
	private CacheHashMap<String, ChargingConfig> feeMap;

	/**
	 * 计算报表
	 */
	public void doCharging(TunnelType tunnelType, Date date) {
		Date firstDay = DateUtil.getDate2(DateUtil.getFirstDayOfMonth(date));
		Date lastDay = DateUtil.getDate(DateUtil.getLastDayOfMonth(date));
		// 删除历史数据
		statChargingDao.deleteByTimeRange(TunnelType.YUNST.getValue(), firstDay, lastDay);
		// 个人验证次数
		SumOfFeeVo sumOfPersonVerify = statChargingDetailDao
			.sumOfFeeByMethod(tunnelType.getValue(), firstDay, lastDay,
				YunstServiceName.MEMBER.getValue(),
				Arrays.asList(YunstMethodName.PERSON_VERIFY.getValue()));
		// 公司验证次数
		SumOfFeeVo sumOfCompanyVerify = statChargingDetailDao
			.sumOfFeeByMethod(tunnelType.getValue(), firstDay, lastDay,
				YunstServiceName.MEMBER.getValue(),
				Arrays.asList(YunstMethodName.COMPANY_VERIFY.getValue()));
		// 提现次数
		SumOfFeeVo sumOfWithdraw = statChargingDetailDao
			.sumOfFeeByMethod(tunnelType.getValue(), firstDay, lastDay,
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
				Arrays
					.asList(YunstMethodName.COLLECT.getValue(), YunstMethodName.CONSUME.getValue(),
						YunstMethodName.REFUND.getValue()));
		StatCharging statCharging = StatCharging.builder()
			.tunnelType(tunnelType.getValue())
			.chargingDate(firstDay)
			.localPayFee(sumOfPay != null ? sumOfPay.getLocalTunnelFee() : null)
			.thirdPayFee(sumOfPay != null ? sumOfPay.getThirdTunnelFee() : null)
			.localRechargeFee(sumOfRecharge != null ? sumOfRecharge.getLocalTunnelFee() : null)
			.thirdRechargeFee(sumOfRecharge != null ? sumOfRecharge.getThirdTunnelFee() : null)
			.withdrawCount(sumOfWithdraw.getLocalTunnelCount())
			.withdrawFee(sumOfWithdraw.getLocalTunnelFee())
			.personVerifyCount(sumOfPersonVerify.getLocalTunnelCount())
			.personVerifyFee(sumOfPersonVerify.getLocalTunnelFee())
			.companyVerifyCount(sumOfCompanyVerify.getLocalTunnelCount())
			.companyVerifyFee(sumOfCompanyVerify.getLocalTunnelFee())
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

				String key = "";
				String name = null;

				WalletTunnel tunnel = walletTunnelDao.selectByTunnelTypeAndBizUserId(
					gatewayLog.getTunnelType().intValue(), gatewayLog.getBizUserId());
				Long walletId = tunnel.getWalletId();
				String bizUserId = tunnel.getBizUserId();
				if (YunstMethodName.PERSON_VERIFY.getValue().equals(gatewayLog.getMethodName())) {
					key = FeeConfigKey.YUNST_PERSON_AUDIT.getValue();
					WalletPerson walletPerson = walletPersonDao
						.selectByWalletId(tunnel.getWalletId());
					name = walletPerson.getName();

				} else if (YunstMethodName.COMPANY_VERIFY.getValue()
					.equals(gatewayLog.getMethodName())) {
					key = FeeConfigKey.YUNST_COMPANY_AUDIT.getValue();
					WalletCompany walletCompany = walletCompanyDao
						.selectByWalletId(tunnel.getWalletId());
					name = walletCompany.getCompanyName();
				}
				ChargingConfig config = feeMap.get(key);
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
						.localTunnelFee(config != null ? config.getChargingValue().longValue() : 0L)
						.chargingType(config != null ? config.getType().byteValue() : null)
						.chargingValue(config != null ? config.getChargingValue() : BigDecimal.ZERO)
						.bizTime(gatewayLog.getInvokeTime())
						.name(name)
						.walletId(walletId)
						.bizUserId(bizUserId)
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
						OrderType.REFUND.getValue(), OrderType.CONSUME.getValue()));
				List<WalletOrder> walletOrders = walletOrderDao
					.selectByExampleWithRowbounds(example, new RowBounds(0, 300));
				return walletOrders;
			}, (walletOrder) -> {

				WalletTunnel tunnel = walletTunnelDao
					.selectByWalletId(walletOrder.getWalletId(), walletOrder.getTunnelType());
				ChargingVo chargingVo = BeanUtil.newInstance(walletOrder, ChargingVo.class);
				StatChargingDetailBuilder builder = StatChargingDetail.builder();
				builder
					.tunnelType(tunnelType.getValue())
					.serviceName(YunstServiceName.ORDER_SERVICE.getValue())
					.methodName(chargingVo.getMethodName())
					.orderNo(chargingVo.getOrderNo())
					.bizNo(chargingVo.getBizNo())
					.localTunnelFee(chargingVo.getTunnelFee())
					.chargingType(chargingVo.getChargingType())
					.chargingValue(chargingVo.getChargingValue())
					.tunnelCount(chargingVo.getTunnelCount())
					.bizTime(chargingVo.getTunnelSuccTime())
					.walletId(tunnel.getWalletId())
					.bizUserId(tunnel.getBizUserId())
					.build();

				if (YunstMemberType.COMPANY.getValue().longValue() == tunnel.getMemberType()) {
					WalletCompany walletCompany = walletCompanyDao
						.selectByWalletId(tunnel.getWalletId());
					builder.name(walletCompany.getCompanyName());
				} else if (YunstMemberType.PERSON.getValue().longValue() == tunnel
					.getMemberType()) {
					WalletPerson walletPerson = walletPersonDao
						.selectByWalletId(tunnel.getWalletId());
					builder.name(walletPerson.getName());
				}

				statChargingDetailDao.insertSelective(builder.build());
				return walletOrder.getId();
			});

		statChargingDetailDao.updateTunnelDetail(firstDay, lastDay);

	}


	public Pagination<StatCharging> queryCharging(Integer limit, Integer offset, Boolean stat) {
		StatChargingCriteria example = new StatChargingCriteria();
		example.setOrderByClause("id desc");
		example.createCriteria()
			.andDeletedEqualTo((byte) 0)
			.andChargingDateLessThan(DateUtil.getDate2(DateUtil.getFirstDayOfMonth(new Date())));
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

	public StatCharging queryChargingByCurrentMonth() {
		// 个人验证次数
		long countOfPersonVerify = cacheService
			.getStatisticsYunstVerify(YunstMethodName.PERSON_VERIFY.getValue());
		// 公司验证次数
		long countOfCompanyVerify = cacheService
			.getStatisticsYunstVerify(YunstMethodName.COMPANY_VERIFY.getValue());
		// 提现次数
		AtomicLong countOfWithdraw = new AtomicLong(0L);
		// 充值手续费
		AtomicLong sumOfRecharge = new AtomicLong(0L);
		// 支付手续费
		AtomicLong sumOfPay = new AtomicLong(0L);

		Date lastDay = new Date();
		Date firstDay = DateUtil.getDate2(DateUtil.getFirstDayOfMonth(lastDay));

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
					sumOfRecharge.set(sumOfRecharge.get() + tunnelFee);
				} else if (OrderType.WITHDRAWAL.getValue().equals(walletOrder.getType())) {
					countOfWithdraw.getAndIncrement();
				} else if (OrderType.COLLECT.getValue().equals(walletOrder.getType())) {
					sumOfPay.set(sumOfPay.get() + tunnelFee);
				} else if (OrderType.REFUND.getValue().equals(walletOrder.getType())) {
					sumOfPay.set(sumOfPay.get() + tunnelFee);
				}

				return walletOrder.getId();
			});
		StatCharging statCharging = StatCharging.builder()
			.tunnelType(TunnelType.YUNST.getValue())
			.chargingDate(firstDay)
			.localPayFee(sumOfPay.get())
			.thirdPayFee(null)
			.localRechargeFee(sumOfRecharge.get())
			.thirdRechargeFee(null)
			.withdrawCount(countOfWithdraw.get())
			.personVerifyCount(countOfPersonVerify)
			.companyVerifyCount(countOfCompanyVerify)
			.build();

		return statCharging;
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


	public byte[] exportChargingDetail(String fileName, Date startTime, Date endTime,List<String> methods) {

		ExcelBean excelBean = ExcelFactory.build2007();
		Sheet sheet = excelBean.creatSheet(fileName);
		excelBean.addTitle(sheet, 0, StatChargingDetailVo.class);

		AtomicInteger cursor = new AtomicInteger(1);
		new MaxIdIterator<StatChargingDetailVo>().apply((maxId) -> {
			try {
				Thread.sleep(20L);
			} catch (Exception e) {
				log.error("", e);
			}
			StatChargingDetailCriteria example = new StatChargingDetailCriteria();
			example.setOrderByClause("id asc");
			Criteria criteria = example.createCriteria();
			if(methods!= null && !methods.isEmpty()){
				criteria.andMethodNameIn(methods);
			}
			criteria
				.andBizTimeBetween(startTime, endTime)
				.andDeletedEqualTo((byte) 0)
				.andIdGreaterThan(maxId);
			List<StatChargingDetail> data = statChargingDetailDao
				.selectByExampleWithRowbounds(example, new RowBounds(0, 1000));
			return data.stream()
				.map(item -> BeanUtil.newInstance(item, StatChargingDetailVo.class))
				.collect(Collectors.toList());
		}, (row) -> {
			excelBean.addData(sheet, cursor.getAndIncrement(), Arrays.asList(row));
			return row.getId();
		});

		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			excelBean.getWorkbook().write(byteOut);

			return byteOut.toByteArray();
		} catch (Exception e) {
			return new byte[0];
		}

	}


}
