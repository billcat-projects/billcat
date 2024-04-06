package net.billcat.web.exception.handler;

import net.billcat.common.exception.handler.GlobalExceptionHandler;

/**
 * 默认的异常日志处理类
 *
 * @author Hccake 2019/10/18 17:06
 */
public class DoNothingGlobalExceptionHandler implements GlobalExceptionHandler {

	/**
	 * 在此处理日志 默认什么都不处理
	 * @param throwable 异常信息
	 */
	@Override
	public void handle(Throwable throwable) {
		// do nothing
	}

}