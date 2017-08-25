package com.xcheng.view.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;

import com.xcheng.view.util.ToastLess;


/**基础的Dialog
 * Created by cc on 2016/11/7.
 */
public abstract class EasyDialog extends Dialog implements IEasyController {
    public EasyDialog(Context context, boolean flag,
                      OnCancelListener listener) {
        super(context, flag, listener);
    }

    public EasyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public EasyDialog(Context context) {
        super(context);
    }


    public Activity getActivity() {
        Context context = getContext();
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
     ***/
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
        ToastLess.showToast(text);
    }

    @Override
    public void showMessage(@StringRes int stringId) {
        ToastLess.showToast(stringId);
    }
}
