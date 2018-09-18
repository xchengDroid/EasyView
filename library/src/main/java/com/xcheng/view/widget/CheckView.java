package com.xcheng.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.xcheng.view.R;
import com.xcheng.view.util.LocalDisplay;

import static com.xcheng.view.widget.ProgressView.TYPE_CIRCLE;

public class CheckView extends View implements Checkable {
    //圆框的颜色
    private int mStrokeWidth; // dp
    //圆框的尺寸
    private int mSize; // dp
    //圆框的颜色
    private int mStrokeColor;
    //圆框内填充的颜色
    private int mbgColor;
    //选中的颜色
    private int mCheckedColor;
    private boolean mChecked;
    private Paint mStrokePaint;
    private Paint mBackgroundPaint;
    private Drawable mCheckDrawable;
    private Rect mCheckRect;
    private Rect mBgRect;
    private Rect mStrokeRect;

    private int mType;

    public CheckView(Context context) {
        this(context, null);
    }

    public CheckView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheckView, defStyleAttr, 0);
        mSize = ta.getDimensionPixelSize(R.styleable.CheckView_ev_size, LocalDisplay.dp2px(18));
        mType = ta.getInt(R.styleable.CheckView_ev_type, TYPE_CIRCLE);

        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.CheckView_ev_strokeWidth, LocalDisplay.dp2px(2));
        mStrokeColor = ta.getColor(R.styleable.CheckView_ev_strokeColor, Color.parseColor("#c2c9cc"));
        mbgColor = ta.getColor(R.styleable.CheckView_ev_bgColor, Color.TRANSPARENT);
        mCheckedColor = ta.getColor(R.styleable.CheckView_ev_checkedColor, Color.parseColor("#0bd38a"));
        mChecked = ta.getBoolean(R.styleable.CheckView_android_checked, false);
        ta.recycle();

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setColor(mStrokeColor);
        mCheckDrawable = ResourcesCompat.getDrawable(context.getResources(),
                R.drawable.ic_check_white_18dp, context.getTheme());

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mCheckRect = getSquareRect(0);
        mBgRect = getSquareRect(mStrokeWidth);
        mStrokeRect = getSquareRect(mStrokeWidth / 2);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize + mStrokeWidth * 2 + getPaddingLeft() + getPaddingRight(),
                mSize + mStrokeWidth * 2 + getPaddingTop() + getPaddingBottom());
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
            mBackgroundPaint.setColor(mCheckedColor);
            if (mType == ProgressView.TYPE_CIRCLE) {
                canvas.drawCircle(getPaddingLeft() + mStrokeWidth + mSize / 2,
                        getPaddingTop() + mStrokeWidth + mSize / 2,
                        mStrokeWidth + mSize / 2, mBackgroundPaint);
            } else {
                canvas.drawRect(mBgRect, mBackgroundPaint);
            }
            mCheckDrawable.setBounds(mCheckRect);
            mCheckDrawable.draw(canvas);
        } else {
            mBackgroundPaint.setColor(mbgColor);
            if (mType == ProgressView.TYPE_CIRCLE) {
                //渲染这个圆形背景
                canvas.drawCircle(getPaddingLeft() + mStrokeWidth + mSize / 2,
                        getPaddingTop() + mStrokeWidth + mSize / 2,
                        mSize / 2 + mStrokeWidth, mBackgroundPaint);
                //渲染这个圆形边框
                canvas.drawCircle(getPaddingLeft() + mStrokeWidth + mSize / 2,
                        getPaddingTop() + mStrokeWidth + mSize / 2,
                        (mStrokeWidth + mSize) / 2/*Paint.Style.STROKE的情况下画笔在StrokeWidth中间*/, mStrokePaint);
            } else {
                //渲染这个方形背景
                canvas.drawRect(mBgRect, mBackgroundPaint);
                //渲染这个方形边框
                canvas.drawRect(mStrokeRect, mStrokePaint);
            }
        }
        // enable hint
        setAlpha(isEnabled() ? 1.0f : 0.5f);
    }


    private Rect getSquareRect(@IntRange(from = 0) int offset) {
        int centerX = getPaddingLeft() + mStrokeWidth + mSize / 2;
        int centerY = getPaddingTop() + mStrokeWidth + mSize / 2;
        return new Rect(centerX - mSize / 2 - offset, centerY - mSize / 2 - offset,
                centerX + mSize / 2 + offset, centerY + mSize / 2 + offset);
    }
}
