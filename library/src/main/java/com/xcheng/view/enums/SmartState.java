package com.xcheng.view.enums;

/**
 * 刷新组件的状态
 */
public enum SmartState {

    NONE("初始状态"),
    REFRESHING("正在刷新"),
    LOADING_MORE("加载更多"),
    REFRESH_FINISHED("刷新结束"),
    LOADING_FINISHED("加载更多结束");

    private final String text;

    SmartState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}