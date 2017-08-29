package com.xcheng.view.divider;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 带分割线的的LinearLayout
 * Created by chengxin on 2017/8/29.
 */
public class DividerLayout extends LinearLayout implements IDividerView {
    private DividerHelper mDividerHelper;

    public DividerLayout(Context context) {
        this(context, null);
    }

    public DividerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DividerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDividerHelper = new DividerHelper(this);
        mDividerHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mDividerHelper.drawDivider(canvas);
    }

    @Override
    public void setTopToLeft(int topToLeft) {
        mDividerHelper.setTopToLeft(topToLeft);
    }

    @Override
    public void setTopToRight(int topToRight) {
        mDividerHelper.setTopToRight(topToRight);
    }

    @Override
    public void setBottomToLeft(int bottomToLeft) {
        mDividerHelper.setBottomToLeft(bottomToLeft);
    }

    @Override
    public void setBottomToRight(int bottomToRight) {
        mDividerHelper.setBottomToRight(bottomToRight);
    }

    @Override
    public void setTopHeight(float topHeight) {
        mDividerHelper.setTopHeight(topHeight);
    }

    @Override
    public void setBottomHeight(float bottomHeight) {
        mDividerHelper.setBottomHeight(bottomHeight);
    }

    @Override
    public void setTopColor(int topColor) {
        mDividerHelper.setTopColor(topColor);
    }

    @Override
    public void setBottomColor(int bottomColor) {
        mDividerHelper.setBottomColor(bottomColor);
    }

    @Override
    public void setPosition(int position) {
        mDividerHelper.setPosition(position);
    }
}
