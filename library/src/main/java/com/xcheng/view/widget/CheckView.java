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
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.xcheng.view.R;
import com.xcheng.view.util.LocalDisplay;

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
    private boolean mChecked = true;
    private Paint mStrokePaint;
    private Paint mBackgroundPaint;
    private Drawable mCheckDrawable;
    private Rect mCheckRect;
    private boolean mEnabled = true;

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
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.CheckView_ev_strokeWidth, LocalDisplay.dp2px(2));
        mStrokeColor = ta.getColor(R.styleable.CheckView_ev_strokeColor, Color.GRAY);
        mbgColor = ta.getColor(R.styleable.CheckView_ev_bgColor, 0);
        mCheckedColor = ta.getColor(R.styleable.CheckView_ev_checkedColor, Color.GREEN);
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize + mStrokeWidth * 2 + getPaddingLeft() + getPaddingRight(), mSize + mStrokeWidth * 2 + getPaddingTop() + getPaddingBottom());
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


    public void setEnabled(boolean enabled) {
        if (mEnabled != enabled) {
            mEnabled = enabled;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
            mBackgroundPaint.setColor(mCheckedColor);
            canvas.drawCircle(getPaddingLeft() + mStrokeWidth + mSize / 2,
                    getPaddingTop() + mStrokeWidth + mSize / 2,
                    (mStrokeWidth + mSize) / 2, mBackgroundPaint);

            mCheckDrawable.setBounds(getCheckRect());
            mCheckDrawable.draw(canvas);
        } else {
            // draw white stroke
            canvas.drawCircle(getPaddingLeft() + mStrokeWidth + mSize / 2,
                    getPaddingTop() + mStrokeWidth + mSize / 2,
                    (mStrokeWidth + mSize) / 2, mStrokePaint);

            mBackgroundPaint.setColor(mbgColor);
            canvas.drawCircle(getPaddingLeft() + mStrokeWidth + mSize / 2,
                    getPaddingTop() + mStrokeWidth + mSize / 2,
                    mSize / 2, mBackgroundPaint);

        }
        // enable hint
        setAlpha(mEnabled ? 1.0f : 0.5f);
    }


    // rect for drawing checked number or mark
    private Rect getCheckRect() {
        if (mCheckRect == null) {
            mCheckRect = new Rect(getPaddingLeft(), getPaddingTop(),
                    mSize + 2 * mStrokeWidth, mSize + 2 * mStrokeWidth);
        }
        return mCheckRect;
    }
}
