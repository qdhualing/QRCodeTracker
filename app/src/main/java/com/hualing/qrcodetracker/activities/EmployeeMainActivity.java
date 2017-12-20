package com.hualing.qrcodetracker.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.model.FunctionType;
import com.hualing.qrcodetracker.model.UserType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.DoubleClickExitUtil;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.MyRecycleViewDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class EmployeeMainActivity extends BaseActivity {

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.menu)
    LinearLayout mMenu;
    @BindView(R.id.functionList)
    RecyclerView mRecyclerView;
    private ActionBarDrawerToggle mDrawerToggle;

    private List mFunctionsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mToolBar.setTitle("二维码追溯-员工模式");//设置Toolbar标题
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.divide_gray_color)));

        mFunctionsData = new ArrayList<Map>();
        String[] names = getResources().getStringArray(R.array.functionItems);
        Map map1 = new HashMap();
        map1.put("name", names[0]);
        map1.put("type", FunctionType.MATERIAL_IN);
        Map map2 = new HashMap();
        map2.put("name", names[1]);
        map2.put("type", FunctionType.MATERIAL_OUT);
        Map map3 = new HashMap();
        map3.put("name", names[2]);
        map3.put("type", FunctionType.PRODUCT_IN);
        Map map4 = new HashMap();
        map4.put("name", names[3]);
        map4.put("type", FunctionType.PRODUCT_OUT);
        mFunctionsData.add(map1);
        mFunctionsData.add(map2);
        mFunctionsData.add(map3);
        mFunctionsData.add(map4);
        mRecyclerView.setAdapter(new MyRecyclerAdapter());

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

    @Override
    protected void getDataFormWeb() {

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
        IntentUtil.openActivity(this, UserTypePickActivity.class);
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
            Map map = (Map) mFunctionsData.get(position);
            String name = (String) map.get("name");
            holder.mFunctionName.setText(name);
            final int type = (int) map.get("type");
            holder.mClickArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalData.currentFunctionType = type;
                    IntentUtil.openActivity(EmployeeMainActivity.this,ScanActivity.class);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFunctionsData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mFunctionName;
            CardView mClickArea;
            public MyViewHolder(View itemView) {
                super(itemView);
                mFunctionName = itemView.findViewById(R.id.functionName);
                mClickArea = itemView.findViewById(R.id.clickArea);
            }
        }

    }
}
