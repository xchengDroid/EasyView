package com.xcheng.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcheng.view.R;
import com.xcheng.view.util.RequestState;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.ShapeBinder;

/**
 * 刷新列表无数据时显示的空View
 * Created by cx on 2016/12/9.
 */
public class EmptyView extends LinearLayout implements View.OnClickListener {
    @DrawableRes
    private int mEmptyLogo = R.drawable.ev_ic_empty_data;
    @DrawableRes
    private int netErrorLogo = R.drawable.ev_ic_network_error;
    private SimpleArrayMap<RequestState, String> mCustomState;//用户自定义状态

    private RequestState mRequestState;
    private ImageView mImageView;
    private TextView mTextView;
    private TextView mHandleView;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setId(R.id.ev_id_emptyView);
        mCustomState = new SimpleArrayMap<>();
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        mImageView = new ImageView(getContext());
        mTextView = new TextView(getContext());
        mTextView.setTextSize(18);
        mTextView.setTextColor(Color.parseColor("#606a6f"));
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        lp.topMargin = LocalDisplay.dp2px(10);
        addView(mImageView);
        addView(mTextView, lp);

        //网络出错时展示重新加载按钮
        mHandleView = new TextView(getContext());
        mHandleView.setText(R.string.ev_reload);
        mHandleView.setTextSize(16);
        int handTextColor = ContextCompat.getColor(getContext(), R.color.ev_light_blue);
        mHandleView.setTextColor(handTextColor);
        int paddingSize = LocalDisplay.dp2px(4);
        mHandleView.setPadding(paddingSize * 3, paddingSize, paddingSize * 3, paddingSize);
        ShapeBinder.with(Color.WHITE).pressed(Color.parseColor("#c9e0ff")).stroke(handTextColor).strokeWidth(LocalDisplay.dp2px(1)).drawableStateTo(mHandleView);
        //默认不可见
        mHandleView.setVisibility(INVISIBLE);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = LocalDisplay.dp2px(14);
        addView(mHandleView, params);
        mHandleView.setOnClickListener(this);
        //default
        setRequestState(RequestState.LOADING);
    }

    @Override
    public void onClick(View v) {
        if (v == mHandleView) {
            if (mOnStateChangeListener != null) {
                mOnStateChangeListener.onClick(mRequestState, mHandleView);
            }
        }
    }

    public static EmptyView with(Context context) {
        return new EmptyView(context);
    }

    public EmptyView setEmptyLogo(@DrawableRes int emptyLogo) {
        mEmptyLogo = emptyLogo;
        mImageView.setImageResource(emptyLogo);
        return this;
    }

    public EmptyView setCustomState(RequestState requestState, String customMessage) {
        if (!TextUtils.isEmpty(customMessage)) {
            mCustomState.put(requestState, customMessage);
            if (mRequestState == requestState) {
                mTextView.setText(customMessage);
            }
        }
        return this;
    }

    public void setRequestState(@NonNull RequestState requestState) {
        if (mRequestState == requestState) {
            return;
        }
        mRequestState = requestState;
        if (requestState == RequestState.NETWORK_ERROR) {
            mImageView.setImageResource(netErrorLogo);
        } else {
            //从异常状态到正在加载状态不改变其icon
            if (mRequestState != RequestState.LOADING) {
                mImageView.setImageResource(mEmptyLogo);
            }
            //default
            if (mImageView.getDrawable() == null) {
                mImageView.setImageResource(mEmptyLogo);
            }
        }
        mTextView.setText(mCustomState.get(requestState) == null ? requestState.getText() : mCustomState.get(requestState));
        if (mOnStateChangeListener != null) {
            boolean isVisible = mOnStateChangeListener.onStateChanged(mRequestState, mTextView);
            mHandleView.setVisibility(isVisible ? VISIBLE : INVISIBLE);
        }
    }

    private OnStateChangeListener mOnStateChangeListener;

    public EmptyView setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
        return this;
    }


    public interface OnStateChangeListener {
        /**
         * 状态发生改变
         *
         * @param state      当前状态
         * @param handleView 被点击的View
         * @return clickView是否处于可见状态
         */
        boolean onStateChanged(RequestState state, TextView handleView);

        //按钮点击
        void onClick(RequestState state, TextView handleView);
    }
}
