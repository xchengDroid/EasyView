package com.xcheng.view.controller;

import android.support.annotation.LayoutRes;
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
    protected Toolbar mToolbar;
    protected TextView mTitleView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mTitleView = findViewById(R.id.ev_id_titleView);
        mToolbar = findViewById(R.id.ev_id_toolBar);
        if (mToolbar != null) {
            if (isSupportActionBar()) {
                setSupportActionBar(mToolbar);
                //noinspection ConstantConditions
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

    /***
     * ViewCompat.postOnAnimation(mDecorToolbar.getViewGroup(), mMenuInvalidator); 推送执行
     * onCreateOptionsMenu(Menu menu) 方法，该方法执行之前会把Menu清除
     * 所以在initView里面执行  mToolbar.inflateMenu后又被清除
     * 故添加是否设置支持ActionBar供子类动态去执行
     */
    public boolean isSupportActionBar() {
        return true;
    }
}
