package com.hualing.qrcodetracker.aframework.yoni;

import com.alibaba.fastjson.JSON;

public class ActionResult<T> {
	public static int STATUS_SUCCEED = 0;// 成功
	public static int STATUS_MESSAGE_ERROR = 1;// 前端认为成功，但是需要显示信息
	public static int STATUS_PARAMS_ERROR = 2;// 参数错误
	public static int STATUS_LOGIC_ERROR = 3;// 逻辑错误
	public static int STATUS_EXCEPTION = 9;// 异常了

	private int code = STATUS_SUCCEED;
	private String message;// 为用户展示的信息
	private String errMsg;// 只提供前端人员开发看
	private T result;// 返回结果
	private Exception exception;// 只提供日志记录

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
