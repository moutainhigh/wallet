package com.rfchina.wallet.server.api.impl;

import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.api.ScheduleApi;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.LockStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.ScheduleService;
import com.rfchina.wallet.server.service.SeniorBalanceService;
import com.rfchina.wallet.server.service.WalletService;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
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

	@Log
	@Override
	public void quartzUpdate() {

		String lockName = "quartzUpdateJunior";
		lockDone(lockName, 900, (date) -> {
			try {
				scheduleService.quartzUpdateJunior(configService.getBatchUpdateSize());
			} catch (Exception e) {
				log.error("", e);
			}
		});

		lockName = "quartzUpdateSenior";
		lockDone(lockName, 900, (date) -> {
			try {
				scheduleService.quartzUpdateSenior(configService.getBatchUpdateSize());
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	@Log
	@Override
	public void quartzPay() {

		String lockName = "quartzPay";
		lockDone(lockName, 1800, (date -> {
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
		}));
	}

	@Override
	public void quartzNotify() {

		String lockName = "quartzNotify";
		lockDone(lockName, 600, (date) -> {
			List<WalletOrder> walletOrders = walletOrderExtDao
				.selectByStatusNotNotified(WalletApplyStatus.WAIT_DEAL.getValue(), 200);
			scheduleService.notifyDeveloper(walletOrders);
		});
	}

	@Override
	public void quartzBalance() {

		String lockName = "quartzBalance";
		lockDone(lockName, 60, (date) -> {
			Date yestoday = DateUtil.getDate2(DateUtil.addDate2(date, -1));
			seniorBalanceService.doBalance(yestoday);
		});
	}

	private void lockDone(String lockName, Integer expireSecord, Consumer<Date> consumer) {
		boolean succ = lock.acquireLock(lockName, expireSecord, 0, 1);
		if (succ) {
			try {
				consumer.accept(new Date());
			} finally {
				lock.unLock(lockName);
			}
		} else {
			log.warn("获取分布式锁失败， 跳过执行的{}任务", lockName);
		}
	}
}
