package com.hualing.qrcodetracker.aframework.util;

public class ArrayUtils {
	public static boolean isEmpty(Object[] array) {
		if (array == null) {
			return true;
		}
		if (array.length == 0) {
			return true;
		}
		if (array.length==1&&"".equals(array[0])) {
			return true;
		}
		return false;
	}
	public static String join(Object[] array) {
		return join(array, ",", null);
	}

	private static String join(Object[] array, String separator, String quote) {
		if (separator == null) {
			separator = "";
		}
		if (quote == null) {
			quote = "";
		}
		StringBuilder sb = new StringBuilder();
		for (Object object : array) {
			if (object != null) {
				sb.append(separator + quote + object + quote);
			}
		}
		return sb.length() == 0 ? "" : sb.substring(separator.length());
	}
}
