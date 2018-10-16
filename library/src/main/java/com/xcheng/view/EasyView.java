package com.xcheng.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;

import com.xcheng.view.controller.dialog.LoadingDialog;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.Preconditions;

/**
 * Easy工具类，引用全局Context ,提供子线程刷新UI方法等
 * Created by chengxin on 2017/8/24.
 */
public class EasyView {
    static String TAG = EasyView.class.getName();
    private static final Handler HANDLER_UI = new Handler(Looper.getMainLooper());
    // by default
    public static DialogFactory FACTORY = new DialogFactory() {
        @NonNull
        @Override
        public Dialog create(Context context, String fromClazz) {
            return new LoadingDialog(context);
        }
    };

    public static MsgDispatcher DISPATCHER = new MsgDispatcher() {
        static final String TAG = "Msg";

        @Override
        public void onError(CharSequence msg) {
            Log.e(TAG, msg.toString());
        }

        @Override
        public void onWarning(CharSequence msg) {
            Log.w(TAG, msg.toString());
        }

        @Override
        public void onSuccess(CharSequence msg) {
            Log.d(TAG, msg.toString());
        }

        @Override
        public void onInfo(CharSequence msg) {
            Log.d(TAG, msg.toString());
        }
    };

    private static Context appContext;

    public static void init(Context context) {
        Preconditions.checkNotNull(context, "context==null");
        appContext = context.getApplicationContext();
        LocalDisplay.init(appContext);
    }

    /**
     * 不要用此context做 View 初始化，inflate等操作，否则会使用系统默认的主题样式，如果你自定义了某些样式可能不会被使用。
     *
     * @return applicationContext 对象
     */
    public static Context getContext() {
        return appContext;
    }

    public static String getString(@StringRes int stringId) {
        return appContext.getString(stringId);
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

    public static void error(final CharSequence msg) {
        if (isOnMainThread()) {
            DISPATCHER.onError(msg);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    error(msg);
                }
            });
        }
    }

    public static void warning(final CharSequence msg) {
        if (isOnMainThread()) {
            DISPATCHER.onWarning(msg);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    warning(msg);
                }
            });
        }
    }

    public static void info(final CharSequence msg) {
        if (isOnMainThread()) {
            DISPATCHER.onInfo(msg);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    info(msg);
                }
            });
        }
    }

    public static void success(final CharSequence msg) {
        if (isOnMainThread()) {
            DISPATCHER.onSuccess(msg);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    success(msg);
                }
            });
        }
    }

    /**
     * 创建LoadingDialog的工厂类
     */
    public interface DialogFactory {
        /**
         * 为每隔需要记载的页面创建Dialog
         *
         * @param context   当前的Context对象
         * @param fromClazz 哪个页面需要创建
         * @return 加载的Dialog对象
         */
        @NonNull
        Dialog create(Context context, String fromClazz);
    }

    /**
     * 全局的消息分发
     */
    public interface MsgDispatcher {

        void onError(CharSequence msg);

        void onWarning(CharSequence msg);

        void onSuccess(CharSequence msg);

        void onInfo(CharSequence msg);
    }
}
