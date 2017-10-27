package com.xcheng.view.callback;

import android.text.Editable;

/**
 * Created by ddq on 2016/11/7.
 */
public class NumberTextWatcher extends SimpleTextWatcher {
    private int integer;//整数部分位数
    private int decimal;//小数部分位数

    public NumberTextWatcher(int integer, int decimal) {
        this.integer = integer;
        this.decimal = decimal;
        if (integer < 0)
            this.integer = 0;
        if (decimal < 0)
            this.decimal = 0;
    }

    public NumberTextWatcher() {
        this(9, 3);
    }

    @Override
    public void afterTextChanged(Editable s) {
        int index = s.toString().indexOf(".");
        if (index > -1) {
            if (index == 0) {
                s.insert(0, "0");
            }

            if (index < s.length() - 1) {
                CharSequence sub = s.subSequence(index + 1, s.length());
                if (sub.length() > decimal) {
                    s.delete(index + decimal + 1, s.length());
                }
            }

            if (index > integer) {
                s.delete(integer, index);
            }
        } else {
            if (s.length() > integer) {
                s.delete(integer, s.length());
            }
        }
    }
}
