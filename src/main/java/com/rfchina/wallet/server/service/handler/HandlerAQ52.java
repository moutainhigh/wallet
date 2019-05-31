package com.rfchina.wallet.server.service.handler;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.server.bank.pudong.builder.PriPayQuery53Builder;
import com.rfchina.wallet.server.bank.pudong.builder.PriPayQuery54Builder;
import com.rfchina.wallet.server.bank.pudong.builder.PriPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.request.PriPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.PriPayQuery53RespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PriPayQuery53RespBody.PriPayQuery53RespWrapper;
import com.rfchina.wallet.server.bank.pudong.domain.response.PriPayQuery54RespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PriPayResp;
import com.rfchina.wallet.server.bank.pudong.domain.response.PriPayRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.util.StringObject;
import com.rfchina.wallet.server.mapper.ext.BankCodeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.AQCardType;
import com.rfchina.wallet.server.msic.EnumWallet.AQPayeeType;
import com.rfchina.wallet.server.msic.EnumWallet.AQTransType;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatusAQ53;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.service.ConfigService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * AQ52处理器
 */
@Slf4j
@Data
@Component
public class HandlerAQ52 implements EBankHandler {

	@Value("${wlpay.pudong.project.number}")
	private String projectNumber;

	@Value("${wlpay.pudong.project.name}")
	private String projectName;

	@Value("${wlpay.pudong.project.costcode}")
	private String costItemCode;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletApplyExtDao walletApplyDao;

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private BankCodeExtDao bankCodeExtDao;

	@Autowired
	private OkHttpClient client;

	private EBankHandler next;

	public boolean isSupportWalletType(Byte walletType) {
//		return WalletType.PERSON.getValue().byteValue() == walletType;
		return false;
	}

	public boolean isSupportMethod(Byte method) {
		return GatewayMethod.PUDONG_AQ52.getValue().byteValue() == method;
	}

	public GatewayMethod getGatewayMethod() {
		return GatewayMethod.PUDONG_AQ52;
	}

	/**
	 * 对私转帐
	 */
	public Tuple<GatewayMethod, PayInResp> pay(List<WalletApply> payInReqs) throws Exception {
		int seq = 1;

		List<PriPayReq> payReqs = payInReqs.stream().map(payInReq -> {

			WalletCard walletCard = getWalletCard(payInReq.getWalletId());
			if (walletCard == null) {
				throw new RuntimeException();
			}

			// 必须注意，分转换为0.00元
			BigDecimal bigAmount = new BigDecimal(payInReq.getAmount())
				.divide(new BigDecimal("100"))
				.setScale(2, BigDecimal.ROUND_DOWN);

			BankCode bankCode = bankCodeExtDao.selectByCode(walletCard.getBankCode());
			String sysFlag = bankCode.getBankName().contains("浦东发展银行") ?
				SysFlag.SELF.getValue() : SysFlag.OTHER.getValue();

			String payeeType = walletCard.getIsPublic() == 1 ? AQPayeeType.PUBLIC.getValue()
				: AQPayeeType.PRIVATE.getValue();
			String cardType = walletCard.getIsPublic() == 1 ? AQCardType.PUBLIC.getValue()
				: AQCardType.BANKCARD.getValue();

			return PriPayReq.builder()
				.isPuFaAcct(sysFlag)
				.payeeType(payeeType)
				.cardType(cardType)
				.payeeAcctNo(walletCard.getBankAccount())
				.payeeName(walletCard.getDepositName())
				.amount(bigAmount.toString())
				.bizLog(payInReq.getElecChequeNo())
				.build();
		}).collect(Collectors.toList());

		for (int i = 0; i < payReqs.size(); i++) {
			payReqs.get(i).setDetailNo(String.format("%0000d", String.valueOf(seq++)));
		}

		String packetId = IdGenerator
			.createBizId(IdGenerator.PREFIX_WALLET, IdGenerator.DEFAULT_LENGTH, (orderId) -> true);
		PriPayReqBuilder req = PriPayReqBuilder.builder()
			.masterId(configService.getMasterId())
			.packetId(packetId)
			.transMasterID(configService.getMasterId())
			.projectNumber(projectNumber)
			.projectName(projectName)
			.costItemCode(costItemCode)
			.transType(AQTransType.TO_PAY.getValue().toString())
			.elecChequeNo(packetId)
			.batchNo(packetId.substring(packetId.length() - 6, packetId.length()))
			.payReqList(payReqs)
			.build();

		PriPayRespBody resp = req.lanch(configService.getHostUrl(), configService.getSignUrl(),
			client);

		PayInResp payInResp = PayInResp.builder()
			.acceptNo(resp.getHandleSeqNo())
			.build();

		return new Tuple<>(getGatewayMethod(), payInResp);
	}

