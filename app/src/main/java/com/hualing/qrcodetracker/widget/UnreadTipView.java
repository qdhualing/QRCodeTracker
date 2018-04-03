package com.hualing.qrcodetracker.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hualing.qrcodetracker.R;


/**
 * @author 马鹏昊
 * @date {2017.8.21}
 * @des 未读提示View
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class UnreadTipView extends FrameLayout {

    private ImageView msgIcon ;
    private View tipDot ;

    private int msgIconSize ;
    private int tipDotSize ;

    private Context mContext ;


    public UnreadTipView(@NonNull Context context) {
        this(context,null,0);
    }

    public UnreadTipView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UnreadTipView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context ;
        msgIconSize = getPxFromDp(25);
        tipDotSize = getPxFromDp(6);

        initView();

    }

    private void initView() {

        msgIcon = new ImageView(mContext);
        LayoutParams params = new LayoutParams(msgIconSize,msgIconSize);
        msgIcon.setLayoutParams(params);
        msgIcon.setImageResource(R.drawable.notification);

        addView(msgIcon);

        tipDot = new View(mContext);
        LayoutParams params1 = new LayoutParams(tipDotSize,tipDotSize);
        params1.gravity = Gravity.TOP| Gravity.RIGHT ;
        tipDot.setLayoutParams(params1);
        tipDot.setBackgroundResource(R.drawable.shape_new_msg_tip);

        addView(tipDot);

    }

    /**
     * 是否有未读消息
     * @return
     */
    public boolean hasUnreadMsg(){
        if (tipDot.getVisibility()==VISIBLE) {
            return true ;
        }else{
            return false ;
        }
    }

    /**
     * 设置是否显示未读状态
     * @param flag
     */
    public void setUnreadState(boolean flag){
        if (flag) {
            tipDot.setVisibility(VISIBLE);
        }else{
            tipDot.setVisibility(INVISIBLE);
        }
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
