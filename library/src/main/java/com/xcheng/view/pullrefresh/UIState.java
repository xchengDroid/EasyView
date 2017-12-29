package com.xcheng.view.pullrefresh;

/**
 * 刷新加载组件的状态
 */
public enum UIState {

    INIT("上拉加载更多", true, true),

    REFRESHING("正在刷新", false, false),

    LOADING_MORE("加载更多", false, false),

    NO_MORE("已全部加载", true, false);

    private final String text;
    private final boolean canRefresh;
    private final boolean canLoadMore;

    UIState(String text, boolean canRefresh, boolean canLoadMore) {
        this.text = text;
        this.canRefresh = canRefresh;
        this.canLoadMore = canLoadMore;

    }

    public String getText() {
        return text;
    }

    /**
     * 能否刷新
     */
    public boolean canRefresh() {
        return canRefresh;
    }

    /**
     * 能否加载更多
     */
    public boolean canLoadMore() {
        return canLoadMore;
    }
}