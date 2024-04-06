package net.billcat.web.exception;

import lombok.RequiredArgsConstructor;
import net.billcat.common.exception.handler.GlobalExceptionHandler;
import net.billcat.web.exception.handler.DoNothingGlobalExceptionHandler;
import net.billcat.web.exception.resolver.GlobalHandlerExceptionResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties({ WebExceptionProperties.class })
public class ExceptionAutoConfiguration {

	/**
	 * 什么都不做的异常处理器
	 * @return {@link DoNothingGlobalExceptionHandler}
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	public GlobalExceptionHandler doNothingGlobalExceptionHandler() {
		return new DoNothingGlobalExceptionHandler();
	}

	/**
	 * 默认的异常处理器
	 * @return GlobalHandlerExceptionResolver
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalHandlerExceptionResolver.class)
	public GlobalHandlerExceptionResolver globalExceptionHandlerResolver(GlobalExceptionHandler globalExceptionHandler,
			WebExceptionProperties webExceptionProperties) {
		GlobalHandlerExceptionResolver globalHandlerExceptionResolver = new GlobalHandlerExceptionResolver(
				globalExceptionHandler);
		WebExceptionProperties.ExceptionResolverConfig resolverConfig = webExceptionProperties.getResolverConfig();
		globalHandlerExceptionResolver.setHideExceptionDetails(resolverConfig.getHideExceptionDetails());
		globalHandlerExceptionResolver.setHiddenMessage(resolverConfig.getHiddenMessage());
		globalHandlerExceptionResolver.setNpeErrorMessage(resolverConfig.getNpeErrorMessage());
		return globalHandlerExceptionResolver;
	}

}
