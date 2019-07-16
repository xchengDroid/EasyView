package com.xcheng.view.controller.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.xcheng.view.R;
import com.xcheng.view.util.LocalDisplay;

public abstract class BottomDialog extends EasyDialog {

    public BottomDialog(Context context) {
        super(context, R.style.ev_dialog_bottom_optionStyle/*默认样式*/);
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void initDialog(@Nullable Bundle savedInstanceState) {
        super.initDialog(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(LocalDisplay.WIDTH_PIXEL, WindowManager.LayoutParams.WRAP_CONTENT);
//        WindowManager m = getWindow().getWindowManager();
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        p.width = d.getWidth();
//        getWindow().setAttributes(p);
    }
}
