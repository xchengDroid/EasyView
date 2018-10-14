package com.xcheng.view.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.EasyView;

/**
 * 基础Fragment，提供公有方法
 * 1、支持缓存RootView
 * 2、支持ViewPager懒加载
 *
 * @author xincheng @date:2014-8-4
 */
public abstract class EasyFragment extends Fragment implements IEasyView {
    /**
     * 需要缓存的RootView;
     */
    private View mRootView;
    /**
     * view是否已经初始化
     */
    private boolean mHasInitView;

    private Dialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean needInflater = !isCacheView() || mRootView == null;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHasInitView = true;
        if (canLazyLoad()) {
            onLazyLoad();
        }
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
    public boolean isCacheView() {
        return true;
    }


    protected void onLazyLoad() {

    }

    /**
     * @return true 视图已经初始化并对用户可见，满足此条件才会调用{@link #onLazyLoad()}
     */
    protected boolean canLazyLoad() {
        return mHasInitView && getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (canLazyLoad()) {
            onLazyLoad();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!isCacheView()) {
            mRootView = null;
            mHasInitView = false;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        hideLoading();
        //页面销毁的时候，防止外部引用Fragment，导致mRootView一直被引用,并且只支持FragmentPagerAdapter的页面缓存
        mRootView = null;
        mHasInitView = false;

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
            mLoadingDialog = EasyView.getConfig().factory().create(getContext(), getClass().getName());
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
        EasyView.info(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            onActivityResultOk(requestCode, data);
    }

    protected void onActivityResultOk(int requestCode, Intent data) {

    }
}
