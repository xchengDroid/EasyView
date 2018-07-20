package com.xcheng.view.round;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.xcheng.view.R;

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
        this(context, attrs, R.attr.EVRoundButtonStyle);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RoundDrawableHelper roundDrawableHelper = new RoundDrawableHelper(this);
        roundDrawableHelper.loadAttributeSet(context, attrs, defStyleAttr);
        roundDrawableHelper.setRoundDrawable();
    }
}
