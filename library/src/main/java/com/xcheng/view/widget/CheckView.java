package com.xcheng.view.widget;

import android.content.Context;
import android.graphics.Canvas;
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

public class CheckView extends View implements Checkable {
    private static final float STROKE_WIDTH = 2.0f; // dp
    private static final int SIZE = 32; // dp
    private static final float STROKE_RADIUS = 10.5f; // dp
    private static final float BG_RADIUS = 10f; // dp
    private static final int CONTENT_SIZE = 16; // dp
    private boolean mChecked;
    private Paint mStrokePaint;
    private Paint mBackgroundPaint;
    private Drawable mCheckDrawable;
    private Rect mCheckRect;
    private boolean mEnabled = true;

    public CheckView(Context context) {
        super(context);
        init(context);
    }

    public CheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // fixed size 48dp x 48dp
        int sizeSpec = MeasureSpec.makeMeasureSpec(SIZE, MeasureSpec.EXACTLY);
        super.onMeasure(sizeSpec, sizeSpec);

    }

    private void init(Context context) {
        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mStrokePaint.setStrokeWidth(STROKE_WIDTH);
        int color = 1;//ta.getColor(0, defaultColor);
        mStrokePaint.setColor(color);

        mCheckDrawable = ResourcesCompat.getDrawable(context.getResources(),
                R.drawable.ic_check_white_18dp, context.getTheme());
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

        // draw white stroke
        canvas.drawCircle((float) SIZE / 2, (float) SIZE / 2,
                STROKE_RADIUS, mStrokePaint);
        if (mChecked) {
            initBackgroundPaint();
            canvas.drawCircle((float) SIZE / 2, (float) SIZE / 2,
                    BG_RADIUS, mBackgroundPaint);

            mCheckDrawable.setBounds(getCheckRect());
            mCheckDrawable.draw(canvas);
        }
        // enable hint
        setAlpha(mEnabled ? 1.0f : 0.5f);
    }

    private void initBackgroundPaint() {
        if (mBackgroundPaint == null) {
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setAntiAlias(true);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            int color = 1;// ta.getColor(0, defaultColor);
            mBackgroundPaint.setColor(color);
        }
    }

    // rect for drawing checked number or mark
    private Rect getCheckRect() {
        if (mCheckRect == null) {
            int rectPadding = (SIZE / 2 - CONTENT_SIZE / 2);
            mCheckRect = new Rect(rectPadding, rectPadding,
                    (SIZE - rectPadding), (SIZE - rectPadding));
        }

        return mCheckRect;
    }
}
