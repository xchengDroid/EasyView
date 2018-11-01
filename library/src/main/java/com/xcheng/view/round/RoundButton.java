package com.xcheng.view.round;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * 创建时间：2018/7/20
 * 编写人： chengxin
 * 功能描述：
 */
public class RoundButton extends AppCompatButton {

    public RoundButton(Context context) {
        this(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        RoundDrawableHelper roundDrawableHelper = new RoundDrawableHelper(this);
        roundDrawableHelper.loadAttributeSet(context, attrs, 0);
        roundDrawableHelper.setRoundDrawable();
    }
}
