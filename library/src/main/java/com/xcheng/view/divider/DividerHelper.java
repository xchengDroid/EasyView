package com.xcheng.view.divider;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.R;

/**
 * 表单View解析帮助类
 * Created by chengxin on 2017/7/29.
 */
public class DividerHelper {
    private Paint mPaint;
    public static final int DIVIDER_NONE = 0x0;
    public static final int DIVIDER_TOP = 0x1;//是否绘制上面
    public static final int DIVIDER_BOTTOM = 0x2;//是否绘制下面

    private int position;
    private int topToLeft;
    private int topToRight;
    private int bottomToLeft;
    private int bottomToRight;

    private View dividerView;

    private float topHeight;
    private float bottomHeight;

    private int topColor;
    private int bottomColor;

    public DividerHelper(View dividerView) {
        if (dividerView instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) dividerView;
            vp.setWillNotDraw(false);
        }
        this.dividerView = dividerView;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @CallSuper
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = dividerView.getContext().obtainStyledAttributes(attrs, R.styleable.DividerView, defStyleAttr, 0);
        int defaultDividerColor = ContextCompat.getColor(dividerView.getContext(), R.color.ev_divider_color);
        position = typedArray.getInt(R.styleable.DividerView_ev_position, DIVIDER_NONE);
        topToLeft = typedArray.getDimensionPixelSize(R.styleable.DividerView_ev_topToLeft, 0);
        topToRight = typedArray.getDimensionPixelSize(R.styleable.DividerView_ev_topToRight, 0);
        topColor = typedArray.getColor(R.styleable.DividerView_ev_topColor, defaultDividerColor);
        topHeight = typedArray.getDimensionPixelSize(R.styleable.DividerView_ev_topHeight, 1);

        bottomToLeft = typedArray.getDimensionPixelSize(R.styleable.DividerView_ev_bottomToLeft, 0);
        bottomToRight = typedArray.getDimensionPixelSize(R.styleable.DividerView_ev_bottomToRight, 0);
        bottomColor = typedArray.getColor(R.styleable.DividerView_ev_bottomColor, defaultDividerColor);
        bottomHeight = typedArray.getDimensionPixelSize(R.styleable.DividerView_ev_bottomHeight, 1);
        typedArray.recycle();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void drawTop(Canvas canvas) {
        if ((position & DIVIDER_TOP) == DIVIDER_TOP) {
            mPaint.setColor(topColor);
            mPaint.setStrokeWidth(topHeight);
            canvas.drawLine(topToLeft, topHeight / 2, dividerView.getWidth() - topToRight, topHeight / 2, mPaint);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void drawBottom(Canvas canvas) {
        if ((position & DIVIDER_BOTTOM) == DIVIDER_BOTTOM) {
            mPaint.setColor(bottomColor);
            mPaint.setStrokeWidth(bottomHeight);
            canvas.drawLine(bottomToLeft, dividerView.getHeight() - bottomHeight / 2, dividerView.getWidth() - bottomToRight, dividerView.getHeight() - bottomHeight / 2, mPaint);
        }
    }

    /**
     * called this method in {@link View#draw(Canvas)}
     *
     * @param canvas
     */
    public void drawDivider(Canvas canvas) {
        drawTop(canvas);
        drawBottom(canvas);
    }

    public void setTopToLeft(int topToLeft) {
        this.topToLeft = topToLeft;
        dividerView.invalidate();
    }


    public void setTopToRight(int topToRight) {
        this.topToRight = topToRight;
        dividerView.invalidate();
    }


    public void setBottomToLeft(int bottomToLeft) {
        this.bottomToLeft = bottomToLeft;
        dividerView.invalidate();
    }


    public void setBottomToRight(int bottomToRight) {
        this.bottomToRight = bottomToRight;
        dividerView.invalidate();
    }


    public void setTopHeight(float topHeight) {
        this.topHeight = topHeight;
        dividerView.invalidate();
    }

    public void setBottomHeight(float bottomHeight) {
        this.bottomHeight = bottomHeight;
        dividerView.invalidate();
    }


    public void setTopColor(int topColor) {
        this.topColor = topColor;
        dividerView.invalidate();
    }


    public void setBottomColor(int bottomColor) {
        this.bottomColor = bottomColor;
        dividerView.invalidate();
    }


    public void setPosition(int position) {
        this.position = position;
        dividerView.invalidate();
    }
}
