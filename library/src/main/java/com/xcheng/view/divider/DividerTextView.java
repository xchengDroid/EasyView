package com.xcheng.view.divider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 带分割线的的TextView
 * Created by chengxin on 2017/8/29.
 */
@SuppressLint("AppCompatCustomView")
public class DividerTextView extends TextView implements IDividerView {
    private DividerHelper mDividerHelper;

    public DividerTextView(Context context) {
        this(context, null);
    }

    public DividerTextView(Context context, AttributeSet attrs) {
        //必须传递android.R.attr.textStyle，否则没有默认的样式掉
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public DividerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
