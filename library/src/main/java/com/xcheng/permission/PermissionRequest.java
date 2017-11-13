package com.xcheng.permission;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * 权限请求类
 * Created by chengxin on 2017/11/9.
 */
final class PermissionRequest {
    final OnRequestCallback onRequestCallback;
    final String[] permissions;
    final int requestCode;

    private PermissionRequest(Builder builder) {
        this.onRequestCallback = builder.onRequestCallback;
        this.permissions = builder.permissions;
        this.requestCode = builder.requestCode;
    }

    public static class Builder {
        private int requestCode;
        private OnRequestCallback onRequestCallback;
        private String[] permissions;
        private EasyPermission easyPermission;

        Builder(@NonNull EasyPermission easyPermission) {
            this.easyPermission = easyPermission;
        }

        public Builder permissions(@NonNull String... permissions) {
            this.permissions = permissions;
            return this;
        }

        public void request(final @IntRange(from = 0) int requestCode, OnRequestCallback onRequestCallback) {
            //此处统一检测
            if (requestCode < 0) {
                throw new IllegalStateException("requestCode must be >=0");
            }
            if (onRequestCallback == null) {
                throw new NullPointerException("onRequestCallback==null");
            }
            if (permissions == null || permissions.length == 0) {
                throw new IllegalStateException("permissions must be not null or empty");
            }
            this.onRequestCallback = onRequestCallback;
            this.requestCode = requestCode;
            easyPermission.request(new PermissionRequest(this));
        }
    }
}