	public List<WalletApply> updatePayStatus(String acceptNo, Date createTime) {
		Date endDate = DateUtil.addDate2(createTime, 7);

		PriPayQuery53Builder req53 = PriPayQuery53Builder.builder()
			.masterId(configService.getMasterId())
			.packetId(IdGenerator.createBizId(IdGenerator.PREFIX_WALLET,
				IdGenerator.DEFAULT_LENGTH, (orderId) -> true))
			.transMasterID(configService.getMasterId())
			.projectNumber(projectNumber)
			.handleSeqNo(acceptNo)
			.beginDate(DateUtil.formatDate(createTime, "yyyyMMdd"))
			.endDate(DateUtil.formatDate(endDate, "yyyyMMdd"))
			.queryNumber("20")
			.beginNumber("1")
			.build();

		try {
			PriPayQuery53RespBody respBody = req53.lanch(configService.getHostUrl(),
				configService.getSignUrl(),client);
			if (respBody.getLists() != null && respBody.getLists().getList() != null) {
				PriPayQuery53RespWrapper wrapper = respBody.getLists().getList().get(0);

				if (TransStatusAQ53.FAIL.getValue().toString()
					.equals(wrapper.getBatchHandleStatus())) {
					walletApplyDao.updateStatusByAcceptNo(wrapper.getHandleSeqNo(),
						WalletApplyStatus.FAIL.getValue(), TransStatusAQ53.FAIL.getValueName());
				}
				if (!TransStatusAQ53.SUCC.getValue().toString()
					.equals(wrapper.getBatchHandleStatus())) {
					return new ArrayList<>();
				}
			}
		} catch (Exception e) {
			log.error("AQ53查询错误", e);
			throw new RuntimeException(e);
		}

		PriPayQuery54Builder req54 = PriPayQuery54Builder.builder()
			.masterId(configService.getMasterId())
			.packetId(IdGenerator.createBizId(IdGenerator.PREFIX_WALLET,
				IdGenerator.DEFAULT_LENGTH, (orderId) -> true))
			.transMasterID(configService.getMasterId())
			.projectNumber(projectNumber)
			.transDate(DateUtil.formatDate(createTime, "yyyyMMdd"))
			.handleSeqNo(acceptNo)
			.build();

		try {
			PriPayQuery54RespBody respBody = req54.lanch(configService.getHostUrl(),
				configService.getSignUrl(),client);
			if (respBody.getLists() != null && respBody.getLists().getList() != null) {
				List<PriPayResp> priPayResps = respBody.getLists().getList().stream()
					.map(wrapper -> {
						return (PriPayResp) StringObject
							.parseStringObject(wrapper.getDetailedContent(),
								PriPayResp.class, "|");
					}).collect(Collectors.toList());

				return priPayResps.stream().map(rs -> {
					WalletApplyStatus status = WalletApplyStatus.parsePuDongAQ54(rs.getStatus());

					WalletApply walletApply = walletApplyDao
						.selectByHostAcctAndElecNo(respBody.getHandleSeqNo()
							, rs.getBizLog(), WalletApplyStatus.PROCESSING.getValue());
					if (walletApply != null) {
						walletApply.setSeqNo(rs.getDetailNo());
						walletApply.setStatus(status.getValue());
						walletApply.setSysErrMsg(rs.getErrMsg());
						walletApply.setEndTime(new Date());
						walletApplyDao.updateByPrimaryKeySelective(walletApply);
					}
					return walletApply;
				}).filter(rs -> rs != null).collect(Collectors.toList());

			}

		} catch (Exception e) {
			log.error("AQ53查询错误", e);
			throw new RuntimeException(e);
		}

		return new ArrayList<>();
	}

	@Override
	public void onAskErr(WalletApply walletApply, IGatewayError err) {

	}


	private WalletCard getWalletCard(Long walletId) {
		return walletCardDao.selectByDef(walletId);
	}
}
