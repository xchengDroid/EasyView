package com.xcheng.view.round;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 创建时间：2018/7/20
 * 编写人： chengxin
 * 功能描述：圆角EditText
 */
public class RoundEditText extends AppCompatEditText {

    public RoundEditText(Context context) {
        this(context, null);
    }

    public RoundEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        RoundDrawableHelper roundDrawableHelper = new RoundDrawableHelper(this);
        roundDrawableHelper.loadAttributeSet(context, attrs, 0);
        roundDrawableHelper.setRoundDrawable();
    }
}
