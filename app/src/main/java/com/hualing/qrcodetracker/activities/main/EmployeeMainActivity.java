package com.hualing.qrcodetracker.activities.main;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_in.BCPInRKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_bcp.bcp_return.BcpTKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_in.CPRKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_cp.cp_out.CPCKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_in.WLInRKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_out.WLCKDInputActivity;
import com.hualing.qrcodetracker.activities.operation_wl.wl_return.WLTKDInputActivity;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.aframework.yoni.YoniClient;
import com.hualing.qrcodetracker.bean.MainParams;
import com.hualing.qrcodetracker.bean.MainResult;
import com.hualing.qrcodetracker.bean.Module2;
import com.hualing.qrcodetracker.bean.NonCheckResult;
import com.hualing.qrcodetracker.dao.MainDao;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.global.TheApplication;
import com.hualing.qrcodetracker.model.FunctionType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.DoubleClickExitUtil;
import com.hualing.qrcodetracker.util.IntentUtil;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.UnreadTipView;

import java.util.ArrayList;
import java.util.List;

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
    //    @BindView(R.id.functionList)
    //    RecyclerView mRecyclerView;
    @BindView(R.id.nickName)
    TextView mNickName;
    @BindView(R.id.realName)
    TextView mRealName;
    @BindView(R.id.portrait)
    SimpleDraweeView mPortrait;
    @BindView(R.id.department)
    TextView mDepartment;
    public static UnreadTipView mUnreadState;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.dot1)
    ImageView mDot1;
    @BindView(R.id.dot2)
    ImageView mDot2;
    @BindView(R.id.dot3)
    ImageView mDot3;
    private ActionBarDrawerToggle mDrawerToggle;

    //    private List<Map> mFunctionsData;
    private MainDao mainDao;
    //    private MyRecyclerAdapter mAdapter;

    public static Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mUnreadState != null) {
                mUnreadState.setUnreadState(SharedPreferenceUtil.checkIfHasUnreadMsg());
            }
            super.handleMessage(msg);
        }
    };

    private MyPagerAdapter mPagerAdapter;
    private List<Module2> mCanUseList;

    //    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
    //        @Override
    //        public boolean onMenuItemClick(MenuItem menuItem) {
    //            String msg = "";
    ////            switch (menuItem.getItemId()) {
    ////                case R.id.action_edit:
    ////                    msg += "Click edit";
    ////                    break;
    ////                case R.id.action_share:
    ////                    msg += "Click share";
    ////                    break;
    ////                case R.id.action_settings:
    ////                    msg += "Click setting";
    ////                    break;
    ////            }
    //
    //            IntentUtil.openActivity(EmployeeMainActivity.this,NonHandleMsgActivity.class);
    //
    //            return true;
    //        }
    //    };

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //
    //        getMenuInflater().inflate(R.menu.menu_toolbar, menu);//加载menu文件到toolbar
    //
    //        return true;
    //    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

        mUnreadState = findViewById(R.id.unreadState);

        getScreenSize();


        mToolBar.setTitle(getResources().getString(R.string.app_name));//设置Toolbar标题
        //        mToolBar.setTitle("二维码追溯-员工模式");//设置Toolbar标题
        mToolBar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(mToolBar);
        //        mToolBar.setOnMenuItemClickListener(onMenuItemClick);
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
        //        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        //        mRecyclerView.addItemDecoration(new MyRecyclerGridLayoutDivider(10));

        //        mFunctionsData = new ArrayList<>();

        //        initFunctionData();

        //        mAdapter = new MyRecyclerAdapter();
        //        mRecyclerView.setAdapter(mAdapter);

        mCanUseList = new ArrayList<>();

        mNickName.setText(GlobalData.userName);
        mRealName.setText(GlobalData.realName);

        mPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //首先全部置为未选中
                mDot1.setSelected(false);
                mDot2.setSelected(false);
                mDot3.setSelected(false);
                //其次单独设置选中的
                switch (position) {
                    case 0:
                        mDot1.setSelected(true);
                        break;
                    case 1:
                        mDot2.setSelected(true);
                        break;
                    case 2:
                        mDot3.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mDot1.setSelected(true);

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

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        TheApplication.setScreenHeight(display.getHeight());
        TheApplication.setScreenWidth(display.getWidth());
    }

    //    private void initFunctionData() {
    //
    //        //初始把所有的功能项填好，默认都是不可用的，然后根据调接口获取的权限来决定开启哪些权限
    //        String[] names = getResources().getStringArray(R.array.functionItems);
    //        Map map;
    //        for (String name : names) {
    //            map = new HashMap();
    //            map.put("name", name);
    //            map.put("flag", AuthorityFlag.ENABLE_FALSE_USE);
    //            int type = 0;
    //            switch (name) {
    //                case "物料入库":
    //                    type = FunctionType.MATERIAL_IN;
    //                    break;
    //                case "物料出库":
    //                    type = FunctionType.MATERIAL_OUT;
    //                    break;
    //                case "物料投料":
    //                    type = FunctionType.MATERIAL_THROW;
    //                    break;
    //                case "物料退库":
    //                    type = FunctionType.MATERIAL_RETURN;
    //                    break;
    //                case "半成品入库":
    //                    type = FunctionType.HALF_PRODUCT_IN;
    //                    break;
    //                case "半成品投料":
    //                    type = FunctionType.HALF_PRODUCT_THROW;
    //                    break;
    //                case "半成品退库":
    //                    type = FunctionType.HALF_PRODUCT_RETURN;
    //                    break;
    //                case "成品入库":
    //                    type = FunctionType.PRODUCT_IN;
    //                    break;
    //                case "成品出库":
    //                    type = FunctionType.PRODUCT_OUT;
    //                    break;
    //                case "追溯":
    //                    type = FunctionType.DATA_TRACK;
    //                    break;
    //                case "审核":
    //                    type = FunctionType.VERIFY;
    //                    break;
    //                case "质检":
    //                    type = FunctionType.QUALITY_CHECKING;
    //                    break;
    //            }
    //            map.put("type", type);
    ////            mFunctionsData.add(map);
    //        }
    //    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean ifGo = false ;
        for (Module2 module2 : mCanUseList) {
            if ("审核".equals(module2.getMname())) {
                ifGo = true ;
            }
        }
        if (!ifGo) {
            return;
        }

        mainDao = YoniClient.getInstance().create(MainDao.class);

        final Dialog progressDialog = TheApplication.createLoadingDialog(this, "");
        progressDialog.show();

        final MainParams params = new MainParams();
        params.setUserId(GlobalData.userId);
        params.setRealName(GlobalData.realName);

        Observable.create(new ObservableOnSubscribe<ActionResult<NonCheckResult>>() {
            @Override
            public void subscribe(ObservableEmitter<ActionResult<NonCheckResult>> e) throws Exception {
                ActionResult<NonCheckResult> nr = mainDao.getNonCheckData(params);
                e.onNext(nr);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<ActionResult<NonCheckResult>>() {
                    @Override
                    public void accept(ActionResult<NonCheckResult> result) throws Exception {
                        progressDialog.dismiss();
                        if (result.getCode() != 0) {
                            Toast.makeText(TheApplication.getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            NonCheckResult mainResult = result.getResult();
                            if (mainResult.getBeans() != null && mainResult.getBeans().size() > 0) {
                                SharedPreferenceUtil.saveIfHasUnreadMsg(true);
                                mUnreadState.setUnreadState(SharedPreferenceUtil.checkIfHasUnreadMsg());
                            } else {
                                SharedPreferenceUtil.saveIfHasUnreadMsg(false);
                                mUnreadState.setUnreadState(SharedPreferenceUtil.checkIfHasUnreadMsg());
                            }
                        }
                    }
                });
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

                            mCanUseList.clear();
                            mCanUseList.addAll(mainResult.getCanUseFunctionList());

                            //                            for (Map item : mFunctionsData) {
                            //                                for (Module2 module2 : ll) {
                            //                                    if (item.get("name").equals(module2.getMname())) {
                            //                                        item.put("flag", AuthorityFlag.ENABLE_TRUE_USE);
                            //                                        break;
                            //                                    }
                            //                                }
                            //                            }
                            //                            mAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.exitBtn, R.id.readNewMsg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exitBtn:
                GlobalData.userId = "";
                //之后获取和用户相关的服务就不需要额外传userId了
                YoniClient.getInstance().setUser(GlobalData.userId);
                //清除本地密码
                SharedPreferenceUtil.logout();
                AllActivitiesHolder.finishAllAct();
                //        IntentUtil.openActivity(this, UserTypePickActivity.class);
                IntentUtil.openActivity(this, EmployeeLoginActivity.class);
                break;
            case R.id.readNewMsg:
                //点击未读消息按钮
                //                SharedPreferenceUtil.saveIfHasUnreadMsg(false);
                //                mUnreadState.setUnreadState(SharedPreferenceUtil.checkIfHasUnreadMsg());
                for (Module2 module2 : mCanUseList) {
                    if ("审核".equals(module2.getMname())) {
                        GlobalData.currentFunctionType = FunctionType.VERIFY;
                        toWhere(GlobalData.currentFunctionType);
                        return;
                    }
                }
                Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        DoubleClickExitUtil.tryExit();
    }

    //    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    //
    //        @Override
    //        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //            View view = LayoutInflater.from(EmployeeMainActivity.this).inflate(R.layout.item_employee_main, parent, false);
    //            return new MyViewHolder(view);
    //        }
    //
    //        @Override
    //        public void onBindViewHolder(MyViewHolder holder, int position) {
    //            Map map = mFunctionsData.get(position);
    //            String name = (String) map.get("name");
    //            holder.mFunctionName.setText(name);
    //            final int type = (int) map.get("type");
    //
    //            int flag = (int) map.get("flag");
    //
    //
    //            if (flag == AuthorityFlag.ENABLE_TRUE_USE) {
    //
    //                holder.mUnUseTip.setVisibility(View.INVISIBLE);
    //                holder.mClickArea.setEnabled(true);
    //                holder.mLogo.setEnabled(true);
    //                holder.mFunctionName.setEnabled(true);
    //
    //                holder.mClickArea.setOnClickListener(new View.OnClickListener() {
    //                    @Override
    //                    public void onClick(View v) {
    //                        GlobalData.currentFunctionType = type;
    //                        toWhere(type);
    //                    }
    //                });
    //            } else {
    //                holder.mUnUseTip.setVisibility(View.VISIBLE);
    //                holder.mClickArea.setEnabled(false);
    //                holder.mLogo.setEnabled(false);
    //                holder.mFunctionName.setEnabled(false);
    //            }
    //        }
    //
    //        @Override
    //        public int getItemCount() {
    //            return mFunctionsData.size();
    //        }
    //
    //        public class MyViewHolder extends RecyclerView.ViewHolder {
    //            ImageView mLogo;
    //            TextView mFunctionName;
    //            CardView mClickArea;
    //            TextView mUnUseTip;
    //
    //            public MyViewHolder(View itemView) {
    //                super(itemView);
    //                mLogo = itemView.findViewById(R.id.logo);
    //                mFunctionName = itemView.findViewById(R.id.functionName);
    //                mClickArea = itemView.findViewById(R.id.clickArea);
    //                mUnUseTip = itemView.findViewById(R.id.unUseTip);
    //            }
    //        }
    //
    //    }

    /**
     * 打开不同的业务页
     *
     * @param type
     */
    private void toWhere(int type) {

        switch (type) {
            case FunctionType.MATERIAL_IN:
                //                IntentUtil.openActivity(EmployeeMainActivity.this, MaterialInMainActivity.class);
                IntentUtil.openActivity(EmployeeMainActivity.this, WLInRKDInputActivity.class);
                break;
            case FunctionType.MATERIAL_OUT:
                //                IntentUtil.openActivity(EmployeeMainActivity.this, MaterialOutMainActivity.class);
                IntentUtil.openActivity(EmployeeMainActivity.this, WLCKDInputActivity.class);
                break;
            case FunctionType.MATERIAL_RETURN:
                //                IntentUtil.openActivity(EmployeeMainActivity.this, MaterialReturnMainActivity.class);
                IntentUtil.openActivity(EmployeeMainActivity.this, WLTKDInputActivity.class);
                break;
            //投料不需要生成单据,所以直接打开扫描二维码页
            case FunctionType.MATERIAL_THROW:
                IntentUtil.openActivity(EmployeeMainActivity.this, ScanActivity.class);
                break;
            case FunctionType.HALF_PRODUCT_IN:
                //                IntentUtil.openActivity(EmployeeMainActivity.this, BcpInMainActivity.class);
                IntentUtil.openActivity(EmployeeMainActivity.this, BCPInRKDInputActivity.class);
                break;
            case FunctionType.HALF_PRODUCT_THROW:
                IntentUtil.openActivity(EmployeeMainActivity.this, ScanActivity.class);
                break;
            case FunctionType.HALF_PRODUCT_RETURN:
                //                IntentUtil.openActivity(EmployeeMainActivity.this, BcpReturnMainActivity.class);
                IntentUtil.openActivity(EmployeeMainActivity.this, BcpTKDInputActivity.class);
                break;
            case FunctionType.PRODUCT_IN:
                //                IntentUtil.openActivity(EmployeeMainActivity.this, CPInMainActivity.class);
                IntentUtil.openActivity(EmployeeMainActivity.this, CPRKDInputActivity.class);
                break;
            case FunctionType.PRODUCT_OUT:
                //                IntentUtil.openActivity(EmployeeMainActivity.this, CPOutMainActivity.class);
                IntentUtil.openActivity(EmployeeMainActivity.this, CPCKDInputActivity.class);
                break;
            case FunctionType.DATA_TRACK:
                IntentUtil.openActivity(EmployeeMainActivity.this, ScanActivity.class);
                break;
            case FunctionType.VERIFY:
                IntentUtil.openActivity(EmployeeMainActivity.this, NonHandleMsgActivity.class);
                break;
            case FunctionType.QUALITY_CHECKING:
                IntentUtil.openActivity(EmployeeMainActivity.this, ScanActivity.class);
                break;
            case FunctionType.MODIFY_DATA:
                IntentUtil.openActivity(EmployeeMainActivity.this, ModifyDataActivity.class);
                break;
        }

    }

    private class MyPagerAdapter extends PagerAdapter {

        //要切换的View
        private View view1;
        private View view2;
        private View view3;
        //view集合
        private List<View> views;


        public MyPagerAdapter() {
            int size = (int) (TheApplication.getScreenWidth()/3.3);
            views = new ArrayList<>();
            view1 = View.inflate(EmployeeMainActivity.this, R.layout.banner_layout_one_pager, null);
            CardView wlIn = view1.findViewById(R.id.wlIn);
            wlIn.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            wlIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("物料入库".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.MATERIAL_IN;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView wlOut = view1.findViewById(R.id.wlOut);
            wlOut.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            wlOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("物料出库".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.MATERIAL_OUT;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView wlThrow = view1.findViewById(R.id.wlThrow);
            wlThrow.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            wlThrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("物料投料".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.MATERIAL_THROW;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView wlReturn = view1.findViewById(R.id.wlReturn);
            wlReturn.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            wlReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("物料退库".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.MATERIAL_RETURN;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView bcpIn = view1.findViewById(R.id.bcpIn);
            bcpIn.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            bcpIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("半成品入库".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.HALF_PRODUCT_IN;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView bcpThrow = view1.findViewById(R.id.bcpThrow);
            bcpThrow.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            bcpThrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("半成品投料".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.HALF_PRODUCT_THROW;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            views.add(view1);


            view2 = View.inflate(EmployeeMainActivity.this, R.layout.banner_layout_two_pager, null);
            CardView bcpReturn = view2.findViewById(R.id.bcpReturn);
            bcpReturn.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            bcpReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("半成品退库".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.HALF_PRODUCT_RETURN;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView cpIn = view2.findViewById(R.id.cpIn);
            cpIn.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            cpIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("成品入库".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.PRODUCT_IN;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView cpOut = view2.findViewById(R.id.cpOut);
            cpOut.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            cpOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("成品出库".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.PRODUCT_OUT;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView track = view2.findViewById(R.id.track);
            track.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("追溯".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.DATA_TRACK;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView verify = view2.findViewById(R.id.verify);
            verify.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("审核".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.VERIFY;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            CardView check = view2.findViewById(R.id.check);
            check.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Module2 module2 : mCanUseList) {
                        if ("质检".equals(module2.getMname())) {
                            GlobalData.currentFunctionType = FunctionType.QUALITY_CHECKING;
                            toWhere(GlobalData.currentFunctionType);
                            return;
                        }
                    }
                    Toast.makeText(EmployeeMainActivity.this, "当前用户无此权限", Toast.LENGTH_SHORT).show();
                }
            });
            views.add(view2);

            view3 = View.inflate(EmployeeMainActivity.this, R.layout.banner_layout_three_pager, null);
            CardView modifyBtn = view3.findViewById(R.id.modifyData);
            modifyBtn.setLayoutParams(new LinearLayout.LayoutParams(size,size));
            modifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalData.currentFunctionType = FunctionType.MODIFY_DATA;
                    toWhere(GlobalData.currentFunctionType);
                }
            });
            views.add(view3);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

}
