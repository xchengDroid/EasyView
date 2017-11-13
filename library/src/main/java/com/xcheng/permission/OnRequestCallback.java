package com.xcheng.permission;

/**
 * 请求结果回调监听
 */
public interface OnRequestCallback {
    /**
     * 所有权限被允许
     */
    void onAllowed();

    /**
     * 申请失败，有权限被拒绝
     */
    void onRefused(DeniedResult deniedResult);
}
