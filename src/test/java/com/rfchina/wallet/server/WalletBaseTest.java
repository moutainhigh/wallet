package com.rfchina.wallet.server;

import com.rfchina.platform.common.security.SecurityCoder;
import com.rfchina.platform.common.utils.SignUtil;
import com.rfchina.platform.unittest.BaseTest;
import com.rfchina.wallet.server.msic.UrlConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class WalletBaseTest extends BaseTest {
    @Value("${test.wallet.web.uri}")
    protected String BASE_URL;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Autowired
    protected MockHttpSession session;

    protected Map<String, Object> createWallet(Byte type, String title, Byte source){
        Map<String, String> params = new HashMap<>();

        params.put("access_token", getAccessToken(appId, appSecret));
        params.put("type", String.valueOf(type));
        params.put("title", title);
        params.put("source", String.valueOf(source));

        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);

        return postAndValidateSuccessCode(BASE_URL, UrlConstant.CREATE_WALLET, params);
    }

    protected Map<String, Object> walletLogList(Long walletId, String startTime, String endTime, int limit, long offset, Boolean stat){
        Map<String, String> params = new HashMap<>();

        params.put("access_token", getAccessToken(appId, appSecret));
        params.put("wallet_id", String.valueOf(walletId));
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("stat", String.valueOf(stat));

        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);

        Optional.ofNullable(startTime).ifPresent( o -> params.put("start_time", startTime) );
        Optional.ofNullable(endTime).ifPresent( o -> params.put("end_time", endTime) );
        return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_LOG_LIST, params);
    }

    protected Map<String, Object> bindingBankCardList(Long walletId){
        Map<String, String> params = new HashMap<>();
        params.put("access_token", getAccessToken(appId, appSecret));
        params.put("wallet_id", String.valueOf(walletId));

        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);

        return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_CARD_LIST, params);
    }

    protected Map<String, Object> bindBankCard(Long walletId, String bankCode, String bankAccount, String depositName, Integer isDef,
                                               String telephone){
        Map<String, String> params = new HashMap<>();
        params.put("access_token", getAccessToken(appId, appSecret));
        params.put("wallet_id", String.valueOf(walletId));
        params.put("bank_code", bankCode);
        params.put("bank_account", bankAccount);
        params.put("deposit_name", depositName);
        params.put("is_def", String.valueOf(isDef));
        params.put("telephone", telephone);

        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);

        return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_CARD_BIND, params);
    }

    protected Map<String, Object> bankClassList(){
        Map<String, String> params = new HashMap<>();
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);
        return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_CLASS_LIST, params);
    }

    protected Map<String, Object> bankAreaList(String classCode){
        Map<String, String> params = new HashMap<>();
        params.put("class_code", classCode);
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);
        return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_AREA_LIST, params);
    }

    protected Map<String, Object> bankList(String classCode, String areaCode){
        Map<String, String> params = new HashMap<>();
        params.put("class_code", classCode);
        params.put("area_code", areaCode);
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);
        return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_BANK_LIST, params);
    }

    protected Map<String, Object> queryWalletByUserId(Long userId){
        Map<String, String> params = new HashMap<>();
        params.put("access_token", getAccessToken(appId, appSecret));
        params.put("user_id", userId.toString());
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);
        return postAndValidateSuccessCode(BASE_URL, UrlConstant.WALLET_QUERY_INFO_BY_UID, params);
    }

}
