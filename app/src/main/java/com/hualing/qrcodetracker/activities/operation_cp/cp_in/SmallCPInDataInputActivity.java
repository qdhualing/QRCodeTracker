package com.hualing.qrcodetracker.activities.operation_cp.cp_in;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectBigCpActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectCJActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectGXActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectLBActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectSXYLActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.SelectHlSortActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.SmallCPINParam;
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

public class SmallCPInDataInputActivity extends BaseActivity {


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
    @BindView(R.id.shlValue)
    EditText mShlValue;
    @BindView(R.id.dwzlValue)
    EditText mDwzlValue;
    @BindView(R.id.dwValue)
    EditText mDwValue;
    @BindView(R.id.cjValue)
    TextView mCjValue;
    @BindView(R.id.gxValue)
    TextView mGxValue;
    @BindView(R.id.sxylValue)
    TextView mSxylValue;
    @BindView(R.id.zjyValue)
    EditText mZjyValue;
    @BindView(R.id.jyztValue)
    EditText mJyztValue;
    @BindView(R.id.jybzValue)
    EditText mJybzValue;
    @BindView(R.id.scTimeValue)
    TextView mScTimeValue;
    @BindView(R.id.bigCpValue)
    TextView mBigCpValue;
    @BindView(R.id.remainShlValue)
    TextView mRemainShlValue;

    //大包装的最大容量
    private static final int MAX_NUM = 50;

    private static final int SELECT_HL_SORT = 10;
    private static final int SELECT_LEI_BIE = 11;
    private static final int SELECT_CHE_JIAN = 20;
    private static final int SELECT_GONG_XU = 30;
    private static final int SELECT_SXYL = 40;
    private static final int SELECT_BIG_CP = 41;

    private MainDao mainDao;

    //二维码解析出的Id
    private String mQrcodeId;
    private SmallCPINParam params;

    private String mSelectedBcpCode;

    private int mSelectedCheJianId;
    private String mCJHasGongXuId;
    private int mSelectedGxId = -1;
    private String mSXYLQrcodeStr = null;
    private int mSelectedLeiBieId = -1;
    private String mSelectedBigCpQrCodeId;

    private CustomDatePicker customDatePicker;
    private String mNowTime;
    private int mRemainShl = 0;

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

