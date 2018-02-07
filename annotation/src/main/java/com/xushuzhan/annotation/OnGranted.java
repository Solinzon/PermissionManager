package com.xushuzhan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xushuzhan on 2018/1/28.
 */
//用于注解授权的时候调用的方法
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnGranted {
    String[] permissionName() default " ";
}
