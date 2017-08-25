package com.xcheng.view.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * 页面跳转工具类
 * Created by chengxin on 2017/4/19.
 */
public class JumpUtil {
    public static Intent getIntent(Context packageContext, Class<?> target, @Nullable Bundle bundle) {
        Intent intent = new Intent(packageContext, target);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        return intent;
    }

    public static Bundle getBundle(String key, Serializable serValue) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, serValue);
        return bundle;
    }

    public static Bundle getBundle(String key, Parcelable parValue) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, parValue);
        return bundle;
    }

    @SuppressWarnings("unchecked")
    @CheckResult
    public static <T extends Serializable> T getSerializable(@NonNull Intent intent, String bundleKey) {
        return (T) intent.getSerializableExtra(bundleKey);
    }

    @CheckResult
    public static <T extends Parcelable> T getParcelable(@NonNull Intent intent, String bundleKey) {
        return intent.getParcelableExtra(bundleKey);
    }


    public static void toActivity(Activity from, Class<?> target) {
        toActivity(from, target, null);
    }

    /**
     * @param from   跳转发起页
     * @param target 目标页
     * @param bundle 传递的数据
     */
    public static void toActivity(Activity from, Class<?> target, Bundle bundle) {
        Intent intent = getIntent(from, target, bundle);
        from.startActivity(intent);
    }

    public static void toActivityForResult(Activity from, Class<?> target, int requestCode, Bundle bundle) {
        Intent intent = getIntent(from, target, bundle);
        from.startActivityForResult(intent, requestCode);
    }

    // 多个Activity的值传递。ActivityA到达ActivityB再到达ActivityC，但ActivityB为过渡页可以finish了，此时ActivityC将值透传至ActivityA。
    public static void toActivityForwardResult(Activity from, Class<?> target, Bundle bundle) {
        Intent intent = getIntent(from, target, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        from.startActivity(intent);
        from.finish();
    }

    public static void toActivityWithFinish(Activity from, Class<?> target, Bundle bundle) {
        Intent intent = getIntent(from, target, bundle);
        from.startActivity(intent);
        from.finish();
    }

    /**
     * http://www.360doc.com/content/12/1225/15/6541311_256191828.shtml
     **/
    public static void toActivityClearTop(Activity from, Class<?> target, Bundle bundle) {
        Intent intent = getIntent(from, target, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//设置NO_ANIMATION在set之后才有效
        from.startActivity(intent);
    }

    /**
     * 和设置singleTASK同样的效果
     * Activity 不会无限制重启 会调用 onNewIntent
     */
    public static void toActivityClearTopWithState(Activity from, Class<?> target, Bundle bundle) {
        Intent intent = getIntent(from, target, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        from.startActivity(intent);
    }

    /**
     * 启动某个Activity至栈底
     **/
    public static void toActivityBeRoot(Activity from, Class<?> target, Bundle bundle) {
        Intent intent = getIntent(from, target, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        from.startActivity(intent);
    }

    public static void toActivity(Fragment from, Class<?> target) {
        toActivity(from, target, null);
    }

    public static void toActivity(Fragment from, Class<?> target, Bundle bundle) {
        Intent intent = getIntent(from.getContext(), target, bundle);
        from.startActivity(intent);
    }

    public static void toActivityForResult(Fragment from, Class<?> target, int requestCode, Bundle bundle) {
        Intent intent = getIntent(from.getContext(), target, bundle);
        from.startActivityForResult(intent, requestCode);
    }
}
