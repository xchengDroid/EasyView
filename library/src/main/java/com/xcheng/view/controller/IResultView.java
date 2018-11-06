package com.xcheng.view.controller;

/**
 * 创建时间：2018/11/6
 * 编写人： chengxin
 * 功能描述：回调数据的接口,处理返回的数据
 */
public interface IResultView<E> {

    void onGetResult(Object result, String tag);

    void onLoseResult(E error, String tag);
}