        params = new SmallCPINParam();

        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(SmallCPInDataInputActivity.this);
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
        return R.layout.activity_small_cpin_data_input;
    }

    @OnClick({R.id.selectBCPCode, R.id.selectCJ, R.id.selectGX, R.id.selectSXYL, R.id.selectBigCp, R.id.scTimeValue, R.id.commitBtn, R.id.selectLB})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.selectBCPCode:
                IntentUtil.openActivityForResult(this, SelectHlSortActivity.class, SELECT_HL_SORT, null);
                break;
            case R.id.selectLB:
                IntentUtil.openActivityForResult(this, SelectLBActivity.class, SELECT_LEI_BIE, null);
                break;
            case R.id.selectBigCp:
                IntentUtil.openActivityForResult(this, SelectBigCpActivity.class, SELECT_BIG_CP, null);
                break;
            case R.id.selectCJ:
                IntentUtil.openActivityForResult(this, SelectCJActivity.class, SELECT_CHE_JIAN, null);
                break;
            case R.id.selectGX:
                if ("请选择车间".equals(mCjValue.getText().toString())) {
                    Toast.makeText(this, "请先选择车间", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("cjGXIds", mCJHasGongXuId);
                IntentUtil.openActivityForResult(this, SelectGXActivity.class, SELECT_GONG_XU, bundle);
                break;
            case R.id.selectSXYL:
                if ("请选择工序".equals(mGxValue.getText().toString())) {
                    Toast.makeText(this, "请先选择工序", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle2 = new Bundle();
                bundle2.putInt("selectedGxId", mSelectedGxId);
                IntentUtil.openActivityForResult(this, SelectSXYLActivity.class, SELECT_SXYL, bundle2);
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
                case SELECT_BIG_CP:
                    String bigCpName = data.getStringExtra("cpName");
                    mBigCpValue.setText(bigCpName);
                    mSelectedBigCpQrCodeId = data.getStringExtra("cpQRCodeId");
                    mRemainShl = MAX_NUM - data.getIntExtra("remainShl", -1);
                    mRemainShlValue.setText(mRemainShl + "");
                    break;
                case SELECT_CHE_JIAN:
                    String cjName = data.getStringExtra("cjName");
                    mCjValue.setText(cjName);
                    mSelectedCheJianId = data.getIntExtra("cjId", -1);
                    mCJHasGongXuId = data.getStringExtra("cjGXIds");
                    break;
                case SELECT_GONG_XU:
                    String s1 = data.getStringExtra("gxName");
                    mGxValue.setText(s1);
                    mSelectedGxId = data.getIntExtra("gxId", -1);
                    break;
                case SELECT_SXYL:
                    String allYlStr = data.getStringExtra("allYlStr");
                    if (TextUtils.isEmpty(allYlStr))
                        return;
                    mSxylValue.setText(allYlStr);
                    //所有所需原料的二维码id字符串
                    mSXYLQrcodeStr = data.getStringExtra("allYlQrCode");
                    Log.d("Test", "get: " + mSXYLQrcodeStr);
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
                ActionResult<ActionResult> nr = mainDao.commitSmallCPInInputedData(params);
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
                            new AlertDialog.Builder(SmallCPInDataInputActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("是否继续扫码录入数据？")
                                    .setPositiveButton("继续扫码录入", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(SmallCPInDataInputActivity.this, ScanActivity.class);
                                            AllActivitiesHolder.removeAct(SmallCPInDataInputActivity.this);
                                        }
                                    })
                                    .setNegativeButton("已录入完毕", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            AllActivitiesHolder.removeAct(SmallCPInDataInputActivity.this);
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
        String bigCpValue = mBigCpValue.getText().toString();
        String ylpcValue = mYlpcValue.getText().toString();
        String scpcValue = mScpcValue.getText().toString();
        String ggValue = mGgValue.getText().toString();
        String shlValue = mShlValue.getText().toString();
        String dwzlValue = mDwzlValue.getText().toString();
        String dwValue = mDwValue.getText().toString();
        String zjyValue = mZjyValue.getText().toString();
        String jyztValue = mJyztValue.getText().toString();
        String sxylValue = mSxylValue.getText().toString();
        String scTimeValue = mScTimeValue.getText().toString();
        if ("请选择成品编码".equals(cpCodeValue)
                || TextUtils.isEmpty(nameValue)
                || "请选择类别".equals(lbValue)
                //                || "请选择所属大包装".equals(bigCpValue)
                || TextUtils.isEmpty(ylpcValue)
                || TextUtils.isEmpty(scpcValue)
                || TextUtils.isEmpty(ggValue)
                || TextUtils.isEmpty(shlValue)
                || TextUtils.isEmpty(dwzlValue)
                || TextUtils.isEmpty(dwValue)
                || TextUtils.isEmpty(zjyValue)
                || TextUtils.isEmpty(jyztValue)
                || "请选择所需原料".equals(sxylValue)
                || "请选择生产时间".equals(scTimeValue)
                ) {
            Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!"请选择所属大包装".equals(bigCpValue) && Float.parseFloat(shlValue) > mRemainShl) {
            Toast.makeText(this, "已超出大包装最大容量", Toast.LENGTH_SHORT).show();
            return false;
        }

        params.setCpCode(mSelectedBcpCode);
        params.setProductName(nameValue);
        params.setSortID(mSelectedLeiBieId);
        if (!"请选择所属大包装".equals(bigCpValue))
            params.setcPS2QRCode(mSelectedBigCpQrCodeId);
        params.setShl(Integer.parseInt(shlValue));
        params.setYlpc(ylpcValue);
        params.setScpc(scpcValue);
        params.setGg(ggValue);
        params.setDwzl(Float.parseFloat(dwzlValue));
        params.setDw(dwValue);
        params.setZjy(zjyValue);
        params.setScTime(scTimeValue);
        params.setJyzt(jyztValue);
        params.setCzy(GlobalData.realName);
        params.setQrCodeId(mQrcodeId);
        params.setBz(1);
        params.setIndh(SharedPreferenceUtil.getBCPRKDNumber());

        //填充所需原料数据
        addSXYLData();

        return true;
    }

    private void addSXYLData() {
        if (TextUtils.isEmpty(mSXYLQrcodeStr))
            return;
        Log.d("Test", "mSXYLQrcodeStr: " + mSXYLQrcodeStr);
        String[] array = mSXYLQrcodeStr.split(",");
        if (array != null) {
            if (array.length > 0 && !TextUtils.isEmpty(array[0])) {
                params.setYl1(array[0]);
            }
            if (array.length > 1 && !TextUtils.isEmpty(array[1])) {
                params.setYl2(array[1]);
            }
            if (array.length > 2 && !TextUtils.isEmpty(array[2])) {
                params.setYl3(array[2]);
            }
            if (array.length > 3 && !TextUtils.isEmpty(array[3])) {
                params.setYl4(array[3]);
            }
            if (array.length > 4 && !TextUtils.isEmpty(array[4])) {
                params.setYl5(array[4]);
            }
            if (array.length > 5 && !TextUtils.isEmpty(array[5])) {
                params.setYl6(array[5]);
            }
            if (array.length > 6 && !TextUtils.isEmpty(array[6])) {
                params.setYl7(array[6]);
            }
            if (array.length > 7 && !TextUtils.isEmpty(array[7])) {
                params.setYl8(array[7]);
            }
            if (array.length > 8 && !TextUtils.isEmpty(array[8])) {
                params.setYl9(array[8]);
            }
            if (array.length > 9 && !TextUtils.isEmpty(array[9])) {
                params.setYl10(array[9]);
            }
        }

    }

}
