package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.ext.*;
import com.rfchina.wallet.domain.model.*;
import com.rfchina.wallet.server.mapper.ext.WalletUserExtDao;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp.WalletInfoRespBuilder;
import com.rfchina.wallet.server.msic.EnumWallet.WalletStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public WalletInfoResp queryWalletInfo(String accessToken, Long walletId) {
		WalletInfoRespBuilder builder = WalletInfoResp.builder();

		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST);
		}

		WalletUser walletUser = walletUserDao.selectByWalletId(walletId);

		if (WalletType.COMPANY.getValue().byteValue() == wallet.getType()) {
			WalletCompany walletCompany = walletCompanyDao.selectByWalletId(walletId);
			builder.companyInfo(walletCompany);
		} else if (WalletType.PERSON.getValue().byteValue() == wallet.getType()) {
			WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
			builder.personInfo(walletPerson);
		}

		return builder.wallet(wallet).userInfo(walletUser).build();
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
	 * @param walletId		钱包ID
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @param limit
	 * @param offset
	 * @param stat
	 * @return
	 */
	public Pagination<WalletLog> walletLogList(@ParamValid(nullable = false) Long walletId, Date startTime, Date endTime, @ParamValid(min = 1, max = SymbolConstant.QUERY_LIMIT) int limit, @ParamValid(min = 0) long offset, Boolean stat){
		Date queryStartTime = DateUtil.getDate2(startTime);
		Date queryEndTime = DateUtil.getDate(endTime);
		return new Pagination.PaginationBuilder<WalletLog>().offset(offset).pageLimit(limit)
				.data(walletLogDao.selectList(walletId, queryStartTime, queryEndTime, limit, offset))
				.total(Optional.ofNullable(stat).orElse(false) ? walletLogDao.selectCount(walletId, queryStartTime, queryEndTime) : 0L).build();
	}

	/**
	 * 查询绑定的银行卡列表
	 * @param walletId		钱包ID
	 * @return
	 */
	public List<WalletCard> bankCardList(@ParamValid(nullable = false) Long walletId){
		return walletCardDao.selectByWalletId(walletId);
	}
}
