package com.xcheng.view.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xcheng.view.R;
import com.xcheng.view.adapter.EasyFragmentAdapter;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 约定大于配置，
 * 设置Viewpager 的id 为ev_id_viewpager，
 * 设置PagerSlidingTabStrip 的 id为ev_id_tab_indicator
 *
 * @author chengxin @date:2017-8-30
 */
public abstract class EasyPagerActivity extends EasyActivity implements IPagerView {

    protected ViewPager mViewPager;
    protected EasyFragmentAdapter mTabsAdapter;
    protected PagerSlidingTabStrip mIndicator;

    @Override
    public int getLayoutId() {
        return R.layout.ev_pager;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.initView(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.ev_id_viewpager);
        mViewPager.setOffscreenPageLimit(getScreenPageLimit());
        List<TabInfo> tabInfos = new ArrayList<>();
        getTabInfos(tabInfos);
        mTabsAdapter = new EasyFragmentAdapter(getSupportFragmentManager(), getContext(), tabInfos) {
            @NonNull
            @Override
            public View getTabView(int position) {
                return createTabView(position, mTabsAdapter.getTabInfo(position));
            }
        };
        mViewPager.setAdapter(mTabsAdapter);
        mIndicator = (PagerSlidingTabStrip) findViewById(R.id.ev_id_tab_indicator);
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    public int getScreenPageLimit() {
        return DEFAULT_PAGE_LIMIT;
    }
}
