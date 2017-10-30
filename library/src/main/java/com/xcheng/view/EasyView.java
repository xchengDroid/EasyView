package com.xcheng.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.xcheng.view.util.LocalDisplay;

/**
 * Easy工具类，引用全局Context ,提供子线程刷新UI方法等
 * Created by chengxin on 2017/8/24.
 */
public class EasyView {
    private static Context sAppContext;
    private static int sLoadingLayout;
    private static final Handler HANDLER_UI = new Handler(Looper.getMainLooper());

    /**
     * *********************************************************************************************
     * Global ApplicationContext
     * *********************************************************************************************
     */
    //调用初始化数据
    public static void init(Context context) {
        init(context, R.layout.ev_dialog_loading);
    }

    //调用初始化数据
    public static void init(Context context, @LayoutRes int loadingLayout) {
        sAppContext = context.getApplicationContext();
        LocalDisplay.init(sAppContext);
        sLoadingLayout = loadingLayout;
    }

    /**
     * 不要用此context做 View 初始化，inflate等操作，否则会使用系统默认的主题样式，如果你自定义了某些样式可能不会被使用。
     *
     * @return applicationContext 对象
     */
    public static Context getContext() {
        return sAppContext;
    }

    public static int getLoadingLayout() {
        return sLoadingLayout;
    }

    public static String getString(@StringRes int stringId) {
        return sAppContext.getString(stringId);
    }

    public static CharSequence getText(@StringRes int stringId) {
        return sAppContext.getText(stringId);
    }

    public static int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(sAppContext, colorId);
    }

    public static Drawable getDrawable(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(sAppContext, drawableId);
    }

    public static int getDimen(@DimenRes int dimenId) {
        return sAppContext.getResources().getDimensionPixelOffset(dimenId);
    }

    /**
     * @param action the action to run on the UI thread
     */
    public static void runOnUiThread(Runnable action) {
        runOnUiThreadDelayed(action, 0);
    }

    /**
     * @param action the action to run on the UI thread
     */
    public static void runOnUiThreadDelayed(Runnable action, long delayMillis) {
        if (!isOnMainThread() || delayMillis > 0) {
            HANDLER_UI.postDelayed(action, delayMillis);
        } else {
            action.run();
        }
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
