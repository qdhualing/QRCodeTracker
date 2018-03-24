package com.hualing.qrcodetracker.activities.operation_bcp.bcp_return;

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
import com.hualing.qrcodetracker.bean.BCPTKGetShowDataParam;
import com.hualing.qrcodetracker.bean.BCPTKParam;
import com.hualing.qrcodetracker.bean.BCPTKShowDataResult;
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

public class BcpTKDataInputActivity extends BaseActivity {

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
    @BindView(R.id.remainShlValue)
    TextView mRemainShlValue;
    @BindView(R.id.shlDwValue)
    TextView mShlDwValue;
    @BindView(R.id.dwZhlValue)
    TextView mDwZhlValue;
    @BindView(R.id.tkShlValue)
    EditText mTkShlValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;

    private MainDao mainDao;
    private BCPTKParam params;
    private BCPTKGetShowDataParam getParam;
    private String mQrcodeId;

    private Dialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        params = new BCPTKParam();
        getParam = new BCPTKGetShowDataParam();

        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
        }
        getParam.setQrcodeId(mQrcodeId);

        mainDao = YoniClient.getInstance().create(MainDao.class);
        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(BcpTKDataInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        progressDialog = TheApplication.createLoadingDialog(this, "");

    }

    @Override
    protected void getDataFormWeb() {
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BCPTKShowDataResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BCPTKShowDataResult>> e) throws Exception {
                ActionResult<BCPTKShowDataResult> nr = mainDao.getBCPTKShowData(getParam);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BCPTKShowDataResult>>() {
                    @Override
                    public void accept(ActionResult<BCPTKShowDataResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(BcpTKDataInputActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(BcpTKDataInputActivity.this);
                            return;
                        } else {
                            BCPTKShowDataResult dataResult = result.getResult();
                            mNameValue.setText(dataResult.getProductName());
                            mLbValue.setText(dataResult.getSortName());
                            mYlpcValue.setText(dataResult.getYlpc());
                            mScpcValue.setText(dataResult.getScpc());
                            mGgValue.setText(dataResult.getGg());
                            mScTimeValue.setText(dataResult.getScTime());
                            mRemainShlValue.setText(dataResult.getShl() + "");
                            mShlDwValue.setText(dataResult.getDw());
                            mDwZhlValue.setText(dataResult.getDwzl()+"");
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_tkdata_input;
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
        if (TextUtils.isEmpty(value)) {
            Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
            return false;
        }
        float remainShL = Float.parseFloat(mRemainShlValue.getText().toString());
        float tkShL = Float.parseFloat(value);
        if (tkShL > remainShL) {
            Toast.makeText(this, "退库数量不得大于剩余数量", Toast.LENGTH_SHORT).show();
            return false;
        }

        params.setQrCodeId(mQrcodeId);
        params.setShl(tkShL);
        params.setCzy(GlobalData.realName);
        params.setRemark(mRemarkValue.getText().toString());
        params.setBackDh(SharedPreferenceUtil.getBCPTKDNumber());

        return true;
    }

    private void commitDataToWeb() {

        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.bcpTK(params);
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
                            new AlertDialog.Builder(BcpTKDataInputActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("是否继续扫码录入投料数据？")
                                    .setPositiveButton("继续扫码录入", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(BcpTKDataInputActivity.this, ScanActivity.class);
                                            AllActivitiesHolder.removeAct(BcpTKDataInputActivity.this);
                                        }
                                    })
                                    .setNegativeButton("已录入完毕", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(BcpTKDataInputActivity.this, EmployeeMainActivity.class);
                                            AllActivitiesHolder.removeAct(BcpTKDataInputActivity.this);

                                            //这里调接口发推送

                                        }
                                    })
                                    .show();
                        }
                    }
                });

    }

}
