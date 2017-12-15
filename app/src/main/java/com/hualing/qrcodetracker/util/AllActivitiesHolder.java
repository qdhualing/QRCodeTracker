package com.hualing.qrcodetracker.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 马鹏昊
 * @date {2016-10-12}
 * @des 持有所有的activity对象
 * @updateAuthor
 * @updateDate
 * @updateDes
 */
public class AllActivitiesHolder {

    //存储activity对象的集合
    private static List<Activity> allActivities = new ArrayList<>();

    //添加Activity对象
    public static void addAct(Activity activity){
        allActivities.add(activity);
    }
    //移除掉Activity对象
    public static void removeAct(Activity activity){
        allActivities.remove(activity);
        activity.finish();
    }
    //销毁所有的activity对象（关闭程序时调用）
    public static void finishAllAct(){
        for (int i = 0; i < allActivities.size(); i++) {
            allActivities.get(i).finish();
        }
        allActivities.clear();
    }

    public static int getActivityNum(){
        return allActivities.size();
    }


}
