package com.xcheng.view.controller;

import android.support.annotation.UiThread;

import com.xcheng.view.pullrefresh.UIState;

import java.util.List;

/**
 * T 代表http请求返回的数据
 * Created by cx on 2016/10/13.
 */
public interface IPullRefreshView<T> {
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
     * 加载数据结束之后复位UI
     *
     * @param isRefresh 是否为刷新
     * @param state     加载状态
     */
    @UiThread
    void complete(boolean isRefresh, UIState state);
}
