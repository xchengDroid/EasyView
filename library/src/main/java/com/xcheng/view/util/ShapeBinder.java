package com.xcheng.view.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.util.StateSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.graphics.drawable.GradientDrawable.LINE;
import static android.graphics.drawable.GradientDrawable.OVAL;
import static android.graphics.drawable.GradientDrawable.RECTANGLE;
import static android.graphics.drawable.GradientDrawable.RING;

/**
 * 动态生成Shape工具类
 * Created by chengxin on 2017/3/13.
 */
public class ShapeBinder {
    /**
     * 无效的值
     */
    public static final int INVALID_VALUE = Integer.MIN_VALUE;

    private static final int CHECKED = android.R.attr.state_checked;
    private static final int FOCUSED = android.R.attr.state_focused;
    private static final int PRESSED = android.R.attr.state_pressed;
    private static final int SELECTED = android.R.attr.state_selected;
    private static final int ENABLED = android.R.attr.state_enabled;
    private int width = INVALID_VALUE;
    private int height = INVALID_VALUE;
    private int shape = RECTANGLE;
    private float[] radii;
    private int strokeColor = INVALID_VALUE;
    private int strokeWidth = INVALID_VALUE;
    private int normalColor = INVALID_VALUE;
    private int pressedColor = INVALID_VALUE;
    private int selectedColor = INVALID_VALUE;
    private int disableColor = INVALID_VALUE;
    private int checkedColor = INVALID_VALUE;
    private int focusedColor = INVALID_VALUE;


    private ShapeBinder(@ColorInt int normalColor) {
        this.normalColor = normalColor;
        radius(6);
        strokeWidth(2);
    }

    public static ShapeBinder with(@ColorInt int normalColor) {
        return new ShapeBinder(normalColor);
    }

    public static ShapeBinder with(Context context, @ColorRes int colorRes) {
        return with(ContextCompat.getColor(context, colorRes));
    }

    /**
     * stateListDrawable 中添加对应状态的GradientDrawable
     */
    private void addDrawable(@ColorInt int solidColor, @AttrRes int state, @NonNull StateListDrawable stateListDrawable) {
        GradientDrawable drawable = getDrawable(solidColor);
        stateListDrawable.addState(state != 0 ? new int[]{state} : StateSet.WILD_CARD, drawable);
    }

    private GradientDrawable getDrawable(@ColorInt int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(shape);
        if (radii != null) {
            drawable.setCornerRadii(radii);
        }
        drawable.setColor(color);
        //边框颜色做简单处理吧
        if (strokeColor != INVALID_VALUE && strokeWidth != INVALID_VALUE) {
            drawable.setStroke(strokeWidth, strokeColor);
        }
        if (width != INVALID_VALUE && height != INVALID_VALUE) {
            drawable.setSize(width, height);
        }
        return drawable;
    }

    /**
     * 有view状态
     *
     * @param view 绑定Drawable的view
     */
    public void drawableStateTo(View view) {
        setBackground(view, create(true));
    }

    @SuppressWarnings("deprecation")
    private static void setBackground(View v, Drawable a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(a);
        } else {
            v.setBackgroundDrawable(a);
        }
    }

    public void drawableTo(View view) {
        setBackground(view, create(false));
    }

    /**
     * @param hasState 是否返回StateListDrawable
     * @return 如果 hasState为true ,返回StateListDrawable 否则返回GradientDrawable
     */
    public Drawable create(boolean hasState) {
        if (hasState) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            if (pressedColor != INVALID_VALUE) {
                addDrawable(pressedColor, PRESSED, stateListDrawable);
            } else {
                addDrawable(ColorUtil.pressed(normalColor), PRESSED, stateListDrawable);
            }
            if (disableColor != INVALID_VALUE) {
                addDrawable(disableColor, -ENABLED, stateListDrawable);
            } else {
                addDrawable(ColorUtil.disabled(normalColor), -ENABLED, stateListDrawable);
            }
            if (selectedColor != INVALID_VALUE) {
                addDrawable(selectedColor, SELECTED, stateListDrawable);
            }
            if (checkedColor != INVALID_VALUE) {
                addDrawable(checkedColor, CHECKED, stateListDrawable);
            }
            if (focusedColor != INVALID_VALUE) {
                addDrawable(focusedColor, FOCUSED, stateListDrawable);
            }
            addDrawable(normalColor, 0, stateListDrawable);
            return stateListDrawable;
        } else {
            return getDrawable(normalColor);
        }
    }

    /**
     * @param radius with px
     */
    public ShapeBinder radius(float radius) {
        return radii(new float[]{radius, radius, radius, radius});
    }

    /**
     * @param radii with px, length==4
     */
    public ShapeBinder radii(@Size(value = 4) float[] radii) {
        float leftTop = radii[0];
        float rightTop = radii[1];
        float rightBottom = radii[2];
        float leftBottom = radii[3];
        this.radii = new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};
        return this;
    }

    public ShapeBinder stroke(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    /**
     * 如果等于0 不渲染边框
     */
    public ShapeBinder strokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public ShapeBinder pressed(@ColorInt int pressedColor) {
        this.pressedColor = pressedColor;
        return this;
    }

    public ShapeBinder disable(@ColorInt int disableColor) {
        this.disableColor = disableColor;
        return this;
    }

    public ShapeBinder selected(@ColorInt int selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    public ShapeBinder focused(@ColorInt int focusedColor) {
        this.focusedColor = focusedColor;
        return this;
    }

    public ShapeBinder ckecked(@ColorInt int checkedColor) {
        this.checkedColor = checkedColor;
        return this;
    }

    /**
     * @param width  with px
     * @param height with px
     */
    public ShapeBinder size(@IntRange(from = 0) int width, @IntRange(from = 0) int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @IntDef({RECTANGLE, OVAL, LINE, RING})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Shape {
    }

    public ShapeBinder shape(@Shape int shape) {
        this.shape = shape;
        return this;
    }
}
