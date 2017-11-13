package com.xcheng.permission;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 被拒绝的权限封装类
 * Created by chengxin on 2017/11/10.
 */
public final class DeniedResult {
    // 所有被拒绝的权限
    public final List<String> deniedPerms;
    // 被拒绝但未勾选不再询问的权限
    public final List<String> showRationalePerms;
    // 被拒绝且勾选不再询问的权限
    public final List<String> neverAskedPerms;
    // 是否全部权限都是被拒绝且不再询问
    public final boolean allNeverAsked;

    DeniedResult(@NonNull List<String> deniedPerms, @NonNull List<String> showRationalePerms) {
        this.deniedPerms = deniedPerms;
        this.showRationalePerms = showRationalePerms;
        this.allNeverAsked = showRationalePerms.isEmpty();
        //处理被完全拒绝的权限
        neverAskedPerms = new ArrayList<>();
        for (String permission : deniedPerms) {
            //rationales 不包含 deniedPermissions 的就是完全被拒绝的
            if (!showRationalePerms.contains(permission)) {
                neverAskedPerms.add(permission);
            }
        }
    }
}