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
import com.hualing.qrcodetracker.activities.operation_common.SelectLBActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.SelectHlSortActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BcpInShowBean;
import com.hualing.qrcodetracker.bean.BcpInVerifyResult;
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

public class BcpInModifyActivity extends BaseActivity {

    private static final int GET_WLSORT_CODE = 30;
    private static final int SELECT_LEI_BIE = 11;

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.indhValue)
    TextView mIndhValue;
    @BindView(R.id.jhdwValue)
    TextView mJhdwValue;
    @BindView(R.id.selectBM)
    LinearLayout mSelectBM;
    @BindView(R.id.ShrValue)
    EditText mShrValue;
    @BindView(R.id.ShFzrValue)
    EditText mShFzrValue;
    @BindView(R.id.JhFhrValue)
    EditText mJhFhrValue;
    @BindView(R.id.remarkValue)
    EditText mRemarkValue;
    @BindView(R.id.childDataList)
    MyListView mChildDataList;

    private MainDao mainDao;
    private MyAdapter mAdapter;
    private List<BcpInShowBean> mData;
    private String mDh;
    private VerifyParam param;
    private BcpInVerifyResult updatedParam;
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
                AllActivitiesHolder.removeAct(BcpInModifyActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        updatedParam = new BcpInVerifyResult();
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

        Observable.create(new ObservableOnSubscribe<ActionResult<BcpInVerifyResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BcpInVerifyResult>> e) throws Exception {
                ActionResult<BcpInVerifyResult> nr = mainDao.getBcpInVerifyData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BcpInVerifyResult>>() {
                    @Override
                    public void accept(ActionResult<BcpInVerifyResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            BcpInVerifyResult dataResult = result.getResult();
                            updatedParam = dataResult;
                            mIndhValue.setText(dataResult.getInDh());
                            mJhdwValue.setText(dataResult.getJhDw());
                            mJhdwValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setJhDw("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mShFzrValue.setText(dataResult.getShFzr());
                            mShFzrValue.addTextChangedListener(new TextWatcher() {
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
                            mShrValue.setText(dataResult.getJhR());
                            mShrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setJhR("" + s);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            mJhFhrValue.setText(dataResult.getJhFzr());
                            mJhFhrValue.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    updatedParam.setJhFzr("" + s);
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

    private void toCommit() {

        for (int i = 0; i < mData.size(); i++) {

            if (TextUtils.isEmpty(mData.get(i).getProductName())
                    || TextUtils.isEmpty(mData.get(i).getdW())
                    || TextUtils.isEmpty(mData.get(i).getgG())
                    || "请选择编码".equals(mData.get(i).getwLCode())
                    || TextUtils.isEmpty(mData.get(i).getyLPC())
                    || mData.get(i).getSortID() < 0
                    || mData.get(i).getdWZL() == -1
                    || mData.get(i).getShl() == -1
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
                ActionResult<ActionResult> nr = mainDao.toUpdateBcpInData(updatedParam);
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
        return R.layout.activity_bcp_in_modify;
    }

    @OnClick(R.id.confirmBtn)
    public void onViewClicked() {
        toCommit();
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
                convertView = View.inflate(BcpInModifyActivity.this, R.layout.item_bcpin_modify, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            final BcpInShowBean bean = mData.get(position);
            if (!TextUtils.isEmpty(bean.getwLCode())) {
                viewHolder.mBcpCodeValue.setText(bean.getwLCode());
            }
            viewHolder.mProductNameValue.setText(bean.getProductName());
            viewHolder.mProductNameValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setProductName("" + s);
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
                    bean.setgG("" + s);
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
                    bean.setyLPC("" + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mDwValue.setText(bean.getdW());
            viewHolder.mDwValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setdW("" + s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mShlValue.setText(bean.getShl() + "");
            viewHolder.mShlValue.addTextChangedListener(new TextWatcher() {
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
            viewHolder.mDwzlValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        float num = Float.parseFloat("" + s);
                        bean.setdWZL(num);
                    } else {
                        bean.setShl(-1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            viewHolder.mSelectBCPCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.openActivityForResult(BcpInModifyActivity.this, SelectHlSortActivity.class, GET_WLSORT_CODE, null);
                    mCurrentPosition = position;
                }
            });

            viewHolder.mSelectLB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.openActivityForResult(BcpInModifyActivity.this, SelectLBActivity.class, SELECT_LEI_BIE, null);
                    mCurrentPosition = position;
                }
            });

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.bcpCodeValue)
            TextView mBcpCodeValue;
            @BindView(R.id.selectBCPCode)
            LinearLayout mSelectBCPCode;
            @BindView(R.id.productNameValue)
            EditText mProductNameValue;
            @BindView(R.id.lbValue)
            TextView mLbValue;
            @BindView(R.id.selectLB)
            LinearLayout mSelectLB;
            @BindView(R.id.ylpcValue)
            EditText mYlpcValue;
            @BindView(R.id.ggValue)
            EditText mGgValue;
            @BindView(R.id.shlValue)
            EditText mShlValue;
            @BindView(R.id.dwzlValue)
            EditText mDwzlValue;
            @BindView(R.id.dwValue)
            EditText mDwValue;

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
                    if (mCurrentPosition != -1) {
                        mData.get(mCurrentPosition).setwLCode(sortCode);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case SELECT_LEI_BIE:
                    String lbName = data.getStringExtra("lbName");
                    int lbId = data.getIntExtra("lbId", -1);
                    if (mCurrentPosition != -1) {
                        mData.get(mCurrentPosition).setSortID(lbId);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
