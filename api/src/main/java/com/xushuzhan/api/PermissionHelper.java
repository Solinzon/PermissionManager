package com.xushuzhan.api;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

/**
 * Created by xushuzhan on 2018/1/28.
 * 用于封装一些检查权限工作的工具类
 */
public class PermissionHelper {
    /**
     * 检测是否拥有某些权限
     *
     * @param context
     * @param premissions
     * @return
     */
    public static boolean checkPermissions(Context context, String[] premissions) {
        if (premissions.length <= 0) return false;
        for (int i = 0; i < premissions.length; i++) {
            if (!hasSelfPermission(context, premissions[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否拥有有某个权限
     *
     * @return
     */
    public static boolean hasSelfPermission(Context context, String permission) {
        //TODO:容错处理，当从传入的权限名出错的时候的处理方式
        return (PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static String[] noGrantedPermissions(Context context, @NonNull String[] permissions) {
        String[] result;
        int resultLength = 0;
        for (int i = 0; i < permissions.length; i++) {
            if (hasSelfPermission(context, permissions[i])) {
                permissions[i] = "granted";

            } else {
                resultLength++;
            }
        }

        result = new String[resultLength];
        int j = 0;
        for (int i = 0; i < permissions.length; i++) {
            if (!permissions[i].equals("granted")) {
                result[j] = permissions[i];
                j++;
            }
        }

        return result;

    }

    public static boolean hasGrantedPermission(int grantedResult) {
        //TODO:容错处理,当结果有误的时候的处理方式
        return grantedResult == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasGrantedPermissions(int[] grantedResults) {
        if (grantedResults.length <= 0) return false;
        for (int i = 0; i < grantedResults.length; i++) {
            if (!hasGrantedPermission(grantedResults[i])) return false;
        }
        return true;
    }
}
