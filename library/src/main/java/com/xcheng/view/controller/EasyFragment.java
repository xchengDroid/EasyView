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

import com.xcheng.view.EasyView;
import com.xcheng.view.controller.dialog.LoadingDialog;
import com.xcheng.view.util.ToastLess;

/**
 * 基础Fragment，提供公有方法
 * 1、支持缓存RootView
 * 2、支持ViewPager懒加载
 *
 * @author xincheng @date:2014-8-4
 */
public abstract class EasyFragment extends Fragment implements IEasyView {
    public static final String TAG = "EasyFragment";
    /**
     * 需要缓存的RootView;
     */
    private View mRootView;
    /**
     * view是否已经初始化
     */
    private boolean mHasInitView;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHasInitView = true;
        //防止在onViewCreate中初始化View，但是在onLazyLoad中需要使用此view导致空指针，故延迟执行
        EasyView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (canLazyLoad()) {
                    onLazyLoad();
                }
            }
        });
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


    protected void onLazyLoad() {
        
    }

    /**
     * @return true 视图已经初始化并对用户可见
     */
    private boolean canLazyLoad() {
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
        mHasInitView = false;
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
