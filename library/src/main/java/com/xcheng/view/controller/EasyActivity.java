package com.xcheng.view.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.xcheng.view.EasyView;
import com.xcheng.view.autosize.AutoSize;
import com.xcheng.view.autosize.ResourcesWrapper;
import com.xcheng.view.util.KeyboardHelper;

/**
 * 所有Activity的基类
 *
 * @author xincheng
 */
public abstract class EasyActivity extends ActionBarSupportActivity implements IEasyView {
//    public int clearTop() {
//        return Intent.FLAG_ACTIVITY_CLEAR_TOP// 注意本行的FLAG设置
//                | Intent.FLAG_ACTIVITY_NO_ANIMATION;//设置NO_ANIMATION在set之后才有效
//    }
//
//    // 多个Activity的值传递。ActivityA到达ActivityB再到达ActivityC，
//    // 但ActivityB为过渡页可以finish了，此时ActivityC将值透传至ActivityA。
//    public int forwardResult() {
//        return Intent.FLAG_ACTIVITY_FORWARD_RESULT;
//    }
//
//    public int clearTopWithState() {
//        return Intent.FLAG_ACTIVITY_CLEAR_TOP// 注意本行的FLAG设置
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP;
//    }
//
//    public int beRoot() {
//        return Intent.FLAG_ACTIVITY_CLEAR_TASK
//                | Intent.FLAG_ACTIVITY_NEW_TASK;
//    }

    private Dialog mLoadingDialog;
    private Resources mResources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //兼容重写onCreate
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            initData();
            initView(savedInstanceState);
            setListener();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isHideKeyboardIfTouchOutSide()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    View view = getCurrentFocus();
                    if (KeyboardHelper.isHideInput(view, ev)) {
                        KeyboardHelper.hide(view);
                    }
                    break;
                default:
                    break;
            }
        }
        try {
            //java.lang.IllegalArgumentException:pointerIndex out of range
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    protected boolean isHideKeyboardIfTouchOutSide() {
        return false;
    }

    /**
     * 从传递的Intent中获取 根据
     * bundle获取值 获取从上一个界面传递的信息
     */
    protected Bundle getBundle() {
        return getIntent().getExtras();
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = EasyView.FACTORY.create(this, getClass().getName());
        }
        mLoadingDialog.show();
    }

    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public Resources getResources() {
        if (mResources == null) {
            final AutoSize autoSize = getAutoSize();
            if (autoSize != null) {
                mResources = new ResourcesWrapper(super.getResources(), autoSize);
            }
        }
        return mResources != null ? mResources : super.getResources();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mResources != null) {
            // The real (and thus managed) resources object was already updated
            // by ResourcesManager, so pull the current metrics from there.
            final DisplayMetrics newMetrics = super.getResources().getDisplayMetrics();
            mResources.updateConfiguration(newConfig, newMetrics);
        }
    }

    /**
     * 子类可重写适配
     **/
    @Nullable
    protected AutoSize getAutoSize() {
        return EasyView.AUTOSIZE;
    }

    @Override
    public void showMessage(CharSequence text) {
        EasyView.info(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            onActivityResultOk(requestCode, data);
    }

    //统一setResult里面的响应成功码为Activity.RESULT_OK，这样会少很多的代码
    protected void onActivityResultOk(int requestCode, Intent data) {

    }

    protected final Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
    }
}
