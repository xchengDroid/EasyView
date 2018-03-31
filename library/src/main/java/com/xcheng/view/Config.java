package com.xcheng.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.xcheng.view.controller.dialog.LoadingDialog;
import com.xcheng.view.util.EasyPreconditions;

/**
 * 创建时间：2018/3/30
 * 编写人： chengxin
 * 功能描述：EasyView全局参数配置类
 */
public class Config {
    public static final Factory DEFAULT_FACTORY = new Factory() {
        @NonNull
        @Override
        public Dialog createLoadingDialog(Context context, String fromClazz) {
            return new LoadingDialog(context);
        }

        @NonNull
        @Override
        public Toast createGlobalToast(Context context) {
            return Toast.makeText(context, null, Toast.LENGTH_SHORT);
        }
    };

    private Context context;
    private Factory factory;

    private Config(Builder builder) {
        context = builder.context;
        factory = builder.factory;
    }

    public Context context() {
        return context;
    }

    public Factory factory() {
        return factory;
    }

    //后续扩展属性
    public static class Builder {
        private Context context;
        private Factory factory;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.factory = DEFAULT_FACTORY;
        }

        public Builder factory(Factory factory) {
            EasyPreconditions.checkNotNull(factory, "factory==null");
            this.factory = factory;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }

    /**
     * 创建LoadingDialog的工厂类
     */
    public interface Factory {

        /**
         * 为每隔需要记载的页面创建Dialog
         *
         * @param context   当前的Context对象
         * @param fromClazz 哪个页面需要创建
         * @return 加载的Dialog对象
         */
        @NonNull
        Dialog createLoadingDialog(Context context, String fromClazz);

        /**
         * 创建全局的Toast
         *
         * @param context applicationContext
         * @return Toast
         */
        @NonNull
        Toast createGlobalToast(Context context);
    }
}
