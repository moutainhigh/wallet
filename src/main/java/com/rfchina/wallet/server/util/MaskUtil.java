package com.rfchina.wallet.server.util;

import com.rfchina.platform.common.utils.StringUtil;
import com.rfchina.wallet.domain.model.WalletTunnel;

public class MaskUtil {

	public static String maskMobile(String mobile) {
		if (null == mobile) {
			return null;
		}
		return StringUtil.mask(mobile, 3, 4, '*');
	}

	public static String maskName(String name) {
		if (null == name) {
			return null;
		}
		return StringUtil.mask(name, 1, 0, '*');
	}

	public static String maskBankAccount(String account) {
		if (null == account) {
			return null;
		}
		return StringUtil.mask(account, 0, 4, '*');
	}

	public static String maskIdNo(String idNo) {
		if (null == idNo) {
			return null;
		}
		return StringUtil.mask(idNo, 0, 4, '*');
	}

	public static String maskMobileV2(String mobile) {
		if (null == mobile) {
			return null;
		}
		return StringUtil.mask(mobile, 0, 4, '*');
	}

	public static WalletTunnel maskWalletTunnel(WalletTunnel walletTunnel) {
		return WalletTunnel.builder().id(walletTunnel.getWalletId())
			.status(walletTunnel.getStatus()).walletId(walletTunnel.getWalletId())
			.isSignContact(walletTunnel.getIsSignContact())
			.hasPayPassword(walletTunnel.getHasPayPassword())
			.securityTel(maskMobile(walletTunnel.getSecurityTel())).build();
	}
}
