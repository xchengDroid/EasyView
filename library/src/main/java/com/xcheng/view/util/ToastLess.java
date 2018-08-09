package com.xcheng.view.util;

import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xcheng.view.EasyView;
import com.xcheng.view.R;

/**
 * 简化Toast的工具类,支持子线程显示Toast
 */
public final class ToastLess {
    private static Toast sToast;

    @UiThread
    private static void show(CharSequence text, boolean isLong) {
        if (sToast == null) {
            sToast = EasyView.getConfig().factory().createGlobalToast(EasyView.getContext());
        }
        if (text != null) {
            sToast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
            View nextView = sToast.getView();
            TextView textView = nextView.findViewById(android.R.id.message);
            if (textView == null) {
                //支持外部设置View id必须为android.R.id.message 或者 R.id.toast_text
                textView = nextView.findViewById(R.id.toast_text);
            }
            if (textView == null) {
                throw new RuntimeException("This Toast was not created with Toast.makeText()");
            }
            textView.setText(text);
            sToast.show();
        }
    }

    public static void showToast(CharSequence text) {
        showToast(text, false);
    }

    public static void showToast(final CharSequence text, final boolean isLong) {
        if (EasyView.isOnMainThread()) {
            show(text, isLong);
        } else {
            EasyView.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    show(text, isLong);
                }
            });
        }
    }

    public static void showToast(@StringRes int stringResId) {
        showToast(stringResId, false);
    }

    public static void showToast(@StringRes int stringResId, boolean isLong) {
        if (stringResId != 0) {
            showToast(EasyView.getText(stringResId), isLong);
        }
    }
}
