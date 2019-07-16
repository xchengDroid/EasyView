package com.xcheng.view.round;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 创建时间：2018/7/20
 * 编写人： chengxin
 * 功能描述：圆角TextView
 */
public class RoundTextView extends AppCompatTextView {

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RoundDrawableHelper roundDrawableHelper = new RoundDrawableHelper(this);
        roundDrawableHelper.loadAttributeSet(context, attrs, 0);
        roundDrawableHelper.setRoundDrawable();
    }
}
