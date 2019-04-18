package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.WalletLogMapper;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletLogDao;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.domain.model.WalletLogCriteria;
import com.rfchina.wallet.domain.model.WalletLogCriteria.Criteria;
import com.rfchina.wallet.server.bank.pudong.PubPayQueryBuilder;
import com.rfchina.wallet.server.bank.pudong.PubPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody.PayResult;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletLogDao walletLogDao;

	/**
	 * 出佣到个人钱包
	 */
	public PubPayRespBody payIn(List<PayInReq> payInReqs) {
		List<Long> logIdList = new ArrayList<>();
		List<PubPayReq> payReqs = payInReqs.stream().map(payInReq -> {

			WalletCard walletCard = getWalletCard(payInReq.getWalletId());
			if (walletCard == null) {
				throw new RuntimeException();
			}

			WalletLog walletLog = WalletLog.builder()
				.walletId(payInReq.getWalletId())
				.type(WalletLogType.TRANSFER.getValue())
				.amount(payInReq.getAmount())
				.refType(null)
				.refAccount(walletCard.getBankAccount())
				.elecChequeNo(payInReq.getElecChequeNo())
				.status(WalletLogStatus.SENDING.getValue())
				.createTime(new Date())
				.build();

			walletLogDao.insert(walletLog);
			logIdList.add(walletLog.getId());

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

		try {
			PubPayRespBody respBody = req.lanch(new Builder().build());
			logIdList.forEach(logId -> {
				walletLogDao.updateStatus(logId, respBody.getAcceptNo(),
					WalletLogStatus.PROCESSING.getValue());
			});

			return respBody;
		} catch (Exception e) {
			log.error("支付错误", e);
			throw new RuntimeException(e);
		}
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

	public void quartUpdate() {
		WalletLogCriteria example = new WalletLogCriteria();
		example.createCriteria()
			.andStatusEqualTo(WalletLogStatus.PROCESSING.getValue());

		List<WalletLog> walletLogs = walletLogDao.selectByExample(example);
		List<String> acceptNoList = walletLogs.stream().map(log -> log.getAcceptNo())
			.collect(Collectors.toList());

		acceptNoList.forEach(acceptNo -> {
			PubPayQueryBuilder req = PubPayQueryBuilder.builder()
				.masterId(masterId)
				.acctNo(acctNo)
				.beginDate("20190416")
				.endDate("20190418")
				.queryNumber("1")
				.beginNumber("1")
				.acceptNo(acceptNo)
				.build();

			try {
				PubPayQueryRespBody respBody = req.lanch(new Builder().build());
				if (respBody.getLists() != null && respBody.getLists().getList() != null) {
					List<PayResult> results = respBody.getLists().getList();
					Map<String, PayResult> resultMap = results.stream()
						.collect(Collectors.toMap(PayResult::getElecChequeNo, rs -> rs));

					walletLogs.forEach(walletLog -> {
						if (walletLog.getAcceptNo().equals(acceptNo)
							&& resultMap.containsKey(walletLog.getElecChequeNo())) {
							PayResult payResult = resultMap.get(walletLog.getElecChequeNo());

							WalletLogStatus status = WalletLogStatus.parsePuDong8804(payResult.getTransStatus());
							walletLogDao.updateStatus(walletLog.getId(),acceptNo,status.getValue(),status.getValue());
						}
					});
				}
			} catch (Exception e) {
				log.error("查询错误", e);
				throw new RuntimeException(e);
			}
		});

	}
}
