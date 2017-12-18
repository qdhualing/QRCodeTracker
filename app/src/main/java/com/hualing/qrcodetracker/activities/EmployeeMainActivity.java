package com.hualing.qrcodetracker.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.DoubleClickExitUtil;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class EmployeeMainActivity extends BaseActivity {

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.menu)
    LinearLayout mMenu;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mToolBar.setTitle("二维码追溯");//设置Toolbar标题
        mToolBar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //修改成主界面跟随侧滑栏移动
        //        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
        //            @Override
        //            public void onDrawerSlide(View drawerView, float slideOffset) {
        //                View mContent = mDrawerLayout.getChildAt(0);
        //                ViewHelper.setTranslationX(mContent,
        //                        mMenu.getMeasuredWidth() * slideOffset);
        //            }
        //
        //            @Override
        //            public void onDrawerOpened(View drawerView) {
        //
        //            }
        //
        //            @Override
        //            public void onDrawerClosed(View drawerView) {
        //
        //            }
        //
        //            @Override
        //            public void onDrawerStateChanged(int newState) {
        //
        //            }
        //        });

    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_employee_main;
    }

    @OnClick(R.id.exitBtn)
    public void onViewClicked() {
        GlobalData.userId = "";
        //之后获取和用户相关的服务就不需要额外传userId了
        YoniClient.getInstance().setUser(GlobalData.userId);
        //清除本地密码
        SharedPreferenceUtil.logout();
        AllActivitiesHolder.finishAllAct();
        IntentUtil.openActivity(this,UserTypePickActivity.class);
    }

    @Override
    public void onBackPressed() {
        DoubleClickExitUtil.tryExit();
    }
}
