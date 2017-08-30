package com.xcheng.view.controller;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xcheng.view.R;

/**
 * 通用的Toolbar 基类,
 * Toolbar id为ev_id_toolBar，
 * 标题TextView id为ev_id_titleView
 * Created by chengxin on 2017/5/2.
 */
public abstract class TopBarSupportActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTitleView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolBar();
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolBar() {
        mTitleView = (TextView) findViewById(R.id.ev_id_titleView);
        mToolbar = (Toolbar) findViewById(R.id.ev_id_toolBar);
        if (mToolbar != null) {
            if (isSupportActionBar()) {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            if (mToolbar.getNavigationIcon() != null) {
                //需要在setSupportActionBar之后执行，否则会被onOptionsItemSelected覆盖
                //默认的操作，如果需要可由子类覆盖
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    /***
     * ViewCompat.postOnAnimation(mDecorToolbar.getViewGroup(), mMenuInvalidator); 推送执行
     * onCreateOptionsMenu(Menu menu) 方法，该方法执行之前会把Menu清除
     * 所以在initView里面执行  mToolbar.inflateMenu后又被清除
     * 固添加是否设置支持ActionBar供子类动态去执行
     ***/
    public boolean isSupportActionBar() {
        return true;
    }

    public void setCustomTitle(@StringRes int titleId) {
        if (titleId != 0)
            setCustomTitle(getText(titleId));
    }

    public void setCustomTitle(CharSequence title) {
        if (mTitleView != null)
            mTitleView.setText(title);
    }

    public void setLogo(Drawable drawable) {
        if (mToolbar != null) {
            mToolbar.setLogo(drawable);
        }
    }

    public void setLogo(@DrawableRes int resId) {
        if (mToolbar != null) {
            mToolbar.setLogo(resId);
        }
    }

    public void setNavigationIcon(@Nullable Drawable icon) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(icon);
        }
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(resId);
        }
    }
}
