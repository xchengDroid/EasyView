package com.xcheng.view.pullrefresh;

public enum LoadingState {
    INIT("上拉加载更多"), REFRESHING("正在刷新"), LOADINGMORE("加载更多"), NOMORE("已全部加载");
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