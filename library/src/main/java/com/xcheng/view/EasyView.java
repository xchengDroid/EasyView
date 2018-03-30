package com.xcheng.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;

import com.xcheng.view.util.LocalDisplay;

/**
 * Easy工具类，引用全局Context ,提供子线程刷新UI方法等
 * Created by chengxin on 2017/8/24.
 */
public class EasyView {
    private static final Handler HANDLER_UI = new Handler(Looper.getMainLooper());
    private static Config sConfig;
    
    public static void init(Config config) {
        sConfig = config;
        LocalDisplay.init(getContext());
    }

    /**
     * 不要用此context做 View 初始化，inflate等操作，否则会使用系统默认的主题样式，如果你自定义了某些样式可能不会被使用。
     *
     * @return applicationContext 对象
     */
    public static Context getContext() {
        return sConfig.context();
    }

    public static Config getConfig() {
        return sConfig;
    }


    public static String getString(@StringRes int stringId) {
        return getContext().getString(stringId);
    }

    public static CharSequence getText(@StringRes int stringId) {
        return getContext().getText(stringId);
    }

    /**
     * @param action the action to run on the UI thread
     */
    public static void runOnUiThread(Runnable action) {
        HANDLER_UI.post(action);
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
