package com.hualing.qrcodetracker.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hualing.qrcodetracker.BuildConfig;
import com.hualing.qrcodetracker.aframework.yoni.AuthorizationInterceptor;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class GlobalData {
    // 用户Id
    public static String userId;

    // 业务服务器地址
//    public static String mainServiceUrl;
    // 资源服务器地址
//    public static String resourceServiceUrl;

    public static String packageName;
    public static int verCode;
    public static String verName;
    public static String appId;
    public static String appName;
    //关于我们
    public static String company;
    public static String phone;

    public static String upDate = null;

    //启动页的图片id
    private static int launchDrawableId ;
    //启动页的停留时间
    private static long launchDelayDuration ;


    public static long getLaunchDelayDuration() {
        return launchDelayDuration;
    }

    public static void setLaunchDelayDuration(long launchDelayDuration) {
        GlobalData.launchDelayDuration = launchDelayDuration;
    }

    public static int getLaunchDrawableId() {
        return launchDrawableId;
    }

    public static void setLaunchDrawableId(int launchDrawableId) {
        GlobalData.launchDrawableId = launchDrawableId;
    }

    public static void Load(Context context) {

//        if (BuildConfig.API_DEBUG) {
//            mainServiceUrl = context.getResources().getString(R.string.main_service_url_debug);
//            resourceServiceUrl = context.getResources().getString(R.string.main_resource_url_debug);
//        } else {
//            mainServiceUrl = context.getResources().getString(R.string.main_service_url);
//            resourceServiceUrl = context.getResources().getString(R.string.main_resource_url);
//        }
        YoniClient.getInstance().setBaseUrl(BuildConfig.SERVICE_URL);
        YoniClient.getInstance().setResourceUrl(BuildConfig.RESOURCE_URL);
        YoniClient.getInstance().setDebug(BuildConfig.API_DEBUG);

//        YoniClient.getInstance().create(MainDao.class);
//        YoniClient.getInstance().create(BusinessDao.class);
//        YoniClient.getInstance().create(UserDao.class);
        YoniClient.getInstance().addInterceptor(new AuthorizationInterceptor());
//        //在登陆成功后设置
//        GlobalData.userId = "xxxx";
//        YoniClient.getInstance().setUser(GlobalData.userId);
//        //退出登陆之后设置
//        GlobalData.userId = "";
//        YoniClient.getInstance().setUser(GlobalData.userId);



        DeviceInfo.load(context);//获取手机信息
        AppInfo.load(context);//获取app信息

    }

    private static String loginName;
    private static String password;
    private static boolean isFirstOpen = true;

    public static boolean getIfFirstOpen(){
        return TheApplication.getSharedPreferences().getBoolean("isFirstOpen",true);
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static class AppInfo {
        static void load(Context context) {
            PackageManager manager;

            PackageInfo info = null;

            manager = context.getPackageManager();

            try {

                info = manager.getPackageInfo(context.getPackageName(), 0);

            } catch (PackageManager.NameNotFoundException e) {

                e.printStackTrace();

            }
            verCode = info.versionCode;//版本号
            verName = info.versionName;//版本名
            packageName = info.packageName;//包名
            appId = BuildConfig.APP_ID;// ios 还是android（101100）
            appName = BuildConfig.APP_NAME;//app名称
        }
    }

    public static String osVersion;
    public static String deviceModel;
    public static int osType = 1;
    public static String deviceId = "";
    //手机IP地址
    public static String ip = "" ;

    public static class DeviceInfo {
        static void load(Context context) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);//获取手机服务信息
            //            RxPermissions rxPermissions = new RxPermissions((Activity) context);
            //            rxPermissions
            //                    .request(Manifest.permission.READ_PHONE_STATE
            //                    )
            //                    .subscribe(granted -> {
            //                        if (granted) {
            //                            deviceId = tm.getDeviceId();//获取手机设备号
            //                        } else {
            //                            // 有一个不允许后执行
            //                        }
            //                    });

            if (TextUtils.isEmpty(deviceId)) {
                deviceId = Build.SERIAL;
            }
            osVersion = Build.VERSION.SDK.replace("Android ", "");//获取手机api版本号
            deviceModel = Build.MODEL;//手机版本型号
            ip = getIPAddress(context);
        }
    }

    /**
     * 各种DEBUG参数
     */
    public static class Debug {
        /**
         * 全局DEBUG
         */
        public static boolean global = false;
    }


    public static class Service {
        /********************
         * 通用
         ***********************/
        /* 服务模板 */
        public static final String TEMPLATE = "999";

        /* 初始化服务 */
        public static final String INIT = "000";
        /* 用户登录 */
        public static final String LOGIN = "001";
        /* 用户注册 */
        public static final String REG = "002";
        /* 重置密码 */
        public static final String RESET_PASSWORD = "003";
        /* 修改密码 */
        public static final String CHANGE_PASSWORD = "004";
        /* 发送验证码 */
        public static final String SEND_AUTH_CODE = "005";
        /* 检查更新 */
        public static final String CHECK_UPDATE = "006";

        /*个人信息*/
        public static final String GET_USER_INFO = "101";
        public static final String UPDATE_USER_INFO = "102";


        public static final String GET_HOME_INFO = "201";

        public static final String GET_HPJX_INFO = "301";
        public static final String GET_HPJX_UL_INFO = "311";
        public static final String GET_HPJX_UL_EQUIP_INFO = "312";
        public static final String ADD_HPJX_UL = "313";

        public static final String GET_HPJX_SL_INFO = "321";
        public static final String GET_HPJX_SL_EQUIP_INFO = "322";
        public static final String ADD_HPJX_SL = "323";

        public static final String GET_HPJX_FBL_INFO = "331";
        public static final String ADD_HPJX_FBL = "333";

        public static final String GET_QXZB_INFO = "401";

        public static final String GET_QXZB_UL_INFO = "411";
        public static final String GET_QXZB_UL_EQUIP_INFO = "412";
        public static final String ADD_QXZB_UL = "413";
        public static final String GET_QXZB_UL_RM = "414";

        public static final String GET_QXZB_SL_INFO = "421";
        public static final String GET_QXZB_SL_EQUIP_INFO = "422";
        public static final String ADD_QXZB_SL = "423";
        public static final String GET_QXZB_SL_RM = "424";

        public static final String GET_QXZB_FBL_INFO = "431";
        public static final String GET_QXZB_FBL_EQUIP_INFO = "432";
        public static final String ADD_QXZB_FBL = "433";
        public static final String GET_QXZB_FBL_RM = "434";

        public static final String GET_JDZY_INFO = "501";
        public static final String ADD_JDZY = "502";

        public static final String GET_BTDZ_INFO = "601";
        public static final String ADD_BTDZ = "602";

        public static final String GET_BFHT_LIST = "701";
        public static final String GET_BFHT_INFO = "702";
        public static final String ADD_BFHT = "703";

        public static final String GET_SIGN_IN_DATA = "103";
        public static final String SIGN_IN = "104";

        public static final String GET_HPJX_EQUIP_LIST = "340";
        public static final String USE_HPJX_EQUIP = "341";
        public static final String GET_QXZB_EQUIP_LIST = "440";
        public static final String USE_QXZB_EQUIP = "441";
        public static final String SEND_MSG = "704";
        public static final String GET_MSG = "705";
        public static final String GET_LOAD_PAGE_URL = "100";
        public static final String GET_QXZB_UL_COST = "415";
        public static final String GET_QXZB_SL_COST = "425";
        public static final String GET_QXZB_FBL_COST = "435";


        public static final String BIND_BANK_CARD = "105";
        public static final String GET_BINDED_LIST = "106";
        public static final String UNBIND_BANK_CARD = "107";
        public static final String GET_BANK_CARD_INFO = "108";
        public static final String GET_ALL_BANK_INFO = "109";
    }
}
