package com.hualing.qrcodetracker.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.DataBean;
import com.hualing.qrcodetracker.bean.DataInputParams;
import com.hualing.qrcodetracker.bean.DataResult;
import com.hualing.qrcodetracker.bean.GetNeedInputedDataParams;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class DataInputActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.dataList)
    RecyclerView mRecyclerView;

    private MyRecyclerAdapter mAdapter;

    //传过来的总的数据集合
    private List<DataBean> mRealData;
    //需要录入的数据集合
    private List<DataBean> mDataL;
    private MainDao mainDao;

    //二维码解析出的Id
    private String mQrcodeId;

    @Override
    protected void initLogic() {

        mainDao = YoniClient.getInstance().create(MainDao.class);

        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
        }

        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(DataInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
        //                this, LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.divide_dark_gray_color)));

        mRealData = new ArrayList<>();
        mDataL = new ArrayList<>();

        mAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void getDataFormWeb() {

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        final GetNeedInputedDataParams params = new GetNeedInputedDataParams();
        params.setId(mQrcodeId);
        params.setType(GlobalData.currentFunctionType);

        Observable.create(new ObservableOnSubscribe<ActionResult<DataResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<DataResult>> e) throws Exception {
                ActionResult<DataResult> nr = mainDao.getNeedInputedData(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<DataResult>>() {
                    @Override
                    public void accept(ActionResult<DataResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            DataResult dataResult = result.getResult();
                            List<DataBean> beanList = dataResult.getNeedToInputDataList();
                            mRealData.clear();
                            mRealData.addAll(beanList);
                            mDataL.clear();
                            for (int i = 0; i < mRealData.size(); i++) {
                                DataBean b = mRealData.get(i);
                                if (!TextUtils.isEmpty(b.getName())) {
                                    mDataL.add(b);
                                }
                            }
                            mAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.commitBtn)
    public void onViewClicked() {

        //数据录入是否完整
        if (checkIfInfoPerfect()) {
            updateInfoList();
            commitDataToWeb();
        }

    }

    private void updateInfoList() {
        for (int i = 0; i < mRealData.size(); i++) {
            DataBean b = mRealData.get(i);
            for (int j = 0; j < mDataL.size(); j++) {
                DataBean b2 = mDataL.get(j);
                if (b.getSqlFieldName().equals(b2.getSqlFieldName())) {
                    b.setValue(b2.getValue());
                    break;
                }
            }

        }
    }

    private boolean checkIfInfoPerfect() {
        for (DataBean bean : mDataL) {
            if (TextUtils.isEmpty(bean.getValue())) {
                Toast.makeText(this, "录入信息不完整", Toast.LENGTH_SHORT).show();
                return false;
            }
            //判断
            if (bean.getDataType() == Types.TIMESTAMP) {
                if (!isValidDate(bean.getValue())){
                    Toast.makeText(this, "输入时间格式不对", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    private void commitDataToWeb() {

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        final DataInputParams params = new DataInputParams();
        params.setNeedToInputDataList(mRealData);
        params.setType(GlobalData.currentFunctionType);

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.commitInputedData(params);
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
                            AllActivitiesHolder.removeAct(DataInputActivity.this);
                        }
                    }
                });

    }


    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DataInputActivity.this).inflate(R.layout.item_data_input, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final DataBean bean = mDataL.get(position);

            switch (bean.getDataType()) {
                case Types.BIGINT:
                case Types.INTEGER:
                    holder.value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                    break;
                case Types.FLOAT:
                case Types.DOUBLE:
                    holder.value.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                    break;
                case Types.TIME:
                case Types.TIMESTAMP:
                    holder.value.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);
                    holder.value.setHint("格式为\"yyyy-MM-dd HH:mm:ss\"");
                    break;
            }

            String name = bean.getName();
            holder.name.setText(name + ":");
            holder.value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setValue(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataL.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            EditText value;

            public MyViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                value = itemView.findViewById(R.id.value);
            }
        }

    }

}
