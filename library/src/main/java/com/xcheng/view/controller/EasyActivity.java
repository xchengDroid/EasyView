package com.xcheng.view.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.xcheng.view.controller.dialog.LoadingDialog;
import com.xcheng.view.util.JumpUtil;
import com.xcheng.view.util.ToastLess;

import java.io.Serializable;

/**
 * 所有Activity的基类
 *
 * @author xincheng
 */
public abstract class EasyActivity extends TopBarSupportActivity implements IEasyController {
    public static final String TAG = "EasyActivity";

    private LoadingDialog mLoadingDialog;

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
        return JumpUtil.getSerializable(intent, bundleKey);
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
        return JumpUtil.getParcelable(intent, bundleKey);
    }


    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext());
        }
        mLoadingDialog.show();
    }

    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(CharSequence text) {
        ToastLess.showToast(text);
    }

    @Override
    public void showMessage(@StringRes int stringId) {
        ToastLess.showToast(stringId);
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
}
