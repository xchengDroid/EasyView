package com.xcheng.view.round;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.xcheng.view.R;

/**
 * 创建时间：2018/7/20
 * 编写人： chengxin
 * 功能描述：圆角FrameLayout
 */
public class RoundFrameLayout extends FrameLayout {

    public RoundFrameLayout(Context context) {
        this(context, null);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EVRoundButtonStyle);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RoundDrawableHelper roundDrawableHelper = new RoundDrawableHelper(this);
        roundDrawableHelper.loadAttributeSet(context, attrs, defStyleAttr);
        roundDrawableHelper.setRoundDrawable();
    }
}
