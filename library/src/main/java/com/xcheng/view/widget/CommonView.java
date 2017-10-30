package com.xcheng.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.xcheng.view.R;
import com.xcheng.view.divider.DividerLayout;
import com.xcheng.view.util.LocalDisplay;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 通用的输入和选择的View
 * Created by chengxin on 2017/10/27.
 */
public class CommonView extends DividerLayout {

    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#333333");
    private static final int DEFAULT_HINT_COLOR = Color.parseColor("#c2c9cc");

    private static final int GRAVITY_START = 0;
    private static final int GRAVITY_CENTER = 1;
    private static final int GRAVITY_END = 2;

    private static final InputFilter[] NO_FILTERS = new InputFilter[0];

    public static final int INPUT = 0;
    public static final int DISPLAY = 1;

    @IntDef({INPUT, DISPLAY})
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {
    }

    public static final int NONE = 0;
    public static final int NUMBER = 1;
    public static final int NUMBER_DECIMAL = 2;
    public static final int TEXT_PASSWORD = 3;
    public static final int NUMBER_PASSWORD = 4;
    public static final int PHONE = 5;

    @IntDef({NONE, NUMBER, NUMBER_DECIMAL, TEXT_PASSWORD, NUMBER_PASSWORD, PHONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface InputMethod {
    }

    //显示左侧
    private TextView mLabelView;
    //展示或选择
    private EditText mInputView;
    private TextView mDisplayView;
    //选项模式下保存选中的内容
    private Object mResult;

    private TextWatcher mTextWatcher;
    //单位或图标 nav_arrow
    private TextView mSuffixView;

    private int mMode;

    private int mInputType;

    public CommonView(Context context) {
        this(context, null);
    }

    public CommonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("WrongConstant")
    public CommonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedValue = getContext().obtainStyledAttributes(attrs, R.styleable.CommonView);
        //初始化布局
        int layoutId = typedValue.getResourceId(R.styleable.CommonView_ev_cv_layout, R.layout.ev_commom_view);
        inflate(getContext(), layoutId, this);
        mLabelView = (TextView) findViewById(R.id.ev_id_cv_label);
        mInputView = (EditText) findViewById(R.id.ev_id_cv_input);
        mDisplayView = (TextView) findViewById(R.id.ev_id_cv_display);
        mSuffixView = (TextView) findViewById(R.id.ev_id_cv_suffix);
        //是否为单行，默认为true
        boolean singleLine = typedValue.getBoolean(R.styleable.CommonView_ev_cv_singleLine, true);
        int minHeight = typedValue.getDimensionPixelSize(R.styleable.CommonView_ev_cv_minHeight, 0);
        //元素的最小高度,不能调用setMinHeight,否则如果后面再调用 setLines setMaxLines setMinLines 会被覆盖，他们都是通用 mMinimum 和mMaximum 属性控制
        //setMinimumHeight是View中的方法，mMinHeight属性在View中不会被覆盖
        //{@link #getSuggestedMinimumHeight()}
        mInputView.setMinimumHeight(minHeight);
        mDisplayView.setMinimumHeight(minHeight);
        if (singleLine) {
            //对于EditText来说，singleLine maxLines minLines lines只有对inputType="none"才有效，其他类型的inputType默认只有一行的..
            mInputView.setSingleLine();
            mDisplayView.setSingleLine();
            mDisplayView.setEllipsize(TextUtils.TruncateAt.END);
        }
        int paddingStart = typedValue.getDimensionPixelSize(R.styleable.CommonView_ev_cv_paddingStart, -1);
        int paddingTop = typedValue.getDimensionPixelSize(R.styleable.CommonView_ev_cv_paddingTop, -1);
        int paddingTopEnd = typedValue.getDimensionPixelSize(R.styleable.CommonView_ev_cv_paddingEnd, -1);
        int paddingBottom = typedValue.getDimensionPixelSize(R.styleable.CommonView_ev_cv_paddingBottom, -1);
        if (paddingStart >= 0 || paddingTop >= 0 || paddingTopEnd >= 0 || paddingBottom >= 0) {
            mInputView.setPadding(paddingStart > 0 ? paddingStart : 0, paddingTop > 0 ? paddingTop : 0, paddingTopEnd > 0 ? paddingTopEnd : 0, paddingBottom > 0 ? paddingBottom : 0);
            mDisplayView.setPadding(paddingStart > 0 ? paddingStart : 0, paddingTop > 0 ? paddingTop : 0, paddingTopEnd > 0 ? paddingTopEnd : 0, paddingBottom > 0 ? paddingBottom : 0);
        }
        // in px
        final int defaultTextSize = LocalDisplay.convert(TypedValue.COMPLEX_UNIT_SP, 16, context);

        int labelSize = typedValue.getDimensionPixelSize(R.styleable.CommonView_ev_cv_labelSize, defaultTextSize);
        int labelWidth = typedValue.getDimensionPixelSize(R.styleable.CommonView_ev_cv_labelWidth, -1);

        int labelColor = typedValue.getColor(R.styleable.CommonView_ev_cv_labelColor, DEFAULT_TEXT_COLOR);
        CharSequence label = typedValue.getText(R.styleable.CommonView_ev_cv_label);

        int textSize = typedValue.getDimensionPixelSize(R.styleable.CommonView_ev_cv_textSize, defaultTextSize);
        int textColor = typedValue.getColor(R.styleable.CommonView_ev_cv_textColor, DEFAULT_TEXT_COLOR);
        int hintColor = typedValue.getColor(R.styleable.CommonView_ev_cv_hintColor, DEFAULT_HINT_COLOR);
        CharSequence text = typedValue.getText(R.styleable.CommonView_ev_cv_text);
        CharSequence hint = typedValue.getText(R.styleable.CommonView_ev_cv_hint);
        //最大长度,默认为15
        int maxLength = typedValue.getInt(R.styleable.CommonView_ev_cv_maxLength, -1);

        //如果没有设置就是用layout中View默认的设置
        int cvGravity = typedValue.getInt(R.styleable.CommonView_ev_cv_gravity, GRAVITY_START);

        int suffixTextColor = typedValue.getColor(R.styleable.CommonView_ev_cv_suffixTextColor, DEFAULT_TEXT_COLOR);
        int suffixTextSize = typedValue.getInt(R.styleable.CommonView_ev_cv_suffixTextSize, defaultTextSize);
        CharSequence suffixText = typedValue.getText(R.styleable.CommonView_ev_cv_suffixText);
        Drawable suffixIcon = typedValue.getDrawable(R.styleable.CommonView_ev_cv_suffixIcon);

        int inputType = typedValue.getInt(R.styleable.CommonView_ev_cv_inputType, NONE);
        int mode = typedValue.getInt(R.styleable.CommonView_ev_cv_mode, INPUT);
        typedValue.recycle();
        mLabelView.setText(label);
        mLabelView.setTextColor(labelColor);
        mLabelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelSize);
        if (labelWidth >= 0) {
            mLabelView.setWidth(labelWidth);
        }

        setText(text);
        setHint(hint);
        setTextColor(textColor);
        setHintColor(hintColor);
        mDisplayView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mInputView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setMaxLength(maxLength);
        int gravity;
        if (cvGravity == GRAVITY_START) {
            gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        } else if (cvGravity == GRAVITY_CENTER) {
            gravity = Gravity.CENTER;
        } else {
            gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        }
        setTextGravity(gravity);

        mSuffixView.setTextColor(suffixTextColor);
        mSuffixView.setTextSize(TypedValue.COMPLEX_UNIT_PX, suffixTextSize);
        if (suffixIcon != null) {
            mSuffixView.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIcon, null);
        }
        if (suffixText != null) {
            mSuffixView.setText(suffixText);
        }
        setInputType(inputType);
        setMode(mode);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        //子View状态不保存，防止Activity重启导致所有的CommonView内容都一样
        //super.dispatchSaveInstanceState(container);
    }

    public void setMode(@Mode int mode) {
        this.mMode = mode;
        if (mMode == INPUT) {
            mDisplayView.setVisibility(GONE);
            mInputView.setVisibility(VISIBLE);
        } else {
            mDisplayView.setVisibility(VISIBLE);
            mInputView.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(mSuffixView.getText()) || mSuffixView.getCompoundDrawables()[2] != null) {
            mSuffixView.setVisibility(VISIBLE);
        } else {
            mSuffixView.setVisibility(GONE);
        }
    }


    /**
     * @param maxLength not limit set -1;
     */
    public void setMaxLength(int maxLength) {
        if (maxLength >= 0) {
            mDisplayView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        } else {
            mDisplayView.setFilters(NO_FILTERS);
            mInputView.setFilters(NO_FILTERS);
        }
    }

    public void setTextGravity(int gravity) {
        mDisplayView.setGravity(gravity);
        mInputView.setGravity(gravity);
    }


    public void setInputType(@InputMethod int inputMethod) {
        this.mInputType = inputMethod;
        switch (mInputType) {
            case NONE:
                // line minLines maxLines 只有在inputType为 none的情况下才有效，否则和singleLine效果一些样，只能左右滑动.
                //但是当inputType为none的时候设置 maxLines为1不能实现单行的效果，能上下滚动，问不左右滚动
                //InputType.TYPE_NULL会导致无法弹出软键盘，无光标。
                break;
            case NUMBER:
                mInputView.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case NUMBER_DECIMAL:
                mInputView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case TEXT_PASSWORD:
                mInputView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case NUMBER_PASSWORD:
                mInputView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case PHONE:
                mInputView.setInputType(InputType.TYPE_CLASS_PHONE);
                setMaxLength(11);
                break;
        }
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        //删除已存在的，再添加
        mInputView.removeTextChangedListener(this.mTextWatcher);
        this.mTextWatcher = textWatcher;
        mInputView.addTextChangedListener(mTextWatcher);
    }

    public void setHint(CharSequence s) {
        mInputView.setHint(s);
        mDisplayView.setHint(s);
    }

    public void setText(CharSequence s) {
        mInputView.setText(s);
        mDisplayView.setText(s);
    }

    public void setTextColor(@ColorInt int color) {
        mInputView.setTextColor(color);
        mDisplayView.setTextColor(color);
    }

    public void setHintColor(@ColorInt int color) {
        mInputView.setHintTextColor(color);
        mDisplayView.setHintTextColor(color);

    }

    public void setTextSize(int size) {
        mInputView.setTextSize(size);
        mDisplayView.setTextSize(size);
    }

    public TextView getLabelView() {
        return mLabelView;
    }

    public EditText getInputView() {
        return mInputView;
    }

    public TextView getDisplayView() {
        return mDisplayView;
    }

    public TextView getSuffixView() {
        return mSuffixView;
    }

    public String getText() {
        if (mMode == INPUT) {
            return mInputView.getText().toString();
        }
        return mDisplayView.getText().toString();
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(getText());
    }

    public int getMode() {
        return mMode;
    }

    public void setResult(Object result) {
        mResult = result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getResult() {
        return (T) mResult;
    }
}
