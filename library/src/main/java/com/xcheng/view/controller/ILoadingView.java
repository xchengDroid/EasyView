package com.xcheng.view.controller;

import android.support.annotation.StringRes;

/**
 * 显示加载提示等
 *
 * @author xincheng
 */
public interface ILoadingView {
    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示消息提醒等
     *
     * @param text 消息内容
     */
    void showMessage(CharSequence text);

    void showMessage(@StringRes int stringId);
}
