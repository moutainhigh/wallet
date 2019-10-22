package com.rfchina.wallet.server.model.ext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class SLWalletMqMessage {
	private Long walletId;
	private String checkTime;
	private Boolean isPass;
	private String failReason;
}
