package com.xcheng.view.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.xcheng.view.EasyView;


/**
 * 基础的Dialog
 * Created by cc on 2016/11/7.
 */
public abstract class EasyDialog extends Dialog implements IEasyView {

    public EasyDialog(@NonNull Context context) {
        super(context);
    }

    public EasyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public EasyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Activity getActivity() {
        Context context = getContext();
        if (context instanceof Activity) {
            return Activity.class.cast(context);
        }
        if (context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return Activity.class.cast(context);
    }


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData();
        initView(savedInstanceState);
        setListener();
        initLocation();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 在dialog设置View之后执行才有效
     */
    protected void initLocation() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(CharSequence text) {
        EasyView.info(text);
    }
}
