package com.hualing.qrcodetracker.activities.operation_modify;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.bean.WLTkShowBean;
import com.hualing.qrcodetracker.bean.WlTkVerifyResult;
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

public class WlTkModifyActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_DEPARTMENT = 12;
    private static final int REQUEST_CODE_SELECT_TLFZR = 31;
    private static final int REQUEST_CODE_SELECT_SLFZR = 32;
    private static final int REQUEST_CODE_SELECT_SLR = 33;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.backdhValue)
    TextView mBackdhValue;
    @BindView(R.id.LldwValue)
    TextView mLldwValue;
    @BindView(R.id.selectLLBM)
    LinearLayout mSelectLLBM;
    @BindView(R.id.slfzrValue)
    TextView mSlfzrValue;
    @BindView(R.id.slRValue)
    TextView mSlRValue;
    @BindView(R.id.tkfzrValue)
    TextView mTkfzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<WLTkShowBean> mData;
    private String mDh;
    private VerifyParam param;
    private WlTkVerifyResult updatedParam;

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
                AllActivitiesHolder.removeAct(WlTkModifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        updatedParam = new WlTkVerifyResult();
        param = new VerifyParam();
        if (getIntent() != null) {
            mDh = getIntent().getStringExtra("dh");
            param.setDh(mDh);
        }

        mData = new ArrayList<>();
        mAdapter = new MyAdapter();
        mChildDataList.setAdapter(mAdapter);
        mChildDataList.setFocusable(false);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<WlTkVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WlTkVerifyResult>> e) throws Exception {
                ActionResult<WlTkVerifyResult> nr = mainDao.getWlTkVerifyData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WlTkVerifyResult>>() {
                    @Override
                    public void accept(ActionResult<WlTkVerifyResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            WlTkVerifyResult dataResult = result.getResult();
                            updatedParam = dataResult;
                            mBackdhValue.setText(dataResult.getBackDh());
                            mLldwValue.setText(dataResult.getThDw());
                            mSelectLLBM.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtil.openActivityForResult(WlTkModifyActivity.this, SelectDepartmentActivity.class, REQUEST_CODE_SELECT_DEPARTMENT, null);
                                }
                            });
                            mSlfzrValue.setText(dataResult.getShFzr());
                            mSlfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setShFzr("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mSlRValue.setText(dataResult.getThR());
                            mSlRValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setThR("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mTkfzrValue.setText(dataResult.getThFzr());
                            mTkfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setThFzr("" + s);
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
        return R.layout.activity_wl_tk_modify;
    }

    @OnClick({R.id.confirmBtn,R.id.selectSLFZR,R.id.selectSLR,R.id.selectTKFZR})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.selectSLR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_SLR, null);
                break;
            case R.id.selectSLFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_SLFZR, null);
                break;
            case R.id.selectTKFZR:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, REQUEST_CODE_SELECT_TLFZR, null);
                break;
            case R.id.confirmBtn:
                toCommit();
                break;
        }
    }

    private void toCommit() {

        for (int i = 0; i < mData.size(); i++) {

            if (mData.get(i).getShl() == -1
                    || "请选择部门".equals(mLldwValue.getText().toString())
                    || "请选择退库负责人".equals(mTkfzrValue.getText().toString())
                    || "请选择收料负责人".equals(mSlfzrValue.getText().toString())
                    || "请选择收料人".equals(mSlRValue.getText().toString())
                    ) {
                Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        updatedParam.setBeans(mData);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.toUpdateWLTkData(updatedParam);
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
                convertView = View.inflate(WlTkModifyActivity.this, R.layout.item_wlout_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final WLTkShowBean bean = mData.get(position);
            if (!TextUtils.isEmpty(bean.getwLCode())) {
                viewHolder.mWlbmValue.setText(bean.getwLCode());
            }
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mLbValue.setText(bean.getSortID() + "");
            viewHolder.mGgValue.setText(bean.getgG());
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mSldwValue.setText(bean.getdW());
            viewHolder.mSlValue.setText(bean.getShl() + "");
            viewHolder.mSlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat("" + s);
                        bean.setShl(num);
                    } else {
                        bean.setShl(-1);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mDwzlValue.setText(bean.getdWZL() + "");

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.wlbmValue)
            TextView mWlbmValue;
            @BindView(R.id.nameValue)
            TextView mNameValue;
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.ggValue)
            TextView mGgValue;
            @BindView(R.id.ylpcValue)
            TextView mYlpcValue;
            @BindView(R.id.sldwValue)
            TextView mSldwValue;
            @BindView(R.id.slValue)
            EditText mSlValue;
            @BindView(R.id.dwzlValue)
            TextView mDwzlValue;

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
                    updatedParam.setThDw(data.getStringExtra("groupName"));
                    break;
                case REQUEST_CODE_SELECT_SLR:
                    mSlRValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_SLFZR:
                    mSlfzrValue.setText(data.getStringExtra("personName"));
                    break;
                case REQUEST_CODE_SELECT_TLFZR:
                    mTkfzrValue.setText(data.getStringExtra("personName"));
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
