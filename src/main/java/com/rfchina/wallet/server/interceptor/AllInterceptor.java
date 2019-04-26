package com.rfchina.wallet.server.interceptor;

import com.rfchina.passport.misc.SessionThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangtiande@rfchina.com
 * @date 2016/8/4
 */
@Component
public class AllInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private SessionThreadLocal sessionThreadLocal = null;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		sessionThreadLocal.addRequestParameters(request.getParameterMap());
		sessionThreadLocal.addRequestPath(request.getServletPath() == null ?
				request.getPathInfo() :
				request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo()));
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		sessionThreadLocal.remove();
	}
}
