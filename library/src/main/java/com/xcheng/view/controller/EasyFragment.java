package com.xcheng.view.controller;

import android.app.Activity;
import android.content.Context;
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
 * 缓存RootView
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

    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        LifeCycleSubject.dispatchAttach(this, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LifeCycleSubject.dispatchCreate(this, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isCacheRootView()) {
            if (mRootView != null) {
                ViewGroup parent = (ViewGroup) mRootView.getParent();
                if (parent != null) {
                    parent.removeView(mRootView);
                }
            } else {
                mRootView = inflater.inflate(getLayoutId(), container, false);
                init(savedInstanceState);
            }
        } else {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            init(savedInstanceState);
        }
        LifeCycleSubject.dispatchCreateView(this, mRootView, savedInstanceState);
        return mRootView;
    }

    private void init(Bundle savedInstanceState) {
        initData();
        initView(savedInstanceState);
        setListener();
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
     * 是否需要缓存rootView 防止onCreteView 重新执行后重新加载数据
     *
     * @return
     */
    protected boolean isCacheRootView() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LifeCycleSubject.dispatchActivityCreated(this, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        LifeCycleSubject.dispatchStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LifeCycleSubject.dispatchResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LifeCycleSubject.dispatchPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        LifeCycleSubject.dispatchStop(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LifeCycleSubject.dispatchSaveInstanceState(this, outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LifeCycleSubject.dispatchDestroyView(this);
        if (!isCacheRootView()) {
            mRootView = null;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LifeCycleSubject.dispatchDestroy(this);
        //页面销毁的时候，防止外部引用Fragment，导致mRootView一直被引用,并且只支持FragmentPagerAdapter的页面缓存
        mRootView = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LifeCycleSubject.dispatchDetach(this);
    }

    public View findViewById(@IdRes int id) {
        return getView() != null ? getView().findViewById(id) : null;
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

    public void onActivityResultOk(int requestCode, Intent data) {

    }
}
