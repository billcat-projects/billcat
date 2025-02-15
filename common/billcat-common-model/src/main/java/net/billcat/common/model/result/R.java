package net.billcat.common.model.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(title = "返回体结构")
public class R<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(title = "返回状态码", defaultValue = "0")
	private int code;

	@Schema(title = "返回信息", defaultValue = "Success")
	private String message;

	@Schema(title = "数据", nullable = true, defaultValue = "null")
	private T data;

	public static <T> R<T> ok() {
		return ok(null);
	}

	public static <T> R<T> ok(T data) {
		return ok(data, SystemResultCode.SUCCESS.getMessage());
	}

	public static <T> R<T> ok(T data, String message) {
		return new R<T>().setCode(SystemResultCode.SUCCESS.getCode()).setData(data).setMessage(message);
	}

	public static <T> R<T> failed(int code, String message) {
		return new R<T>().setCode(code).setMessage(message);
	}

	public static <T> R<T> failed(ResultCode failMsg) {
		return failed(failMsg.getCode(), failMsg.getMessage());
	}

	public static <T> R<T> failed(ResultCode failMsg, String message) {
		return failed(failMsg.getCode(), message);
	}

}