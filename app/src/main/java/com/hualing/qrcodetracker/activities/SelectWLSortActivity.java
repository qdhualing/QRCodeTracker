package com.hualing.qrcodetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hualing.qrcodetracker.R;
import com.hualing.qrcodetracker.bean.HlSortBean;
import com.hualing.qrcodetracker.util.AllActivitiesHolder;
import com.hualing.qrcodetracker.widget.MyRecycleViewDivider;
import com.hualing.qrcodetracker.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @desc 选择物料编码
 */
public class SelectWLSortActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.inputValue)
    EditText mInputValue;
    @BindView(R.id.dataList)
    RecyclerView mRecyclerView;

    private MyAdapter mAdapter;
    private List<HlSortBean> mData ;
    //模糊过滤后的数据
    private List<HlSortBean> mFilterData ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(SelectWLSortActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        mData = new ArrayList<>();
        mFilterData = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyRecycleViewDivider(
                                this, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.divide_gray_color)));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mInputValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void getDataFormWeb() {
        HlSortBean bean1 = new HlSortBean();
        bean1.setSortCode("111");
        bean1.setSortName("阿克苏家的哈斯发");
        HlSortBean bean2 = new HlSortBean();
        bean2.setSortCode("222");
        bean2.setSortName("放到相册vxretgerg");
        HlSortBean bean3 = new HlSortBean();
        bean3.setSortCode("333");
        bean3.setSortName("突然还不如淘宝");
        HlSortBean bean4 = new HlSortBean();
        bean4.setSortCode("444");
        bean4.setSortName("梵蒂冈还不如通过");
        mData.add(bean1);
        mData.add(bean2);
        mData.add(bean3);
        mData.add(bean4);
        mFilterData.addAll(mData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_wlsort;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v =  LayoutInflater.from(SelectWLSortActivity.this).inflate(R.layout.adapter_single,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final HlSortBean bean = mFilterData.get(position);
            holder.sortName.setText(bean.getSortName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ii = new Intent();
                    ii.putExtra("sortName",bean.getSortName());
                    ii.putExtra("sortCode",bean.getSortCode());
                    setResult(RESULT_OK,ii);
                    AllActivitiesHolder.removeAct(SelectWLSortActivity.this);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFilterData.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                //执行过滤操作
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        //没有过滤的内容，则使用源数据
                        mFilterData = mData;
                    } else {
                        List<HlSortBean> filteredList = new ArrayList<>();
                        for (HlSortBean bean : mData) {
                            //这里根据需求，添加匹配规则
                            if (bean.getSortName().contains(charString)) {
                                filteredList.add(bean);
                            }
                        }

                        mFilterData = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilterData;
                    return filterResults;
                }
                //把过滤后的值返回出来
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilterData = (ArrayList<HlSortBean>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView sortName;

            public MyViewHolder(View itemView) {
                super(itemView);
                sortName = itemView.findViewById(R.id.sortName);
            }
        }

    }
}
