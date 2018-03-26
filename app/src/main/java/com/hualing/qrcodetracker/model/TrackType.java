package com.hualing.qrcodetracker.model;

/**
 * @author 马鹏昊
 * @date {date}
 * @des 二维码属于物料、半成品、小包装成品还是大包装成品
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class TrackType {

    //截取二维码的始末位置
    public static final int START_INDEX = 8;
    public static final int END_INDEX = 9;

    public final static String WL = "1";
    public final static String BCP = "2";
    public final static String SMALL_CP = "3";
    public final static String BIG_CP = "4";

}
