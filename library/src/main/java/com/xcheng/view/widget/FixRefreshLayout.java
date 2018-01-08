package com.xcheng.view.widget;

import android.content.Context;
import android.util.AttributeSet;

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
        super.onDetachedFromWindow();
        if (reboundAnimator != null) {
            reboundAnimator.removeAllUpdateListeners();
            reboundAnimator.removeAllListeners();
            reboundAnimator.cancel();
            reboundAnimator = null;
        }
    }
}
