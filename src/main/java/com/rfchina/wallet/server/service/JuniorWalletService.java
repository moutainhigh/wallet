package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.domain.model.WalletLogCriteria;
import com.rfchina.wallet.domain.model.WalletLogCriteria.Criteria;
import com.rfchina.wallet.server.bank.pudong.PriPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.PubPayQueryBuilder;
import com.rfchina.wallet.server.bank.pudong.PubPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.request.PriPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.PriPayRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody.PayResult;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletLogExtDao;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.msic.EnumWallet.AQCardType;
import com.rfchina.wallet.server.msic.EnumWallet.AQPayeeType;
import com.rfchina.wallet.server.msic.EnumWallet.AQTransType;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.rfchina.biztools.generate.IdGenerator;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class JuniorWalletService {

	@Value("${wlpay.pudong.masterid}")
	private String masterId;

	@Value("${wlpay.pudong.acctno}")
	private String acctNo;

	@Value("${wlpay.pudong.project.number}")
	private String projectNumber;

	@Value("${wlpay.pudong.project.name}")
	private String projectName;

	@Value("${wlpay.pudong.project.costcode}")
	private String costItemCode;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletLogExtDao walletLogDao;

	@Autowired
	private WalletExtDao walletDao;

	/**
	 * 出佣到个人钱包
	 */
	public PayInResp payIn(List<PayInReq> payInReqs) {
		// 出佣请求不能为空, 数量不能大于20
		if (payInReqs.isEmpty() || payInReqs.size() > 20) {
			throw new RuntimeException();
		}

		// 钱包类型唯一
		List<Long> walletIds = payInReqs.stream()
			.map(payInReq -> payInReq.getWalletId())
			.distinct()
			.collect(Collectors.toList());
		List<Byte> walletTypes = walletDao.selectWalletType(walletIds);
		if (walletTypes.size() != 1) {
			throw new RuntimeException();
		}
		Byte walletType = walletTypes.get(0);

		// 记录钱包流水
		List<WalletLog> walletLogs = payInReqs.stream().map(payInReq -> {

			WalletCard walletCard = getWalletCard(payInReq.getWalletId());
			if (walletCard == null) {
				throw new RuntimeException();
			}

			WalletLog walletLog = WalletLog.builder()
				.walletId(payInReq.getWalletId())
				.type(WalletLogType.TRANSFER.getValue())
				.amount(payInReq.getAmount())
				.refType(walletType)
				.refAccount(walletCard.getBankAccount())
				.elecChequeNo(payInReq.getElecChequeNo())
				.status(WalletLogStatus.SENDING.getValue())
				.createTime(new Date())
				.build();

			walletLogDao.insert(walletLog);
			return walletLog;
		}).collect(Collectors.toList());

		// 请求网关
		PayInResp respBody = null;
		try {
			if (WalletType.COMPANY.getValue().byteValue() == walletType) {
				PubPayRespBody resp = payBy8800(payInReqs);
				respBody = BeanUtil.newInstance(resp, PayInResp.class);
			} else if (WalletType.PERSON.getValue().byteValue() == walletType) {
				PriPayRespBody resp = payByAq52(payInReqs);
				respBody = PayInResp.builder()
					.acceptNo(resp.getHandleSeqNo())
					.successCount(String.valueOf(payInReqs.size()))
					.failCount("0")
					.build();
			}
		} catch (Exception e) {
			log.error("支付错误", e);
			throw new RuntimeException(e);
		}
		String acceptNo = respBody.getAcceptNo();
		walletLogs.forEach(walletLog -> {
			walletLogDao.updateStatusAndAcceptNo(walletLog.getId(),
				WalletLogStatus.PROCESSING.getValue(), acceptNo);
		});

		return respBody;
	}


	/**
	 * 对私转帐
	 */
	private PriPayRespBody payByAq52(List<PayInReq> payInReqs) throws Exception {
		List<PriPayReq> payReqs = payInReqs.stream().map(payInReq -> {
			WalletCard walletCard = getWalletCard(payInReq.getWalletId());
			if (walletCard == null) {
				throw new RuntimeException();
			}
			// 必须注意，分转换为0.00元
			BigDecimal bigAmount = new BigDecimal(payInReq.getAmount())
				.divide(new BigDecimal("100"))
				.setScale(2, BigDecimal.ROUND_DOWN);
			return PriPayReq.builder()
				.detailNo("")
				.isPuFaAcct("1")
				.payeeType(AQPayeeType.PRIVATE.getValue().toString())
				.cardType(AQCardType.BANKCARD.getValue().toString())
				.payeeAcctNo(walletCard.getBankAccount())
				.payeeName(walletCard.getDepositName())
				.amount(bigAmount.toString())
				.bizLog(payInReq.getElecChequeNo())
				.build();
		}).collect(Collectors.toList());

		String packetId = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 11, (orderId) -> true);
		PriPayReqBuilder req = PriPayReqBuilder.builder()
			.masterId(masterId)
			.packetId(packetId)
			.transMasterID(masterId)
			.projectNumber(projectNumber)
			.projectName(projectName)
			.costItemCode(costItemCode)
			.transType(AQTransType.TO_PAY.getValue().toString())
			.elecChequeNo(packetId)
			.batchNo(packetId.substring(packetId.length() - 6, packetId.length()))
			.payReqList(payReqs)
			.build();

		return req.lanch(new Builder().build());
	}


	/**
	 * 对公转帐
	 */
	private PubPayRespBody payBy8800(List<PayInReq> payInReqs) throws Exception {
		List<PubPayReq> payReqs = payInReqs.stream().map(payInReq -> {

			WalletCard walletCard = getWalletCard(payInReq.getWalletId());
			if (walletCard == null) {
				throw new RuntimeException();
			}

			// 必须注意，分转换为0.00元
			BigDecimal bigAmount = new BigDecimal(payInReq.getAmount())
				.divide(new BigDecimal("100"))
				.setScale(2, BigDecimal.ROUND_DOWN);

			return PubPayReq.builder()
				.acctNo(walletCard.getBankAccount())
				.acctName(walletCard.getDepositName())
				.amount(bigAmount.toString())
				.elecChequeNo(payInReq.getElecChequeNo())
				.note(payInReq.getNote())
				.payPurpose(
					payInReq.getPayPurpose() != null ? payInReq.getPayPurpose().toString() : null)
				.sysFlag(SysFlag.SELF.getValue())
				.remitLocation(RemitLocation.SELF.getValue())
				.build();

		}).collect(Collectors.toList());

		String packetId = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 11, (orderId) -> true);
		PubPayReqBuilder req = PubPayReqBuilder
			.builder()
			.masterId(masterId)
			.packetId(packetId)
			.payList(payReqs)
			.build();

		return req.lanch(new Builder().build());

	}

	private WalletCard getWalletCard(Long walletId) {
		return walletCardDao.selectByDef(walletId);
	}

	/**
	 * 查询出佣结果
	 */
	public List<PayStatusResp> query(String elecChequeNo, String acceptNo) {

		WalletLogCriteria example = new WalletLogCriteria();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isEmpty(elecChequeNo)) {
			criteria.andElecChequeNoEqualTo(elecChequeNo);
		}
		if (!StringUtils.isEmpty(acceptNo)) {
			criteria.andAcceptNoEqualTo(acceptNo);
		}

		List<WalletLog> walletLogs = walletLogDao.selectByExample(example);
		if (walletLogs.isEmpty()) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST);
		}

		boolean wantToQuery = walletLogs.stream().map(log -> log.getStatus())
			.anyMatch(status -> status.byteValue() != WalletLogStatus.SUCC.getValue());

		if (!wantToQuery) {
			// notify query
		}

		return walletLogs.stream().map(walletLog -> {
			return PayStatusResp.builder()
				.acceptNo(walletLog.getAcceptNo())
				.elecChequeNo(walletLog.getElecChequeNo())
				.transDate(DateUtil.formatDate(walletLog.getCreateTime()))
				.status(walletLog.getStatus())
				.errMsg(walletLog.getErrMsg())
				.build();
		}).collect(Collectors.toList());

	}


	/**
	 * 定时更新支付状态
	 */
	public void quartzUpdate() {

		log.info("scheduler: 开始更新支付状态[银企直连]");
		WalletLogCriteria example = new WalletLogCriteria();
		example.createCriteria()
			.andStatusEqualTo(WalletLogStatus.PROCESSING.getValue());

		List<WalletLog> walletLogs = walletLogDao.selectByExample(example);
		List<Tuple<String, Date>> acceptNoList = walletLogs.stream()
			.map(log -> new Tuple<String, Date>(log.getAcceptNo(), log.getCreateTime()))
			.collect(Collectors.toList());

		acceptNoList.forEach(tp -> {

			String acceptNo = tp.left;
			Date createTime = tp.right;
			Date endDate = DateUtil.addDate2(createTime, 7);

			PubPayQueryBuilder req = PubPayQueryBuilder.builder()
				.masterId(masterId)
				.acctNo(acctNo)
				.beginDate(DateUtil.formatDate(createTime, "yyyyMMdd"))
				.endDate(DateUtil.formatDate(endDate, "yyyyMMdd"))
				.queryNumber("30")
				.beginNumber("1")
				.acceptNo(acceptNo)
				.build();

			PubPayQueryRespBody respBody;
			try {
				respBody = req.lanch(new Builder().build());
			} catch (Exception e) {
				log.error("查询错误", e);
				throw new RuntimeException(e);
			}

			if (respBody.getLists() != null && respBody.getLists().getList() != null) {

				List<PayResult> results = respBody.getLists().getList();

				Map<String, PayResult> resultMap = results.stream()
					.collect(Collectors.toMap(PayResult::getElecChequeNo, rs -> rs));

				walletLogs.forEach(walletLog -> {
					if (walletLog.getAcceptNo().equals(acceptNo)
						&& resultMap.containsKey(walletLog.getElecChequeNo())) {

						PayResult payResult = resultMap.get(walletLog.getElecChequeNo());

						WalletLogStatus status = WalletLogStatus
							.parsePuDong8804(payResult.getTransStatus());
						TransStatus transStatus = TransStatus.parse(payResult.getTransStatus());
						walletLogDao.updateStatusAndErrMsg(walletLog.getId(), status.getValue(),
							transStatus.getDescription());
					}
				});
			}

		});

		log.info("scheduler: 结束更新支付状态[银企直连]");
	}
}
