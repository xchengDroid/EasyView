package com.xcheng.view.validator;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * 创建时间：2018/9/29
 * 编写人： chengxin
 * 功能描述： 保存View和注解
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
    
    public boolean isEmpty() {
        return TextUtils.isEmpty(getText());
    }

    public boolean isLessThanMin() {
        return getText().length() < val.min();
    }
}
