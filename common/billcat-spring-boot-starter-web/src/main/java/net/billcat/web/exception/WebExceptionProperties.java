package net.billcat.web.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web 异常的配置
 */
@Data
@ConfigurationProperties(prefix = WebExceptionProperties.PREFIX)
public class WebExceptionProperties {

	public static final String PREFIX = "billcat.web.exception";

	private ExceptionResolverConfig resolverConfig = new ExceptionResolverConfig();

	@Data
	public static class ExceptionResolverConfig {

		/**
		 * 是否隐藏异常的详细信息。
		 * <p>
		 * 仅针对于 GlobalHandlerExceptionResolver 中响应状态码为 500 的异常。
		 */
		private Boolean hideExceptionDetails = true;

		/**
		 * 设置隐藏后的提示信息。
		 */
		private String hiddenMessage = "系统异常，请联系管理员";

		/**
		 * 未设置隐藏异常信息时，空指针异常的提示信息。
		 */
		private String npeErrorMessage = "空指针异常!";

	}

}