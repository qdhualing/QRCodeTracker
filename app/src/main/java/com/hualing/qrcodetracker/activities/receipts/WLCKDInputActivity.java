package com.hualing.qrcodetracker.activities.receipts;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.ScanActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.CreateWLCKDParam;
import com.hualing.qrcodetracker.bean.WLCKDResult;
import com.hualing.qrcodetracker.dao.MainDao;
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


    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.LldwValue)
    EditText mLldwValue;
    @BindView(R.id.FlrValue)
    EditText mFlrValue;
    @BindView(R.id.FlfzrValue)
    EditText mFlfzrValue;
    @BindView(R.id.LlrValue)
    EditText mLlrValue;
    @BindView(R.id.LlfzrValue)
    EditText mLlfzrValue;
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

    @OnClick(R.id.commitBtn)
    public void onViewClicked(View view) {

        commitDataToWeb();

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
                            Toast.makeText(TheApplication.getContext(), "出库单创建成功~", Toast.LENGTH_SHORT).show();
                            //保存物料入库单号
                            //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                            WLCKDResult ckdResult = result.getResult();
                            SharedPreferenceUtil.setWlCKDNumber(String.valueOf(ckdResult.getOutDh()));
                            IntentUtil.openActivity(WLCKDInputActivity.this, ScanActivity.class);
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
        String llrValue = mLlrValue.getText().toString();
        String llfzrValue = mLlfzrValue.getText().toString();
        String remarkValue = mRemarkValue.getText().toString();
        if (TextUtils.isEmpty(lldwValue)
                || TextUtils.isEmpty(flrValue)
                || TextUtils.isEmpty(flfzrValue)
                || TextUtils.isEmpty(llrValue)
                || TextUtils.isEmpty(llfzrValue)
                || TextUtils.isEmpty(remarkValue)
                ) {
            return false;
        }
        params.setLhDw(lldwValue);
        params.setFhR(flrValue);
        params.setFhFzr(flfzrValue);
        params.setLhR(llrValue);
        params.setLhFzr(llfzrValue);
        params.setRemark(remarkValue);
        return true;
    }

}
