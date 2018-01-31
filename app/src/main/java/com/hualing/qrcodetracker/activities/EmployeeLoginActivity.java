package com.hualing.qrcodetracker.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.LoginParams;
import com.hualing.qrcodetracker.bean.LoginResult;
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

public class EmployeeLoginActivity extends BaseActivity {

    // UI references.
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    private MainDao mainDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(EmployeeLoginActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_employee_login;
    }

    private void toLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        View focusView = null;

        if (TextUtils.isEmpty(userName)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) /*|| !isPasswordValid(password)*/) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.setError("密码不能为空");
            focusView = mPasswordView;
            focusView.requestFocus();
            return;
        }

        //测试
//        SharedPreferenceUtil.rememberUser(userName, password);
//        IntentUtil.openActivity(EmployeeLoginActivity.this, EmployeeMainActivity.class);

        loginByWeb(userName, password);

    }

    private void loginByWeb(final String userName, final String password) {
        mainDao = YoniClient.getInstance().create(MainDao.class);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        final LoginParams loginParams = new LoginParams();
        loginParams.setUserName(userName);
//        loginParams.setPassword(Cipher.MD5(password));
        loginParams.setPassword(password);

        Observable.create(new ObservableOnSubscribe<ActionResult<LoginResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<LoginResult>> e) throws Exception {
                ActionResult<LoginResult> nr = mainDao.login(loginParams);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<LoginResult>>() {
                    @Override
                    public void accept(ActionResult<LoginResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            LoginResult loginResult = result.getResult();
                            GlobalData.userId = String.valueOf(loginResult.getUserId());
                            GlobalData.userName = loginResult.getUserName();
                            GlobalData.realName = loginResult.getTrueName();
                            //之后获取和用户相关的服务就不需要额外传userId了
                            YoniClient.getInstance().setUser(GlobalData.userId);
                            AllActivitiesHolder.removeAct(EmployeeLoginActivity.this);

                            //保存信息
//                            SharedPreferenceUtil.rememberUser(userName, Cipher.MD5(password));
                            SharedPreferenceUtil.rememberUser(userName, password);
                            IntentUtil.openActivity(EmployeeLoginActivity.this, EmployeeMainActivity.class);

                        }
                    }
                });
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    @OnClick(R.id.email_sign_in_button)
    public void onViewClicked() {
        toLogin();
    }
}

