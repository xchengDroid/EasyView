package com.xcheng.view.pullrefresh;

/**
 * 刷新加载组件的状态
 */
public enum LoadingState {

    INIT("上拉加载更多"/*可以刷新和加载更多*/),

    REFRESHING("正在刷新"/*无法再刷新或加载更多*/),

    LOADING_MORE("加载更多"/*无法再刷新或加载更多*/),

    NO_MORE("已全部加载"/*可以刷新*/);

    private final String text;

    LoadingState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}