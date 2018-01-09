package com.xcheng.view.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.controller.dialog.LoadingDialog;
import com.xcheng.view.util.ToastLess;

/**
 * 基础Fragment，提供公有方法
 * 支持缓存RootView
 *
 * @author xincheng @date:2014-8-4
 */
public abstract class EasyFragment extends Fragment implements IEasyView {
    public static final String TAG = "EasyFragment";
    /**
     * 需要缓存的RootView;
     */
    private View mRootView;

    private LoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean needInflater = !cacheView() || mRootView == null;
        if (needInflater) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            initData();
            initView(savedInstanceState);
            setListener();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    /**
     * 添加监听 initView() 之后被调用
     */
    @Override
    public void setListener() {

    }

    /**
     * 是否需要缓存rootView 防止onCreteView 重新执行后重新初始化视图
     *
     * @return
     */
    public boolean cacheView() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!cacheView()) {
            mRootView = null;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //页面销毁的时候，防止外部引用Fragment，导致mRootView一直被引用,并且只支持FragmentPagerAdapter的页面缓存
        mRootView = null;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        final View view = getView();
        return view != null ? view.<T>findViewById(id) : null;
    }

    @Nullable
    @Override
    public View getView() {
        View view = super.getView();
        return view != null ? view : mRootView;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            onActivityResultOk(requestCode, data);
    }

    protected void onActivityResultOk(int requestCode, Intent data) {

    }
}
