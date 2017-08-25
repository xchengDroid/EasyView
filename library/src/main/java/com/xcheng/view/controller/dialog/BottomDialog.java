package com.xcheng.view.controller.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.xcheng.view.R;
import com.xcheng.view.controller.EasyDialog;
import com.xcheng.view.util.LocalDisplay;

public abstract class BottomDialog extends EasyDialog {

    public BottomDialog(Context context) {
        super(context, R.style.ev_dialog_bottom_optionStyle/*默认样式*/);
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void initLocation() {
        super.initLocation();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(LocalDisplay.widthPixel(), WindowManager.LayoutParams.WRAP_CONTENT);
//        WindowManager m = getWindow().getWindowManager();
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        p.width = d.getWidth();
//        getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
