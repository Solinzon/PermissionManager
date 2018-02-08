package com.xushuzhan.api;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


/**
 * Created by xushuzhan on 2018/1/28.
 * 连接APT生成类和目标类的桥梁
 * 也是本Library的入口函数
 */

public class PermissionManager {
    private static final String TAG = "PermissionManager";
    public static final String SUFFIX = "_PermissionListener";
    public static int initRequestCode = 1000;
    public static void requestPermissions(Activity target, String[] permissions){
        if (PermissionHelper.checkPermissions(target,permissions)){
            //有权限
            Log.d(TAG, "requestPermissions -> 之前 已经获得权限：" + permissions);
        }else{
            //没有权限
            String[] noGrantedPermission = PermissionHelper.noGrantedPermissions(target,permissions);

            ActivityCompat.requestPermissions(target,noGrantedPermission,getNextRequestCode());
        }
    }

    public static <T>PermissionListener getPermissionListener(T t){
        try {
            Class listenerClazz = Class.forName(t.getClass().getCanonicalName() + SUFFIX);//com.xushuzhan.permissionmanagertest.MainActivity_PermissionListener
            return (PermissionListener) listenerClazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getNextRequestCode(){
        return initRequestCode ++;
    }
}
