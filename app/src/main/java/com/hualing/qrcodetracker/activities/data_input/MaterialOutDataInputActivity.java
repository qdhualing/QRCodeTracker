//package com.hualing.qrcodetracker.activities.data_input;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hualing.qrcodetracker.R;
//import com.hualing.qrcodetracker.activities.BaseActivity;
//import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
//import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
//import com.hualing.qrcodetracker.bean.DataBean;
//import com.hualing.qrcodetracker.bean.DataInputParams;
//import com.hualing.qrcodetracker.dao.MainDao;
//import com.hualing.qrcodetracker.global.TheApplication;
//import com.hualing.qrcodetracker.util.AllActivitiesHolder;
//import com.hualing.qrcodetracker.widget.TitleBar;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;
//
//public class MaterialOutDataInputActivity extends BaseActivity {
//
//    @BindView(R.id.title)
//    TitleBar mTitle;
//    @BindView(R.id.dataList)
//    RecyclerView mRecyclerView;
//
//    private MyRecyclerAdapter mAdapter;
//
//    //传过来的总的数据集合
//    //    private List<DataBean> mRealData;
//    //需要录入的数据集合
//    private List<DataBean> mDataL;
//    private MainDao mainDao;
//
//    //二维码解析出的Id
//    private String mQrcodeId;
//
//    @Override
//    protected void initLogic() {
//
//        mainDao = YoniClient.getInstance().create(MainDao.class);
//
//        if (getIntent() != null) {
//            mQrcodeId = getIntent().getStringExtra("qrCodeId");
//        }
//
//        mTitle.setRightButtonEnable(false);
//        mTitle.setEvents(new TitleBar.AddClickEvents() {
//            @Override
//            public void clickLeftButton() {
//                AllActivitiesHolder.removeAct(MaterialOutDataInputActivity.this);
//            }
//
//            @Override
//            public void clickRightButton() {
//
//            }
//        });
//
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
//
//        initData();
//
//    }
//
//    private void initData() {
//        String[] names = getResources().getStringArray(R.array.materialOutData);
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
//
//    @Override
//    protected void getDataFormWeb() {
//
//        //        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
//        //        progressDialog.show();
//        //
//        //        final GetNeedInputedDataParams params = new GetNeedInputedDataParams();
//        //        params.setId(mQrcodeId);
//        //        params.setType(GlobalData.currentFunctionType);
//        //
//        //        Observable.create(new ObservableOnSubscribe<ActionResult<DataResult>>() {
//        //            @Override
//        //            public void subscribe(ObservableEmitter<ActionResult<DataResult>> e) throws Exception {
//        //                ActionResult<DataResult> nr = mainDao.getNeedInputedData(params);
//        //                e.onNext(nr);
//        //            }
//        //        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
//        //                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
//        //                .subscribe(new Consumer<ActionResult<DataResult>>() {
//        //                    @Override
//        //                    public void accept(ActionResult<DataResult> result) throws Exception {
//        //                        progressDialog.dismiss();
//        //                        if (result.getCode() != 0) {
//        //                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
//        //                            AllActivitiesHolder.removeAct(MaterialInDataInputActivity.this);
//        //                            return;
//        //                        } else {
//        //                            DataResult dataResult = result.getResult();
//        //                            List<DataBean> beanList = dataResult.getNeedToInputDataList();
//        //                            mRealData.clear();
//        //                            mRealData.addAll(beanList);
//        //                            mDataL.clear();
//        //                            for (int i = 0; i < mRealData.size(); i++) {
//        //                                DataBean b = mRealData.get(i);
//        //                                if (!TextUtils.isEmpty(b.getName())) {
//        //                                    mDataL.add(b);
//        //                                }
//        //                            }
//        //                            mAdapter.notifyDataSetChanged();
//        //                        }
//        //                    }
//        //                });
//
//    }
//
//    @Override
//    protected void debugShow() {
//
//    }
//
//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_material_out_data_input;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
//
//    @OnClick(R.id.commitBtn)
//    public void onViewClicked() {
//
//        //数据录入是否完整
//        if (checkIfInfoPerfect()) {
//            commitDataToWeb();
//        }
//
//    }
//
//    private boolean checkIfInfoPerfect() {
//        for (DataBean bean : mDataL) {
//            if (TextUtils.isEmpty(bean.getValue())) {
//                Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void commitDataToWeb() {
//
//        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
//        progressDialog.show();
//
//        final DataInputParams params = new DataInputParams();
//        params.setNeedToInputDataList(mDataL);
//
//        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
//            @Override
//            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
//                ActionResult<ActionResult> nr = mainDao.commitMaterialInInputedData(params);
//                e.onNext(nr);
//            }
//        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
//                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
//                .subscribe(new Consumer<ActionResult<ActionResult>>() {
//                    @Override
//                    public void accept(ActionResult<ActionResult> result) throws Exception {
//                        progressDialog.dismiss();
//                        Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
//                        if (result.getCode() != 0) {
//                        } else {
//                            AllActivitiesHolder.removeAct(MaterialOutDataInputActivity.this);
//                        }
//                    }
//                });
//
//    }
//
//
//    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
//
//        @Override
//        public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(MaterialOutDataInputActivity.this).inflate(R.layout.item_data_input, parent, false);
//            return new MyRecyclerAdapter.MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(MyRecyclerAdapter.MyViewHolder holder, int position) {
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
//
//}
