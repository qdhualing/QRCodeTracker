package com.hualing.qrcodetracker.activities.data_input;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.EmployeeMainActivity;
import com.hualing.qrcodetracker.activities.ScanActivity;
import com.hualing.qrcodetracker.activities.SelectWLSortActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.PdtSortBean;
import com.hualing.qrcodetracker.bean.PdtSortResult;
import com.hualing.qrcodetracker.bean.WLINParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

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

    private ListView popupView;
    private BaseAdapter mPopAdapter;
    private PopupWindow popupWindow;
    private List<PdtSortBean> mSortData;
    private int mSelectedSortId;
    private String mSelectedWLBM ;

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

        mSortData = new ArrayList<>();
        popupWindow = new PopupWindow(MaterialInDataInputActivity.this);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.popupwindow_back_shape));
        popupView = (ListView) LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        popupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PdtSortBean sort2Result = mSortData.get(position);
                mLbValue.setText(sort2Result.getSortName());
                mSelectedSortId = sort2Result.getiD();
                popupWindow.dismiss();
            }
        });
        mPopAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mSortData.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(MaterialInDataInputActivity.this).inflate(R.layout.popup_single_layout,parent,false);
                }
                TextView tV = (TextView) convertView;
                final PdtSortBean sort2Result = mSortData.get(position);
                tV.setText(sort2Result.getSortName());
                return convertView;
            }

        };
        popupView.setAdapter(mPopAdapter);
        popupWindow.setContentView(popupView);


        mLbValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else
                    popupWindow.showAsDropDown(mLbValue);
            }
        });

        //        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //        //        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
        //        //                this, LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.divide_dark_gray_color)));
        //
        //        //        mRealData = new ArrayList<>();
        //        mDataL = new ArrayList<>();
        //
        //        mAdapter = new MyRecyclerAdapter();
        //        mRecyclerView.setAdapter(mAdapter);

        //        initData();

    }

    //    private void initData() {
    //        String[] names = getResources().getStringArray(R.array.materialInData);
    //        mDataL.clear();
    //        DataBean b1 = new DataBean();
    //        b1.setName(names[0]);
    //        DataBean b2 = new DataBean();
    //        b2.setName(names[1]);
    //        DataBean b3 = new DataBean();
    //        b3.setName(names[2]);
    //        DataBean b4 = new DataBean();
    //        b4.setName(names[3]);
    //        DataBean b5 = new DataBean();
    //        b5.setName(names[4]);
    //        DataBean b6 = new DataBean();
    //        b6.setName(names[5]);
    //        mDataL.add(b1);
    //        mDataL.add(b2);
    //        mDataL.add(b3);
    //        mDataL.add(b4);
    //        mDataL.add(b5);
    //        mDataL.add(b6);
    //    }

    @Override
    protected void getDataFormWeb() {

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();


        Observable.create(new ObservableOnSubscribe<ActionResult<PdtSortResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<PdtSortResult>> e) throws Exception {
                ActionResult<PdtSortResult> nr = mainDao.getPdtSort();
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<PdtSortResult>>() {
                    @Override
                    public void accept(ActionResult<PdtSortResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            PdtSortResult data = result.getResult();
                            List<PdtSortBean> sortBeans = data.getSortBeans();
                            mSortData.clear();
                            mSortData.addAll(sortBeans);
                            mPopAdapter.notifyDataSetChanged();
                        }
                    }
                });

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

    @OnClick({R.id.selectWLBM,R.id.commitBtn})
    public void onViewClicked(View view) {

        switch (view.getId()){
            case R.id.selectWLBM:
                IntentUtil.openActivityForResult(this, SelectWLSortActivity.class,GET_WLSORT_CODE,null);
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

        if (resultCode==RESULT_OK) {
            if (requestCode == GET_WLSORT_CODE) {
                String ss = data.getStringExtra("sortName");
                mWlbmValue.setText(ss);
                mSelectedWLBM = data.getStringExtra("sortCode");
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
        params.setLb(mSelectedSortId);
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
