package com.xcheng.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcheng.view.R;

/**
 * 封装Toolbar 添加到TopBarLayout成为其第一个子控件,实现Toolbar布局文件的复用
 * Created by chengxin on 2017/5/2.
 */
public class TopBarLayout extends LinearLayout {

    public TopBarLayout(Context context) {
        this(context, null);
    }

    public TopBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTopBar(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopBarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTopBar(attrs);
    }

    private void initTopBar(AttributeSet attrs) {
        setOrientation(VERTICAL);
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.TopBarLayout);
        int topBarLayoutId = a.getResourceId(R.styleable.TopBarLayout_ev_topBarLayout, 0);
        CharSequence topBarTitle = a.getText(R.styleable.TopBarLayout_ev_topBarTitle);
        a.recycle();
        if (topBarLayoutId != 0) {
            //设置Toolbar
            LayoutInflater.from(getContext()).inflate(topBarLayoutId, this);
        }
        if (!TextUtils.isEmpty(topBarTitle)) {
            TextView titleView = (TextView) findViewById(R.id.ev_id_titleView);
            if (titleView != null) {
                titleView.setText(topBarTitle);
            }
        }
    }
}
