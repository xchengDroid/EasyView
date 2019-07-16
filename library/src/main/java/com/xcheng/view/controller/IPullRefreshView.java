package com.xcheng.view.controller;

import androidx.annotation.UiThread;

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
    void refreshView(boolean isRefresh, List<? extends T> data);

    /**
     * @param isRefresh  是否为刷新
     * @param success    是否成功
     * @param noMoreData 是否已全部加在
     */
    void complete(boolean isRefresh, boolean success, boolean noMoreData);
}
