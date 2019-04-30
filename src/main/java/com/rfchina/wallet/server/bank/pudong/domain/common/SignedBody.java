package com.rfchina.wallet.server.bank.pudong.domain.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nzm
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignedBody {
	private String signature;
}
