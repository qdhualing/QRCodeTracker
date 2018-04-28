package com.hualing.qrcodetracker.activities.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.QualityDataParam;
import com.hualing.qrcodetracker.bean.QualityDataResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.TrackType;
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

/**
 * @desc 扫码后的质检页面
 */
public class QualityCheckActivity extends BaseActivity {


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
    @BindView(R.id.wlLayout)
    ScrollView mWlLayout;
    @BindView(R.id.bcpNameValue)
    TextView mBcpNameValue;
    @BindView(R.id.bcpLbValue)
    TextView mBcpLbValue;
    @BindView(R.id.bcpBmValue)
    TextView mBcpBmValue;
    @BindView(R.id.bcpYlpcValue)
    TextView mBcpYlpcValue;
    @BindView(R.id.bcpScpcValue)
    TextView mBcpScpcValue;
    @BindView(R.id.bcpScTimeValue)
    TextView mBcpScTimeValue;
    @BindView(R.id.bcpGgValue)
    TextView mBcpGgValue;
    @BindView(R.id.bcpCjValue)
    TextView mBcpCjValue;
    @BindView(R.id.bcpGxValue)
    TextView mBcpGxValue;
    @BindView(R.id.bcpCzyValue)
    TextView mBcpCzyValue;
    @BindView(R.id.bcpSldwValue)
    TextView mBcpSldwValue;
    @BindView(R.id.bcpZhlValue)
    TextView mBcpZhlValue;
    @BindView(R.id.bcpLayout)
    ScrollView mBcpLayout;
    @BindView(R.id.smallCpNameValue)
    TextView mSmallCpNameValue;
    @BindView(R.id.smallCpLbValue)
    TextView mSmallCpLbValue;
    @BindView(R.id.smallCpBmValue)
    TextView mSmallCpBmValue;
    @BindView(R.id.smallCpYlpcValue)
    TextView mSmallCpYlpcValue;
    @BindView(R.id.smallCpScpcValue)
    TextView mSmallCpScpcValue;
    @BindView(R.id.smallCpScTimeValue)
    TextView mSmallCpScTimeValue;
    @BindView(R.id.smallCpGgValue)
    TextView mSmallCpGgValue;
    @BindView(R.id.smallCpCzyValue)
    TextView mSmallCpCzyValue;
    @BindView(R.id.smallCpSldwValue)
    TextView mSmallCpSldwValue;
    @BindView(R.id.smallCpZhlValue)
    TextView mSmallCpZhlValue;
    @BindView(R.id.smallCpLayout)
    ScrollView mSmallCpLayout;
    @BindView(R.id.bigCpNameValue)
    TextView mBigCpNameValue;
    @BindView(R.id.bigCpLbValue)
    TextView mBigCpLbValue;
    @BindView(R.id.bigCpBmValue)
    TextView mBigCpBmValue;
    @BindView(R.id.bigCpYlpcValue)
    TextView mBigCpYlpcValue;
    @BindView(R.id.bigCpScpcValue)
    TextView mBigCpScpcValue;
    @BindView(R.id.bigCpScTimeValue)
    TextView mBigCpScTimeValue;
    @BindView(R.id.bigCpGgValue)
    TextView mBigCpGgValue;
    @BindView(R.id.bigCpCzyValue)
    TextView mBigCpCzyValue;
    @BindView(R.id.bigCpSldwValue)
    TextView mBigCpSldwValue;
    @BindView(R.id.bigCpZhlValue)
    TextView mBigCpZhlValue;
    @BindView(R.id.bigCpLayout)
    ScrollView mBigCpLayout;
    private String mQrcodeId;
    private String mSort;
    private Dialog progressDialog;
    private MainDao mainDao;
    private QualityDataParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

