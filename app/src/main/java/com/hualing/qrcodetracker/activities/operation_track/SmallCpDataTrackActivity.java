package com.hualing.qrcodetracker.activities.operation_track;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.ComponentBean;
import com.hualing.qrcodetracker.bean.SmallCpTrackResult;
import com.hualing.qrcodetracker.bean.WlTrackParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.TrackType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.widget.MyListView;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.hualing.qrcodetracker.model.TrackType.END_INDEX;
import static com.hualing.qrcodetracker.model.TrackType.START_INDEX;

public class SmallCpDataTrackActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.nameValue)
    TextView mNameValue;
    @BindView(R.id.lbValue)
    TextView mLbValue;
    @BindView(R.id.wlbmValue)
    TextView mWlbmValue;
    @BindView(R.id.ylpcValue)
    TextView mYlpcValue;
    @BindView(R.id.scpcValue)
    TextView mScpcValue;
    @BindView(R.id.scTimeValue)
    TextView mScTimeValue;
    @BindView(R.id.ggValue)
    TextView mGgValue;
    @BindView(R.id.czyValue)
    TextView mCzyValue;
    @BindView(R.id.zjyValue)
    TextView mZjyValue;
    @BindView(R.id.jyztValue)
    TextView mJyztValue;
    @BindView(R.id.sldwValue)
    TextView mSldwValue;
    @BindView(R.id.zhlValue)
    TextView mZhlValue;
    @BindView(R.id.componentList)
    MyListView mComponentList;
    @BindView(R.id.nonTip)
    TextView mNonTip;

    private MainDao mainDao;
    private WlTrackParam param;
    private String mQrcodeId;

    private MyAdapter mAdapter;
    private List<ComponentBean> mData;

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
                AllActivitiesHolder.removeAct(SmallCpDataTrackActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        param = new WlTrackParam();
        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
            param.setQrCodeId(mQrcodeId);
        }

        mData = new ArrayList<>();
        mAdapter = new MyAdapter();
        mComponentList.setAdapter(mAdapter);
    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<SmallCpTrackResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<SmallCpTrackResult>> e) throws Exception {
                ActionResult<SmallCpTrackResult> nr = mainDao.getSmallCpTrackShowData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<SmallCpTrackResult>>() {
                    @Override
                    public void accept(ActionResult<SmallCpTrackResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(SmallCpDataTrackActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(SmallCpDataTrackActivity.this);
                            return;
                        } else {
                            SmallCpTrackResult dataResult = result.getResult();
                            mNameValue.setText(dataResult.getCpName());
                            mJyztValue.setText(dataResult.getJyzt());
                            mLbValue.setText(dataResult.getSortName());
                            mGgValue.setText(dataResult.getGg());
                            mWlbmValue.setText(dataResult.getCpCode());
                            mYlpcValue.setText(dataResult.getYlpc());
                            mScpcValue.setText(dataResult.getScpc());
                            mScTimeValue.setText(dataResult.getScTime());
                            mCzyValue.setText(dataResult.getCzy());
                            mSldwValue.setText(dataResult.getDw());
                            mZhlValue.setText(dataResult.getDwzl() + "");
                            mZjyValue.setText(dataResult.getZjy());

                            if (dataResult.getComponentBeans()==null||dataResult.getComponentBeans().size()<=0) {
                                mNonTip.setVisibility(View.VISIBLE);
                                mComponentList.setVisibility(View.GONE);
                            }else {
                                mNonTip.setVisibility(View.GONE);
                                mComponentList.setVisibility(View.VISIBLE);
                                mData.clear();
                                mData.addAll(dataResult.getComponentBeans());
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
        return R.layout.activity_small_cp_data_track;
    }

    private class MyAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(SmallCpDataTrackActivity.this, R.layout.component_single_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.name = convertView.findViewById(R.id.name);
                viewHolder.sort = convertView.findViewById(R.id.sort);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            ComponentBean bean = mData.get(position);
            viewHolder.name.setText(bean.getName());
            String sort = bean.getQrCodeId().substring(START_INDEX, END_INDEX);
            String sortName = "";
            Intent intent = null;
            switch (sort) {
                case TrackType.WL:
                    sortName = "物料";
                    intent = new Intent(SmallCpDataTrackActivity.this, WlDataTrackActivity.class);
                    break;
                case TrackType.BCP:
                    sortName = "半成品";
                    intent = new Intent(SmallCpDataTrackActivity.this, BcpDataTrackActivity.class);
                    break;
                //下面的两种基本不会出现，因为组成成分中不可能会有成品
                case TrackType.SMALL_CP:
                    sortName = "小包装";
                    intent = new Intent(SmallCpDataTrackActivity.this, SmallCpDataTrackActivity.class);
                    break;
                case TrackType.BIG_CP:
                    sortName = "大包装";
                    intent = new Intent(SmallCpDataTrackActivity.this, BigCpDataTrackActivity.class);
                    break;
            }
            viewHolder.sort.setText(sortName);

            final Intent finalIntent = intent;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalIntent.putExtra("qrCodeId", mQrcodeId);
                    startActivity(finalIntent);
                }
            });

            return convertView;
        }

        class ViewHolder {
            private TextView name;
            private TextView sort;
        }

    }

}
