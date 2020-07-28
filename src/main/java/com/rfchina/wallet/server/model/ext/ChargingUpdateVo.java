package com.rfchina.wallet.server.model.ext;

import com.rfchina.wallet.server.msic.EnumWallet.CollectPayType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargingUpdateVo {

	private String orderNo;
	private CollectPayType collectPayType;
	private String acctType;
	private Long amount;
	private Date createTime;

}
