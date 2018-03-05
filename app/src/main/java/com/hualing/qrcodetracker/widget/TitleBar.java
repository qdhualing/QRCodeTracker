package com.hualing.qrcodetracker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hualing.qrcodetracker.R;


/**
 * @author 马鹏昊
 * @date {2017.7.31}
 * @des 通用标题栏，可以设置右按钮不可见
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class TitleBar extends RelativeLayout implements View.OnClickListener {

    private int mDefLeftButtonSize ;
    private int mDefRightButtonSize ;
    private float mDefTitleTextSize ;
    private int mDefTitleTextColor = Color.WHITE;
    private int mDefBackgroundColor = android.R.color.black;
    private int mDefLeftButtonImg = -1;
    private int mDefRightButtonImg = -1;
    private String mDefTextContent = "";

    private Context mContext;
    private ImageView mLeftButton;
    private ImageView mRightButton;
    private TextView mTitle;

    private AddClickEvents mEvents;

    private RelativeLayout mTouchLayout ;
    private RelativeLayout mTouchLayout2 ;


    public TitleBar(Context context) {
        this(context, null, 0);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mDefLeftButtonSize = getPxFromDp(35);
        mDefRightButtonSize = getPxFromDp(35);
        mDefTitleTextSize = 16;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        //得到的是在xml的组件中设置了的属性个数
        final int N = array.getIndexCount();
        Log.d("AAA", "" + N);
        for (int i = 0; i < N; i++) {
            initCustomAttr(array.getIndex(i), array);
        }
        array.recycle();

        initViews();

    }


    /**
     * 读取xml配置内容
     *
     * @param attr
     * @param typedArray
     */
    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.TitleBar_background_color) {
            //标题栏背景
            mDefBackgroundColor = typedArray.getResourceId(attr,android.R.color.black);
        } else if (attr == R.styleable.TitleBar_left_img) {
            //左按钮图案
            mDefLeftButtonImg = typedArray.getResourceId(attr, -1);

        } else if (attr == R.styleable.TitleBar_right_img) {
            //右按钮图案
            mDefRightButtonImg = typedArray.getResourceId(attr, -1);
        } else if (attr == R.styleable.TitleBar_title_textColor) {
            //标题文本颜色
            mDefTitleTextColor = typedArray.getColor(attr, Color.WHITE);
        } else if (attr == R.styleable.TitleBar_title_textSize) {
            //标题文字大小
            mDefTitleTextSize = typedArray.getDimensionPixelSize(attr,16);
        } else if (attr == R.styleable.TitleBar_leftButton_size) {
            //左按钮大小
            mDefLeftButtonSize = typedArray.getDimensionPixelSize(attr, getPxFromDp(20));
        } else if (attr == R.styleable.TitleBar_rightButton_size) {
            //右按钮大小
            mDefRightButtonSize = typedArray.getDimensionPixelSize(attr, getPxFromDp(20));
        } else if (attr == R.styleable.TitleBar_text_content) {
            //右按钮大小
            mDefTextContent = typedArray.getString(attr);
        }

    }


    /**
     * 添加组件并根据xml中获取的设置和默认值设置组件初始状态
     */
    private void initViews() {

        mTitle = new TextView(mContext);
        LayoutParams params0 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params0.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTitle.setLayoutParams(params0);
        //从xml中获取的textSize一定要这么设置,不能用textview的setTextSize
        mTitle.getPaint().setTextSize(mDefTitleTextSize);
        mTitle.setTextColor(mDefTitleTextColor);
        mTitle.setMaxLines(1);
        mTitle.setMaxWidth(getPxFromDp(200));
        mTitle.setEllipsize(TextUtils.TruncateAt.END);
        mTitle.setText(mDefTextContent);

        int size = getPxFromDp(48);//触摸按钮的标准大小

        mTouchLayout = new RelativeLayout(mContext);
        LayoutParams tP = new LayoutParams(size,size);
        tP.addRule(RelativeLayout.CENTER_VERTICAL);
        tP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mTouchLayout.setLayoutParams(tP);
        mLeftButton = new ImageView(mContext);
        LayoutParams params = new LayoutParams(mDefLeftButtonSize, mDefLeftButtonSize);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.bottomMargin = getPxFromDp(5);
        params.topMargin = getPxFromDp(5);
        params.leftMargin = getPxFromDp(10);
        params.rightMargin = getPxFromDp(10);
        mLeftButton.setLayoutParams(params);
        mLeftButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (mDefLeftButtonImg != -1)
            mLeftButton.setImageResource(mDefLeftButtonImg);
        mTouchLayout.addView(mLeftButton);
        mTouchLayout.setOnClickListener(this);

        mTouchLayout2 = new RelativeLayout(mContext);
        LayoutParams tP2 = new LayoutParams(size,size);
        tP2.addRule(RelativeLayout.CENTER_VERTICAL);
        tP2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tP2.rightMargin = getPxFromDp(10);
        mTouchLayout2.setLayoutParams(tP2);
        mRightButton = new ImageView(mContext);
        LayoutParams params2 = new LayoutParams(mDefRightButtonSize, mDefRightButtonSize);
        //addRule()方法必须一个一个添加，不能用|
        params2.addRule(RelativeLayout.CENTER_IN_PARENT);
        params2.bottomMargin = getPxFromDp(5);
        params2.topMargin = getPxFromDp(5);
        params2.leftMargin = getPxFromDp(10);
        params2.rightMargin = getPxFromDp(10);
        mRightButton.setLayoutParams(params2);
        mRightButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (mDefRightButtonImg != -1)
            mRightButton.setImageResource(mDefRightButtonImg);
        mTouchLayout2.addView(mRightButton);
        mTouchLayout2.setOnClickListener(this);

        //添加顺序必须一定，因为点击事件需要判断
        addView(mTouchLayout);
        addView(mTitle);
        addView(mTouchLayout2);

        setBackgroundResource(mDefBackgroundColor);

    }


    @Override
    public void onClick(View view) {
        int i = indexOfChild(view);
        //此处按照添加顺序去判断点击的是哪个按钮
        switch (i) {
            case 0:
                mEvents.clickLeftButton();
                break;
            case 2:
                mEvents.clickRightButton();
                break;
        }
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    /**
     * 设置左按钮不可见
     *
     * @param flag
     */
    public void setLeftButtonEnable(boolean flag) {
        if (flag)
            mTouchLayout.setVisibility(VISIBLE);
        else
            mTouchLayout.setVisibility(GONE);
    }
    /**
     * 设置右按钮不可见
     *
     * @param flag
     */
    public void setRightButtonEnable(boolean flag) {
        if (flag)
            mTouchLayout2.setVisibility(VISIBLE);
        else
            mTouchLayout2.setVisibility(GONE);
    }

    public AddClickEvents getEvents() {
        return mEvents;
    }

    public void setEvents(AddClickEvents events) {
        mEvents = events;
    }

    /**
     * 外部添加左右按钮点击事件的接口
     */
    public interface AddClickEvents {
        void clickLeftButton();

        void clickRightButton();
    }

    /*
       得到dp转化成的px
   */
    public int getPxFromDp(float dip) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mContext.getResources().getDisplayMetrics());
        return (int) result;
    }
    /*
        得到dp转化成的px
    */
    public float getPxFromSp(float sp) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext.getResources().getDisplayMetrics());
        return result;
    }

}
