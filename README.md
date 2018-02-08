# PermissionManager 
## 安卓动态权限申请库  
**AOP部分基于[Lancet](https://github.com/eleme/lancet/blob/dev/README_zh.md)实现**  
## 使用方式：  
### 1.引入依赖  

```
annotationProcessor 'com.solinzon:permissionmanager-compiler:1.0.1-beta1'
compile 'com.solinzon:permissionmanager:1.0.1-Beta1'
```

### 2.添加Lancet支持


在根目录的 build.gradle 添加:

```
dependencies{
    classpath 'me.ele:lancet-plugin:1.0.4'
}
```
在 app 目录的`build.gradle` 添加：

```
apply plugin: 'me.ele.lancet'
```
### 3.使用示例  
1.在需要请求权限的Activity上添加`@NeedPermission`注解。  
如：

```
@NeedPermission
public class MainActivity extends AppCompatActivity {}
```

2.调用`PermissionManager.requestPermissions()`方法申请权限。
如：

```
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	PermissionManager.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE});

}
```
3.定义权限申请结果的回调方法，并加上相应注解。（方法名字可任意，但不可带参数）。  
如：

```
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
```  
**完整示例：**[点击这里](https://github.com/Solinzon/PermissionManager/blob/master/app/src/main/java/com/xushuzhan/permissionmanager/MainActivity.java)  

## 注意事项  
处在开发阶段，还有许多TODO尚未完成，仅仅适合与学习交流。**暂不适合用于生产环境。**
