package com.hualing.qrcodetracker.activities.abandon;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.activities.BaseActivity;
import com.hualing.qrcodetracker.model.UserType;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.util.SharedPreferenceUtil;
import com.hualing.qrcodetracker.widget.MyRecycleViewDivider;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author 马鹏昊
 * @date {2017-12-20}
 * @des 录入信息页和客户查看信息页，根据返回的json格式来决定显示TextView还是EditText
 * @updateAuthor
 * @updateDate
 * @updateDes
 */
public class InfoShowActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.confirmBtn)
    CardView mConfirmBtn;

    private List mData;

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
                AllActivitiesHolder.removeAct(InfoShowActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        mData = new ArrayList();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.divide_gray_color)));
        mRecyclerView.setAdapter(new MyRecyclerAdapter());

        if (SharedPreferenceUtil.getUserType() == UserType.EMPLOYEE) {
            mConfirmBtn.setVisibility(View.VISIBLE);
        }else{
            mConfirmBtn.setVisibility(View.GONE);
        }

    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_info_show;
    }

    @OnClick(R.id.confirmBtn)
    public void onViewClicked() {
        commitInfo();
    }

    /**
     * 提交录入数据到服务器
     */
    private void commitInfo() {

    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(InfoShowActivity.this).inflate(R.layout.item_info_show, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Map map = (Map) mData.get(position);
            String name = (String) map.get("attributeName");
            holder.mAttribute.setText(name);
            String value = (String) map.get("value");
            if (TextUtils.isEmpty(value)) {
                holder.mInputValue.setVisibility(View.VISIBLE);
                holder.mValue.setVisibility(View.GONE);
            } else {
                holder.mInputValue.setVisibility(View.GONE);
                holder.mValue.setVisibility(View.VISIBLE);
                holder.mValue.setText(value);
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mAttribute;
            TextView mValue;
            EditText mInputValue;

            public MyViewHolder(View itemView) {
                super(itemView);
                mAttribute = itemView.findViewById(R.id.attribute);
                mValue = itemView.findViewById(R.id.value);
                mInputValue = itemView.findViewById(R.id.inputValue);
            }
        }

    }
}
