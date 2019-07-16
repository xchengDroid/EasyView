package com.xcheng.view.controller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;


/**
 * 基础的Dialog
 * Created by chengxin on 2016/11/7.
 */
public abstract class EasyDialog extends Dialog implements View.OnClickListener {

    public EasyDialog(@NonNull Context context) {
        super(context);
    }

    public EasyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public EasyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initDialog(savedInstanceState);
    }

    /**
     * 获取布局Layout的id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化dialog
     */
    protected void initDialog(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {

    }
}
