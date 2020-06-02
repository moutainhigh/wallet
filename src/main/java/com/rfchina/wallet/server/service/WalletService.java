package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.annotation.EnumParamValid;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.platform.common.utils.RegexUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.BankCodeDao;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletFinanceDao;
import com.rfchina.wallet.domain.mapper.ext.WalletOwnerDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletAuditType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletCardStatus;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletLevel;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletSource;
import com.rfchina.wallet.domain.misc.EnumDef.WalletStatus;
import com.rfchina.wallet.domain.misc.EnumDef.WalletType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.ApplyStatusChange;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.BankCodeCriteria;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletCompany;
import com.rfchina.wallet.domain.model.WalletFinance;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletOrderCriteria;
import com.rfchina.wallet.domain.model.WalletOrderCriteria.Criteria;
import com.rfchina.wallet.domain.model.WalletOwner;
import com.rfchina.wallet.domain.model.WalletPerson;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletUser;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.domain.model.ext.WalletCardExt;
import com.rfchina.wallet.server.mapper.ext.ApplyStatusChangeExtDao;
import com.rfchina.wallet.server.mapper.ext.GatewayTransExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletCompanyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletPersonExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletUserExtDao;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp.WalletInfoRespBuilder;
import com.rfchina.wallet.server.msic.EnumWallet.CardPro;
import com.rfchina.wallet.server.msic.EnumWallet.GwPayeeType;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class WalletService {

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private WalletCompanyExtDao walletCompanyDao;

	@Autowired
	private WalletPersonExtDao walletPersonDao;

	@Autowired
	private WalletUserExtDao walletUserDao;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private AppService appService;

	@Autowired
	private BankCodeDao bankCodeDao;

	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	@Autowired
	private ConfigService configService;

	@Autowired
	private GatewayTransService gatewayTransService;

	@Autowired
	private SeniorWalletService seniorWalletService;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private WalletFinanceDao walletFinanceDao;

	@Autowired
	private GatewayTransExtDao gatewayTransDao;

	@Autowired
	private ApplyStatusChangeExtDao applyStatusChangeExtDao;

	@Autowired
	private WalletEventService walletEventService;

	@Autowired
	private WalletOwnerDao walletOwnerDao;


	/**
	 * 查询出佣结果
	 */
	public List<PayStatusResp> queryWalletApply(String bizNo, String batchNo) {

		WalletOrderCriteria example = new WalletOrderCriteria();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isEmpty(bizNo)) {
			criteria.andBizNoEqualTo(bizNo);
		}
		if (!StringUtils.isEmpty(batchNo)) {
			criteria.andBatchNoEqualTo(batchNo);
		}
		example.setOrderByClause("id desc");
		List<WalletOrder> walletOrders = walletOrderDao.selectByExample(example);
		if (walletOrders.isEmpty()) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST,
				batchNo + "_" + bizNo);
		}

		return walletOrders.stream().map(walletOrder -> {
			PayStatusResp resp = new PayStatusResp();
			resp.setBizNo(walletOrder.getBizNo());
			resp.setBatchNo(walletOrder.getBatchNo());
			resp.setTransDate(DateUtil.formatDate(walletOrder.getCreateTime()));
			resp.setErrCode(walletOrder.getTunnelErrCode());
			resp.setUserErrMsg(walletOrder.getUserErrMsg());
			resp.setSysErrMsg(walletOrder.getTunnelErrMsg());
			resp.setCreateTime(walletOrder.getCreateTime());
			resp.setLanchTime(walletOrder.getStartTime());
			resp.setEndTime(walletOrder.getEndTime());
			resp.setTransDate(DateUtil.formatDate(walletOrder.getCreateTime()));
			resp.setWalletId(walletOrder.getWalletId());
			resp.setNote(walletOrder.getNote());
			resp.setAmount(walletOrder.getAmount());
			resp.setStatus(walletOrder.getStatus());

			WalletFinance walletFinance = walletFinanceDao.selectByOrderId(walletOrder.getId());
			Byte payeeType = walletFinance.getCardPro().byteValue() == CardPro.COMPANY.getValue()
				? GwPayeeType.COMPANY.getValue() : GwPayeeType.PERSON.getValue();
			resp.setPayeeAccount(walletFinance.getPayeeAccount());
			resp.setPayeeName(walletFinance.getPayeeName());
			resp.setPayeeType(payeeType);
			resp.setRemark(walletOrder.getRemark());
			resp.setPayeeBankCode(walletFinance.getPayeeBankCode());
			if (!StringUtils.isEmpty(walletFinance.getPayeeBankCode())) {
				BankCode bankCode = bankCodeDao.selectByBankCode(walletFinance.getPayeeBankCode());
				resp.setPayeeBankInfo(bankCode);
			}

			GatewayTrans trans = gatewayTransDao.selectByPrimaryKey(walletFinance.getCurrTransId());
			resp.setElecChequeNo(trans.getElecChequeNo());
			resp.setErrCode(trans.getErrCode());
			resp.setUserErrMsg(trans.getUserErrMsg());
			resp.setSysErrMsg(trans.getSysErrMsg());
			resp.setEndTime(trans.getEndTime());
			resp.setBizTime(trans.getBizTime());

			return resp;
		}).collect(Collectors.toList());

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

		if (wallet.getLevel() == EnumWalletLevel.SENIOR.getValue().byteValue()) {
			try {
				WalletTunnel walletTunnel = seniorWalletService
					.getWalletTunnelInfo(TunnelType.YUNST.getValue(), walletId);
				builder.walletTunnel(walletTunnel);
			} catch (Exception e) {
				log.error("获取高级钱包渠道信息失败 walletId:{}", walletId);
				throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
					, String.valueOf(walletId));
			}
		}

		if (WalletType.COMPANY.getValue().byteValue() == wallet.getType()) {
			WalletCompany walletCompany = walletCompanyDao.selectByWalletId(walletId);
			builder.companyInfo(walletCompany);
		} else if (WalletType.PERSON.getValue().byteValue() == wallet.getType()) {
			WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
			builder.personInfo(walletPerson);
		}

		List<WalletCard> walletCardList = walletCardDao
			.selectByWalletId(walletId);

		WalletCard defCard = Objects.nonNull(walletCardList) && !walletCardList.isEmpty() ?
			walletCardDao.selectDefCardByWalletId(walletId) : null;

		int bankCardCount = walletCardDao
			.selectCountByWalletId(walletId, EnumWalletCardStatus.BIND.getValue());
		return builder.wallet(wallet).defWalletCard(defCard)
			.bankCardCount(bankCardCount)
			.build();
	}

	public WalletInfoResp queryWalletInfoByUserId(Long userId) {
		WalletUser walletUser = walletUserDao.selectByPrimaryKey(userId);

		if (walletUser == null) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST,
				"user_id");
		}

		return queryWalletInfo(walletUser.getWalletId());
	}

	public Pagination<Wallet> walletList(String title, Byte type, Byte walletLevel,
		Byte status, Integer limit, Integer offset, Boolean stat) {
		List<Wallet> list = walletDao
			.selectByCondition(title, type, walletLevel, status, limit, offset);

		Long total = 0L;
		if (stat) {
			total = walletDao.countByCondition(title, type, walletLevel, status);
		}
		return new Pagination.PaginationBuilder<Wallet>()
			.total(total)
			.data(list)
			.offset(offset)
			.pageLimit(limit)
			.build();
	}


	/**
	 * 开通未审核的钱包
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Wallet createMchWallet(Byte type, String title, Byte source, String mchId,
		String companyName, String tel, String email) throws Exception {

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
			.level(EnumWalletLevel.SENIOR.getValue())
			.build();
		walletDao.insertSelective(wallet);

		WalletCompany walletCompany = WalletCompany.builder()
			.walletId(wallet.getId())
			.companyName(companyName)
			.email(email)
			.tel(tel)
			.createTime(new Date())
			.build();
		walletCompanyDao.insertSelective(walletCompany);

		// 创建通联会员
		seniorWalletService
			.createTunnel(TunnelType.YUNST.getValue().intValue(), wallet.getId(), source);

		// 关联商家和钱包
		WalletOwner walletOwner = WalletOwner.builder()
			.walletId(wallet.getId())
			.ownerType(WalletSource.FHT_CORP.getValue())
			.ownerId(mchId)
			.deleted(EnumDef.Deleted.NO.getValue())
			.build();
		walletOwnerDao.insertSelective(walletOwner);

		// 发送钱包事件
		walletEventService.sendEventMq(EnumDef.WalletEventType.CREATE, wallet.getId()
			, wallet.getLevel(), wallet.getStatus(), Arrays.asList(walletOwner));
		return wallet;
	}


	/**
	 * 查詢钱包流水
	 *
	 * @param walletId 钱包ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 */
	public Pagination<WalletOrder> walletOrderList(@ParamValid(nullable = false) Long walletId,
		Date startTime, Date endTime,
		@ParamValid(min = 1, max = SymbolConstant.QUERY_LIMIT) int limit,
		@ParamValid(min = 0) int offset, Boolean stat) {

		WalletOrderCriteria example = new WalletOrderCriteria();
		example.setOrderByClause("order by create_time desc");

		Criteria criteria = example.createCriteria();
		criteria.andWalletIdEqualTo(walletId);
		if (null != startTime) {
			criteria.andCreateTimeGreaterThanOrEqualTo(DateUtil.getDate2(startTime));
		}
		if (null != startTime) {
			criteria.andCreateTimeLessThanOrEqualTo(DateUtil.getDate(endTime));
		}

		List<WalletOrder> data = walletOrderDao
			.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
		long total = Optional.ofNullable(stat).orElse(false) ?
			walletOrderDao.countByExample(example) : 0;

		return new Pagination.PaginationBuilder<WalletOrder>()
			.offset(offset)
			.pageLimit(limit)
			.data(data)
			.total(total)
			.build();
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
	public WalletCardExt bindBankCard(@ParamValid(nullable = false) Long walletId,
		@ParamValid(nullable = false, min = 12, max = 12) String bankCode,
		@ParamValid(nullable = false, min = 20, max = 32) String bankAccount,
		@ParamValid(nullable = false, min = 1, max = 256) String depositName,
		@EnumParamValid(valuableEnumClass = EnumDef.EnumDefBankCard.class) Integer isDef,
		@ParamValid(pattern = RegexUtil.REGEX_MOBILE) String telephone) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (null == wallet) {
			throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
		}

		BankCode bankCodeResult = bankCodeDao.selectByBankCode(bankCode);
		if (null == bankCodeResult) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_INVALID_PARAMS, "bank_code");
		}

		//更新已绑定的银行卡状态为已解绑
		int effectRows = walletCardDao
			.updateWalletCard(walletId, EnumDef.EnumWalletCardStatus.UNBIND.getValue(),
				EnumDef.EnumWalletCardStatus.BIND.getValue(), null,
				EnumDef.EnumDefBankCard.NO.getValue());

		//首次绑定银行卡
		int firstBind =
			(0 == effectRows) ? EnumDef.FirstBindBankCard.YES.getValue()
				: EnumDef.FirstBindBankCard.NO.getValue();

		Date now = new Date();

		WalletCard walletCard = WalletCard.builder()
			.walletId(walletId)
			.bankAccount(bankAccount)
			.bankCode(bankCode)
			.bankName(bankCodeResult.getClassName())
			.depositName(depositName)
			.depositBank(bankCodeResult.getBankName())
			.isDef(isDef.byteValue())
			.isPublic(EnumDef.EnumPublicAccount.YES.getValue().byteValue())
			.telephone(telephone)
			.lastUpdTime(now)
			.build();

		effectRows = walletCardDao.insertSelective(walletCard);
		if (effectRows < 1) {
			log.error("绑定银行卡失败, wallet: {}, effectRows: {}", JsonUtil.toJSON(wallet), effectRows);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}

		WalletCardExt walletCardExt = new WalletCardExt(walletCard);
		walletCardExt.setFirstBind(firstBind);
		return walletCardExt;
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

	public BankCode bank(String bankCode) {
		BankCodeCriteria bankCodeCriteria = new BankCodeCriteria();
		bankCodeCriteria.createCriteria().andBankCodeEqualTo(bankCode);
		List<BankCode> bankCodeList = bankCodeDao.selectByExample(bankCodeCriteria);
		if (bankCodeList.isEmpty()) {
			return null;
		}
		return bankCodeList.get(0);
	}

	/**
	 * 富慧通审核通过企业商家钱包
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void auditWalletCompany(Long walletId, String companyName, Byte status,
		Long auditType, String tel, String email) {

		WalletCompany walletCompany = walletCompanyDao.selectByWalletId(walletId);
		if (status.byteValue() == WalletStatus.ACTIVE.getValue().byteValue()) {
			boolean isUpdate = walletCompany != null;
			walletCompany = isUpdate ? walletCompany : new WalletCompany();
			walletCompany.setWalletId(walletId);
			walletCompany.setCompanyName(companyName);
			walletCompany.setEmail(email);
			walletCompany.setTel(tel);

			if (isUpdate) {
				walletCompanyDao.updateByPrimaryKeySelective(walletCompany);
			} else {
				walletCompanyDao.insertSelective(walletCompany);
			}
		}
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		wallet.setStatus(status);
		wallet.setAuditType(auditType.byteValue());
		walletDao.updateByPrimaryKeySelective(wallet);

		// 发送钱包事件
		walletEventService.sendEventMq(EnumDef.WalletEventType.CHANGE, wallet.getId()
			, wallet.getLevel(), wallet.getStatus());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void setWalletApplyStatus(Long applyId, String userId, String user, String comment) {
		if (0 >= walletOrderDao.setApplyStatusFail(applyId)) {
			throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"更新申请单状态失败");
		}

		ApplyStatusChange change = ApplyStatusChange.builder()
			.applyId(applyId)
			.auditStatus(OrderStatus.FAIL.getValue().toString())
			.auditStatusDesc(OrderStatus.FAIL.getValueName())
			.auditUserId(userId)
			.auditUser(user)
			.auditComment(comment)
			.auditTime(new Date())
			.createTime(new Date())
			.oldVal(OrderStatus.SUCC.getValue().toString())
			.newVal(OrderStatus.FAIL.getValue().toString())
			.build();
		if (0 >= applyStatusChangeExtDao.insertSelective(change)) {
			throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"插入更新记录失败");
		}
	}

	public void bindMchWallet(Long walletId, WalletSource source, String mchId) {
		WalletOwner walletOwner = walletOwnerDao
			.selectByWalletOwnerId(walletId, source.getValue(), mchId);
		if (Optional.ofNullable(walletOwner).isPresent()) {
			return;
		}
		walletOwner = WalletOwner.builder()
			.walletId(walletId)
			.ownerType(source.getValue())
			.ownerId(mchId)
			.deleted(EnumDef.Deleted.NO.getValue())
			.build();
		walletOwnerDao.insertSelective(walletOwner);
		// 发送钱包事件
		walletEventService.sendEventMq(EnumDef.WalletEventType.BIND, walletId);
	}

}
