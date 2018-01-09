package com.xcheng.view.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xcheng.view.EasyView;

/**
 * 懒加载Fragment,当对用用户可见的时候调用
 */
public abstract class LazyLoadFragment extends EasyFragment {
    /**
     * view是否已经初始化
     */
    private boolean mHasInitView;

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

    protected abstract void onLazyLoad();

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
    }
}
