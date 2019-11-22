package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletFinance;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.mapper.ext.GatewayTransExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletFinanceExtDao;
import com.rfchina.wallet.server.msic.EnumWallet.CardPro;
import com.rfchina.wallet.server.msic.EnumWallet.GwPayeeType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GatewayTransService {

	@Autowired
	private GatewayTransExtDao gatewayTransDao;

	@Autowired
	private WalletFinanceExtDao walletFinanceDao;


	public GatewayTrans selOrCrtTrans(WalletOrder walletOrder, WalletFinance walletFinance) {
		GatewayTrans gatewayTrans = null;

		if (walletFinance.getCurrTransId() != null) {
			gatewayTrans = gatewayTransDao.selectByPrimaryKey(walletFinance.getCurrTransId());
		}

		if (gatewayTrans == null) {
			gatewayTrans = createTrans(walletOrder, walletFinance);
			walletFinance.setCurrTransId(gatewayTrans.getId());
			walletFinanceDao.updateByPrimaryKeySelective(walletFinance);
		}

		return gatewayTrans;
	}

	public List<GatewayTrans> getTransIds(List<Long> tranIds) {
		if (tranIds == null || tranIds.isEmpty()) {
			return new ArrayList<>();
		}
		return gatewayTransDao.selectByIds(tranIds);
	}

	/**
	 * 判断凭证号是否已存在
	 */
	public boolean existElecNo(String elecNo) {
		Integer count = gatewayTransDao.selectCountByElecNo(elecNo);
		return count > 0;
	}

	public void updateTrans(GatewayTrans gatewayTrans) {
		gatewayTransDao.updateByPrimaryKey(gatewayTrans);
	}

	public GatewayTrans createTrans(WalletOrder walletOrder, WalletFinance walletFinance) {

		Byte payeeType = walletFinance.getCardPro().byteValue() == CardPro.COMPANY.getValue()
			? GwPayeeType.COMPANY.getValue() : GwPayeeType.PERSON.getValue();
		GatewayTrans gatewayTrans = GatewayTrans.builder()
			.financeId(walletFinance.getId())
			.payerAccount(walletFinance.getPayerAccount())
			.payeeAccount(walletFinance.getPayeeAccount())
			.payeeName(walletFinance.getPayeeName())
			.payeeType(payeeType)
			.bankCode(walletFinance.getPayeeBankCode())
			.amount(walletOrder.getAmount())
			.payPurpose(walletFinance.getPayPurpose())
			.note(walletOrder.getNote())
			.createTime(new Date())
			.build();
		gatewayTransDao.insert(gatewayTrans);
		return gatewayTrans;
	}
}
