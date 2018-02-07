package com.xushuzhan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xushuzhan on 2018/1/28.
 */
//标记在Activity或者Fragment上面，标记这个Activty或者Frament中的代码会涉及到权限请求
// （TODO：另外还可以用于AOP过滤需要HOOK的onPermissionResult）
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface NeedPermission {
    String[] permissionName() default " ";
    int requestCode() default 0;
}
