package com.hualing.qrcodetracker.activities.operation_cp.cp_out;

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
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BCPCKDResult;
import com.hualing.qrcodetracker.bean.CreateBCPCKDParam;
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
 * @desc 实际上成品也算某种半成品，所以操作ckd_bcp表
 */
public class CPCKDInputActivity extends BaseActivity {

    private static final int GET_DEPARTMENT = 10;
    private static final int REQUEST_CODE_SELECT_JHFZR = 31;
    private static final int REQUEST_CODE_SELECT_SHFZR = 32;
    private static final int REQUEST_CODE_SELECT_SHR = 33;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.departmentValue)
    TextView mDepartmentValue;
    @BindView(R.id.ShrValue)
    TextView mShrValue;
    @BindView(R.id.ShFzrValue)
    TextView mShFzrValue;
    @BindView(R.id.JhFhrValue)
    TextView mJhFhrValue;
    @BindView(R.id.shdwValue)
    EditText mShdwValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;

    private MainDao mainDao;

    private CreateBCPCKDParam params;

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
                AllActivitiesHolder.removeAct(CPCKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        params = new CreateBCPCKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cpckdinput;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_DEPARTMENT:
                    mDepartmentValue.setText(data.getStringExtra("groupName"));
                    break;
                case REQUEST_CODE_SELECT_JHFZR:
                    mJhFhrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_SHFZR:
                    mShFzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_SHR:
                    mShrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkDataIfCompleted() {
        //工厂内部产品出库的部门
        String jhdwValue = mDepartmentValue.getText().toString();
        //收货客户的单位
        String shdwValue = mShdwValue.getText().toString();
        String shrValue = mShrValue.getText().toString();
        String shfzrValue = mShFzrValue.getText().toString();
        String jhfzrValue = mJhFhrValue.getText().toString();
        if (TextUtils.isEmpty(shdwValue)
                || "请选择部门".equals(jhdwValue)
                || "请选择收货人".equals(shrValue)
                || "请选择收货负责人".equals(shfzrValue)
                || "请选择交货负责人".equals(jhfzrValue)) {
            return false;
        }
        params.setJhDw(jhdwValue);
        params.setLhDw(shdwValue);
        params.setLhr(shrValue);
        params.setLhFzr(shfzrValue);
        //当前用户提交
        params.setFhr(GlobalData.realName);
        params.setFhFzr(jhfzrValue);
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

        Observable.create(new ObservableOnSubscribe<ActionResult<BCPCKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BCPCKDResult>> e) throws Exception {
                ActionResult<BCPCKDResult> nr = mainDao.createBCP_CKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BCPCKDResult>>() {
                    @Override
                    public void accept(ActionResult<BCPCKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "出库单创建成功~", Toast.LENGTH_SHORT).show();
                            //保存物料入库单号
                            //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                            BCPCKDResult ckdResult = result.getResult();
                            SharedPreferenceUtil.setBCPCKDNumber(String.valueOf(ckdResult.getOutDh()));
                            //去选择大包装出库还是小包装出库
                            IntentUtil.openActivity(CPCKDInputActivity.this, CPOutProductStylePickActivity.class);
                            AllActivitiesHolder.removeAct(CPCKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @OnClick({R.id.selectBM, R.id.commitBtn,R.id.selectSHR,R.id.selectSHFZR,R.id.selectJHFZR})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectBM:
                IntentUtil.openActivityForResult(this, SelectDepartmentActivity.class, GET_DEPARTMENT, null);
                break;
            case R.id.selectSHR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_SHR, null);
                break;
            case R.id.selectSHFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_SHFZR, null);
                break;
            case R.id.selectJHFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_JHFZR, null);
                break;
            case R.id.commitBtn:
                commitDataToWeb();
                break;
        }
    }


}
