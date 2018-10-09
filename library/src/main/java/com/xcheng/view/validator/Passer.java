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
    public final Valid valid;
    public final String label;
    public final int order;
    public final String key;
    public final int min;
    public final String fieldName;

    Passer(TextView textView, Valid valid, String fieldName) {
        this.textView = textView;
        this.valid = valid;
        this.fieldName = fieldName;
        this.order = valid.order();
        this.key = valid.key();
        this.min = valid.min();
        this.label = createLabel();
    }

    public String getText() {
        String text = textView.getText().toString();
        return valid.trim() ? text.trim() : text;
    }

    public boolean isEmpty() {
        return min > 0 && TextUtils.isEmpty(getText());
    }

    public boolean isLessThanMin() {
        return getText().length() < min;
    }

    private String createLabel() {
        int labelResId = valid.labelResId();
        String label = labelResId != -1 ?
                textView.getResources().getString(labelResId)
                : valid.label();
        if (TextUtils.isEmpty(label)) {
            label = fieldName;
        }
        return label;
    }
}
