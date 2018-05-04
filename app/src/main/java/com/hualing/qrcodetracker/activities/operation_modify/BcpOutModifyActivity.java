package com.hualing.qrcodetracker.activities.operation_modify;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.SelectDepartmentActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.CpOutShowBean;
import com.hualing.qrcodetracker.bean.CpOutVerifyResult;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.widget.MyListView;
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

public class BcpOutModifyActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 12;
    private static final int REQUEST_CODE_SELECT_FHFZR = 31;
    private static final int REQUEST_CODE_SELECT_FHR = 32;
    private static final int REQUEST_CODE_SELECT_SHFZR = 33;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.outdhValue)
    TextView mOutdhValue;
    @BindView(R.id.LldwValue)
    TextView mLldwValue;
    @BindView(R.id.selectLLBM)
    LinearLayout mSelectLLBM;
    @BindView(R.id.fhfzrValue)
    TextView mFhfzrValue;
    @BindView(R.id.fhRValue)
    TextView mFhRValue;
    @BindView(R.id.shfzrValue)
    TextView mShfzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<CpOutShowBean> mData;
    private String mDh;
    private VerifyParam param;
    private CpOutVerifyResult updatedParam;

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
                AllActivitiesHolder.removeAct(BcpOutModifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        updatedParam = new CpOutVerifyResult();
        param = new VerifyParam();
        if (getIntent() != null) {
            mDh = getIntent().getStringExtra("dh");
            param.setDh(mDh);
        }

        mData = new ArrayList<>();
        mAdapter = new BcpOutModifyActivity.MyAdapter();
        mChildDataList.setAdapter(mAdapter);
        mChildDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<CpOutVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<CpOutVerifyResult>> e) throws Exception {
                ActionResult<CpOutVerifyResult> nr = mainDao.getCpOutVerifyData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<CpOutVerifyResult>>() {
                    @Override
                    public void accept(ActionResult<CpOutVerifyResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            CpOutVerifyResult dataResult = result.getResult();
                            updatedParam = dataResult;
                            mOutdhValue.setText(dataResult.getOutDh());
                            mLldwValue.setText(dataResult.getLhDw());
                            mSelectLLBM.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtil.openActivityForResult(BcpOutModifyActivity.this, SelectDepartmentActivity.class, REQUEST_CODE_SELECT_DEPARTMENT, null);
                                }
                            });
                            mFhfzrValue.setText(dataResult.getFhFzr());
                            mFhfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setFhFzr("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mFhRValue.setText(dataResult.getLhR());
                            mFhRValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setLhR("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mShfzrValue.setText(dataResult.getLhFzr());
                            mShfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setLhFzr("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mRemarkValue.setText(dataResult.getRemark());
                            mRemarkValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setRemark("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                            if (dataResult.getBeans() != null && dataResult.getBeans().size() > 0) {
                                mData.clear();
                                mData.addAll(dataResult.getBeans());
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bcp_out_modify;
    }

    @OnClick({R.id.confirmBtn,R.id.selectFHFZR,R.id.selectFHR,R.id.selectSHFZR})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.selectFHFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_FHFZR, null);

                break;
            case R.id.selectFHR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_FHR, null);

                break;
            case R.id.selectSHFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_SHFZR, null);

                break;
            case R.id.confirmBtn:
                toCommit();
                break;
        }

    }

    private void toCommit() {

        if ("请选择部门".equals(mLldwValue.getText().toString())
                || "请选择发货人".equals(mFhRValue.getText().toString())
                || "请选择收货负责人".equals(mShfzrValue.getText().toString())
                || "请选择发货负责人".equals(mFhfzrValue.getText().toString())
                ) {
            Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        updatedParam.setBeans(mData);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.toUpdateCpOutData(updatedParam);
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
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(BcpOutModifyActivity.this, R.layout.item_cpout_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final CpOutShowBean bean = mData.get(position);
            viewHolder.mNameValue.setText(bean.getCpName());
            viewHolder.mLbValue.setText(bean.getSortID() + "");
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mScpcValue.setText(bean.getsCPC());
            viewHolder.mScTimeValue.setText(bean.getfHDate());

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.nameValue)
            TextView mNameValue;
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.scpcValue)
            TextView mScpcValue;
            @BindView(R.id.scTimeValue)
            TextView mScTimeValue;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_DEPARTMENT:
                    mLldwValue.setText(data.getStringExtra("groupName"));
                    updatedParam.setLhDw(data.getStringExtra("groupName"));
                    break;
                case REQUEST_CODE_SELECT_FHFZR:
                    mFhfzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_FHR:
                    mFhRValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_SHFZR:
                    mShfzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
