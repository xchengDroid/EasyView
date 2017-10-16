package com.xcheng.view.controller;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;

import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.pullrefresh.LoadingState;

import java.util.List;

/**
 * T 代表http请求返回的数据
 * Created by cx on 2016/10/13.
 */
public interface IPullRefreshView<T> extends EasyAdapter.OnBindHolderListener {
    /**
     * Http 请求
     *
     * @param isRefresh 是否为刷新
     */
    @UiThread
    void requestData(boolean isRefresh);

    @UiThread
    void refreshView(boolean isRefresh, List<T> data);

    /**
     * 是否自动刷新
     *
     * @return true 自动刷新,否则false
     */
    boolean isAutoRefresh();

    /**
     * 加载数据结束之后复位UI
     *
     * @param isRefresh 是否为刷新
     * @param state     加载状态
     */
    @UiThread
    void complete(boolean isRefresh, LoadingState state);


    /**
     * 获取HeaderView ,如果为0不设置
     */
    @LayoutRes
    int getHeaderId();

    /**
     * 获取EmptyView 如果为0不设置
     */
    @LayoutRes
    int getEmptyId();

    /**
     * 获取FooterView,如果为0不设置
     */
    @LayoutRes
    int getFooterId();

    @NonNull
    EasyAdapter<T> getEasyAdapter();

    /**
     * 每页的长度
     *
     * @return limit
     */
    @IntRange(from = 1)
    int getLimit();

    /**
     * for recyclerView
     */
    @NonNull
    RecyclerView.LayoutManager getLayoutManager();

    /**
     * 为null的情况下 不设置ItemDecoration
     *
     * @return ItemDecoration
     */
    @Nullable
    RecyclerView.ItemDecoration getItemDecoration();

    /**
     * 为null的情况下 没有ItemAnimator
     *
     * @return ItemAnimator
     */
    @Nullable
    RecyclerView.ItemAnimator getItemAnimator();
}
