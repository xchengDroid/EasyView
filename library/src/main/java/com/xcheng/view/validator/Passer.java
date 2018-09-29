package com.xcheng.view.validator;

import android.widget.TextView;

/**
 * 创建时间：2018/9/29
 * 编写人： chengxin
 * 功能描述：
 */
public class Passer {
    public final TextView textView;
    public final Val val;

    public Passer(TextView textView, Val val) {
        this.textView = textView;
        this.val = val;
    }

    public String getText() {
        return textView.getText().toString();
    }

    public int getInt() {
        return Integer.parseInt(getText());
    }
}