        mainDao = YoniClient.getInstance().create(MainDao.class);
        progressDialog = TheApplication.createLoadingDialog(this, "");

        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(QualityCheckActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });


        param = new QualityDataParam();
        if (getIntent() != null) {
            mQrcodeId = getIntent().getStringExtra("qrCodeId");
            param.setQrCodeId(mQrcodeId);
            //二维码信息包含着扫描的属于物料、半成品、小包装还是大包装
            //测试
            String sort = "1";
            //            mSort = mQrcodeId.substring(START_INDEX, END_INDEX);
            mSort = sort;
            param.setSort(mSort);
            //当前用户为质检员
            param.setZjy(GlobalData.realName);
            mWlLayout.setVisibility(View.GONE);
            mBcpLayout.setVisibility(View.GONE);
            mSmallCpLayout.setVisibility(View.GONE);
            mBigCpLayout.setVisibility(View.GONE);
            switch (mSort) {
                case TrackType.WL:
                    mTitle.setTitle("物料质检");
                    mWlLayout.setVisibility(View.VISIBLE);
                    break;
                case TrackType.BCP:
                    mTitle.setTitle("半成品质检");
                    mBcpLayout.setVisibility(View.VISIBLE);
                    break;
                case TrackType.SMALL_CP:
                    mTitle.setTitle("小包装质检");
                    mSmallCpLayout.setVisibility(View.VISIBLE);
                    break;
                case TrackType.BIG_CP:
                    mTitle.setTitle("大包装质检");
                    mBigCpLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void getDataFormWeb() {
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<QualityDataResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<QualityDataResult>> e) throws Exception {
                ActionResult<QualityDataResult> nr = mainDao.getQualityData(param);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<QualityDataResult>>() {
                    @Override
                    public void accept(ActionResult<QualityDataResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            IntentUtil.openActivity(QualityCheckActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(QualityCheckActivity.this);
                            return;
                        } else {
                            QualityDataResult dataResult = result.getResult();
                            showData(dataResult);
                        }
                    }
                });
    }

    private void showData(QualityDataResult dataResult) {
        switch (mSort) {
            case TrackType.WL:
                 mNameValue.setText(dataResult.getWlProductName());
                 mLbValue.setText(dataResult.getWlSortName());
                 mCdValue.setText(dataResult.getWlChd());
                 mWlbmValue.setText(dataResult.getWlCode());
                 mYlpcValue.setText(dataResult.getWlYlpc());
                 mPczlValue.setText(dataResult.getWlPczl()+"");
                 mLlTimeValue.setText(dataResult.getWlLlTime());
                 mGgValue.setText(dataResult.getWlGg());
                 mCzyValue.setText(dataResult.getWlCzy());
                 mSldwValue.setText(dataResult.getWlDw());
                 mZhlValue.setText(dataResult.getWlDwzl()+"");
                break;
            case TrackType.BCP:
                 mBcpNameValue.setText(dataResult.getBcpProductName());
                 mBcpLbValue.setText(dataResult.getBcpSortName());
                 mBcpBmValue.setText(dataResult.getBcpCode());
                 mBcpYlpcValue.setText(dataResult.getBcpYlpc());
                 mBcpScpcValue.setText(dataResult.getBcpScpc());
                 mBcpScTimeValue.setText(dataResult.getBcpScTime());
                 mBcpGgValue.setText(dataResult.getBcpGg());
                 mBcpCjValue.setText(dataResult.getBcpCheJian());
                 mBcpGxValue.setText(dataResult.getBcpGx());
                 mBcpCzyValue.setText(dataResult.getBcpCzy());
                 mBcpSldwValue.setText(dataResult.getBcpDw());
                 mBcpZhlValue.setText(dataResult.getBcpDwzl()+"");
                break;
            case TrackType.SMALL_CP:
                 mSmallCpNameValue.setText(dataResult.getCpName());
                 mSmallCpLbValue.setText(dataResult.getCpsortName());
                 mSmallCpBmValue.setText(dataResult.getCpCode());
                 mSmallCpYlpcValue.setText(dataResult.getCpYlpc());
                 mSmallCpScpcValue.setText(dataResult.getCpScpc());
                 mSmallCpScTimeValue.setText(dataResult.getCpScTime());
                 mSmallCpGgValue.setText(dataResult.getCpGg());
                 mSmallCpCzyValue.setText(dataResult.getCpCzy());
                 mSmallCpSldwValue.setText(dataResult.getCpDw());
                 mSmallCpZhlValue.setText(dataResult.getCpDwzl()+"");
                break;
            case TrackType.BIG_CP:
                 mBigCpNameValue.setText(dataResult.getBigCpName());
                 mBigCpLbValue.setText(dataResult.getBigCpSortName());
                 mBigCpBmValue.setText(dataResult.getBigCpCode());
                 mBigCpYlpcValue.setText(dataResult.getBigCpYlpc());
                 mBigCpScpcValue.setText(dataResult.getBigCpScpc());
                 mBigCpScTimeValue.setText(dataResult.getBigCpScTime());
                 mBigCpGgValue.setText(dataResult.getBigCpGg());
                 mBigCpCzyValue.setText(dataResult.getBigCpCzy());
                 mBigCpSldwValue.setText(dataResult.getBigCpDw());
                 mBigCpZhlValue.setText(dataResult.getBigCpDwzl()+"");
                break;
        }
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_quality_check;
    }

    @OnClick(R.id.passBtn)
    public void onViewClicked() {
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<ActionResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<ActionResult>> e) throws Exception {
                ActionResult<ActionResult> nr = mainDao.passCheck(param);
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
                            Toast.makeText(TheApplication.getContext(), "已通过质检", Toast.LENGTH_SHORT).show();
                            AllActivitiesHolder.removeAct(QualityCheckActivity.this);
                            return;
                        }
                    }
                });
    }
}
