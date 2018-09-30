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
        String text = textView.getText().toString();
        return val.trim() ? text.trim() : text;
    }

    public boolean isEmpty() {
        return min() > 0 && TextUtils.isEmpty(getText());
    }

    public boolean isLessThanMin() {
        return getText().length() < min();
    }

    public String label() {
        int labelResId = val.labelResId();
        return labelResId != -1 ?
                textView.getResources().getString(val.labelResId())
                : val.label();
    }

    public String key() {
        return val.key();
    }

    public int min() {
        return val.min();
    }

    public int order() {
        return val.order();
    }
}
