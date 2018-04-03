package com.hualing.qrcodetracker.activities.operation_cp.cp_out;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.SmallCpOutGetDataParam;
import com.hualing.qrcodetracker.bean.SmallCpOutGetDataResult;
import com.hualing.qrcodetracker.bean.SmallCpOutParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.NotificationType;
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

public class SmallCPOutDataInputActivity extends BaseActivity {


    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.nameValue)
    TextView mNameValue;
    @BindView(R.id.lbValue)
    TextView mLbValue;
    @BindView(R.id.ylpcValue)
    TextView mYlpcValue;
    @BindView(R.id.scpcValue)
    TextView mScpcValue;
    @BindView(R.id.ggValue)
    TextView mGgValue;
    @BindView(R.id.scTimeValue)
    TextView mScTimeValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;


    private MainDao mainDao;
    private SmallCpOutGetDataParam mSmallCpOutGetDataParam;
    private SmallCpOutParam params;
    private String mQrcodeId;
    private Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(SmallCPOutDataInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
        mainDao = YoniClient.getInstance().create(MainDao.class);

        progressDialog = TheApplication.createLoadingDialog(this, "");
        params = new SmallCpOutParam();
        mSmallCpOutGetDataParam = new SmallCpOutGetDataParam();

        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
        }
        params.setQrCodeId(mQrcodeId);
        mSmallCpOutGetDataParam.setQrCodeId(mQrcodeId);
    }

    @Override
    protected void getDataFormWeb() {
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<SmallCpOutGetDataResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<SmallCpOutGetDataResult>> e) throws Exception {
                ActionResult<SmallCpOutGetDataResult> nr = mainDao.getSmallCpOutData(mSmallCpOutGetDataParam);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<SmallCpOutGetDataResult>>() {
                    @Override
                    public void accept(ActionResult<SmallCpOutGetDataResult> result) throws Exception {
                        progressDialog.dismiss();
                        Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        if (result.getCode() != 0) {
                        } else {
                            SmallCpOutGetDataResult dataResult = result.getResult();
                            mNameValue.setText(dataResult.getCpName());
                            mLbValue.setText(dataResult.getSortName());
                            mYlpcValue.setText(dataResult.getYlpc());
                            mScpcValue.setText(dataResult.getScpc());
                            mGgValue.setText(dataResult.getGg());
                            mScTimeValue.setText(dataResult.getScDate());
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_small_cpout_data_input;
    }

    @OnClick(R.id.commitBtn)
    public void onViewClicked() {
        progressDialog.show();

        if (!TextUtils.isEmpty(mRemarkValue.getText().toString())) {
            params.setRemark(mRemarkValue.getText().toString());
        }
        params.setOutDh(SharedPreferenceUtil.getBCPCKDNumber());
        params.setFhr(GlobalData.realName);

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.smallCpOut(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<ActionResult>>() {
                    @Override
                    public void accept(ActionResult<ActionResult> result) throws Exception {
                        progressDialog.dismiss();
                        Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        if (result.getCode() != 0) {
                        } else {
                            new AlertDialog.Builder(SmallCPOutDataInputActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("是否继续出库？")
                                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(SmallCPOutDataInputActivity.this, ScanActivity.class);
                                            AllActivitiesHolder.removeAct(SmallCPOutDataInputActivity.this);
                                        }
                                    })
                                    .setNegativeButton("不了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //调接口发推送审核
                                            sendNotification();

                                        }
                                    })
                                    .show();
                        }
                    }
                });
    }

    private void sendNotification() {

        final NotificationParam notificationParam = new NotificationParam();
        //根据单号去查找审核人
        String dh = SharedPreferenceUtil.getBCPCKDNumber();
        notificationParam.setDh(dh);
        notificationParam.setStyle(NotificationType.CP_CKD);


        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.sendNotification(notificationParam);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<ActionResult>>() {
                    @Override
                    public void accept(ActionResult<ActionResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TheApplication.getContext(), "已通知仓库管理员审核", Toast.LENGTH_SHORT).show();
                            AllActivitiesHolder.removeAct(SmallCPOutDataInputActivity.this);

                        }
                    }
                });

    }

}
