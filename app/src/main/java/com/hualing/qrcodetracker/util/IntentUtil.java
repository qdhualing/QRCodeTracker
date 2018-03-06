package com.hualing.qrcodetracker.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
    public static void openActivityForResult(Activity context, Class dClass, int requestCode, Bundle bundle){
        Intent intent = new Intent(context,dClass);
        if (bundle==null) {
            context.startActivityForResult(intent,requestCode);
        }else{
            context.startActivityForResult(intent,requestCode,bundle);
        }
        context.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
