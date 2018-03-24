package com.hualing.qrcodetracker.activities.operation_cp.cp_in;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BCPRKDResult;
import com.hualing.qrcodetracker.bean.CreateBCPRKDParam;
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


/**
 * @author 具体操作和半成品一样，因为操作的都是rkd_bcp表
 * @date 2018-3-17
 */
public class CPRKDInputActivity extends BaseActivity {

    private static final int GET_DEPARTMENT = 10;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.departmentValue)
    TextView mDepartmentValue;
    @BindView(R.id.ShrValue)
    EditText mShrValue;
    @BindView(R.id.ShFzrValue)
    EditText mShFzrValue;
    @BindView(R.id.JhFhrValue)
    EditText mJhFhrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;

    private MainDao mainDao;

    private CreateBCPRKDParam params;

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
                AllActivitiesHolder.removeAct(CPRKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        params = new CreateBCPRKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cprkdinput;
    }

    @OnClick({R.id.selectBM, R.id.commitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectBM:
                IntentUtil.openActivityForResult(this, SelectDepartmentActivity.class,GET_DEPARTMENT,null);
                break;
            case R.id.commitBtn:
                commitDataToWeb();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == GET_DEPARTMENT) {
                mDepartmentValue.setText(data.getStringExtra("groupName"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkDataIfCompleted() {
        String fhdwValue = mDepartmentValue.getText().toString();
        String shrValue = mShrValue.getText().toString();
        String shfzrValue = mShFzrValue.getText().toString();
        String jhfzrValue = mJhFhrValue.getText().toString();
        if (TextUtils.isEmpty(fhdwValue)
                || "请选择部门".equals(fhdwValue)
                || TextUtils.isEmpty(shrValue)
                || TextUtils.isEmpty(shfzrValue)
                || TextUtils.isEmpty(jhfzrValue)) {
            return false;
        }
        params.setJhDw(fhdwValue);
        params.setShr(shrValue);
        params.setShFzr(shfzrValue);
        params.setJhr(GlobalData.realName);
        params.setJhFzr(jhfzrValue);
        params.setRemark(mRemarkValue.getText().toString());
        return true;
    }

    private void commitDataToWeb() {
        if (!checkDataIfCompleted()) {
            Toast.makeText(this, "数据录入不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BCPRKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BCPRKDResult>> e) throws Exception {
                ActionResult<BCPRKDResult> nr = mainDao.createBCP_RKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BCPRKDResult>>() {
                    @Override
                    public void accept(ActionResult<BCPRKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "入库单创建成功~", Toast.LENGTH_SHORT).show();
                            //保存物料入库单号
                            //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                            BCPRKDResult rkdResult = result.getResult();
                            SharedPreferenceUtil.setBCPRKDNumber(String.valueOf(rkdResult.getIndh()));
                            //去选择大包装入库还是小包装入库
                            IntentUtil.openActivity(CPRKDInputActivity.this, CPInProductStylePickActivity.class);
                            AllActivitiesHolder.removeAct(CPRKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
