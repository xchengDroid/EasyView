package com.xcheng.view.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.View;
import android.widget.Button;

import com.xcheng.view.R;
import com.xcheng.view.util.ColorUtil;
import com.xcheng.view.util.ViewHelper;

/**
 * 使按钮能方便地指定圆角、边框颜色、边框粗细、背景色
 * <p>
 * 优先级关系
 * <li>1、ev_radius</li>
 * <li>2、ev_radiusTopLeft、ev_radiusTopRight....</li>
 * <li>3、ev_isRadiusAdjustBounds</li>
 * </p>
 * <p>
 * 注意: 因为该控件的圆角采用 View 的 background 实现, 所以与原生的 <code>android:background</code> 有冲突。
 * <ul>
 * <li>如果在 xml 中用 <code>android:background</code> 指定 background, 该 background 不会生效。</li>
 * <li>如果在该 View 构造完后用 {@link View#setBackgroundResource(int)} 等方法设置背景, 该背景将覆盖圆角效果。</li>
 * </ul>
 * </p>
 * <p>
 * 如需在 xml 中指定圆角、边框颜色、边框粗细、背景色等值,采用 xml 属性 {@link com.xcheng.view.R.styleable#RoundButton}
 * </p>
 */
public class RoundDrawableHelper {
    private final View mView;
    private int mFillColor;
    //enable为false的颜色
    private int mFillColorDisable;
    //按下的颜色
    private int mFillColorPressed;

    private int mBorderColor;
    //enable为false的颜色
    private int mBorderColorDisable;
    //按下的颜色
    private int mBorderColorPressed;
    private int mBorderWidth;
    /**
     * 默认为null,为null标识没有圆角
     */
    private float[] mRadii;
    private float mRadius;

    private boolean mRadiusAdjustBounds;
    private boolean mHasState;

    public RoundDrawableHelper(View view) {
        this.mView = view;
    }

    /**
     * 先调用此方法
     * 解析AttributeSet里面的属性信息
     */
    public void loadAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundButton, defStyleAttr, 0);
        mFillColor = typedArray.getColor(R.styleable.RoundButton_ev_fillColor, 0);
        mFillColorPressed = typedArray.getColor(R.styleable.RoundButton_ev_fillColorPressed, ColorUtil.pressed(mFillColor));
        mFillColorDisable = typedArray.getColor(R.styleable.RoundButton_ev_fillColorDisable, ColorUtil.disabled(mFillColor));

        mBorderColor = typedArray.getColor(R.styleable.RoundButton_ev_borderColor, 0);
        mBorderColorPressed = typedArray.getColor(R.styleable.RoundButton_ev_borderColorPressed, ColorUtil.pressed(mBorderColor));
        mBorderColorDisable = typedArray.getColor(R.styleable.RoundButton_ev_borderColorDisable, ColorUtil.disabled(mBorderColor));
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_borderWidth, 0);
        mRadiusAdjustBounds = typedArray.getBoolean(R.styleable.RoundButton_ev_isRadiusAdjustBounds, true);
        mHasState = typedArray.getBoolean(R.styleable.RoundButton_ev_hasState,
                mView instanceof Button && mView.isClickable());
        int radius = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radius, 0);
        int radiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radiusTopLeft, 0);
        int radiusTopRight = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radiusTopRight, 0);
        int radiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radiusBottomLeft, 0);
        int radiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_radiusBottomRight, 0);
        typedArray.recycle();
        //ev_radius优先级高于ev_radiusTopLeft、ev_radiusTopRight
        if (radius > 0) {
            mRadius = radius;
            mRadiusAdjustBounds = false;
        } else {
            //如果mBorderColor==1的情况下，这种设置方式边框会模糊
            if (radiusTopLeft > 0 || radiusTopRight > 0 || radiusBottomLeft > 0 || radiusBottomRight > 0) {
                mRadii = new float[]{
                        radiusTopLeft, radiusTopLeft,
                        radiusTopRight, radiusTopRight,
                        radiusBottomRight, radiusBottomRight,
                        radiusBottomLeft, radiusBottomLeft
                };
                mRadiusAdjustBounds = false;
            }
        }
    }

    /**
     * 设置圆角背景
     */
    public void setRoundDrawable() {
        Drawable drawable;
        if (mHasState) {
            drawable = createStateListDrawable();
        } else {
            drawable = createDrawable(mFillColor, mBorderColor);
        }
        ViewHelper.setBackgroundKeepingPadding(mView, drawable);
    }

    /**
     * 创建一个stateListDrawable
     */
    public StateListDrawable createStateListDrawable() {
        //构建各种状态的颜色数组，1-->状态 2-->填充颜色  3-->边框颜色
        final int[] statesColor = new int[]{
                android.R.attr.state_pressed, mFillColorPressed, mBorderColorPressed,
                -android.R.attr.state_enabled, mFillColorDisable, mBorderColorDisable,
                0, mFillColor, mBorderColor
        };
        StateListDrawable stateListDrawable = new StateListDrawable();
        for (int index = 0; index < statesColor.length / 3; index++) {
            int state = statesColor[index * 3];
            int fillColor = statesColor[index * 3 + 1];
            int borderColor = statesColor[index * 3 + 2];
            GradientDrawable drawable = createDrawable(fillColor, borderColor);
            stateListDrawable.addState(state != 0 ? new int[]{state} : StateSet.WILD_CARD, drawable);
        }
        return stateListDrawable;
    }

    /**
     * 创建一个GradientDrawable
     *
     * @param fillColor   填充颜色
     * @param borderColor 边框颜色
     * @return
     */
    public GradientDrawable createDrawable(@ColorInt int fillColor, @ColorInt int borderColor) {
        RoundDrawable drawable = new RoundDrawable(mRadiusAdjustBounds);
        if (!mRadiusAdjustBounds) {
            if (mRadius > 0) {
                drawable.setCornerRadius(mRadius);
            } else if (mRadii != null) {
                drawable.setCornerRadii(mRadii);
            }
        }
        drawable.setColor(fillColor);

        if (mBorderWidth > 0) {
            drawable.setStroke(mBorderWidth, borderColor);
        }
        return drawable;
    }


    static class RoundDrawable extends GradientDrawable {
        /**
         * 圆角大小是否自适应为 View 的高度的一般
         */
        private final boolean mRadiusAdjustBounds;

        RoundDrawable(boolean radiusAdjustBounds) {
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
