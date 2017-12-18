package com.hualing.qrcodetracker.util;

import android.content.Context;
import android.content.Intent;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class IntentUtil {

    public static void openActivity(Context context,Class dClass){
        Intent intent = new Intent(context,dClass);
        context.startActivity(intent);
    }

}
