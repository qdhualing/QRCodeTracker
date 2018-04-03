package com.hualing.qrcodetracker.activities.operation_wl.wl_return;

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
import com.hualing.qrcodetracker.activities.main.EmployeeMainActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.WLTKGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLTKParam;
import com.hualing.qrcodetracker.bean.WLTKShowDataResult;
import com.hualing.qrcodetracker.dao.MainDao;
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

public class MaterialTKDataInputActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.nameValue)
    TextView mNameValue;
    @BindView(R.id.cdValue)
    TextView mCdValue;
    @BindView(R.id.lbValue)
    TextView mLbValue;
    @BindView(R.id.ggValue)
    TextView mGgValue;
    @BindView(R.id.sldwValue)
    TextView mSldwValue;
    @BindView(R.id.remainShlValue)
    TextView mRemainShlValue;
    @BindView(R.id.zhlValue)
    TextView mZhlValue;
    @BindView(R.id.tkShlValue)
    EditText mTkShlValue;
    @BindView(R.id.remark)
    EditText mRemark;
    private MainDao mainDao;
    private WLTKParam params;
    private WLTKGetShowDataParam getParam;
    private String mQrcodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

        params = new WLTKParam();
        getParam = new WLTKGetShowDataParam();

        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
        }
        getParam.setQrcodeId(mQrcodeId);

        mainDao = YoniClient.getInstance().create(MainDao.class);
        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(MaterialTKDataInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<WLTKShowDataResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WLTKShowDataResult>> e) throws Exception {
                ActionResult<WLTKShowDataResult> nr = mainDao.getWlTKShowData(getParam);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WLTKShowDataResult>>() {
                    @Override
                    public void accept(ActionResult<WLTKShowDataResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(MaterialTKDataInputActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(MaterialTKDataInputActivity.this);
                            return;
                        } else {
                            WLTKShowDataResult dataResult = result.getResult();
                            mNameValue.setText(dataResult.getProductName());
                            mCdValue.setText(dataResult.getChd());
                            mLbValue.setText(dataResult.getSortName());
                            mGgValue.setText(dataResult.getGg());
                            mSldwValue.setText(dataResult.getDw());
                            mRemainShlValue.setText(dataResult.getShl() + "");
                            mZhlValue.setText(dataResult.getDwzl() + "");
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_material_tkdata_input;
    }

    @OnClick(R.id.commitBtn)
    public void onViewClicked() {
        //数据录入是否完整
        if (checkIfInfoPerfect()) {
            commitDataToWeb();
        }
    }

    private boolean checkIfInfoPerfect() {
        String value = mTkShlValue.getText().toString();
        String remark = mRemark.getText().toString();
        //        String llbm = mLlbmValue.getText().toString();
        float remainShL = Float.parseFloat(mRemainShlValue.getText().toString());
        float ckShL = Float.parseFloat(value);
        if (TextUtils.isEmpty(value)
            //                || "请选择领料部门".equals(llbm)
                ) {
            Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ckShL > remainShL) {
            Toast.makeText(this, "退库数量不得大于剩余数量", Toast.LENGTH_SHORT).show();
            return false;
        }

        params.setQrCodeId(mQrcodeId);
        params.setTkShL(Float.parseFloat(value));
        params.setBz(remark);
        //        params.setLlbm(llbm);
        params.setOutDh(SharedPreferenceUtil.getWlTKDNumber());

        return true;
    }

    private void commitDataToWeb() {

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.wlTK(params);
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
                            new AlertDialog.Builder(MaterialTKDataInputActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("是否继续扫码录入退库数据？")
                                    .setPositiveButton("继续扫码录入", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(MaterialTKDataInputActivity.this, ScanActivity.class);
                                            AllActivitiesHolder.removeAct(MaterialTKDataInputActivity.this);
                                        }
                                    })
                                    .setNegativeButton("已录入完毕", new DialogInterface.OnClickListener() {
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
        String dh = SharedPreferenceUtil.getWlTKDNumber();
        notificationParam.setDh(dh);
        notificationParam.setStyle(NotificationType.WL_TKD);


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
                            IntentUtil.openActivity(MaterialTKDataInputActivity.this, EmployeeMainActivity.class);
                            AllActivitiesHolder.removeAct(MaterialTKDataInputActivity.this);
                        }
                    }
                });

    }

}
