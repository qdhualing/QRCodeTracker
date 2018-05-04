package com.hualing.qrcodetracker.activities.operation_modify;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectLBActivity;
import com.hualing.qrcodetracker.activities.operation_common.SelectPersonActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.SelectHlSortActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.bean.WLINShowBean;
import com.hualing.qrcodetracker.bean.WlInVerifyResult;
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

public class WlInModifyActivity extends BaseActivity {

    private static final int GET_WLSORT_CODE = 30;
    private static final int SELECT_LEI_BIE = 11;
    private static final int SELECT_PERSON = 111;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.indhValue)
    TextView mIndhValue;
    @BindView(R.id.jhdwValue)
    EditText mJhdwValue;
    @BindView(R.id.shrqValue)
    TextView mShrqValue;
    @BindView(R.id.shfzrValue)
    TextView mShfzrValue;
//    @BindView(R.id.jhRValue)
//    EditText mJhRValue;
//    @BindView(R.id.jhfzrValue)
//    EditText mJhfzrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<WLINShowBean> mData;
    private String mDh;
    private VerifyParam param;
    private WlInVerifyResult updatedParam;
    //记录选择物料编码或者类别的数据位置
    private int mCurrentPosition = -1;

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
                AllActivitiesHolder.removeAct(WlInModifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        updatedParam = new WlInVerifyResult();
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

        Observable.create(new ObservableOnSubscribe<ActionResult<WlInVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WlInVerifyResult>> e) throws Exception {
                ActionResult<WlInVerifyResult> nr = mainDao.getWlInVerifyData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WlInVerifyResult>>() {
                    @Override
                    public void accept(ActionResult<WlInVerifyResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            WlInVerifyResult dataResult = result.getResult();
                            updatedParam = dataResult ;
                            mIndhValue.setText(dataResult.getInDh());
                            mJhdwValue.setText(dataResult.getFhDw());
                            mJhdwValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setFhDw(""+s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mShrqValue.setText(dataResult.getShRq());
                            mShrqValue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    View view = LayoutInflater.from(WlInModifyActivity.this).inflate(R.layout.date_select, null);
                                    final DatePicker datePicker = view.findViewById(R.id.datePicker);
                                    String lastDate = mShrqValue.getText().toString();
                                    String[] sa = null;
                                    if (!"请选择收获日期".equals(lastDate)) {
                                        sa = lastDate.split("-");
                                    }
                                    if (sa != null&& !lastDate.contains(":")) {
                                        datePicker.updateDate(Integer.parseInt(sa[0]), Integer.parseInt(sa[1]) - 1, Integer.parseInt(sa[2]));
                                    }
                                    new AlertDialog.Builder(WlInModifyActivity.this).setView(view)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String dateStr = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                                                    mShrqValue.setText(dateStr);
                                                    updatedParam.setShRq(dateStr);
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            });
                            mShfzrValue.setText(dataResult.getShFzr());
                            mShfzrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setShFzr(""+s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
//                            mJhRValue.setText(dataResult.getFhR());
//                            mJhRValue.addTextChangedListener(new TextWatcher() {
//                                @Override
//                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                                }
//
//                                @Override
//                                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                    updatedParam.setFhR(""+s);
//                                }
//
//                                @Override
//                                public void afterTextChanged(Editable s) {
//
//                                }
//                            });
//                            mJhfzrValue.setText(dataResult.getJhFzr());
//                            mJhfzrValue.addTextChangedListener(new TextWatcher() {
//                                @Override
//                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                                }
//
//                                @Override
//                                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                    updatedParam.setJhFzr(""+s);
//                                }
//
//                                @Override
//                                public void afterTextChanged(Editable s) {
//
//                                }
//                            });
                            mRemarkValue.setText(dataResult.getRemark());
                            mRemarkValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setRemark(""+s);
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

    private void toCommit() {

        for (int i = 0; i < mData.size(); i++) {

            if (TextUtils.isEmpty(mData.get(i).getProductName())
                    ||TextUtils.isEmpty(mData.get(i).getcHD())
                    ||TextUtils.isEmpty(mData.get(i).getdW())
                    ||TextUtils.isEmpty(mData.get(i).getgG())
                    ||TextUtils.isEmpty(mData.get(i).getwLCode())
                    ||TextUtils.isEmpty(mData.get(i).getyLPC())
                    ||mData.get(i).getSortID()<0
                    ||mData.get(i).getdWZL()==-1
                    ||mData.get(i).getShl()==-1
                    ||"请选择收获日期".equals(mShrqValue.getText().toString())
                    ||TextUtils.isEmpty(mJhdwValue.getText().toString())
                    ||"请选择仓库负责人".equals(mShfzrValue.getText().toString())
//                    ||TextUtils.isEmpty(mJhRValue.getText().toString())
//                    ||TextUtils.isEmpty(mJhfzrValue.getText().toString())
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
                ActionResult<ActionResult> nr = mainDao.toUpdateWLInData(updatedParam);
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

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wl_in_modify;
    }

    @OnClick({R.id.confirmBtn,R.id.selectPerson})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.confirmBtn:
                toCommit();
                break;
            case R.id.selectPerson:
                IntentUtil.openActivityForResult(this, SelectPersonActivity.class, SELECT_PERSON, null);
                break;
        }

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
                convertView = View.inflate(WlInModifyActivity.this, R.layout.item_wlin_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final WLINShowBean bean = mData.get(position);
            if (!TextUtils.isEmpty(bean.getwLCode())) {
                viewHolder.mWlbmValue.setText(bean.getwLCode());
            }
            viewHolder.mNameValue.setText(bean.getProductName());
            viewHolder.mNameValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setProductName(""+s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mCdValue.setText(bean.getcHD());
            viewHolder.mCdValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setcHD(""+s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mLbValue.setText(bean.getSortID() + "");
            viewHolder.mGgValue.setText(bean.getgG());
            viewHolder.mGgValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setgG(""+s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mYlpcValue.setText(bean.getyLPC());
            viewHolder.mYlpcValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setyLPC(""+s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mSldwValue.setText(bean.getdW());
            viewHolder.mSldwValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setdW(""+s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mSlValue.setText(bean.getShl() + "");
            viewHolder.mSlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat(""+s);
                        bean.setShl(num);
                    }else {
                        bean.setShl(-1);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mZhlValue.setText(bean.getdWZL() + "");
            viewHolder.mZhlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat(""+s);
                        bean.setdWZL(num);
                    }else {
                        bean.setShl(-1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mRemarkValue.setText(TextUtils.isEmpty(bean.getRemark()) ? "无备注信息" : bean.getRemark());
            viewHolder.mRemarkValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setRemark(""+s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            viewHolder.mSelectWLBM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.openActivityForResult(WlInModifyActivity.this, SelectHlSortActivity.class, GET_WLSORT_CODE, null);
                    mCurrentPosition = position;
                }
            });

            viewHolder.mSelectLB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.openActivityForResult(WlInModifyActivity.this, SelectLBActivity.class, SELECT_LEI_BIE, null);
                    mCurrentPosition = position;
                }
            });

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.wlbmValue)
            TextView mWlbmValue;
            @BindView(R.id.selectWLBM)
            LinearLayout mSelectWLBM;
            @BindView(R.id.nameValue)
            EditText mNameValue;
            @BindView(R.id.cdValue)
            EditText mCdValue;
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.selectLB)
            LinearLayout mSelectLB;
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

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_WLSORT_CODE:
                    String sortName = data.getStringExtra("sortName");
                    String sortCode = data.getStringExtra("sortCode");
                    if (mCurrentPosition!=-1) {
                        mData.get(mCurrentPosition).setwLCode(sortCode);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case SELECT_LEI_BIE:
                    String lbName = data.getStringExtra("lbName");
                    int lbId = data.getIntExtra("lbId", -1);
                    if (mCurrentPosition!=-1) {
                        mData.get(mCurrentPosition).setSortID(lbId);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case SELECT_PERSON:
                    String personName = data.getStringExtra("personName");
                    mShfzrValue.setText(personName);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
