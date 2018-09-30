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
    private String label;
    private final String fieldName;

    public Passer(TextView textView, Val val, String fieldName) {
        this.textView = textView;
        this.val = val;
        this.fieldName = fieldName;
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

    //lazy
    public String label() {
        if (label == null) {
            int labelResId = val.labelResId();
            label = labelResId != -1 ?
                    textView.getResources().getString(labelResId)
                    : val.label();
            if (TextUtils.isEmpty(label)) {
                label = fieldName;
            }
        }
        return label;
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
