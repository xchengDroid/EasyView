package com.xcheng.view.processbtn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Button;

import com.xcheng.view.R;
import com.xcheng.view.util.ColorUtil;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.ShapeBinder;


@SuppressLint("AppCompatCustomView")
public class FlatButton extends Button {

    private StateListDrawable mNormalDrawable;
    private CharSequence mNormalText;
    private float cornerRadius;

    public FlatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlatButton(Context context) {
        super(context);
        init(context, null);
    }

    @CallSuper
    protected void init(Context context, AttributeSet attrs) {
        LocalDisplay.init(context);
        mNormalDrawable = new StateListDrawable();
        TypedArray attr = getTypedArray(context, attrs, R.styleable.FlatButton);
        cornerRadius = attr.getDimension(R.styleable.FlatButton_ev_pb_cornerRadius, LocalDisplay.dp2px(2));
        int colorNormal = attr.getColor(R.styleable.FlatButton_ev_pb_colorNormal, getColor(R.color.ev_blue_normal));
        int colorPressed = attr.getColor(R.styleable.FlatButton_ev_pb_colorPressed, ColorUtil.pressed(colorNormal));
        int colorDisable = attr.getColor(R.styleable.FlatButton_ev_pb_colorDisable, ColorUtil.disabled(colorNormal));
        //边框颜色
        int colorStroke = attr.getColor(R.styleable.FlatButton_ev_pb_colorStroke, ShapeBinder.INVALID_VALUE);
        //边框宽度
        int strokeWidth = attr.getDimensionPixelSize(R.styleable.FlatButton_ev_pb_strokeWidth, ShapeBinder.INVALID_VALUE);
        final ShapeBinder shapeBinder = ShapeBinder.with(colorNormal)
                .pressed(colorPressed)
                .disable(colorDisable)
                .radius(cornerRadius);
        //如果设置了边框颜色
        if (colorStroke != ShapeBinder.INVALID_VALUE) {
            shapeBinder.stroke(colorStroke);
        }
        //如果设置了边框宽度
        if (strokeWidth != ShapeBinder.INVALID_VALUE) {
            shapeBinder.strokeWidth(strokeWidth);
        }
        mNormalDrawable = (StateListDrawable) shapeBinder.create(true);
        attr.recycle();

        mNormalText = getText().toString();
        setBackgroundCompat(mNormalDrawable);
    }

    protected Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    protected float getDimension(int id) {
        return getResources().getDimension(id);
    }

    protected int getColor(int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    public final float getCornerRadius() {
        return cornerRadius;
    }

    public StateListDrawable getNormalDrawable() {
        return mNormalDrawable;
    }

    public CharSequence getNormalText() {
        return mNormalText;
    }

    public void setNormalText(CharSequence normalText) {
        mNormalText = normalText;
    }

    /**
     * Set the View's background. Masks the API changes made in Jelly Bean.
     *
     * @param drawable
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundCompat(Drawable drawable) {
        int pL = getPaddingLeft();
        int pT = getPaddingTop();
        int pR = getPaddingRight();
        int pB = getPaddingBottom();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
        setPadding(pL, pT, pR, pB);
    }
}
