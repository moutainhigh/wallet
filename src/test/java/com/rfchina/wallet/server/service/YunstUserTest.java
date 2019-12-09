package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef.EnumIdType;
import com.rfchina.wallet.domain.misc.EnumDef.EnumVerifyCodeType;
import com.rfchina.wallet.server.SpringBaseTest;
import com.rfchina.wallet.server.bank.yunst.response.result.ApplyBindBankCardResp;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstPersonSetRealNameResult;
import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class YunstUserTest extends SpringBaseTest {

	private Long WALLET_ID = 10035L;
	public String CARD_PHONE = "13710819640";
	public String CARD_NO = "6214850201481956";
	private String ID_NO = "440923198711033434";
	private String NAME = "观富昌";
	private String JUMP_URL = "http://www.baidu.com";
	private String BIZ_USER_ID = "";

	@Spy
	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito
			.doReturn(IdGenerator.createBizId("J" + WALLET_ID + "_", 20, (id) -> true))
			.when(yunstUserHandler)
			.transferToYunstBizUserFormat(Mockito.any(), Mockito.any(), Mockito.anyString())
		;
	}


	@Test
	public void regist() throws Exception {
		// 创建会员
		Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = yunstUserHandler
			.createMember(WALLET_ID, (byte) 3);
		BIZ_USER_ID = member.left.getBizUserId();
		// 实名
		YunstPersonSetRealNameResult certRs = yunstUserHandler.personCertification(
			BIZ_USER_ID, NAME, EnumIdType.ID_CARD.getValue().longValue(), ID_NO);
		log.info("{}", JsonUtil.toJSON(certRs));
		// 绑手机
		yunstUserHandler.sendVerificationCode(BIZ_USER_ID, CARD_PHONE,
			EnumVerifyCodeType.YUNST_BIND_PHONE.getValue());
		yunstUserHandler.bindPhone(BIZ_USER_ID, CARD_PHONE, "11111");
		// 会员协议
		String url = yunstUserHandler
			.generateSignContractUrl(BIZ_USER_ID, JUMP_URL);
		log.info("会员协议 URL = {}", url);
		nextRequire();
		// 设置密码
		url = yunstUserHandler.generatePersonSetPayPasswordUrl(BIZ_USER_ID, CARD_PHONE,
			NAME, EnumIdType.ID_CARD.getValue().longValue(), ID_NO, JUMP_URL);
		log.info("设置密码 URL = {}", url);
		nextRequire();
		// 申请绑定银行卡
		ApplyBindBankCardResp cardRs = yunstUserHandler.applyBindBankCard(BIZ_USER_ID,
			CARD_NO, NAME, CARD_PHONE, EnumIdType.ID_CARD.getValue().longValue(), ID_NO, null,
			null);
		String code = nextRequire();
		yunstUserHandler.bindBankCard(BIZ_USER_ID, cardRs.getTranceNum(),
			cardRs.getTransDate(), CARD_PHONE, null, null, code);
		// 解绑
		yunstUserHandler.unbindBankCard(BIZ_USER_ID, CARD_NO);

	}

	private String nextRequire() throws Exception {
		String input = "";
		try {
			while (input.length() == 0) {
				Thread.sleep(1000);
			}
			return input;
		} catch (Exception e) {
			throw e;
		}
	}

}
