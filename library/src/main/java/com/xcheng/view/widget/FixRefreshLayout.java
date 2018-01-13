package com.xcheng.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 修复{@link SmartRefreshLayout 的一些bug和参数的适配类}
 * Created by chengxin on 2018/1/8.
 */
public class FixRefreshLayout extends SmartRefreshLayout {
    public FixRefreshLayout(Context context) {
        this(context, null);
    }

    public FixRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (reboundAnimator != null) {
            reboundAnimator.removeAllUpdateListeners();
            reboundAnimator.removeAllListeners();
            reboundAnimator.cancel();
            reboundAnimator = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * @param spinner    竖直方向偏移
     * @param isAnimator true 是否为动画回弹 false 为{@link #dispatchTouchEvent(MotionEvent)}
     *                   {@link MotionEvent#ACTION_CANCEL} 的touch事件回弹
     */
    @Override
    protected void moveSpinner(int spinner, boolean isAnimator) {
        super.moveSpinner(spinner, isAnimator);
    }
}
