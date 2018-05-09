package com.hualing.qrcodetracker.global;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hualing.qrcodetracker.R;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * @author 马鹏昊
 * @date {2016-10-28}
 * @des 获取全局对象
 * @updateAuthor
 * @updateDate
 * @updateDes
 */
public class TheApplication extends Application {


    //全局Context
    private static Context mContext;

    //屏幕宽度
    public static int screenWidth;
    //屏幕高度
    public static int screenHeight;

    //SharedPreference
    private static SharedPreferences sSharedPreferences;

    //此App一些信息的存储文件夹路径
    private static final String appDirectoryPath = File.separator + "mnt" + File.separator + "sdcard" + File.separator + "qrcodetracker";
    //此App一些信息的存储文件夹
    private static File appRootDirectory;


    public static String ImagePath;

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        TheApplication.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        TheApplication.screenHeight = screenHeight;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        sSharedPreferences = mContext.getSharedPreferences("loginInfo", MODE_PRIVATE);

        GlobalData.Load(mContext);//获取手机跟app信息及初始化url

        //设置启动页的时间和图片
        GlobalData.setLaunchDelayDuration(2000);
//        GlobalData.setLaunchDrawableId(R.drawable.launch);

        //初始化Fresco
        Fresco.initialize(this);

        appRootDirectory = new File(appDirectoryPath);
        if (appRootDirectory.exists() && appRootDirectory.isDirectory()) {
            appRootDirectory.delete();
        }
        appRootDirectory.mkdirs();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }

    //外部得到全局context的接口
    public static Context getContext() {
        return mContext;
    }

    //外部得到SharedPreference的接口
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    //外部得到App根目录文件夹路径
    public static String getAppRootDirectory() {
        return appDirectoryPath;
    }

    /*
        得到dp转化成的px
    */
    public static int getPxFromDp(float dip) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mContext.getResources().getDisplayMetrics());
        return (int) result;
    }
    /*
        得到dp转化成的px
    */
    public static float getPxFromSp(float sp) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext.getResources().getDisplayMetrics());
        return result;
    }

    /**
     * 得到自定义的progressDialog
     *
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg/*int parentWidth,int parentHeight*/) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        //        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parentWidth,parentHeight);
        //        v.setLayoutParams(params);
        //        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (!TextUtils.isEmpty(msg))
            tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        //        loadingDialog.getWindow().setLayout(parentWidth,parentHeight);
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

}
