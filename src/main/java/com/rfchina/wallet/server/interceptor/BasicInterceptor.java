package com.rfchina.wallet.server.interceptor;

import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author huangtiande@rfchina.com
 * @date 2016/8/4
 */
@Component
public class BasicInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SessionThreadLocal sessionThreadLocal = null;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        sessionThreadLocal.addTimestamp(Optional.ofNullable(request.getParameter("timestamp"))
            .orElseThrow(
                () -> new RfchinaResponseException(
                    ResponseCode.EnumResponseCode.COMMON_MISSING_PARAMS, "timestamp")
            )
        );
        sessionThreadLocal.addSign(Optional.ofNullable(request.getParameter("sign"))
            .orElseThrow(
                () -> new RfchinaResponseException(
                    ResponseCode.EnumResponseCode.COMMON_MISSING_PARAMS, "sign")
            )
        );
        return true;
    }
}
