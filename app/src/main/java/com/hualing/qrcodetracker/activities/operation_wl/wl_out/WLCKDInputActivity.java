package com.hualing.qrcodetracker.activities.operation_wl.wl_out;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.CreateWLCKDParam;
import com.hualing.qrcodetracker.bean.WLCKDResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WLCKDInputActivity extends BaseActivity {


    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 30;
    private static final int REQUEST_CODE_SELECT_LLFZR = 31;
    private static final int REQUEST_CODE_SELECT_FLFZR = 32;
    private static final int REQUEST_CODE_SELECT_FLR = 33;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.LldwValue)
    TextView mLldwValue;
    @BindView(R.id.FlrValue)
    TextView mFlrValue;
    @BindView(R.id.FlfzrValue)
    TextView mFlfzrValue;
    @BindView(R.id.LlfzrValue)
    TextView mLlfzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    private CreateWLCKDParam params;
    private MainDao mainDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(WLCKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
        params = new CreateWLCKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wlckdinput;
    }

    private void commitDataToWeb() {
        if (!checkDataIfCompleted()) {
            Toast.makeText(this, "数据录入不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<WLCKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WLCKDResult>> e) throws Exception {
                ActionResult<WLCKDResult> nr = mainDao.createWL_CKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WLCKDResult>>() {
                    @Override
                    public void accept(ActionResult<WLCKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "出库单创建成功~", Toast.LENGTH_SHORT).show();
                            //保存物料出库单号
                            //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                            WLCKDResult ckdResult = result.getResult();
                            SharedPreferenceUtil.setWlCKDNumber(String.valueOf(ckdResult.getOutdh()));
                            IntentUtil.openActivity(WLCKDInputActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(WLCKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkDataIfCompleted() {
        String lldwValue = mLldwValue.getText().toString();
        String flrValue = mFlrValue.getText().toString();
        String flfzrValue = mFlfzrValue.getText().toString();
        String llfzrValue = mLlfzrValue.getText().toString();
        String remarkValue = mRemarkValue.getText().toString();
        if ("请选择部门".equals(lldwValue)
                || "请选择发料人".equals(flrValue)
                || "请选择发料负责人".equals(flfzrValue)
                || "请选择领料负责人".equals(llfzrValue)
            //                || TextUtils.isEmpty(remarkValue)
                ) {
            return false;
        }
        params.setLhDw(lldwValue);
        params.setFhR(flrValue);
        params.setFhFzr(flfzrValue);
        params.setLhR(GlobalData.realName);
        params.setLhFzr(llfzrValue);
        params.setRemark(remarkValue);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_DEPARTMENT:
                    mLldwValue.setText(data.getStringExtra("groupName"));
                    break;
                case REQUEST_CODE_SELECT_LLFZR:
                    mLlfzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_FLFZR:
                    mFlfzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_FLR:
                    mFlrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.selectLLBM, R.id.commitBtn, R.id.selectLLFZR, R.id.selectFLFZR, R.id.selectFLR})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectLLBM:
                IntentUtil.openActivityForResult(this, SelectDepartmentActivity.class, REQUEST_CODE_SELECT_DEPARTMENT, null);
                break;
            case R.id.selectLLFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_LLFZR, null);
                break;
            case R.id.selectFLFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_FLFZR, null);
                break;
            case R.id.selectFLR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_FLR, null);
                break;
            case R.id.commitBtn:
                commitDataToWeb();
                break;
        }
    }
}
