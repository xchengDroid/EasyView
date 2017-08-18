package com.xcheng.view.processbtn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.util.AttributeSet;
import android.widget.Button;

import com.xcheng.view.R;
import com.xcheng.view.util.ColorUtil;
import com.xcheng.view.util.ShapeBinder;


@SuppressLint("AppCompatCustomView")
public class FlatButton extends Button {

    private StateListDrawable mNormalDrawable;
    private CharSequence mNormalText;
    private float cornerRadius;

    public FlatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FlatButton(Context context) {
        super(context);
        initView(context, null);
    }

    @CallSuper
    protected void initView(Context context, AttributeSet attrs) {
        mNormalDrawable = new StateListDrawable();
        TypedArray attr = getTypedArray(context, attrs, R.styleable.FlatButton);
        float defValue = dp2px(2);
        cornerRadius = attr.getDimension(R.styleable.FlatButton_ev_pb_cornerRadius, defValue);
        int colorNormal = attr.getColor(R.styleable.FlatButton_ev_pb_colorNormal, getColor(R.color.ev_blue_normal));
        int colorPressed = attr.getColor(R.styleable.FlatButton_ev_pb_colorPressed, ColorUtil.pressed(colorNormal));
        int colorDisable = attr.getColor(R.styleable.FlatButton_ev_pb_colorDisable, ColorUtil.disabled(colorNormal));
        mNormalDrawable = (StateListDrawable) ShapeBinder.with(colorNormal)
                .pressed(colorPressed)
                .disable(colorDisable)
                .radius(cornerRadius)
                .create(true);
        attr.recycle();
        mNormalText = getText().toString();
        setBackgroundCompat(mNormalDrawable);
    }

    protected Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    protected float getDimension(int id) {
        return getResources().getDimension(id);
    }

    protected int getColor(int id) {
        return getResources().getColor(id);
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    public float getCornerRadius() {
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

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dp(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
