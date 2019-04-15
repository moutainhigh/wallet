package com.rfchina.wallet.server.bank.pudong.domain.common;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@XmlRootElement(name = "packet")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPacket {
	private RequestHeader head;
	private SignedBody body;
}
