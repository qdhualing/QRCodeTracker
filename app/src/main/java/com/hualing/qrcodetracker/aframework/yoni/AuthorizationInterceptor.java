package com.hualing.qrcodetracker.aframework.yoni;


import com.hualing.qrcodetracker.aframework.security.Cipher;

/**
 * Created by Administrator on 2017-08-22.
 */

public class AuthorizationInterceptor implements Interceptor {
    @Override
    public boolean intercept(RequestParams params, NetResponse result) {
        params.setVer("1");
        params.setTimestamp(String.valueOf(System.currentTimeMillis()));
        params.setSign(Cipher.MD5(params.getFunc() + params.getTimestamp()));
        return true;
    }
}
