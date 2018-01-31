package com.hualing.qrcodetracker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hualing.qrcodetracker.util.AllActivitiesHolder;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author 马鹏昊
 * @date {2016-10-28}
 * @des 自定义的一切Activity类的基类
 * @updateAuthor
 * @updateDate
 * @updateDes
 */
public abstract class BaseActivity extends AppCompatActivity {

    Unbinder unBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //解决透明化状态栏（一般人说的沉浸式，其实不是沉浸）,需要每个Activity都要实现getLayoutId()方法
        //        setContentView(getLayoutResId());//把设置布局文件的操作交给继承的子类
        //        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        //        View parentView = contentFrameLayout.getChildAt(0);
        //        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
        //            parentView.setFitsSystemWindows(true);
        //        }


        //自己填充statusBar部分(无法做到每个Activity页最上层的颜色都和状态栏一致)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ViewGroup firstChildAtDecorView = ((ViewGroup) ((ViewGroup) getWindow().getDecorView()).getChildAt(0));
//            View statusView = new View(this);
//            ViewGroup.LayoutParams statusViewLp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
//            //颜色的设置可抽取出来让子类实现之
//            statusView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
//            firstChildAtDecorView.addView(statusView, 0, statusViewLp);
//        }


        setContentView(getLayoutResId());
        unBinder = ButterKnife.bind(this);
        AllActivitiesHolder.addAct(this);
        initLogic();
//        if (BuildConfig.API_DEBUG){
//            debugShow();
//        }else{
//            getDataFormWeb();
//        }
        getDataFormWeb();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBinder.unbind();
        AllActivitiesHolder.removeAct(this);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    /**
     * 初始化逻辑
     */
    protected abstract void initLogic();

    /**
     * 获取后台数据
     */
    protected abstract void getDataFormWeb();

    /**
     * 静态数据展示（未调接口时）
     */
    protected abstract void debugShow();

    /**
     * 返回当前Activity布局文件的id
     * @return 布局id
     */
    abstract protected int getLayoutResId();

}
