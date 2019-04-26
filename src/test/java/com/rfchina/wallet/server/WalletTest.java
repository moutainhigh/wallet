package com.rfchina.wallet.server;

import com.alibaba.fastjson.JSON;
import com.rfchina.internal.api.request.app.GetAccessTokenRequest;
import com.rfchina.internal.api.response.ResponseData;
import com.rfchina.internal.api.response.model.app.GetAccessTokenReponseModel;
import com.rfchina.internal.api.response.model.app.base.AccessTokenModel;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WalletTest extends WalletBaseTest{

    @Autowired
    private com.rfchina.internal.api.ApiClient internalApiClient;

    @Autowired
    private ConfigService configService;

    @Test
    public void testCreateWallet(){
        createWallet((byte)2, "个人钱包1", (byte)3);
    }

    @Test
    public void testWalletLogList(){
        walletLogList(1L, null, null, 10, 0, true);
    }

    @Test
    public void testBindingBankCardList(){
        bindingBankCardList(1L);
    }

    @Test
    public void testBindBankCard(){
        bindBankCard(1L, "402336100092", "12345678901234567890", "浙江德清农村商业银行股份有限公司新安支行", "哈哈", 1, "");
    }


    /*@Test
    public void test(){
        String value = "{\"accessToken\":\"26CBC964F7E75B56E61C21EFB3B58DEA70D7147ADCE98FE0653AAA8CB11DC23F188ECC3366EECF910C4EC65236DE98E819F1CF6AE6F97F1A97DFF1D3EDD1448D\",\"expire_time\":1628191801000,\"refresh_token\":\"0586360ECF7F2658366F09A091CED614DD9768A66D7D8890C272C347C5CF00F804539ED8594987ABC32D532951746C7CE1769BF362C86ECE97DFF1D3EDD1448D\"}";
        GetAccessTokenReponseModel object = JSON.parseObject(value, GetAccessTokenReponseModel.class);
        log.info(object.getAccessToken());
    }*/
}
