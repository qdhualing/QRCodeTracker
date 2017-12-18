package com.hualing.qrcodetracker.activities;

import android.os.Bundle;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.util.DoubleClickExitUtil;

public class GuestMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guest_main;
    }

    @Override
    public void onBackPressed() {
        DoubleClickExitUtil.tryExit();
    }
}
