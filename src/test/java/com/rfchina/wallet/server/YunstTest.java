package com.rfchina.wallet.server;

import com.rfchina.wallet.server.bank.yunst.response.YunstMemberInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class YunstTest extends YunstBaseTest{

    @Autowired
    private com.rfchina.internal.api.ApiClient internalApiClient;

    @Test
    public void testCreateMember(){
        String bizUserId = "Test" + randomPersonCompany() + System.currentTimeMillis();
        Integer type = bizUserId.startsWith("TestC") ? 1 : 2;
        createMember(bizUserId,type);
    }


    private String randomPersonCompany() {
        return new Random().nextInt(2) < 1 ? "U" : "C";
    }
}
