package com.xushuzhan.api;


/**
 * Created by xushuzhan on 2018/1/28.
 */

public interface PermissionListener<T> {
    void onGranted(T target);//参数代表目标Activity或者Fragment
    void onDenied(T target);
//    void onNeverAsk(T target);
    void onShowRationale(T target);
}
