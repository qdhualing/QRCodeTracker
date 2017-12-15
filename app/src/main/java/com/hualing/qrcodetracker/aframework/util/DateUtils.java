package com.hualing.qrcodetracker.aframework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
	public static final String F_HMS = "HH:mm:ss";
	public static final String F_YMD = "yyyy-MM-dd";
	public static final String F_YMDHM = "yyyy-MM-dd HH:mm";
	public static final String F_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String F_YMDHMSS = "yyyy-MM-dd HH:mm:ss.SSS";

	public static final String F_ZH_HM = "HH时mm分";
	public static final String F_ZH_HMS = "HH时mm分ss秒";
	public static final String F_ZH_YM = "yyyy年MM月";
	public static final String F_ZH_MD = "MM月dd日";
	public static final String F_ZH_YMD = "yyyy年MM月dd日";
	static Map<Integer, String> dateFormatMap = new HashMap<>();
	static {
		dateFormatMap.put(F_YMD.length(), F_YMD);
		dateFormatMap.put(F_YMDHM.length(), F_YMDHM);
		dateFormatMap.put(F_YMDHMS.length(), F_YMDHMS);
		dateFormatMap.put(F_YMDHMSS.length(), F_YMDHMSS);
	}

	public static String format(Date date) {
		return format(date, F_YMDHMS);
	}

	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Date parse(String dateStr) {
		if (dateStr == null || dateStr.length() == 0) {
			return null;
		}
		String pattern = dateFormatMap.get(dateStr.length());
		if (pattern == null) {
			throw new RuntimeException("时间" + dateStr + "未找到对应格式化模板");
		}
		return parse(dateStr, pattern);
	}

	public static Date parse(String dateStr, String pattern) {
		if (dateStr == null || dateStr.length() == 0) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("时间" + dateStr + "格式化失败");
		}

	}

	public static int getYear() {
		return getYear(new Date());
	}

	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth() {
		return getMonth(new Date());
	}

	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getDay() {
		return getDay(new Date());
	}

	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour() {
		return getHour(new Date());
	}

	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return getMinute(new Date());
	}

	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	public static int getSecond() {
		return getSecond(new Date());
	}

	public static int getSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	public static Date addYears(Date date, int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	public static Date addMonths(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	public static Date addWeeks(Date date, int amount) {
		return add(date, Calendar.WEEK_OF_YEAR, amount);
	}

	public static Date addDays(Date date, int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}

	public static Date addHours(Date date, int amount) {
		return add(date, Calendar.HOUR_OF_DAY, amount);
	}

	public static Date addMinutes(Date date, int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	public static Date addSeconds(Date date, int amount) {
		return add(date, Calendar.SECOND, amount);
	}

	public static Date addMilliseconds(Date date, int amount) {
		return add(date, Calendar.MILLISECOND, amount);
	}

	private static Date add(Date date, int calendarField, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTime(date);
		calendar.add(calendarField, num);
		return calendar.getTime();
	}

	public static Date now() {
		return new Date();
	}

	public static Date nowDay() {
		return day(now());
	}

	public static Date nowDate() {
		return date(now());
	}

	public static Date nowTime() {
		return time(now());
	}

	public static Date day(Date date) {
		return truncate(date, Calendar.DAY_OF_MONTH);
	}

	public static Date date(Date date) {
		return truncate(date, Calendar.SECOND);
	}

	public static Date time(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		c.clear();
		c.set(0, 0, 1, hour, minute, second);
		return c.getTime();
	}

	private static boolean isSame(Date date1, Date date2) {
		return date1.getTime() == date2.getTime();
	}

	public static boolean isSameDay(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}

	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1
				.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) && cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) && cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND);
	}

	public static boolean isSameTime(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) && cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) && cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND);
	}

	public static int diffYear(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
	}

	public static int diffMonth(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
	}

	public static long diffDay(Date date1, Date date2) {
		Date dateTime1 = truncate(date1, Calendar.DAY_OF_MONTH);
		Date dateTime2 = truncate(date2, Calendar.DAY_OF_MONTH);
		return (dateTime1.getTime() - dateTime2.getTime()) / 1000 / 60 / 60 / 24;
	}

	public static long diffHour(Date date1, Date date2) {
		Date dateTime1 = truncate(date1, Calendar.HOUR_OF_DAY);
		Date dateTime2 = truncate(date2, Calendar.HOUR_OF_DAY);
		return (dateTime1.getTime() - dateTime2.getTime()) / 1000 / 60 / 60;
	}

	public static long diffMinute(Date date1, Date date2) {
		Date dateTime1 = truncate(date1, Calendar.MINUTE);
		Date dateTime2 = truncate(date2, Calendar.MINUTE);
		return (dateTime1.getTime() - dateTime2.getTime()) / 1000 / 60;
	}

	public static long diffSecond(Date date1, Date date2) {
		Date dateTime1 = truncate(date1, Calendar.SECOND);
		Date dateTime2 = truncate(date2, Calendar.SECOND);
		return (dateTime1.getTime() - dateTime2.getTime()) / 1000;
	}

	private static Date truncate(Date date, int calendarField) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		c.clear();
		switch (calendarField) {
		case Calendar.YEAR:
			c.set(year, 0, 1);
			break;
		case Calendar.MONTH:
			c.set(year, month, 1);
			break;
		case Calendar.DAY_OF_MONTH:
			c.set(year, month, day);
			break;
		case Calendar.HOUR_OF_DAY:
			c.set(year, month, day, hour, 0, 0);
			break;
		case Calendar.MINUTE:
			c.set(year, month, day, hour, minute, 0);
			break;
		case Calendar.SECOND:
			c.set(year, month, day, hour, minute, second);
			break;
		default:
			break;
		}
		return c.getTime();
	}

	public static Date firstDayOfMonth() {
		return firstDayOfMonth(new Date());
	}

	public static Date firstDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		c.set(year, month, 1,0,0,0);
		return c.getTime();
	}

	public static Date lastDayOfMonth() {
		return lastDayOfMonth(new Date());
	}

	public static Date lastDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(year, month, day,0,0,0);
		return c.getTime();
	}
}
