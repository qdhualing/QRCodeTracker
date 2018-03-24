package com.hualing.qrcodetracker.activities.operation_cp.cp_in;

import android.os.Bundle;
import android.view.View;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_out.CPOutProductStylePickActivity;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.model.CPType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

public class CPInProductStylePickActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(CPInProductStylePickActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cpin_product_style_pick;
    }

    @OnClick({R.id.bigIn, R.id.smallIn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bigIn:
                GlobalData.currentCPInType = CPType.BIG_CP_IN;
                break;
            case R.id.smallIn:
                GlobalData.currentCPInType = CPType.SMALL_CP_IN;
                break;
        }
        IntentUtil.openActivity(this, ScanActivity.class);
    }
}
