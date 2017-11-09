package com.xcheng.permission;

import java.util.List;

/**
 * 请求结果回调监听
 */
public interface OnRequestCallback {
    /**
     * 所有权限被允许
     */
    void onAllowed();

    /**
     * @param rationalePermissions 有权限被拒绝，但是没有勾选不再提示
     */
    void onShowRationale(List<String> rationalePermissions);

    void onNeverAsked(List<String> neverAskedPermissions);
}
