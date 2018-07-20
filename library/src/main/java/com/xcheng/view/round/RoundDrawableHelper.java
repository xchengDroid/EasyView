package com.xcheng.view.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.View;

import com.xcheng.view.R;
import com.xcheng.view.util.ColorUtil;

public class RoundDrawableHelper {
    private final View mView;
    private int mFillColor;
    private int mStrokeColor;
    private int mStrokeWidth;
    private float[] mRadii;
    private boolean mRadiusAdjustBounds;
    private boolean mHasState;

    public RoundDrawableHelper(View view) {
        this.mView = view;
    }

    /**
     * 先调用此方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public void loadAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundButton, defStyleAttr, 0);
        mFillColor = typedArray.getColor(R.styleable.RoundButton_ev_fillColor, 0);
        mStrokeColor = typedArray.getColor(R.styleable.RoundButton_ev_strokeColor, 0);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_strokeWidth, 0);
        mRadiusAdjustBounds = typedArray.getBoolean(R.styleable.RoundButton_ev_isRadiusAdjustBounds, true);
        mHasState = typedArray.getBoolean(R.styleable.RoundButton_ev_isRadiusAdjustBounds, mView.isClickable());
        int mRadius = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radius, 0);
        int mRadiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radiusTopLeft, 0);
        int mRadiusTopRight = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radiusTopRight, 0);
        int mRadiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radiusBottomLeft, 0);
        int mRadiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radiusBottomRight, 0);
        typedArray.recycle();
        if (mRadiusTopLeft > 0 || mRadiusTopRight > 0 || mRadiusBottomLeft > 0 || mRadiusBottomRight > 0) {
            mRadii = new float[]{
                    mRadiusTopLeft, mRadiusTopLeft,
                    mRadiusTopRight, mRadiusTopRight,
                    mRadiusBottomRight, mRadiusBottomRight,
                    mRadiusBottomLeft, mRadiusBottomLeft
            };
            mRadiusAdjustBounds = false;
        } else {
            if (mRadius > 0) {
                mRadiusAdjustBounds = false;
                mRadii = new float[]{
                        mRadius, mRadius,
                        mRadius, mRadius,
                        mRadius, mRadius,
                        mRadius, mRadius
                };
            }
        }
    }

    //设置圆角背景
    public void setRoundDrawable() {
        Drawable drawable;
        if (mHasState) {
            drawable = getStateListDrawable();
        } else {
            drawable = createDrawable(mFillColor, mStrokeColor);
        }
    }

    public StateListDrawable getStateListDrawable() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        addState(ColorUtil.pressed(mFillColor), ColorUtil.pressed(mStrokeColor),
                stateListDrawable, android.R.attr.state_pressed);

        addState(ColorUtil.disabled(mFillColor), ColorUtil.disabled(mStrokeColor),
                stateListDrawable, -android.R.attr.state_enabled);
        addState(mFillColor, mStrokeColor,
                stateListDrawable, 0);
        return stateListDrawable;
    }

    private void addState(@ColorInt int fillColor, @ColorInt int strokeColor,
                          StateListDrawable stateListDrawable, @AttrRes int state) {
        GradientDrawable drawable = createDrawable(fillColor, strokeColor);
        stateListDrawable.addState(state != 0 ? new int[]{state} : StateSet.WILD_CARD, drawable);
    }

    public GradientDrawable createDrawable(@ColorInt int fillColor, @ColorInt int strokeColor) {
        RoundDrawable drawable = new RoundDrawable(mRadiusAdjustBounds);
        if (mRadiusAdjustBounds) {
            drawable.setCornerRadii(mRadii);
        }
        drawable.setColor(fillColor);
        //边框颜色做简单处理吧
        if (mStrokeWidth > 0) {
            drawable.setStroke(strokeColor, mStrokeColor);
        }
        return drawable;
    }

    static class RoundDrawable extends GradientDrawable {
        /**
         * 圆角大小是否自适应为 View 的高度的一般
         */
        private boolean mRadiusAdjustBounds;

        public RoundDrawable(boolean radiusAdjustBounds) {
            this.mRadiusAdjustBounds = radiusAdjustBounds;
        }

        @Override
        protected void onBoundsChange(Rect r) {
            super.onBoundsChange(r);
            if (mRadiusAdjustBounds) {
                // 修改圆角为短边的一半
                setCornerRadius(Math.min(r.width(), r.height()) / 2);
            }
        }
    }
}
