package com.hualing.qrcodetracker.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hualing.qrcodetracker.R;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class IntentUtil {

    public static void openActivity(Activity context, Class dClass){
        Intent intent = new Intent(context,dClass);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
