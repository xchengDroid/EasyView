package com.xcheng.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.xcheng.view.controller.dialog.LoadingDialog;
import com.xcheng.view.util.Preconditions;

/**
 * 创建时间：2018/3/30
 * 编写人： chengxin
 * 功能描述：EasyView全局参数配置类
 */
public class Config {
    static final DialogFactory DEFAULT_FACTORY = new DialogFactory() {
        @NonNull
        @Override
        public Dialog create(Context context, String fromClazz) {
            return new LoadingDialog(context);
        }
    };

    private final Context context;
    private final DialogFactory factory;
    private final MsgDispatcher dispatcher;

    private Config(Builder builder) {
        context = builder.context;
        factory = builder.factory;
        dispatcher = builder.dispatcher;
    }

    public Context context() {
        return context;
    }

    public DialogFactory factory() {
        return factory;
    }

    public MsgDispatcher dispatcher() {
        return dispatcher;
    }

    //后续扩展属性
    public static class Builder {
        private Context context;
        private DialogFactory factory;
        private MsgDispatcher dispatcher;


        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.factory = DEFAULT_FACTORY;
        }

        public Builder factory(DialogFactory factory) {
            Preconditions.checkNotNull(factory, "factory==null");
            this.factory = factory;
            return this;
        }

        public Builder dispatcher(MsgDispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Config build() {
            Preconditions.checkNotNull(dispatcher, "dispatcher==null");
            return new Config(this);
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
