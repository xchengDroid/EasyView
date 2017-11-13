package com.xcheng.permission;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class PermissionFragment extends Fragment {
    // Contains all the current permission requests.
    // Once granted or denied, they are removed from it.
    //如果在发起请求时，Activity重启，之前的请求回调会丢失这是采用这种调用方法无法避免的，用户重新手动点击请求就OK了。
    //除非手动在onRequestPermissionsResult的回调方法里处理，但是那样整个请求流程会很麻烦，而且必须依赖在Activity和Fragment内部处理
    private final SparseArray<PermissionRequest> mPermissionRequests = new SparseArray<>();

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull PermissionRequest permissionRequest) {
        final int requestCode = permissionRequest.requestCode;
        int index = mPermissionRequests.indexOfKey(requestCode);
        if (index >= 0) {
            Log.e(EasyPermission.TAG, "EasyPermission have a same requestCode " + requestCode + " that not deal");
            return;
        }
        mPermissionRequests.put(requestCode, permissionRequest);
        List<String> denyPermissions = EasyPermission.findDeniedPermissions(getContext(), permissionRequest.permissions);
        requestPermissions(EasyPermission.toArray(denyPermissions), requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        final PermissionRequest permissionRequest = mPermissionRequests.get(requestCode);
        if (permissionRequest == null) {
            // No PermissionRequest found
            Log.e(EasyPermission.TAG, "EasyPermission.onRequestPermissionsResult invoked but didn't find the corresponding permission request.");
            return;
        }
        mPermissionRequests.remove(requestCode);
        //just in case
        if (permissions.length == 0) {
            permissionRequest.onRequestCallback.onRefused(new DeniedResult(new ArrayList<String>(), new ArrayList<String>()));
            return;
        }

        List<String> deniedPerms = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPerms.add(permissions[i]);
            }
        }
        if (deniedPerms.isEmpty()) {
            permissionRequest.onRequestCallback.onAllowed();
            return;
        }

        List<String> showRationalePerms = EasyPermission.findRationalePermissions(getActivity(), EasyPermission.toArray(deniedPerms));
        permissionRequest.onRequestCallback.onRefused(new DeniedResult(deniedPerms, showRationalePerms));
    }
}
