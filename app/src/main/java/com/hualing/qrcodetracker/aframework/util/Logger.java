package com.hualing.qrcodetracker.aframework.util;

import android.util.Log;

/********************************************
 * 日志管理类.<br>
 * 类详细描述……<br>
 * CreateDate: 2014年7月28日<br>
 * Copyright: Copyright(c) 2014年7月28日<br>
 * Company: 蓝海伟略<br>
 * @since v1.0.0
 * @author  巫作坤
 * @version v1.0.0
 *********************************************/
public class Logger {
	public static final int WRITE_MODE_LOGCAT=1;
	public static final int WRITE_MODE_FILE=2;
	public static final int WRITE_MODE_ALL=3;
	public static int writeMode=1;
	public static int level=3;
	
	
	
	
	public static void d(String tag,String text)
	{
		Log.d(tag, text);
	}
	public static void e(String tag,String text)
	{
		Log.e(tag, text);
	}
	public static void i(String tag,String text)
	{
		Log.i(tag, text);
	}

	public static void w(String tag, String text, Exception e) {
		Log.w(tag, text);
	}
}
