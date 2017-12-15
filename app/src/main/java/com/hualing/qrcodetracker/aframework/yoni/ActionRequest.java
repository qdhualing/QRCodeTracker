package com.hualing.qrcodetracker.aframework.yoni;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionRequest {
	String func();

	String user() default "";

	Class<?> resultClass() default Object.class;

	String description() default "";
}
