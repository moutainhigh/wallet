package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletLevel;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletClearInfo;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletCollectMethod;
import com.rfchina.wallet.domain.model.WalletCollectMethod.WalletCollectMethodBuilder;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearInfoExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletClearingExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCollectMethodExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRechargeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletRefundExtDao;
import com.rfchina.wallet.server.model.ext.CollectReq;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Alipay;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Balance;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.CodePay;
import com.rfchina.wallet.server.model.ext.CollectReq.WalletPayMethod.Wechat;
import com.rfchina.wallet.server.model.ext.RechargeReq;
import com.rfchina.wallet.server.model.ext.RefundReq.RefundInfo;
import com.rfchina.wallet.server.model.ext.SettleReq.Reciever;
import com.rfchina.wallet.server.model.ext.SettleResp;
import com.rfchina.wallet.server.msic.EnumWallet.ChannelType;
import com.rfchina.wallet.server.msic.EnumWallet.ClearInfoStatus;
import com.rfchina.wallet.server.msic.EnumWallet.ClearingStatus;
import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import com.rfchina.wallet.server.msic.EnumWallet.CollectStatus;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.msic.EnumWallet.RefundStatus;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletType;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeniorWalletService {


	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private WalletApplyExtDao walletApplyDao;

	@Autowired
	private WalletCollectExtDao walletCollectDao;

	@Autowired
	private WalletCollectMethodExtDao walletCollectMethodDao;

	@Autowired
	private WalletClearInfoExtDao walletClearInfoDao;

	@Autowired
	private WalletClearingExtDao walletClearingDao;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private WalletRefundExtDao walletRefundDao;

	@Autowired
	private WalletRechargeExtDao walletRechargeDao;

	private Long defPayerWalletId = 10001L;
	private Long defEntWalletId = 10000L;


	/**
	 * 预代收
	 */
	public WalletCollect preCollect(String accessToken, CollectReq collectReq) {

		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.walletId(null)
			.batchNo(batchNo)
			.bizNo(collectReq.getBizNo())
			.type(WalletApplyType.COLLECT.getValue())
			.amount(collectReq.getAmount())
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(EnumWalletLevel.SENIOR.getValue())
			.walletType(WalletType.COMPANY.getValue())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		Long payerWalletId = (collectReq.getPayerWalletId() != null) ? collectReq.getPayerWalletId()
			: defPayerWalletId;
		WalletPayMethod payMethod = collectReq.getWalletPayMethod();
		Wallet payerWallet = walletDao.selectByPrimaryKey(payerWalletId);
		if (payerWallet == null || payerWallet.getStatus() != WalletStatus.ACTIVE.getValue()
			.byteValue()) {
			throw new RuntimeException();
		}
		// 代收记录
		WalletCollect collect = WalletCollect.builder()
			.applyId(walletApply.getId())
			.payerWalletId(payerWallet.getId())
			.payeeWalletId(defEntWalletId)
			.amount(collectReq.getAmount())
			.channelType(TunnelType.YUNST.getValue())
			.tunnelType(payMethod.getTunnels())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(CollectStatus.WAIT_PAY.getValue())
			.build();
		walletCollectDao.insertSelective(collect);
		savePayMethod(collect.getId(), WalletApplyType.COLLECT.getValue(), payMethod);

		// 清分记录
		collectReq.getRecievers().forEach(reciever -> {
			WalletClearInfo clearInfo = WalletClearInfo.builder()
				.collectId(collect.getId())
				.walletId(reciever.getWalletId())
				.budgetAmount(reciever.getAmount())
				.clearAmount(0L)
				.status(ClearInfoStatus.WAITING.getValue())
				.build();
			walletClearInfoDao.insertSelective(clearInfo);
		});

		return collect;
	}

	/**
	 * 保存支付方式
	 */
	private void savePayMethod(Long collectId, Byte type, WalletPayMethod payMethod) {
		// 支付方式
		WalletCollectMethodBuilder builder = WalletCollectMethod.builder()
			.refId(collectId)
			.type(type);
		if (payMethod.getBalance() != null) {
			Balance balance = payMethod.getBalance();
			builder.channelType(ChannelType.WALLET.getValue())
				.payType(CollectPayType.BALANCE.getValue())
				.amount(balance.getAmount());
		} else if (payMethod.getWechat() != null) {
			Wechat wechat = payMethod.getWechat();
			CollectPayType payType = EnumUtil.parse(CollectPayType.class, wechat.getPayType());
			builder.channelType(ChannelType.WECHAT.getValue())
				.payType(payType.getValue().byteValue())
				.amount(wechat.getAmount())
				.openId(wechat.getOpenId())
				.cusIp(wechat.getCusip())
				.appId(wechat.getSubAppId())
				.sceneInfo(wechat.getSceneInfo());
		} else if (payMethod.getAlipay() != null) {
			Alipay alipay = payMethod.getAlipay();
			CollectPayType payType = EnumUtil.parse(CollectPayType.class, alipay.getPayType());
			builder.channelType(ChannelType.ALIPAY.getValue())
				.payType(payType.getValue().byteValue())
				.amount(alipay.getAmount())
				.openId(alipay.getUserId());
		} else if (payMethod.getCodePay() != null) {
			CodePay codePay = payMethod.getCodePay();
			CollectPayType payType = EnumUtil.parse(CollectPayType.class, codePay.getPayType());
			builder.channelType(ChannelType.ALIPAY.getValue())
				.payType(payType.getValue().byteValue())
				.amount(codePay.getAmount())
				.sceneInfo(codePay.getAuthcode());
		}
		walletCollectMethodDao.insertSelective(builder.build());
	}

	/**
	 * 发起代收
	 */
	public WalletCollect doCollect(String accessToken, WalletCollect collect) {
		WalletApply walletApply = walletApplyDao.selectByPrimaryKey(collect.getApplyId());

		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		handler.collect(walletApply.getId());

		return collect;
	}

	/**
	 * 查询代收结果
	 */
	public List<WalletCollect> getCollects(String accessToken, String batchNo) {
		WalletApply walletApply = walletApplyDao.selectByBatchNo(batchNo);
		return walletCollectDao.selectByApplyId(walletApply.getId());
	}


	/**
	 * 发起代付（加锁）
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public SettleResp agentPay(String accessToken, Long collectId, List<Reciever> receivers) {
		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.batchNo(batchNo)
			.type(WalletApplyType.AGENT_PAY.getValue())
			.amount(receivers.stream().map(req -> req.getAmount()).reduce(0L, (x, y) -> x + y))
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(EnumWalletLevel.SENIOR.getValue())
			.walletType(WalletType.COMPANY.getValue())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		List<WalletClearInfo> clearInfos = walletClearInfoDao.selectByCollectId(collectId);
		// 判断收款人记录不重复
		// 匹配原始分账记录
		receivers.forEach(receiver -> {
			Optional<WalletClearInfo> opt = clearInfos.stream().filter(
				clear -> clear.getWalletId().longValue() == receiver.getWalletId().longValue())
				.findFirst();
			WalletClearInfo clear = opt.orElseThrow(() -> new RuntimeException());
			// 收款金额不超过剩余代付
			if (clear.getBudgetAmount() - clear.getClearAmount() < receiver.getAmount()) {
				throw new RuntimeException();
			}
		});
		List<WalletClearing> clearings = receivers.stream().map(receiver -> {
			WalletClearing clearing = WalletClearing.builder()
				.applyId(walletApply.getId())
				.collectId(collectId)
				.walletId(receiver.getWalletId())
				.amount(receiver.getAmount())
				.status(ClearingStatus.WAITING.getValue())
				.build();
			walletClearingDao.insertSelective(clearing);
			return clearing;
		}).collect(Collectors.toList());

		// 代付给每个收款人
		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		handler.agentPay(walletApply.getId());

		return SettleResp.builder()
			.applyId(walletApply.getId())
			.clearings(clearings)
			.build();

	}

	/**
	 * 查询代付结果
	 */
	public List<WalletClearing> agentPayQuery(Long collectId) {
		return walletClearingDao.selectByApplyId(collectId);
	}


	/**
	 * 退款
	 */
	public void refund(Long collectId, List<RefundInfo> refundList) {
		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.batchNo(batchNo)
			.type(WalletApplyType.REFUND.getValue())
			.amount(
				refundList.stream().map(refund -> refund.getAmount()).reduce((x, y) -> x + y).get())
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(EnumWalletLevel.SENIOR.getValue())
			.walletType(WalletType.COMPANY.getValue())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		// 记录退款单
		WalletCollect collect = walletCollectDao.selectByPrimaryKey(collectId);
		refundList.forEach(refund -> {
			WalletRefund walletRefund = WalletRefund.builder()
				.applyId(walletApply.getId())
				.collectId(collectId)
				.fromWalletId(collect.getPayeeWalletId())
				.toWalletId(collect.getPayerWalletId())
				.refundAmount(refund.getAmount())
				.status(RefundStatus.WAITING.getValue())
				.build();
			walletRefundDao.insertSelective(walletRefund);
		});

		// 代付给每个收款人
		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		handler.refund(collectId);

	}

	/**
	 * 充值
	 */
	public void recharge(String accessToken, RechargeReq rechargeReq) {
		// 工单记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			WalletApply walletApply = walletApplyDao.selectByBatchNo(id);
			return walletApply == null;
		});
		WalletApply walletApply = WalletApply.builder()
			.batchNo(batchNo)
			.bizNo(rechargeReq.getBizNo())
			.type(WalletApplyType.RECHARGE.getValue())
			.amount(rechargeReq.getAmount())
			.status(WalletApplyStatus.WAIT_SEND.getValue())
			.walletLevel(EnumWalletLevel.SENIOR.getValue())
			.walletType(WalletType.COMPANY.getValue())
			.channelType(TunnelType.YUNST.getValue())
			.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
			.createTime(new Date())
			.build();
		walletApplyDao.insertSelective(walletApply);

		Long payerWalletId =
			(rechargeReq.getPayerWalletId() != null) ? rechargeReq.getPayerWalletId()
				: defPayerWalletId;
		WalletPayMethod payMethod = rechargeReq.getWalletPayMethod();
		Wallet payerWallet = walletDao.selectByPrimaryKey(payerWalletId);
		if (payerWallet == null || payerWallet.getStatus() != WalletStatus.ACTIVE.getValue()
			.byteValue()) {
			throw new RuntimeException();
		}
		// 代收记录
		String orderNo = IdGenerator.createBizId("WR", 19, id -> {
			return walletRechargeDao.selectCountByOrderNo(id) == 0;
		});
		WalletRecharge recharge = WalletRecharge.builder()
			.applyId(walletApply.getId())
			.orderNo(orderNo)
			.payerWalletId(payerWallet.getId())
			.payeeWalletId(defEntWalletId)
			.amount(rechargeReq.getAmount())
			.channelType(payMethod.getTunnels())
			.tunnelType(TunnelType.YUNST.getValue())
			.progress(GwProgress.WAIT_SEND.getValue())
			.status(CollectStatus.WAIT_PAY.getValue())
			.expireTime(rechargeReq.getExpireTime())
			.build();
		walletRechargeDao.insertSelective(recharge);
		savePayMethod(recharge.getId(), WalletApplyType.RECHARGE.getValue(), payMethod);

		// 银行充值
		EBankHandler handler = handlerHelper.selectByWalletLevel(walletApply.getWalletLevel());
		handler.recharge(walletApply.getId());

	}
}
