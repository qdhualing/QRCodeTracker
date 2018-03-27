package com.hualing.qrcodetracker.activities.operation_track;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.BigCpTrackResult;
import com.hualing.qrcodetracker.bean.WlTrackParam;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BigCpDataTrackActivity extends BaseActivity {

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
    @BindView(R.id.lookSmallBtn)
    CardView mLookSmallBtn;

    private MainDao mainDao;
    private WlTrackParam param;
    private String mQrcodeId;
    //小包装二维码id（取第一个就行）
    private String mSmallQrcodeId;

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
                AllActivitiesHolder.removeAct(BigCpDataTrackActivity.this);
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

    }

    @Override
    protected void getDataFormWeb() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<BigCpTrackResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<BigCpTrackResult>> e) throws Exception {
                ActionResult<BigCpTrackResult> nr = mainDao.getBigCpTrackShowData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<BigCpTrackResult>>() {
                    @Override
                    public void accept(ActionResult<BigCpTrackResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(BigCpDataTrackActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(BigCpDataTrackActivity.this);
                            return;
                        } else {
                            BigCpTrackResult dataResult = result.getResult();
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

                            if (TextUtils.isEmpty(dataResult.getSmlPk1())) {
                                mLookSmallBtn.setEnabled(false);
                            } else {
                                mLookSmallBtn.setEnabled(true);
                                mSmallQrcodeId = dataResult.getSmlPk1();
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
        return R.layout.activity_big_cp_data_track;
    }

    @OnClick(R.id.lookSmallBtn)
    public void onViewClicked() {
        Intent intent = new Intent(this, SmallCpDataTrackActivity.class);
        intent.putExtra("qrCodeId", mSmallQrcodeId);
        startActivity(intent);
    }

}
