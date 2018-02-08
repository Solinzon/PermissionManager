package com.xushuzhan.complier;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.xushuzhan.annotation.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

//import com.xushuzhan.api.PermissionListener;


@AutoService(Processor.class)
public class PermissionListenerProcessor extends AbstractProcessor {
    private Elements elementUtils;
    //日志辅助类
    private Messager messager;

    public static final String SUFFIX = "_PermissionListener";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE, ">>>>>>>>>>>>>>>>>process>>>>>>>>>>>>>>>>>>>>>>...");
        //1.获取NeedPermission元素注解的集合
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(NeedPermission.class);
        if (elements == null || elements.size() <= 0) return true;
        //2.遍历集合
        for (Element element : elements) {
            if (isRightLocation(element, ElementKind.CLASS)) {
                JavaFile listenerJavaFile = makeListenerJavaFile(element);
                try {
                    listenerJavaFile.writeTo(processingEnv.getFiler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        messager.printMessage(Diagnostic.Kind.NOTE, ">>>>>>>>>>>>>>>>>getSupportedAnnotationTypes>>>>>>>>>>>>>>>>>>>>>>");

        Set<String> type = new HashSet<>();
        type.add(NeedPermission.class.getCanonicalName());
        type.add(OnDenied.class.getCanonicalName());
        type.add(OnGranted.class.getCanonicalName());
        type.add(OnShowRationale.class.getCanonicalName());
        return type;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {

        return SourceVersion.latestSupported();
    }

    //判断注解的目标是否符合要求
    private boolean isRightLocation(Element element, ElementKind kind) {
        //TODO:如果注释的元素不符合要求的话，还可以抛出一个异常
        return element.getKind() == kind;
    }

    /**
     * 利用JavaPoet构建PermissionListener
     *
     * @param element
     * @return
     */
    private JavaFile makeListenerJavaFile(Element element) {
        messager.printMessage(Diagnostic.Kind.NOTE, ">>>>>>>>>>>>>>>>>makeListenerJavaFile>>>>>>>>>>>>>>>>>>>>>>");

        //获取类元素
        TypeElement typeElement = (TypeElement) element;
        //获取类里面的成员（这里是指被标记的某个Activity或者Fragment）
        List<? extends Element> members = elementUtils.getAllMembers(typeElement);
        //创建类
        TypeSpec.Builder typeBuilder = TypeSpec
                //class MainActivity_OnGrantedListener
                .classBuilder(element.getSimpleName() + SUFFIX)
                //public
                .addModifiers(Modifier.PUBLIC)
                //implements PermissionListener<MainActivity>
                .addSuperinterface(ParameterizedTypeName.get(ClassName.bestGuess( "com.xushuzhan.api.PermissionListener")
                        , ClassName.bestGuess(element.getSimpleName().toString())));

        //创建四个回调方法
        //1.onGranted
        MethodSpec.Builder grantedMethodBuilder = MethodSpec
                .methodBuilder("onGranted")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(ClassName.get(typeElement.asType()), "target");

        //2.onDenied
        MethodSpec.Builder deniedMethodBuilder = MethodSpec
                .methodBuilder("onDenied")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(ClassName.get(typeElement.asType()), "target");

        //3.onNeverAsk
//        MethodSpec.Builder neverAskMethodBuilder = MethodSpec
//                .methodBuilder("onNeverAsk")
//                .addAnnotation(Override.class)
//                .addModifiers(Modifier.PUBLIC)
//                .returns(TypeName.VOID)
//                .addParameter(ClassName.get(typeElement.asType()), "target");

        //4.onShowRationale
        MethodSpec.Builder showRationaleMethodBuilder = MethodSpec
                .methodBuilder("onShowRationale")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(ClassName.get(typeElement.asType()), "target");


        for (Element method : members) {

            OnGranted onGranted = method.getAnnotation(OnGranted.class);
            OnDenied onDenied = method.getAnnotation(OnDenied.class);
            OnShowRationale onShowRationale = method.getAnnotation(OnShowRationale.class);

            if (onGranted != null) {
                String[] permissions = onGranted.permissionName();
//                //TODO:  验证标记的权限是否和回调函数的参数里面的权限一致
//                /*
//                //构建出权限数组的文本内容:{"XXX","XXX"}
//                StringBuilder permissionsText = new StringBuilder();
//                permissionsText.append("{");
//                for (int i = 0; i < permissions.length; i++) {
//                    permissionsText.append("\"")
//                            .append(permissions[i])
//                            .append("\"")
//                            .append(",");
//                }
//                permissionsText.append("}");
//
//                grantedMethodBuilder
//                        .addStatement(String.format("if(Arrays.equals())"))
//
//                   //.addStatement(String.format("if(Arrays.equals(permissions,new String[] %s)){ \ntarget.%s() ; \nreturn ;\n}", new Object[] { stringBuilder.toString(), item.getSimpleName().toString() }), new Object[0]);
//                */
                grantedMethodBuilder.addStatement(String.format("target.%s();", method.getSimpleName().toString()));
            }
            if (onDenied != null) {
                String[] permissions = onDenied.permissionName();
                //TODO:  验证标记的权限是否和回调函数的参数里面的权限一致
                deniedMethodBuilder.addStatement(String.format("target.%s();", method.getSimpleName().toString()));
            }
//            if (onNeverAsk != null) {
//                String[] permissions = onNeverAsk.permissionName();
//                //TODO:  验证标记的权限是否和回调函数的参数里面的权限一致
//                neverAskMethodBuilder.addStatement(String.format("target.%s();", method.getSimpleName().toString()));
//            }

            if (onShowRationale != null) {
                String[] permissions = onShowRationale.permissionName();
                //TODO:  验证标记的权限是否和回调函数的参数里面的权限一致
                showRationaleMethodBuilder.addStatement(String.format("target.%s();", method.getSimpleName().toString()));
            }
        }
        typeBuilder.addMethod(grantedMethodBuilder.build());
        typeBuilder.addMethod(deniedMethodBuilder.build());
//        typeBuilder.addMethod(neverAskMethodBuilder.build());
        typeBuilder.addMethod(showRationaleMethodBuilder.build());

        TypeSpec typeSpec = typeBuilder.build();

        JavaFile javaFile = JavaFile.builder(elementUtils.getPackageOf(typeElement).getQualifiedName().toString(), typeSpec).build();
        return javaFile;
    }

}
