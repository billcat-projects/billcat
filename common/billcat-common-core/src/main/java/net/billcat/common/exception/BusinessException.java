package net.billcat.common.exception;

import lombok.Getter;
import net.billcat.common.model.result.ResultCode;

import java.text.MessageFormat;

/**
 * 通用业务异常
 *
 * @author Hccake
 */
@Getter
public class BusinessException extends RuntimeException {

	private final String message;

	private final int code;

	public BusinessException(ResultCode resultCode) {
		super(resultCode.getMessage());
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	/**
	 * 用于需要format返回结果的异常
	 */
	public BusinessException(ResultCode resultCode, Object... args) {
		this(resultCode.getCode(), MessageFormat.format(resultCode.getMessage(), args));
	}

	public BusinessException(ResultCode resultCode, Throwable e) {
		super(resultCode.getMessage(), e);
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	/**
	 * 用于需要format返回结果的异常
	 */
	public BusinessException(ResultCode resultCode, Throwable e, Object... args) {
		this(resultCode.getCode(), MessageFormat.format(resultCode.getMessage(), args), e);
	}

	public BusinessException(int code, String message) {
		super(message);
		this.message = message;
		this.code = code;
	}

	public BusinessException(int code, String message, Throwable e) {
		super(message, e);
		this.message = message;
		this.code = code;
	}

}