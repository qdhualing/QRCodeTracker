package com.hualing.qrcodetracker.aframework.yoni;


/********************************************
 * 网络请求返回结果.<br>
 * 类详细描述……<br>
 * CreateDate: 2014年7月23日<br>
 * Copyright: Copyright(c) 2014年7月23日<br>
 * Company: 体育街<br>
 * @since v1.0.0
 * @author  巫作坤
 * @version v1.0.0
 *********************************************/
public class NetResponse {
	/* 网络数据请求状态：成功 */
	public static final int STATUS_OK=0;
	/* 网络数据请求状态：参数错误 */
	public static final int STATUS_ERROR_PARAMS=-1;
	/* 网络数据请求状态：连接错误，可能原因有URL地址不对或网络未正常连接 */
	public static final int STATUS_ERROR_CONNECT=-2;
	/* 网络数据请求状态：连接超时，可以请求，但是网络延时太大而造成 */
	public static final int STATUS_ERROR_TIMEOUT=-3;
	
	/* 网络数据请求状态：除以上几种异常外的通信异常 */
	public static final int STATUS_ERROR_IO=-9;
	
	
	
	/********************************************
	 * 构造方法简述加英文符号.<br>
	 * 构造方法业务逻辑详细描述……<br>
	 * @since v1.0.0
	 * @param 参数名 参数类型 参数描述
	 * @hide 
	 * <br>
	 * --------------------------------------<br>
	 * 编辑历史<br>
	 * 2014年7月25日::巫作坤::创建此构造方法<br>
	 *********************************************/
	/*package*/
	NetResponse(int status,String message,Object result)
	{
		
	}
	/*package*/
	NetResponse()
	{
		
	}

	/* 状态，0正常，其余为错误，小于0时表示是系统级错误，大于0为业务级错误,业务级错误表示服务器接收到了请求，但是未能正常处理 */
	private int code;
	/* 请求响应结果描述，成功时一般返回OK，异常时返回错误原因 */
	private String message;
	/* 响应结果，一般为JSON序列化后的字符串 */
	private Object result;

	public int getCode() {
		return code;
	}
	public boolean isSeccuess()
	{
		return code==0;
	}

	public void setCode(int status) {
		this.code = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
