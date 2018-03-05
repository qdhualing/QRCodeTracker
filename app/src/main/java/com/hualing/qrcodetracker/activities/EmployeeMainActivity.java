package com.hualing.qrcodetracker.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.MainParams;
import com.hualing.qrcodetracker.bean.MainResult;
import com.hualing.qrcodetracker.bean.Module2;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.AuthorityFlag;
import com.hualing.qrcodetracker.model.FunctionType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.DoubleClickExitUtil;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.MyRecyclerGridLayoutDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class EmployeeMainActivity extends BaseActivity {

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.menu)
    LinearLayout mMenu;
    @BindView(R.id.functionList)
    RecyclerView mRecyclerView;
    @BindView(R.id.nickName)
    TextView mNickName;
    @BindView(R.id.realName)
    TextView mRealName;
    @BindView(R.id.portrait)
    SimpleDraweeView mPortrait;
    @BindView(R.id.department)
    TextView mDepartment;
    private ActionBarDrawerToggle mDrawerToggle;

    private List<Map> mFunctionsData;
    private MainDao mainDao;
    private MyRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mToolBar.setTitle("二维码追溯员工端");//设置Toolbar标题
        //        mToolBar.setTitle("二维码追溯-员工模式");//设置Toolbar标题
        mToolBar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
        //                this, LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.divide_gray_color)));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyRecyclerGridLayoutDivider(10));

        mFunctionsData = new ArrayList<>();

        initFunctionData();

        mAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mNickName.setText(GlobalData.userName);
        mRealName.setText(GlobalData.realName);

        //修改成主界面跟随侧滑栏移动
        //        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
        //            @Override
        //            public void onDrawerSlide(View drawerView, float slideOffset) {
        //                View mContent = mDrawerLayout.getChildAt(0);
        //                ViewHelper.setTranslationX(mContent,
        //                        mMenu.getMeasuredWidth() * slideOffset);
        //            }
        //
        //            @Override
        //            public void onDrawerOpened(View drawerView) {
        //
        //            }
        //
        //            @Override
        //            public void onDrawerClosed(View drawerView) {
        //
        //            }
        //
        //            @Override
        //            public void onDrawerStateChanged(int newState) {
        //
        //            }
        //        });

    }

    private void initFunctionData() {

        //初始把所有的功能项填好，默认都是不可用的，然后根据调接口获取的权限来决定开启哪些权限
        String[] names = getResources().getStringArray(R.array.functionItems);
        Map map1 = new HashMap();
        map1.put("name", names[0]);
        map1.put("type", FunctionType.MATERIAL_IN);
        map1.put("flag", AuthorityFlag.ENABLE_FALSE_USE);
        Map map2 = new HashMap();
        map2.put("name", names[1]);
        map2.put("type", FunctionType.MATERIAL_OUT);
        map2.put("flag", AuthorityFlag.ENABLE_FALSE_USE);
        Map map3 = new HashMap();
        map3.put("name", names[2]);
        map3.put("type", FunctionType.HALF_PRODUCT_IN);
        map3.put("flag", AuthorityFlag.ENABLE_FALSE_USE);
        Map map4 = new HashMap();
        map4.put("name", names[3]);
        map4.put("type", FunctionType.PRODUCT_IN);
        map4.put("flag", AuthorityFlag.ENABLE_FALSE_USE);
        Map map5 = new HashMap();
        map5.put("name", names[4]);
        map5.put("type", FunctionType.PRODUCT_OUT);
        map5.put("flag", AuthorityFlag.ENABLE_FALSE_USE);
        Map map6 = new HashMap();
        map6.put("name", names[5]);
        map6.put("type", FunctionType.MATERIAL_THROW);
        map6.put("flag", AuthorityFlag.ENABLE_FALSE_USE);
        Map map7 = new HashMap();
        map7.put("name", names[6]);
        map7.put("type", FunctionType.MATERIAL_RETURN);
        map7.put("flag", AuthorityFlag.ENABLE_FALSE_USE);
        mFunctionsData.add(map1);
        mFunctionsData.add(map2);
        mFunctionsData.add(map3);
        mFunctionsData.add(map4);
        mFunctionsData.add(map5);
        mFunctionsData.add(map6);
        mFunctionsData.add(map7);
    }

    @Override
    protected void getDataFormWeb() {

        mainDao = YoniClient.getInstance().create(MainDao.class);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        final MainParams params = new MainParams();
        params.setUserId(GlobalData.userId);

        Observable.create(new ObservableOnSubscribe<ActionResult<MainResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<MainResult>> e) throws Exception {
                ActionResult<MainResult> nr = mainDao.getMainData(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<MainResult>>() {
                    @Override
                    public void accept(ActionResult<MainResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            MainResult mainResult = result.getResult();
                            mDepartment.setText(mainResult.getDepartmentName());

                            List<Module2> ll = mainResult.getCanUseFunctionList();
                            for (Map item : mFunctionsData) {
                                for (Module2 module2 : ll) {
                                    if (item.get("name").equals(module2.getMname())) {
                                        item.put("flag",AuthorityFlag.ENABLE_TRUE_USE);
                                        break;
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    protected void debugShow() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_employee_main;
    }

    @OnClick(R.id.exitBtn)
    public void onViewClicked() {
        GlobalData.userId = "";
        //之后获取和用户相关的服务就不需要额外传userId了
        YoniClient.getInstance().setUser(GlobalData.userId);
        //清除本地密码
        SharedPreferenceUtil.logout();
        AllActivitiesHolder.finishAllAct();
        //        IntentUtil.openActivity(this, UserTypePickActivity.class);
        IntentUtil.openActivity(this, EmployeeLoginActivity.class);
    }

    @Override
    public void onBackPressed() {
        DoubleClickExitUtil.tryExit();
    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(EmployeeMainActivity.this).inflate(R.layout.item_employee_main, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Map map = mFunctionsData.get(position);
            String name = (String) map.get("name");
            holder.mFunctionName.setText(name);
            final int type = (int) map.get("type");

            int flag = (int) map.get("flag");


            if (flag == AuthorityFlag.ENABLE_TRUE_USE) {

                holder.mUnUseTip.setVisibility(View.INVISIBLE);
                holder.mClickArea.setEnabled(true);
                holder.mLogo.setEnabled(true);
                holder.mFunctionName.setEnabled(true);

                holder.mClickArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalData.currentFunctionType = type;
                        toWhere(type);
                    }
                });
            } else {
                holder.mUnUseTip.setVisibility(View.VISIBLE);
                holder.mClickArea.setEnabled(false);
                holder.mLogo.setEnabled(false);
                holder.mFunctionName.setEnabled(false);
            }
        }

        @Override
        public int getItemCount() {
            return mFunctionsData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView mLogo;
            TextView mFunctionName;
            CardView mClickArea;
            TextView mUnUseTip;

            public MyViewHolder(View itemView) {
                super(itemView);
                mLogo = itemView.findViewById(R.id.logo);
                mFunctionName = itemView.findViewById(R.id.functionName);
                mClickArea = itemView.findViewById(R.id.clickArea);
                mUnUseTip = itemView.findViewById(R.id.unUseTip);
            }
        }

    }

    /**
     * 打开不同的业务页
     * @param type
     */
    private void toWhere(int type) {

        switch (type){
            case FunctionType.MATERIAL_IN:
                IntentUtil.openActivity(EmployeeMainActivity.this, MaterialInMainActivity.class);
                break;
        }

    }
}
