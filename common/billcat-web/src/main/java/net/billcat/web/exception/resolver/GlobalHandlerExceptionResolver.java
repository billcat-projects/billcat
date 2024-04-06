package net.billcat.web.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.billcat.common.exception.handler.GlobalExceptionHandler;
import net.billcat.common.model.result.R;
import net.billcat.common.model.result.SystemResultCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Setter
@Order
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalHandlerExceptionResolver {

	private final GlobalExceptionHandler globalExceptionHandler;

	/**
	 * 隐藏异常的详细信息。
	 */
	private Boolean hideExceptionDetails = true;

	/**
	 * 设置隐藏后的提示信息。
	 */
	private String hiddenMessage = "系统异常，请联系管理员";

	private String npeErrorMessage = "空指针异常!";

	private boolean isHideExceptionDetails() {
		return Boolean.TRUE.equals(this.hideExceptionDetails);
	}

	/**
	 * 全局异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleGlobalException(Exception e, HttpServletRequest request) {
		log.error(String.format("请求地址: %s, 全局异常信息 ex=%s", request.getRequestURI(), e.getMessage()), e);
		this.globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = isHideExceptionDetails() ? this.hiddenMessage : e.getLocalizedMessage();
		return R.failed(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * 空指针异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
		log.error(String.format("请求地址: %s, 空指针异常 ex=%s", request.getRequestURI(), e.getMessage()), e);
		this.globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = isHideExceptionDetails() ? this.hiddenMessage : this.npeErrorMessage;
		return R.failed(SystemResultCode.SERVER_ERROR, errorMessage);
	}

}
