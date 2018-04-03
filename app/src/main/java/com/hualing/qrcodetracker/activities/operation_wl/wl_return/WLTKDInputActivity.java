package com.hualing.qrcodetracker.activities.operation_wl.wl_return;

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
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.CreateWLTKDParam;
import com.hualing.qrcodetracker.bean.WLTKDResult;
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

public class WLTKDInputActivity extends BaseActivity {


    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 30;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.thdwValue)
    TextView mThdwValue;
    @BindView(R.id.shrValue)
    EditText mShrValue;
    @BindView(R.id.shfzrValue)
    EditText mShfzrValue;
    @BindView(R.id.thfzrValue)
    EditText mThfzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    private CreateWLTKDParam params;
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
                AllActivitiesHolder.removeAct(WLTKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
        params = new CreateWLTKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wltkdinput;
    }

    private void commitDataToWeb() {
        if (!checkDataIfCompleted()) {
            Toast.makeText(this, "数据录入不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<WLTKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WLTKDResult>> e) throws Exception {
                ActionResult<WLTKDResult> nr = mainDao.createWL_TKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WLTKDResult>>() {
                    @Override
                    public void accept(ActionResult<WLTKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        //                        if (result.getCode() == 2) {//入库单已存在
                        //                            //保存物料入库单号
                        //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                        //
                        //                            IntentUtil.openActivity(WLInRKDInputActivity.this, ScanActivity.class);
                        //                            AllActivitiesHolder.removeAct(WLInRKDInputActivity.this);
                        ////                            new AlertDialog.Builder(WLInRKDInputActivity.this)
                        ////                                    .setCancelable(false)
                        ////                                    .setTitle("提示")
                        ////                                    .setMessage("此入库单已存在，是否继续向其扫码录入数据？")
                        ////                                    .setPositiveButton("继续扫码录入", new DialogInterface.OnClickListener() {
                        ////                                        @Override
                        ////                                        public void onClick(DialogInterface dialog, int which) {
                        ////                                            IntentUtil.openActivity(WLInRKDInputActivity.this, ScanActivity.class);
                        ////                                            AllActivitiesHolder.removeAct(WLInRKDInputActivity.this);
                        ////                                        }
                        ////                                    })
                        ////                                    .setNegativeButton("作废此入库单", new DialogInterface.OnClickListener() {
                        ////                                        @Override
                        ////                                        public void onClick(DialogInterface dialog, int which) {
                        ////                                            deleteRKD();
                        ////                                        }
                        ////                                    })
                        ////                                    .show();
                        //                        } else
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "退库单创建成功~", Toast.LENGTH_SHORT).show();
                            WLTKDResult tkdResult = result.getResult();
                            //保存物料退库单号
                            SharedPreferenceUtil.setWlTKDNumber(String.valueOf(tkdResult.getBackDh()));
                            IntentUtil.openActivity(WLTKDInputActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(WLTKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkDataIfCompleted() {
        String thdwValue = mThdwValue.getText().toString();
        String shrValue = mShrValue.getText().toString();
        String shfzrValue = mShfzrValue.getText().toString();
        String thfzrValue = mThfzrValue.getText().toString();
        String remarkValue = mRemarkValue.getText().toString();
        if ("请选择部门".equals(thdwValue)
                || TextUtils.isEmpty(shrValue)
                || TextUtils.isEmpty(shfzrValue)
                || TextUtils.isEmpty(thfzrValue)
//                || TextUtils.isEmpty(remarkValue)
                ) {
            return false;
        }
        params.setThDw(thdwValue);
        params.setShrr(shrValue);
        params.setShFzr(shfzrValue);
        params.setThr(GlobalData.realName);
        params.setThFzr(thfzrValue);
        params.setRemark(remarkValue);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_SELECT_DEPARTMENT) {
                mThdwValue.setText(data.getStringExtra("groupName"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.selectLLBM, R.id.commitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectLLBM:
                IntentUtil.openActivityForResult(this, SelectDepartmentActivity.class,REQUEST_CODE_SELECT_DEPARTMENT,null);
                break;
            case R.id.commitBtn:
                commitDataToWeb();
                break;
        }
    }
}
