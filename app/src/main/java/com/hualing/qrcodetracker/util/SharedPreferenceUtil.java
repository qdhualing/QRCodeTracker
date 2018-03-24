package com.hualing.qrcodetracker.util;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.UserType;


/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class SharedPreferenceUtil {

    /**
     * 记住是什么身份使用程序
     */
    public static void setUserType(int userType){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userType",userType);
        editor.commit();
    }


    /**
     * 获取用户类型
     */
    public static int getUserType(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getInt("userType", UserType.NON_SELECTED);
    }


    /**
     * 记住用户名、密码
     * @param userName
     * @param pwd
     */
    public static void rememberUser(String userName, String pwd){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",userName);
        editor.putString("password",pwd);
        editor.commit();
    }

    public static boolean ifHasLocalUserInfo(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        if (TextUtils.isEmpty(preferences.getString("userName",null))||TextUtils.isEmpty(preferences.getString("password",null))) {
            return false;
        }
        return true;
    }

    /**
     * 获取上次登陆的用户信息
     * @return
     */
    public static String[] getUserInfo(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        String userName = preferences.getString("userName",null);
        String password = preferences.getString("password",null);
        return new String[]{userName,password};
    }

    /**
     * 注销登录
     */
    public static void logout(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName","");
        editor.putString("password","");
        editor.commit();
    }

    /**
     * 获取加载页地址
     */
    public static String getLoadPageUrl(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("loadPageUrl",null);
    }

    /**
     * 获取加载页地址
     */
    public static void setLoadPageUrl(String url){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("loadPageUrl", url);
        editor.commit();
    }

    /**
     * 保存是否有未读消息
     */
    public static void saveIfHasUnreadMsg(boolean flag){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ifHasUnreadMsg", flag);
        editor.commit();
    }

    /**
     * 查看是否有未读消息
     */
    public static boolean checkIfHasUnreadMsg() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getBoolean("ifHasUnreadMsg",false);
    }

    /**
     *  保存物料入库单号
     */
    public static void setWlRKDNumber(String number) {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("WlRKDNumber", number);
        editor.commit();
    }
    /**
     *  获取物料入库单号
     */
    public static String getWlRKDNumber() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("WlRKDNumber",null);
    }
    /**
     *  保存物料出库单号
     */
    public static void setWlCKDNumber(String number) {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("WlCKDNumber", number);
        editor.commit();
    }
    /**
     *  获取物料出库单号
     */
    public static String getWlCKDNumber() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("WlCKDNumber",null);
    }

    /**
     * 保存物料退库单号
     * @param wlTKDNumber
     */
    public static void setWlTKDNumber(String wlTKDNumber) {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("WlTKDNumber", wlTKDNumber);
        editor.commit();
    }
    /**
     *  获取物料退库单号
     */
    public static String getWlTKDNumber() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("WlTKDNumber",null);
    }
    /**
     * 保存半成品入库单号
     * @param s
     */
    public static void setBCPRKDNumber(String s) {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BCPRKDNumber", s);
        editor.commit();
    }
    /**
     *  获取半成品入库单号
     */
    public static String getBCPRKDNumber() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("BCPRKDNumber",null);
    }
    /**
     * 保存半成品退库单号
     * @param s
     */
    public static void setBCPTKDNumber(String s) {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BCPTKDNumber", s);
        editor.commit();
    }
    /**
     *  获取半成品退库单号
     */
    public static String getBCPTKDNumber() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("BCPTKDNumber",null);
    }
    /**
     *  获取半成品/成品出库单号
     */
    public static void setBCPCKDNumber(String s) {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BCPCKDNumber", s);
        editor.commit();
    }
    /**
     *  获取半成品/成品出库单号
     */
    public static String getBCPCKDNumber() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("BCPCKDNumber",null);
    }
}
