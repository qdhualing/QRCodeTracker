package com.hualing.qrcodetracker.aframework.util;

import com.alibaba.fastjson.JSON;

public class BeanUtils {

	public static <T> T copy(Object source,Class<T> clazz){
		String jsonString=JSON.toJSONString(source);
		T target=JSON.parseObject(jsonString, clazz);
		return target;
	}
}
