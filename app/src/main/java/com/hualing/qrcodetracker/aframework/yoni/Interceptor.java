package com.hualing.qrcodetracker.aframework.yoni;

public interface Interceptor {
	boolean intercept(RequestParams params, NetResponse result);
}
