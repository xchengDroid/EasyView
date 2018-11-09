package com.xcheng.view.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.xcheng.view.EasyView;
import com.xcheng.view.util.KeyboardHelper;
import com.xcheng.view.util.ResourcesWrapper;
import com.xcheng.view.util.Router;

import java.io.Serializable;

/**
 * 所有Activity的基类
 *
 * @author xincheng
 */
public abstract class EasyActivity extends ActionBarSupportActivity implements IEasyView {

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
        return super.dispatchTouchEvent(ev);
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

    /**
     * @param bundleKey 存入Bundle的 key，或者是存入Intent的key,从getIntent()对象中获取
     * @param <T>
     * @return
     */
    @CheckResult
    protected <T extends Serializable> T getSerializable(String bundleKey) {
        return getSerializable(getIntent(), bundleKey);
    }

    @CheckResult
    protected <T extends Serializable> T getSerializable(@NonNull Intent intent, String bundleKey) {
        return Router.getSerializable(intent, bundleKey);
    }

    /**
     * @param bundleKey 存入Bundle的 key，或者是存入Intent的key,从getIntent()对象中获取
     * @param <T>
     * @return
     */
    @CheckResult
    protected <T extends Parcelable> T getParcelable(String bundleKey) {
        return getParcelable(getIntent(), bundleKey);
    }

    @CheckResult
    protected <T extends Parcelable> T getParcelable(@NonNull Intent intent, String bundleKey) {
        return Router.getParcelable(intent, bundleKey);
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
        final int designSizeInDp = getDesignSizeInDp();
        if (designSizeInDp == 0) {
            return super.getResources();
        } else {
            if (mResources == null) {
                mResources = new ResourcesWrapper(super.getResources(), designSizeInDp);
            }
            return mResources;
        }
    }

    /**
     * 获取设计的尺寸
     * 0：不适配
     * >0:适配宽度
     * <0:适配高度
     *
     * @return designSizeInDp
     */
    protected int getDesignSizeInDp() {
        return EasyView.DESIGN_SIZE_IN_DP;
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
