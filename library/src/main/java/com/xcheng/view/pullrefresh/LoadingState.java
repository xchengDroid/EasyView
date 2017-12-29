package com.xcheng.view.pullrefresh;

/**
 * 刷新加载组件的状态
 */
public enum LoadingState {
    //可以刷新和加载更多
    INIT("上拉加载更多"),
    //刷新和加载更多有某一项正在进行
    REFRESHING("正在刷新"),
    LOADING_MORE("加载更多"),
    //可以刷新
    NO_MORE("已全部加载");
    private String text;

    LoadingState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}