package com.rfchina.wallet.server.api.impl;

import com.rfchina.app.model.App;
import com.rfchina.biztools.functional.MaxIdIterator;
import com.rfchina.biztools.functionnal.LockDone;
import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef.DirtyType;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletTunnelAuditStatus;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletConfig;
import com.rfchina.wallet.domain.model.WalletConfigCriteria;
import com.rfchina.wallet.domain.model.WalletConfigCriteria.Criteria;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletTunnelCriteria;
import com.rfchina.wallet.server.api.ScheduleApi;
import com.rfchina.wallet.server.mapper.ext.WalletConfigExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.AutoWithdrawStatus;
import com.rfchina.wallet.server.msic.EnumWallet.LockStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletConfigStatus;
import com.rfchina.wallet.server.msic.RedisConstant;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.ScheduleService;
import com.rfchina.wallet.server.service.SeniorBalanceService;
import com.rfchina.wallet.server.service.SeniorChargingService;
import com.rfchina.wallet.server.service.SeniorPayService;
import com.rfchina.wallet.server.service.WalletOrderService;
import com.rfchina.wallet.server.service.WalletService;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler.YunstMemberType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduleApiImpl implements ScheduleApi {

	@Autowired
	private SeniorBalanceService seniorBalanceService;

	@Autowired
	private SimpleExclusiveLock lock;

	@Autowired
	private WalletService walletService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletOrderExtDao walletOrderExtDao;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private SeniorChargingService seniorChargingService;

	@Autowired
	private WalletConfigExtDao walletConfigDao;

	@Autowired
	private SessionThreadLocal sessionThreadLocal;

	@Autowired
	private WalletTunnelExtDao walletTunnelDao;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private SeniorPayService seniorPayService;
	@Autowired
	private WalletOrderService walletOrderService;


	@Log
	@Override
	public void quartzUpdateJunior() {

		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_UPDATE_JUNIOR, 900, () -> {
			try {
				scheduleService.quartzUpdateJunior(configService.getBatchUpdateSize());
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	@Log
	@Override
	public void quartzUpdateSenior() {

		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_UPDATE_SENIOR, 900, () -> {
			try {
				scheduleService
					.quartzUpdateSenior(configService.getBatchUpdateSize());
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	@Log
	@Override
	public void quartzPay() {

		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_PAY, 1800, () -> {
			// 待处理订单
			List<String> batchNos = walletOrderExtDao
				.selectUnSendBatchNo(OrderType.FINANCE.getValue(), configService.getBatchPaySize());
			batchNos.forEach(batchNo -> {

				int c = walletOrderExtDao.updateBatchLock(batchNo, LockStatus.UNLOCK.getValue(),
					LockStatus.LOCKED.getValue());
				if (c <= 0) {
					log.error("锁定记录失败, orderId = {}", batchNo);
					return;
				}

				log.info("开始更新批次号 [{}]", batchNo);
				try {
					scheduleService.doTunnelFinanceJob(batchNo);
				} catch (Exception e) {
					log.error("", e);
				} finally {
					log.info("结束更新批次号 [{}]", batchNo);
					walletOrderExtDao.updateBatchLock(batchNo, LockStatus.LOCKED.getValue(),
						LockStatus.UNLOCK.getValue());
				}
			});
		});
	}

	@Override
	public void quartzNotify() {

		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_Notify, 600, () -> {
			List<WalletOrder> walletOrders = walletOrderExtDao
				.selectByStatusNotNotified(WalletApplyStatus.WAIT_DEAL.getValue(), 200);
			scheduleService.notifyDeveloper(walletOrders);
		});
	}

	@Override
	public void quartzBalance(String balanceDate) {

		Date theDay = (balanceDate == null) ?
			DateUtil.getDate2(DateUtil.addDate2(new Date(), -1))
			: DateUtil.parse(balanceDate, DateUtil.STANDARD_DTAE_PATTERN);
		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_BALANCE, 60, () -> {
			try {
				log.info("[定时对账] 开始日期 {}", theDay);
				seniorBalanceService.doBalance(theDay);
				log.info("[定时对账] 结束");
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	@Override
	public void quartzCharging(String balanceDate) {

		Date theDay = (balanceDate == null) ?
			new Date() : DateUtil.parse(balanceDate, DateUtil.STANDARD_DTAE_PATTERN);
		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_BALANCE, 60, () -> {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(theDay);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			if (balanceDate == null) {
				calendar.add(Calendar.MONTH, -1);
			}
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			Date toDay = calendar.getTime();
			seniorChargingService.doExtract(TunnelType.YUNST, toDay);
			seniorChargingService.doCharging(TunnelType.YUNST, toDay);
		});
	}


	@Override
	public void quartzWithdraw() {
		App app = new App();
		app.setId(configService.getAppId());
		sessionThreadLocal.addApp(app);
		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_WITHDRAW, 1800, () -> {
			new MaxIdIterator<WalletConfig>().apply((maxId) -> {

				WalletConfigCriteria example = new WalletConfigCriteria();
				Criteria criteria = example.createCriteria();
				criteria.andIdGreaterThan(maxId)
					.andAutoWithdrawStatusEqualTo(AutoWithdrawStatus.OPEN.getValue())
					.andStatusEqualTo(WalletConfigStatus.NORMAL.getValue())
					.andWalletIdGreaterThan(0L);
				return walletConfigDao.selectByExampleWithRowbounds(example, new RowBounds(0, 100));
			}, (walletConfig) -> {

				try {
					scheduleService.doWithdraw(walletConfig);
				} catch (Exception e) {
					log.error("[自动提现] 钱包提现异常 " + walletConfig.getWalletId(), e);
				}
				return walletConfig.getId();
			});
		});
	}


	@Override
	public void quartzSyncTunnel() {

		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_SYNC_TUNNEL, 1800, () -> {
			new MaxIdIterator<WalletTunnel>().apply((maxId) -> {

				WalletTunnelCriteria example = new WalletTunnelCriteria();
				WalletTunnelCriteria.Criteria criteria = example.createCriteria();
				criteria.andIdGreaterThan(maxId)
					.andStatusEqualTo(WalletTunnelAuditStatus.WAITING_AUDIT.getValue().byteValue())
					.andMemberTypeEqualTo(YunstMemberType.COMPANY.getValue().byteValue())
					.andWalletIdGreaterThan(0L);
				return walletTunnelDao
					.selectByExampleWithRowbounds(example, new RowBounds(0, 100));
			}, (walletTunnel) -> {

				scheduleService.syncCompanyTunnel(walletTunnel);
				return walletTunnel.getId();
			});
		});
	}


	@Override
	public void quartzSyncBalance() {

		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_SYNC_BALANCE, 1800, () -> {

			new MaxIdIterator<WalletTunnel>().apply((maxId) -> {

				WalletTunnelCriteria example = new WalletTunnelCriteria();
				WalletTunnelCriteria.Criteria criteria = example.createCriteria();
				criteria.andIdGreaterThan(maxId)
					.andStatusEqualTo(WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue().byteValue())
					.andWalletIdGreaterThan(0L);
				return walletTunnelDao
					.selectByExampleWithRowbounds(example, new RowBounds(0, 100));
			}, (walletTunnel) -> {

				boolean needUpdate = !Optional.ofNullable(walletTunnel.getIsDirty()).isPresent()
					|| walletTunnel.getIsDirty() != DirtyType.NORMAL.getValue().byteValue();
				log.info("[余额同步] 通道记录[{}]状态[{}]", walletTunnel.getId(), needUpdate);
				Wallet wallet = walletDao.selectByPrimaryKey(walletTunnel.getWalletId());
				if (!needUpdate) {
					needUpdate = !Optional.ofNullable(wallet.getBalanceUpdTime()).isPresent()
						|| DateUtil.addSecs(wallet.getBalanceUpdTime(), 1800).before(new Date());
				}
				log.info("[余额同步] 钱包记录[{}]状态[{}]", wallet.getId(), needUpdate);

				if (needUpdate) {
					seniorPayService.syncTunnelAmount(walletTunnel);
				}

				return walletTunnel.getId();
			});
		});

	}

	@Override
	public void quartzOrderSettleFailed() {
		Date orderStartDate = DateUtils.addDays(new Date(), -7);
		Date orderEndDate = DateUtils.addDays(new Date(), -2);
		log.info("结算通知订单日期:{}###{}",
			DateUtil.formatDate(orderStartDate, DateUtil.STANDARD_DTAETIME_PATTERN),
			DateUtil.formatDate(orderEndDate, DateUtil.STANDARD_DTAETIME_PATTERN));
		new LockDone(lock).apply(RedisConstant.LOCK_QUARTZ_ORDER_SETTLE_FAILED, 900, () -> {
			try {
				log.info("[定时通知结算不成功订单] 开始");
				walletOrderService.failedSettleOrderSendMail(orderStartDate, orderEndDate);
				log.info("[定时通知结算不成功订单] 结束");
			} catch (Exception e) {
				log.error("定时通知结算不成功订单异常", e);
			}
		});
	}

}
