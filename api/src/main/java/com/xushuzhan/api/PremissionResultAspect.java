package com.xushuzhan.api;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.TargetClass;

/**
 * Created by xushuzhan on 2018/1/29
 * 用来Hook onRequestPermissionsResult方法的切面，将将处理权限的代码织入其中
 */
@SuppressWarnings("unchecked")
public class PremissionResultAspect {
    @TargetClass(value = "android.support.v7.app.AppCompatActivity", scope = Scope.LEAF)
    @Insert(value = "onRequestPermissionsResult", mayCreateSuper = true)
    public void onRequestPermissionsResultAspect(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("xsz", "onRequestPermissionsResultAspect -> 权限的数量是 " + permissions.length);
        Origin.callVoid();
        PermissionListener listener = PermissionManager.getPermissionListener(This.get());
        if (grantResults.length > 0 && listener != null) {
            for (int i = 0; i < grantResults.length; i++) {
                if (PermissionHelper.hasGrantedPermission(grantResults[i])) {
                    //已经获得用户权限
                    listener.onGranted(this);
                } else {
                    // 用户拒绝授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) This.get(), permissions[i])) {
                        //用户拒绝了授权，但是没有点击不再询问
                        //提示用户为什么APP需要这个权限
                        listener.onDenied(this);
//                        Log.d("xsz", "用户拒绝了授权，但是没有点击不再询问:"+permissions[i]);
                    } else {
                        //返回false -> 用户点击了不再询问
                        listener.onShowRationale(this);
//                        Log.d("xsz", "用户点击了不再询问。。。。。" + permissions[i]);
                    }
                }
            }
        }
    }
}


