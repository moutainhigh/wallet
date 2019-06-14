package com.rfchina.wallet.server.service;

import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.server.mapper.ext.GatewayTransExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
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
	private WalletApplyExtDao walletApplyExtDao;


	public GatewayTrans selOrCrtTrans(WalletApply walletApply) {
		GatewayTrans gatewayTrans = null;

		if (walletApply.getCurrTransId() != null) {
			gatewayTrans = gatewayTransDao.selectByPrimaryKey(walletApply.getCurrTransId());
		}

		if (gatewayTrans == null) {
			gatewayTrans = createTrans(walletApply);
			walletApply.setCurrTransId(gatewayTrans.getId());
			walletApplyExtDao.updateCurrTransId(walletApply.getId(), gatewayTrans.getId());
		}

		return gatewayTrans;
	}

	public List<GatewayTrans> getTransIds(List<Long> tranIds) {
		if(tranIds == null || tranIds.isEmpty()){
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

	public GatewayTrans createTrans(WalletApply walletApply) {

		GatewayTrans gatewayTrans = GatewayTrans.builder()
			.walletApplyId(walletApply.getId())
			.payerAccount(walletApply.getPayerAccount())
			.payeeAccount(walletApply.getPayeeAccount())
			.payeeName(walletApply.getPayeeName())
			.payeeType(walletApply.getPayeeType())
			.bankCode(walletApply.getPayeeBankCode())
			.amount(walletApply.getAmount())
			.payPurpose(walletApply.getPayPurpose())
			.note(walletApply.getNote())
			.createTime(new Date())
			.build();
		gatewayTransDao.insert(gatewayTrans);
		return gatewayTrans;
	}
}
