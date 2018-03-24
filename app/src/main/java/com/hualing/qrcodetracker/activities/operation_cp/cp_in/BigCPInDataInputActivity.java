package com.hualing.qrcodetracker.activities.operation_cp.cp_in;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectLBActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.SelectHlSortActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BigCPINParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.TitleBar;
import com.hualing.qrcodetracker.widget.datetime_picker.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BigCPInDataInputActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.bcpCodeValue)
    TextView mBcpCodeValue;
    @BindView(R.id.productNameValue)
    EditText mProductNameValue;
    @BindView(R.id.lbValue)
    TextView mLbValue;
    @BindView(R.id.ylpcValue)
    EditText mYlpcValue;
    @BindView(R.id.scpcValue)
    EditText mScpcValue;
    @BindView(R.id.ggValue)
    EditText mGgValue;
    @BindView(R.id.dwzlValue)
    EditText mDwzlValue;
    @BindView(R.id.dwValue)
    EditText mDwValue;
    @BindView(R.id.zjyValue)
    EditText mZjyValue;
    @BindView(R.id.jyztValue)
    EditText mJyztValue;
    @BindView(R.id.jybzValue)
    EditText mJybzValue;
    @BindView(R.id.scTimeValue)
    TextView mScTimeValue;


    private static final int SELECT_HL_SORT = 10;
    private static final int SELECT_LEI_BIE = 11;

    private MainDao mainDao;

    //二维码解析出的Id
    private String mQrcodeId;
    private BigCPINParam params;

    private String mSelectedBcpCode;

    private int mSelectedLeiBieId = -1;

    private CustomDatePicker customDatePicker;
    private String mNowTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mainDao = YoniClient.getInstance().create(MainDao.class);

        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
        }

        params = new BigCPINParam();

        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(BigCPInDataInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        initDatePicker();
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        mNowTime = now;
        //        currentDate.setText(now.split(" ")[0]);
        //        currentTime.setText(now);

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                mScTimeValue.setText(time);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动

    }

    @Override
    protected void getDataFormWeb() {
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cpin_data_input;
    }

    @OnClick({R.id.selectBCPCode, R.id.selectLB, R.id.scTimeValue, R.id.commitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectBCPCode:
                IntentUtil.openActivityForResult(this, SelectHlSortActivity.class, SELECT_HL_SORT, null);
                break;
            case R.id.selectLB:
                IntentUtil.openActivityForResult(this, SelectLBActivity.class, SELECT_LEI_BIE, null);
                break;
            case R.id.scTimeValue:
                customDatePicker.show(mNowTime);
                break;
            case R.id.commitBtn:
                //数据录入是否完整
                if (checkIfInfoPerfect()) {
                    commitDataToWeb();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_HL_SORT:
                    String sortName = data.getStringExtra("sortName");
                    mBcpCodeValue.setText(sortName);
                    mSelectedBcpCode = data.getStringExtra("sortCode");
                    break;
                case SELECT_LEI_BIE:
                    String lbName = data.getStringExtra("lbName");
                    mLbValue.setText(lbName);
                    mSelectedLeiBieId = data.getIntExtra("lbId", -1);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void commitDataToWeb() {

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.commitBigCPInInputedData(params);
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
                            new AlertDialog.Builder(BigCPInDataInputActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("是否继续扫码录入数据？")
                                    .setPositiveButton("继续扫码录入", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(BigCPInDataInputActivity.this, ScanActivity.class);
                                            AllActivitiesHolder.removeAct(BigCPInDataInputActivity.this);
                                        }
                                    })
                                    .setNegativeButton("已录入完毕", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            AllActivitiesHolder.removeAct(BigCPInDataInputActivity.this);
                                        }
                                    })
                                    .show();
                        }
                    }
                });

    }

    private boolean checkIfInfoPerfect() {
        String cpCodeValue = mBcpCodeValue.getText().toString();
        String nameValue = mProductNameValue.getText().toString();
        String lbValue = mLbValue.getText().toString();
        String ylpcValue = mYlpcValue.getText().toString();
        String scpcValue = mScpcValue.getText().toString();
        String ggValue = mGgValue.getText().toString();
        String dwzlValue = mDwzlValue.getText().toString();
        String dwValue = mDwValue.getText().toString();
        String zjyValue = mZjyValue.getText().toString();
        String jyztValue = mJyztValue.getText().toString();
        String jybzValue = mJybzValue.getText().toString();
        String scTimeValue = mScTimeValue.getText().toString();
        if ("请选择成品编码".equals(cpCodeValue)
                || TextUtils.isEmpty(nameValue)
                || "请选择类别".equals(lbValue)
                || TextUtils.isEmpty(ylpcValue)
                || TextUtils.isEmpty(scpcValue)
                || TextUtils.isEmpty(ggValue)
                || TextUtils.isEmpty(dwzlValue)
                || TextUtils.isEmpty(dwValue)
                || TextUtils.isEmpty(zjyValue)
                || TextUtils.isEmpty(jyztValue)
                || "请选择生产时间".equals(scTimeValue)
                ) {
            Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
            return false;
        }

        params.setCpCode(mSelectedBcpCode);
        params.setProductName(nameValue);
        params.setSortID(mSelectedLeiBieId);
        params.setYlpc(ylpcValue);
        params.setScpc(scpcValue);
        params.setGg(ggValue);
        params.setDwzl(Float.parseFloat(dwzlValue));
        params.setDw(dwValue);
        params.setZjy(zjyValue);
        params.setScTime(scTimeValue);
        params.setJyzt(jyztValue);
        params.setJybz(jybzValue);
        params.setCzy(GlobalData.realName);
        params.setQrCodeId(mQrcodeId);
        params.setBz(1);
        params.setIndh(SharedPreferenceUtil.getBCPRKDNumber());
        return true;
    }

}
