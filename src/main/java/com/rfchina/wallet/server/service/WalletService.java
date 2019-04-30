package com.rfchina.wallet.server.service;

import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.annotation.EnumParamValid;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.platform.common.utils.RegexUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.*;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.WalletResponseCode;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.*;
import com.rfchina.wallet.domain.model.WalletLogCriteria.Criteria;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.server.adapter.UserAdapter;
import com.rfchina.wallet.server.mapper.ext.WalletLogExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletUserExtDao;
import com.rfchina.wallet.server.model.ext.AcceptNo;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp.WalletInfoRespBuilder;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletType;
import com.rfchina.wallet.server.msic.MqConstant;
import com.rfchina.wallet.server.service.handler.HandlerHelper;
import com.rfchina.wallet.server.service.handler.PuDongHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class WalletService {

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private WalletCompanyDao walletCompanyDao;

	@Autowired
	private WalletPersonDao walletPersonDao;

	@Autowired
	private WalletUserExtDao walletUserDao;

	@Autowired
	private WalletLogDao walletLogDao;

	@Autowired
	private WalletCardDao walletCardDao;


	@Autowired
	private UserAdapter userAdapter;

	@Autowired
	private AppService appService;

	@Autowired
	private BankCodeDao bankCodeDao;

	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private WalletLogExtDao walletLogExtDao;

	/**
	 * 查询出佣结果
	 */
	public List<PayStatusResp> query(String bizNo, String batchNo) {

		WalletLogCriteria example = new WalletLogCriteria();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isEmpty(bizNo)) {
			criteria.andBizNoEqualTo(bizNo);
		}
		if (!StringUtils.isEmpty(batchNo)) {
			criteria.andBatchNoEqualTo(batchNo);
		}

		List<WalletLog> walletLogs = walletLogDao.selectByExample(example);
		if (walletLogs.isEmpty()) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
				, batchNo + "_" + bizNo);
		}

		return walletLogs.stream().map(walletLog -> {
			return PayStatusResp.builder()
				.bizNo(walletLog.getBizNo())
				.batchNo(walletLog.getBatchNo())
				.amount(walletLog.getAmount())
				.transDate(DateUtil.formatDate(walletLog.getCreateTime()))
				.status(walletLog.getStatus())
				.errMsg(walletLog.getErrMsg())
				.build();
		}).collect(Collectors.toList());

	}


	/**
	 * 定时支付
	 */
	public void quartzPay() {
		List<String> acceptNos = walletLogExtDao.selectUnSendBatchNo();
		acceptNos.forEach(batchNo -> {
			List<WalletLog> walletLogs = walletLogExtDao.selectByBatchNo(batchNo);
			if(StringUtils.isEmpty(batchNo) || walletLogs.size() == 0){
				return;
			}
			// 请求网关
			try {
				PuDongHandler puDongHandler = handlerHelper.selectByWalletType(null);
				Tuple<GatewayMethod, PayInResp> rs = puDongHandler.pay(walletLogs);

				GatewayMethod method = rs.left;
				PayInResp payInResp = rs.right;
				for (WalletLog walletLog : walletLogs) {
					walletLogExtDao.updateStatusAndAcceptNo(walletLog.getId(),
						WalletLogStatus.PROCESSING.getValue(), payInResp.getAcceptNo(),
						method.getValue());
				}
			} catch (Exception e) {
				log.error("银行网关支付错误", e);
			}
		});

	}

	/**
	 * 定时更新支付状态
	 */
	@PostMq(routingKey = MqConstant.WALLET_PAY_RESULT)
	public List<PayStatusResp> quartzUpdate() {

		log.info("scheduler: 开始更新支付状态[银企直连]");

		List<AcceptNo> acceptNos = walletLogExtDao.selectUnFinish();

		List<WalletLog> result = acceptNos.stream().map(item -> {
			PuDongHandler handler = handlerHelper.selectByMethod(item.getRefMethod());
			List<WalletLog> walletLogs = handler.updatePayStatus(item.getAcceptNo()
				, item.getCreateTime());
			return walletLogs;
		}).reduce((rs, item) -> {
			rs.addAll(item);
			return rs;
		}).orElse(new ArrayList<>());

		String elecs = result.stream().map(rs -> rs.getElecChequeNo())
			.collect(Collectors.joining("|"));
		log.info("更新批次状态，批次数量= {}，更新笔数= {}，业务凭证号= {}", acceptNos.size(), result.size(), elecs);
		log.info("scheduler: 结束更新支付状态[银企直连]");

		if (result == null || result.size() == 0) {
			return new ArrayList<>();
		}
		return result.stream()
			.map(rs -> PayStatusResp.builder()
				.batchNo(rs.getBatchNo())
				.bizNo(rs.getBizNo())
				.transDate(DateUtil.formatDate(rs.getCreateTime()))
				.amount(rs.getAmount())
				.status(rs.getStatus())
				.errMsg(rs.getErrMsg())
				.build())
			.collect(Collectors.toList());
	}

	/**
	 * 查询钱包明细
	 */
	public WalletInfoResp queryWalletInfo(Long walletId) {
		WalletInfoRespBuilder builder = WalletInfoResp.builder();

		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
				, String.valueOf(walletId));
		}

		if (WalletType.COMPANY.getValue().byteValue() == wallet.getType()) {
			WalletCompany walletCompany = walletCompanyDao.selectByWalletId(walletId);
			builder.companyInfo(walletCompany);
		} else if (WalletType.PERSON.getValue().byteValue() == wallet.getType()) {
			WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
			builder.personInfo(walletPerson);
		}

		WalletCard walletCard = walletCardDao.selectByDef(walletId);

		return builder.wallet(wallet).defWalletCard(walletCard).build();
	}

	public WalletInfoResp queryWalletInfoByUserId(Long userId){
		WalletUser walletUser = walletUserDao.selectByPrimaryKey(userId);

		if (walletUser == null) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST, "user_id");
		}

		return queryWalletInfo(walletUser.getWalletId());
	}

	/**
	 * 开通未审核的钱包
	 */
	public Wallet createWallet(Byte type, String title, Byte source) {

		Wallet wallet = Wallet.builder()
			.type(type)
			.title(title)
			.walletBalance(0L)
			.rechargeAmount(0L)
			.rechargeCount(0)
			.payAmount(0L)
			.payCount(0)
			.source(source)
			.status(WalletStatus.WAIT_AUDIT.getValue())
			.createTime(new Date())
			.build();
		walletDao.insert(wallet);

		return wallet;
	}

	/**
	 * 查詢钱包流水
	 *
	 * @param walletId 钱包ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 */
	public Pagination<WalletLog> walletLogList(@ParamValid(nullable = false) Long walletId,
		Date startTime, Date endTime,
		@ParamValid(min = 1, max = SymbolConstant.QUERY_LIMIT) int limit,
		@ParamValid(min = 0) long offset, Boolean stat) {
		Date queryStartTime = null;

		if (null != startTime) {
			queryStartTime = DateUtil.getDate2(startTime);
		}

		Date queryEndTime = null;

		if (null != startTime) {
			queryEndTime = DateUtil.getDate(endTime);
		}

		List<WalletLog> data = walletLogDao.selectList(walletId, queryStartTime, queryEndTime,
			limit, offset);
		long total = Optional.ofNullable(stat).orElse(false) ? walletLogDao.selectCount(
			walletId, queryStartTime, queryEndTime) : 0L;

		return new Pagination.PaginationBuilder<WalletLog>().offset(offset).pageLimit(limit)
			.data(data)
			.total(total).build();
	}

	/**
	 * 查询绑定的银行卡列表
	 *
	 * @param walletId 钱包ID
	 */
	public List<WalletCard> bankCardList(@ParamValid(nullable = false) Long walletId) {
		return walletCardDao.selectByWalletId(walletId);
	}

	/**
	 * 绑定对工银行卡
	 *
	 * @param walletId 钱包id
	 * @param bankCode 银行代码
	 * @param bankAccount 银行帐号
	 * @param depositName 开户名
	 * @param isDef 是否默认银行卡: 1:是，2：否
	 * @param telephone 预留手机号
	 */
	public WalletCard bindBankCard(@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false, min = 12, max = 12) String bankCode,
		@ParamValid(nullable = false, min = 20, max = 32) String bankAccount,
		@ParamValid(nullable = false, min = 1, max = 256) String depositName,
		@EnumParamValid(valuableEnumClass = EnumDef.EnumDefBankCard.class) Integer isDef,
		@ParamValid(pattern = RegexUtil.REGEX_MOBILE) String telephone) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (null == wallet) {
			throw new WalletResponseException(
				WalletResponseCode.EnumWalletResponseCode.WALLET_NOT_EXIST);
		}

		BankCode bankCodeResult = bankCodeDao.selectByBankCode(bankCode);
		if (null == bankCodeResult) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_INVALID_PARAMS, "bank_code");
		}

		//更新已绑定的银行卡状态为已解绑
		walletCardDao.updateWalletCard(walletId, EnumDef.EnumCardBindStatus.UNBIND.getValue(),
			EnumDef.EnumCardBindStatus.BIND.getValue(), null,
			EnumDef.EnumDefBankCard.NO.getValue());

		Date now = new Date();

		WalletCard walletCard = WalletCard.builder().walletId(walletId).bankAccount(bankAccount)
			.bankCode(bankCode)
			.bankName(bankCodeResult.getClassName())
			.depositName(depositName)
			.depositBank(bankCodeResult.getBankName())
			.isDef(isDef.byteValue())
			.isPublic(EnumDef.EnumPublicAccount.YES.getValue().byteValue())
			.telephone(telephone)
			.lastUpdTime(now)
			.build();

		int effectRows = walletCardDao.replace(walletCard);
		if (effectRows < 1) {
			log.error("绑定银行卡失败, wallet: {}, effectRows: {}", JsonUtil.toJSON(wallet), effectRows);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}

		return walletCard;
	}

	/**
	 * 查询银行类别列表
	 */
	public List<BankClass> bankClassList() {
		return bankCodeDao.selectBankClassList();
	}

	/**
	 * 查询银行地区列表
	 *
	 * @param classCode 类别编码
	 */
	public List<BankArea> bankAreaList(String classCode) {
		return bankCodeDao.selectBankAreaList(classCode);
	}

	/**
	 * 查询银行支行列表
	 *
	 * @param classCode 类别编码
	 * @param areaCode 地区编码
	 */
	public List<Bank> bankList(String classCode, String areaCode) {
		return bankCodeDao.selectBankList(classCode, areaCode);
	}


}
