package com.hualing.qrcodetracker.activities.abandon;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.EmployeeLoginActivity;
import com.hualing.qrcodetracker.model.UserType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class UserTypePickActivity extends BaseActivity {

    @BindView(R.id.selectEmployee)
    LinearLayout mSelectEmployee;
    @BindView(R.id.selectGuest)
    LinearLayout mSelectGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //只要是显示此页，那么置为未选择模式
        SharedPreferenceUtil.setUserType(UserType.NON_SELECTED);
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
        return R.layout.activity_user_type_pick;
    }

    @OnClick({R.id.selectEmployee, R.id.selectGuest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectEmployee:
                SharedPreferenceUtil.setUserType(UserType.EMPLOYEE);
                IntentUtil.openActivity(this, EmployeeLoginActivity.class);
                break;
            case R.id.selectGuest:
                SharedPreferenceUtil.setUserType(UserType.GUEST);
                AllActivitiesHolder.removeAct(this);
                IntentUtil.openActivity(this, GuestMainActivity.class);
                break;
        }
    }
}
