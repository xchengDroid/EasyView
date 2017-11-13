package com.simple.view;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xcheng.permission.DeniedResult;
import com.xcheng.permission.EasyPermission;
import com.xcheng.permission.OnRequestCallback;
import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.util.ToastLess;


public class PermissionActivity extends EasyActivity {

    @Override
    public int getLayoutId() {
        return R.layout.ac_permission;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        findViewById(R.id.singleRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPermission.with(PermissionActivity.this)
                        .permissions(Manifest.permission.CAMERA)
                        .request(12, new OnRequestCallback() {
                            @Override
                            public void onAllowed() {
                                // request allowed
                                ToastLess.showToast("CAMERA  被允许");

                            }

                            @Override
                            public void onRefused(DeniedResult deniedResult) {
                                ToastLess.showToast(deniedResult.deniedPerms + " 被禁止");
                                if (deniedResult.allNeverAsked) {

                                } else {

                                }
                            }
                        });
            }
        });
        findViewById(R.id.multiRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPermission.with(PermissionActivity.this)
                        .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request(12, new OnRequestCallback() {
                            @Override
                            public void onAllowed() {
                                // request allowed
                                ToastLess.showToast("CAMERA WRITE_EXTERNAL_STORAGE 被允许");
                            }

                            @Override
                            public void onRefused(DeniedResult deniedResult) {
                                ToastLess.showToast(deniedResult.deniedPerms + " 被禁止");

                                if (deniedResult.allNeverAsked) {
                                } else {

                                }
                            }
                        });

            }
        });

    }
}
