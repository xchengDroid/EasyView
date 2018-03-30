package com.xcheng.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.xcheng.view.controller.dialog.LoadingDialog;
import com.xcheng.view.util.EasyPreconditions;
import com.xcheng.view.util.ToastLess;

/**
 * 创建时间：2018/3/30
 * 编写人： chengxin
 * 功能描述：EasyView全局参数配置类
 */
public class Config {
    private static final LoadingFactory LOADING_FACTORY = new LoadingFactory() {
        @NonNull
        @Override
        public Dialog create(Context context, String fromClazz) {
            return new LoadingDialog(context);
        }
    };
    private static final MessageDispatcher MESSAGE_DISPATCHER = new MessageDispatcher() {
        @Override
        public void dispatch(Context context, String fromClazz, CharSequence message) {
            ToastLess.showToast(message);
        }
    };

    private Context context;
    private LoadingFactory factory;
    private MessageDispatcher dispatcher;

    private Config(Builder builder) {
        context = builder.context;
        factory = builder.factory;
    }

    public Context context() {
        return context;
    }

    public LoadingFactory loadingFactory() {
        return factory;
    }

    public MessageDispatcher messageDispatcher() {
        return dispatcher;
    }


    //后续扩展属性
    public static class Builder {
        private Context context;
        private LoadingFactory factory;
        private MessageDispatcher dispatcher;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.factory = LOADING_FACTORY;
            this.dispatcher = MESSAGE_DISPATCHER;
        }

        public Builder loadingFactory(LoadingFactory factory) {
            EasyPreconditions.checkNotNull(factory, "factory==null");
            this.factory = factory;
            return this;
        }

        public Builder messageDispatcher(MessageDispatcher dispatcher) {
            EasyPreconditions.checkNotNull(dispatcher, "dispatcher==null");
            this.dispatcher = dispatcher;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }

    /**
     * 创建LoadingDialog的工厂类
     */
    public interface LoadingFactory {
        @NonNull
        Dialog create(Context context, String fromClazz);
    }

    public interface MessageDispatcher {
        void dispatch(Context context, String fromClazz, CharSequence message);
    }
}
