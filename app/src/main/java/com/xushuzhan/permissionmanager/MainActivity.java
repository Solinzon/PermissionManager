package com.xushuzhan.permissionmanager;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xushuzhan.annotation.*;
import com.xushuzhan.api.PermissionManager;
@NeedPermission
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionManager.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE});

    }
    @OnGranted
    void onGranted(){
        Toast.makeText(this, "YES!用户同意了授权！", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "YES!用户同意了授权!");
    }
    @OnDenied
    void onDenied(){
        Toast.makeText(this, "oh NO!用户拒绝了授权！", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "oh NO!用户拒绝了授权！");
    }

    @OnShowRationale
    void onShowRationale(){
        Toast.makeText(this, "用户选择了了不再询问！", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "用户选择了了不再询问");
    }
}
