package com.xcheng.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

/**
 * 全局EasyView配置类
 * Created by chengxin on 2017/12/6.
 */
public class ViewConfig {
    private final int loadingLayout;
    private final Context context;

    public ViewConfig(Builder builder) {
        loadingLayout = builder.loadingLayout;
        context = builder.context;
    }

    public static Builder newBuilder(@NonNull Context context) {
        return new Builder(context);
    }

    @LayoutRes
    public int getLoadingLayout() {
        return loadingLayout;
    }

    public Context getContext() {
        return context;
    }

    public static class Builder {
        private int loadingLayout;
        private Context context;

        public Builder(@NonNull Context context) {
            this.context = context.getApplicationContext();
        }

        /**
         * 设置加载Dialog的默认布局
         */
        public Builder loadingLayout(@LayoutRes int loadingLayout) {
            this.loadingLayout = loadingLayout;
            return this;
        }

        public ViewConfig build() {
            if (loadingLayout == 0) {
                loadingLayout = R.layout.ev_dialog_loading;
            }
            return new ViewConfig(this);
        }
    }
}
