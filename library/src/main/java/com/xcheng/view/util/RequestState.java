package com.xcheng.view.util;


public enum RequestState {
    LOADING("正在加载..."),
    SUCCESS("加载成功"),
    FAILED("加载失败"),
    NO_DATA("暂无数据"),
    SESSION_TIME_OUT("会话过期,请重新登录"),
    TIME_OUT("请求超时"),
    NETWORK_ERROR("网络异常"),
    SERVER_ERROR("服务异常"),
    AUTH_FAILED_ERROR("验证出错");
    private String text;

    RequestState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}