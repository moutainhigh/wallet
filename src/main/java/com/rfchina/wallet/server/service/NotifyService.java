package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.utils.Valuable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class NotifyService {

    public void yunstNotify(Map<String, String> params) {
        String service = params.get("service");
        String methodName = params.get("method");

        if (StringUtils.isBlank(service) && StringUtils.isBlank(methodName)){
            log.error("云商通回调参数有误,缺少service 或 method");
            return;
        }
        if (YunstServiceName.MEMBER.getValue().equals(service)){
            if (YunstMethodName.VERIFY_RESULT.getValue().equals(methodName)){
                this.handleVerfiyResult(params);
            }else if (YunstMethodName.SIGN_CONTRACT.getValue().equals(methodName)){
                this.handleSignContractResult(params);
            }else {
                log.error("云商通回调,未知method参数:{}",methodName);
            }
        }else if (YunstServiceName.ORDER.getValue().equals(service)){

        }else{
            log.error("云商通回调,service:{}",service);
        }

        return;
    }






    private void handleVerfiyResult(Map<String, String> params){
        log.info("处理企业信息审核结果通知");
    }


    private void handleSignContractResult(Map<String, String> params){
        log.info("处理会员电子签约通知");
    }



    public enum YunstServiceName implements Valuable<String> {
        MEMBER("MemberService"), ORDER("OrderService");

        private String value;

        YunstServiceName(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }



    public enum YunstMethodName implements Valuable<String> {
        VERIFY_RESULT("verifyResult"),
        SIGN_CONTRACT("signContract");

        private String value;

        YunstMethodName(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

}
