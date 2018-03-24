package com.hualing.qrcodetracker.activities.operation_wl.wl_in;

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
import com.hualing.qrcodetracker.activities.main.EmployeeMainActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectLBActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.WLINParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MaterialInDataInputActivity extends BaseActivity {

    private static final int GET_WLSORT_CODE = 30;
    private static final int SELECT_LEI_BIE = 11;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.wlbmValue)
    TextView mWlbmValue;
    //    @BindView(R.id.wlbmValue)
    //    EditText mWlbmValue;
    @BindView(R.id.nameValue)
    EditText mNameValue;
    @BindView(R.id.cdValue)
    EditText mCdValue;
    //    @BindView(R.id.lbValue)
    //    EditText mLbValue;
    @BindView(R.id.lbValue)
    TextView mLbValue;
    @BindView(R.id.ggValue)
    EditText mGgValue;
    @BindView(R.id.ylpcValue)
    EditText mYlpcValue;
    @BindView(R.id.sldwValue)
    EditText mSldwValue;
    @BindView(R.id.slValue)
    EditText mSlValue;
    @BindView(R.id.zhlValue)
    EditText mZhlValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    //    @BindView(R.id.dataList)
    //    RecyclerView mRecyclerView;

    //    private MyRecyclerAdapter mAdapter;

    //传过来的总的数据集合
    //    private List<DataBean> mRealData;
    //需要录入的数据集合
    //    private List<DataBean> mDataL;
    private MainDao mainDao;

    //二维码解析出的Id
    private String mQrcodeId;
    private WLINParam params;

    private String mSelectedWLBM;
    private int mSelectedLeiBieId = -1;

    @Override
    protected void initLogic() {

        mainDao = YoniClient.getInstance().create(MainDao.class);

        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
        }

        params = new WLINParam();

        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(MaterialInDataInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });


    }

    @Override
    protected void getDataFormWeb() {
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_data_input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.selectWLBM, R.id.commitBtn, R.id.selectLB})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.selectWLBM:
                IntentUtil.openActivityForResult(this, SelectHlSortActivity.class, GET_WLSORT_CODE, null);
                break;
            case R.id.selectLB:
                IntentUtil.openActivityForResult(this, SelectLBActivity.class, SELECT_LEI_BIE, null);
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
                case GET_WLSORT_CODE:
                    String ss = data.getStringExtra("sortName");
                    mWlbmValue.setText(ss);
                    mSelectedWLBM = data.getStringExtra("sortCode");
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

    private boolean checkIfInfoPerfect() {
        String wlbmValue = mWlbmValue.getText().toString();
        String nameValue = mNameValue.getText().toString();
        String cdValue = mCdValue.getText().toString();
        String lbValue = mLbValue.getText().toString();
        String ggValue = mGgValue.getText().toString();
        String ylpcValue = mYlpcValue.getText().toString();
        String sldwValue = mSldwValue.getText().toString();
        String slValue = mSlValue.getText().toString();
        String zhlValue = mZhlValue.getText().toString();
        String beizhuValue = mRemarkValue.getText().toString();
        if ("请选择物料种类".equals(wlbmValue)
                || TextUtils.isEmpty(nameValue)
                || TextUtils.isEmpty(cdValue)
                || "请选择类别".equals(lbValue)
                || TextUtils.isEmpty(ggValue)
                || TextUtils.isEmpty(ylpcValue)
                || TextUtils.isEmpty(sldwValue)
                || TextUtils.isEmpty(slValue)
                || TextUtils.isEmpty(zhlValue)
                || TextUtils.isEmpty(beizhuValue)
                ) {
            Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
            return false;
        }

        params.setwLCode(mSelectedWLBM);
        params.setProductName(nameValue);
        params.setcHD(cdValue);
        params.setLb(mSelectedLeiBieId);
        params.setgG(ggValue);
        params.setyLPC(ylpcValue);
        params.setdW(sldwValue);
        params.setShl(Float.parseFloat(slValue));
        params.setpCZL(Float.parseFloat(zhlValue) * Float.parseFloat(slValue));
        params.setdWZL(Float.parseFloat(zhlValue));
        params.setRemark(beizhuValue);

        params.setcZY(GlobalData.realName);
        params.setqRCodeID(mQrcodeId);
        params.setInDh(SharedPreferenceUtil.getWlRKDNumber());

        return true;
    }

    private void commitDataToWeb() {

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.commitMaterialInInputedData(params);
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
                            new AlertDialog.Builder(MaterialInDataInputActivity.this)
                                    .setCancelable(false)
                                    .setTitle("提示")
                                    .setMessage("是否继续扫码录入数据？")
                                    .setPositiveButton("继续扫码录入", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(MaterialInDataInputActivity.this, ScanActivity.class);
                                            AllActivitiesHolder.removeAct(MaterialInDataInputActivity.this);
                                        }
                                    })
                                    .setNegativeButton("已录入完毕", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.openActivity(MaterialInDataInputActivity.this, EmployeeMainActivity.class);
                                            AllActivitiesHolder.removeAct(MaterialInDataInputActivity.this);
                                        }
                                    })
                                    .show();
                        }
                    }
                });

    }


    //    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    //
    //        @Override
    //        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //            View view = LayoutInflater.from(MaterialInDataInputActivity.this).inflate(R.layout.item_data_input, parent, false);
    //            return new MyViewHolder(view);
    //        }
    //
    //        @Override
    //        public void onBindViewHolder(MyViewHolder holder, int position) {
    //            final DataBean bean = mDataL.get(position);
    //
    //            String name = bean.getName();
    //            holder.name.setText(name + ":");
    //            holder.value.addTextChangedListener(new TextWatcher() {
    //                @Override
    //                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    //                }
    //
    //                @Override
    //                public void onTextChanged(CharSequence s, int start, int before, int count) {
    //                    bean.setValue(s.toString());
    //                }
    //
    //                @Override
    //                public void afterTextChanged(Editable s) {
    //                }
    //            });
    //        }
    //
    //        @Override
    //        public int getItemCount() {
    //            return mDataL.size();
    //        }
    //
    //        public class MyViewHolder extends RecyclerView.ViewHolder {
    //            TextView name;
    //            EditText value;
    //
    //            public MyViewHolder(View itemView) {
    //                super(itemView);
    //                name = itemView.findViewById(R.id.name);
    //                value = itemView.findViewById(R.id.value);
    //            }
    //        }
    //
    //    }

}
