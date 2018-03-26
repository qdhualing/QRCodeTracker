package com.hualing.qrcodetracker.activities.operation_track;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.WlTrackParam;
import com.hualing.qrcodetracker.bean.WlTrackResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.widget.TitleBar;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WlDataTrackActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.nameValue)
    TextView mNameValue;
    @BindView(R.id.cdValue)
    TextView mCdValue;
    @BindView(R.id.lbValue)
    TextView mLbValue;
    @BindView(R.id.wlbmValue)
    TextView mWlbmValue;
    @BindView(R.id.ylpcValue)
    TextView mYlpcValue;
    @BindView(R.id.pczlValue)
    TextView mPczlValue;
    @BindView(R.id.llTimeValue)
    TextView mLlTimeValue;
    @BindView(R.id.ggValue)
    TextView mGgValue;
    @BindView(R.id.czyValue)
    TextView mCzyValue;
    @BindView(R.id.sldwValue)
    TextView mSldwValue;
    @BindView(R.id.zhlValue)
    TextView mZhlValue;

    private MainDao mainDao;
    private WlTrackParam param ;
    private String mQrcodeId;

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
                AllActivitiesHolder.removeAct(WlDataTrackActivity.this);
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

        Observable.create(new ObservableOnSubscribe<ActionResult<WlTrackResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WlTrackResult>> e) throws Exception {
                ActionResult<WlTrackResult> nr = mainDao.getWlTrackShowData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WlTrackResult>>() {
                    @Override
                    public void accept(ActionResult<WlTrackResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(WlDataTrackActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(WlDataTrackActivity.this);
                            return;
                        } else {
                            WlTrackResult dataResult = result.getResult();
                            mNameValue.setText(dataResult.getProductName());
                            mCdValue.setText(dataResult.getChd());
                            mLbValue.setText(dataResult.getSortName());
                            mGgValue.setText(dataResult.getGg());
                            mWlbmValue.setText(dataResult.getWlCode());
                            mYlpcValue.setText(dataResult.getYlpc());
                            mPczlValue.setText(dataResult.getPczl()+"");
                            mLlTimeValue.setText(dataResult.getLlTime());
                            mCzyValue.setText(dataResult.getCzy());
                            mSldwValue.setText(dataResult.getDw());
                            mZhlValue.setText(dataResult.getDwzl() + "");
                        }
                    }
                });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wl_data_track;
    }
}
