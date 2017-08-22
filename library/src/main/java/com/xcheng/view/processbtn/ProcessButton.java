package com.xcheng.view.processbtn;

import android.app.Dialog;
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
    public static final int NORMAL = -1;
    public static final int MIN_PROGRESS = 0;
    public static final int MAX_PROGRESS = 100;

    private int mProgress = NORMAL;
    private Dialog mTransparentDialog;
    /**
     * 按钮是否可用当处于Progress模式的时候
     */
    private boolean mDisableWhenProgress;

    /**
     * 是否遮罩当处于Progress模式的时候
     */
    private boolean mMaskWhenProgress;

    private GradientDrawable mProgressDrawable;
    private GradientDrawable mCompleteDrawable;
    private GradientDrawable mErrorDrawable;

    private CharSequence mProgressText;
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
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        TypedArray attr = getTypedArray(context, attrs, R.styleable.ProcessButton);
        mDisableWhenProgress = attr.getBoolean(R.styleable.ProcessButton_ev_pb_disableWhenProgress, false);
        mMaskWhenProgress = attr.getBoolean(R.styleable.ProcessButton_ev_pb_maskWhenProgress, false);

        mProgressText = attr.getString(R.styleable.ProcessButton_ev_pb_textProgress);
        mCompleteText = attr.getString(R.styleable.ProcessButton_ev_pb_textComplete);
        mErrorText = attr.getString(R.styleable.ProcessButton_ev_pb_textError);

        mProgressDrawable = createGradient(attr, R.styleable.ProcessButton_ev_pb_colorProgress, getColor(R.color.ev_purple_progress));
        mCompleteDrawable = createGradient(attr, R.styleable.ProcessButton_ev_pb_colorComplete, getColor(R.color.ev_green_complete));
        mErrorDrawable = createGradient(attr, R.styleable.ProcessButton_ev_pb_colorError, getColor(R.color.ev_red_error));
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

    /**
     * 是否是处于progress模式
     */
    protected final boolean inProgress() {
        return mProgress >= MIN_PROGRESS && mProgress <= MAX_PROGRESS;
    }

    /**
     * 核心刷新方法,progress不通取值范围代表不通的状态
     * progress =-1 调用 onNormalState  默认状态,
     * progress <-1 调用 onErrorState  失败状态,
     * progress [0,100] 调用 onProgress  加载状态,
     * progress > 100 调用 onCompleteState  完成成功
     *
     * @param progress 加载进度
     */
    public void setProgress(int progress) {
        mProgress = progress;
        if (mProgress == NORMAL) {
            onNormalState();
        } else if (mProgress < NORMAL) {
            onErrorState();
        } else if (mProgress > MAX_PROGRESS) {
            onCompleteState();
        } else {
            onProgress();
        }
        if (mMaskWhenProgress) {
            ensureTransparentDialog();
            if (inProgress()) {
                mTransparentDialog.show();
            } else {
                mTransparentDialog.dismiss();
            }
        }
        if (mDisableWhenProgress) {
            setEnabled(!inProgress());
        }
        invalidate();
    }

    public void normal() {
        setProgress(NORMAL);
    }

    public void error() {
        setProgress(NORMAL - 1);
    }

    public void complete() {
        setProgress(MAX_PROGRESS + 1);
    }

    private void ensureTransparentDialog() {
        if (mTransparentDialog == null) {
            mTransparentDialog = new Dialog(getContext(), R.style.ev_Translucent_NoTitle);
            mTransparentDialog.setCancelable(false);
        }
    }


    protected void onErrorState() {
        if (getErrorText() != null) {
            setText(getErrorText());
        }
        setBackgroundCompat(getErrorDrawable());
    }

    protected void onProgress() {
        if (getProgressText() != null) {
            setText(getProgressText());
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
        if (inProgress()) {
            drawProgress(canvas);
        }
        //下面调用不会遮盖文字
        super.onDraw(canvas);
    }

    public abstract void drawProgress(Canvas canvas);

    public int getProgress() {
        return mProgress;
    }

    public GradientDrawable getProgressDrawable() {
        return mProgressDrawable;
    }

    public GradientDrawable getCompleteDrawable() {
        return mCompleteDrawable;
    }

    public CharSequence getProgressText() {
        return mProgressText;
    }

    public CharSequence getCompleteText() {
        return mCompleteText;
    }

    public void setProgressDrawable(GradientDrawable progressDrawable) {
        mProgressDrawable = progressDrawable;
        setProgress(mProgress);
    }

    public void setCompleteDrawable(GradientDrawable completeDrawable) {
        mCompleteDrawable = completeDrawable;
        setProgress(mProgress);
    }

    public void setNormalText(CharSequence normalText) {
        super.setNormalText(normalText);
        setProgress(mProgress);
    }

    public void setProgressText(CharSequence progressText) {
        mProgressText = progressText;
        setProgress(mProgress);
    }

    public void setCompleteText(CharSequence completeText) {
        mCompleteText = completeText;
        setProgress(mProgress);
    }

    public GradientDrawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public void setErrorDrawable(GradientDrawable errorDrawable) {
        mErrorDrawable = errorDrawable;
        setProgress(mProgress);
    }

    public CharSequence getErrorText() {
        return mErrorText;
    }

    public void setErrorText(CharSequence errorText) {
        mErrorText = errorText;
        setProgress(mProgress);
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
