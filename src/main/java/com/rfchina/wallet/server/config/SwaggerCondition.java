package com.rfchina.wallet.server.config;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class SwaggerCondition implements Condition {

	public static final String SWAGGER_DOCKET_ENABLED = "swagger.docket.enabled";

	@Override
	public boolean matches(ConditionContext conditionContext,
		AnnotatedTypeMetadata annotatedTypeMetadata) {

		String enabled = conditionContext.getEnvironment().getProperty(SWAGGER_DOCKET_ENABLED);
		return "1".equals(enabled);
	}
}
