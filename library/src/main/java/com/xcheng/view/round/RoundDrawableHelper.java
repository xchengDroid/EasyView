package com.xcheng.view.round;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.View;
import android.widget.Button;

import com.xcheng.view.R;
import com.xcheng.view.util.ColorUtil;

/**
 * 使按钮能方便地指定圆角、边框颜色、边框粗细、背景色
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
    private int mBorderColor;
    private int mBorderWidth;
    private float[] mRadii;
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
        mBorderColor = typedArray.getColor(R.styleable.RoundButton_ev_borderColor, 0);
        mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.RoundButton_ev_borderWidth, 0);
        mRadiusAdjustBounds = typedArray.getBoolean(R.styleable.RoundButton_ev_isRadiusAdjustBounds, true);
        mHasState = typedArray.getBoolean(R.styleable.RoundButton_ev_hasState,
                mView instanceof Button && mView.isClickable());
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
        setBackgroundKeepingPadding(mView, drawable);
    }

    /**
     * 创建一个stateListDrawable
     */
    public StateListDrawable createStateListDrawable() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        addState(ColorUtil.pressed(mFillColor), ColorUtil.pressed(mBorderColor),
                stateListDrawable, android.R.attr.state_pressed);

        addState(ColorUtil.disabled(mFillColor), ColorUtil.disabled(mBorderColor),
                stateListDrawable, -android.R.attr.state_enabled);
        addState(mFillColor, mBorderColor,
                stateListDrawable, 0);
        return stateListDrawable;
    }

    /**
     * 添加状态
     *
     * @param fillColor         填充颜色
     * @param borderColor       边框颜色
     * @param stateListDrawable 状态Drawable
     * @param state             对应状态
     */
    private void addState(@ColorInt int fillColor, @ColorInt int borderColor,
                          StateListDrawable stateListDrawable, @AttrRes int state) {
        GradientDrawable drawable = createDrawable(fillColor, borderColor);
        stateListDrawable.addState(state != 0 ? new int[]{state} : StateSet.WILD_CARD, drawable);
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
            drawable.setCornerRadii(mRadii);
        }
        drawable.setColor(fillColor);

        if (mBorderWidth > 0) {
            drawable.setStroke(mBorderWidth, borderColor);
        }
        return drawable;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackgroundKeepingPadding(View view, Drawable drawable) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    @SuppressWarnings("deprecation")
    public static void setBackgroundKeepingPadding(View view, int backgroundResId) {
        setBackgroundKeepingPadding(view, view.getResources().getDrawable(backgroundResId));
    }

    public static void setBackgroundColorKeepPadding(View view, @ColorInt int color) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        view.setBackgroundColor(color);
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
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
