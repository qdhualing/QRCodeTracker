package com.hualing.qrcodetracker.activities.operation_wl.wl_in;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.main.EmployeeMainActivity;
import com.hualing.qrcodetracker.activities.main.ScanActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.CreateWLRKDParam;
import com.hualing.qrcodetracker.bean.WLRKDResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
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
 * @desc 物料入库单数据录入
 */
public class WLInRKDInputActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.FhDwValue)
    EditText mFhDwValue;
    //    @BindView(R.id.ShRqValue)
    //    EditText mShRqValue;
    @BindView(R.id.ShRqValue)
    TextView mShRqValue;
    //    @BindView(R.id.InDhValue)
    //    EditText mInDhValue;
    @BindView(R.id.ShrValue)
    EditText mShrValue;
    @BindView(R.id.ShFzrValue)
    EditText mShFzrValue;
    @BindView(R.id.JhFzrValue)
    EditText mJhFzrValue;
    private MainDao mainDao;

    private CreateWLRKDParam params;


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
                AllActivitiesHolder.removeAct(WLInRKDInputActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        params = new CreateWLRKDParam();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wlin_rkdinput;
    }

    private boolean checkDataIfCompleted() {
        String fhdwValue = mFhDwValue.getText().toString();
        String shrqValue = mShRqValue.getText().toString();
        //        String indhValue = mInDhValue.getText().toString();
        String shrValue = mShrValue.getText().toString();
        String shfzrValue = mShFzrValue.getText().toString();
        String jhfzrValue = mJhFzrValue.getText().toString();
        if (TextUtils.isEmpty(fhdwValue)
                || "请选择收获日期".equals(shrqValue)
                //                || TextUtils.isEmpty(indhValue)
                || TextUtils.isEmpty(shrValue)
                || TextUtils.isEmpty(jhfzrValue)
                || TextUtils.isEmpty(shfzrValue)) {
            return false;
        }
        params.setFhDw(fhdwValue);
        params.setShRq(shrqValue);
        //        params.setInDh(indhValue);
        params.setShr(shrValue);
        params.setShFzr(shfzrValue);
        params.setFhr(GlobalData.realName);
        params.setJhFzr(jhfzrValue);
        return true;
    }

    @OnClick({R.id.ShRqValue, R.id.commitBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ShRqValue:

                View v = LayoutInflater.from(this).inflate(R.layout.date_select, null);
                final DatePicker datePicker = v.findViewById(R.id.datePicker);
                String lastDate = mShRqValue.getText().toString();
                String[] sa = null;
                if (!"请选择收获日期".equals(lastDate)) {
                    sa = lastDate.split("-");
                }
                if (sa != null) {
                    datePicker.updateDate(Integer.parseInt(sa[0]), Integer.parseInt(sa[1]) - 1, Integer.parseInt(sa[2]));
                }
                new AlertDialog.Builder(this).setView(v)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dateStr = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                                mShRqValue.setText(dateStr);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
            case R.id.commitBtn:

                commitDataToWeb();

                break;
        }
    }

    private void commitDataToWeb() {
        if (!checkDataIfCompleted()) {
            Toast.makeText(this, "数据录入不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult<WLRKDResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<WLRKDResult>> e) throws Exception {
                ActionResult<WLRKDResult> nr = mainDao.createWL_RKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<WLRKDResult>>() {
                    @Override
                    public void accept(ActionResult<WLRKDResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() == 0) {
                            Toast.makeText(TheApplication.getContext(), "入库单创建成功~", Toast.LENGTH_SHORT).show();
                            //保存物料入库单号
                            //                            SharedPreferenceUtil.setWlRKDNumber(mInDhValue.getText().toString());
                            WLRKDResult rkdResult = result.getResult();
                            SharedPreferenceUtil.setWlRKDNumber(String.valueOf(rkdResult.getInDh()));
                            IntentUtil.openActivity(WLInRKDInputActivity.this, ScanActivity.class);
                            AllActivitiesHolder.removeAct(WLInRKDInputActivity.this);
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteRKD() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        Observable.create(new ObservableOnSubscribe<ActionResult>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult> e) throws Exception {
                ActionResult nr = mainDao.createWL_RKD(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult>() {
                    @Override
                    public void accept(ActionResult result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(TheApplication.getContext(), "入库单删除成功~", Toast.LENGTH_SHORT).show();
                            //保存物料入库单号
                            SharedPreferenceUtil.setWlRKDNumber(null);
                            IntentUtil.openActivity(WLInRKDInputActivity.this, EmployeeMainActivity.class);
                            AllActivitiesHolder.removeAct(WLInRKDInputActivity.this);
                        }
                    }
                });
    }
}
