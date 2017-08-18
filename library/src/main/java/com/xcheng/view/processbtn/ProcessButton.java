package com.xcheng.view.processbtn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;

import com.xcheng.view.R;
import com.xcheng.view.util.ShapeBinder;


public abstract class ProcessButton extends FlatButton {
    private int mProgress;

    private static final int MAX_PROGRESS = 100;
    private static final int MIN_PROGRESS = 0;

    private GradientDrawable mProgressDrawable;
    private GradientDrawable mCompleteDrawable;
    private GradientDrawable mErrorDrawable;

    private CharSequence mLoadingText;
    private CharSequence mCompleteText;
    private CharSequence mErrorText;

    public ProcessButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ProcessButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProcessButton(Context context) {
        super(context);
    }

    @CallSuper
    @Override
    protected void initView(Context context, AttributeSet attrs) {
        super.initView(context, attrs);
        TypedArray attr = getTypedArray(context, attrs, R.styleable.ProcessButton);

        mLoadingText = attr.getString(R.styleable.ProcessButton_ev_pb_textProgress);
        mCompleteText = attr.getString(R.styleable.ProcessButton_ev_pb_textComplete);
        mErrorText = attr.getString(R.styleable.ProcessButton_ev_pb_textError);

        mProgressDrawable = createGradient(attr, R.styleable.ProcessButton_ev_pb_colorProgress, getColor(R.color.ev_purple_progress));
        mCompleteDrawable =createGradient(attr, R.styleable.ProcessButton_ev_pb_colorComplete, getColor(R.color.ev_green_complete));
        mErrorDrawable =createGradient(attr, R.styleable.ProcessButton_ev_pb_colorError, getColor(R.color.ev_red_error));
        attr.recycle();
    }

    /**
     * 解析TypedArray 创建 GradientDrawable
     *
     * @param attr     TypedArray
     * @param index    StyleableRes
     * @param defColor 默认的颜色
     * @return
     */
    public GradientDrawable createGradient(TypedArray attr, @StyleableRes int index, @ColorInt int defColor) {
        GradientDrawable gradientDrawable = (GradientDrawable) ShapeBinder.with(defColor).radius(getCornerRadius()).create(false);
        int color = attr.getColor(index, defColor);
        if (color != defColor) {
            gradientDrawable.setColor(color);
        }
        return gradientDrawable;
    }

    public void setProgress(int progress) {
        mProgress = progress;

        if (mProgress == MIN_PROGRESS) {
            onNormalState();
        } else if (mProgress >= MAX_PROGRESS) {
            onCompleteState();
        } else if (mProgress < MIN_PROGRESS) {
            onErrorState();
        } else {
            onProgress();
        }
        invalidate();
    }

    protected void onErrorState() {
        if (getErrorText() != null) {
            setText(getErrorText());
        }
        setBackgroundCompat(getErrorDrawable());
    }

    protected void onProgress() {
        if (getLoadingText() != null) {
            setText(getLoadingText());
        }
        setBackgroundCompat(getNormalDrawable());
    }

    protected void onCompleteState() {
        if (getCompleteText() != null) {
            setText(getCompleteText());
        }
        setBackgroundCompat(getCompleteDrawable());
    }

    protected void onNormalState() {
        if (getNormalText() != null) {
            setText(getNormalText());
        }
        setBackgroundCompat(getNormalDrawable());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // progress
        if (mProgress > MIN_PROGRESS && mProgress < MAX_PROGRESS) {
            drawProgress(canvas);
        }

        super.onDraw(canvas);
    }

    public abstract void drawProgress(Canvas canvas);

    public int getProgress() {
        return mProgress;
    }

    public static int getMaxProgress() {
        return MAX_PROGRESS;
    }

    public static int getMinProgress() {
        return MIN_PROGRESS;
    }

    public GradientDrawable getProgressDrawable() {
        return mProgressDrawable;
    }

    public GradientDrawable getCompleteDrawable() {
        return mCompleteDrawable;
    }

    public CharSequence getLoadingText() {
        return mLoadingText;
    }

    public CharSequence getCompleteText() {
        return mCompleteText;
    }

    public void setProgressDrawable(GradientDrawable progressDrawable) {
        mProgressDrawable = progressDrawable;
    }

    public void setCompleteDrawable(GradientDrawable completeDrawable) {
        mCompleteDrawable = completeDrawable;
    }

    public void setNormalText(CharSequence normalText) {
        super.setNormalText(normalText);
        if (mProgress == MIN_PROGRESS) {
            setText(normalText);
        }
    }

    public void setLoadingText(CharSequence loadingText) {
        mLoadingText = loadingText;
    }

    public void setCompleteText(CharSequence completeText) {
        mCompleteText = completeText;
    }

    public GradientDrawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public void setErrorDrawable(GradientDrawable errorDrawable) {
        mErrorDrawable = errorDrawable;
    }

    public CharSequence getErrorText() {
        return mErrorText;
    }

    public void setErrorText(CharSequence errorText) {
        mErrorText = errorText;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mProgress = mProgress;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mProgress = savedState.mProgress;
            super.onRestoreInstanceState(savedState.getSuperState());
            setProgress(mProgress);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    /**
     * A {@link Parcelable}
     * state.
     */
    private static class SavedState extends BaseSavedState {

        private int mProgress;

        private SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mProgress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mProgress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
