package com.xcheng.view.controller.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.xcheng.view.EasyView;
import com.xcheng.view.R;
import com.xcheng.view.controller.EasyDialog;

public class LoadingDialog extends EasyDialog {
    private LoadingDialog(Context context, int defStyle) {
        super(context, defStyle);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public LoadingDialog(Context context) {
        this(context, R.style.ev_dialog_loadingStyle);
    }

    public static LoadingDialog getSimpleDialog(Context context, @LayoutRes final int layoutId) {
        return new LoadingDialog(context) {
            @Override
            public int getLayoutId() {
                return layoutId;
            }
        };
    }

    @Override
    public int getLayoutId() {
        return EasyView.getLoadingLayout();
    }
}
