package com.rfchina.wallet.server.bank.pudong.domain.common;


import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * @author nzm
 */
@Data
@XmlRootElement(name = "packet")
public class ResponsePacket {
	private ResponseHeader head;
	private SignedBody body;
}
