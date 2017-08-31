package com.xcheng.view.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xcheng.view.R;
import com.xcheng.view.adapter.EasyFragmentPagerAdapter;
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
public abstract class EasyPagerFragment extends EasyFragment implements IPagerController {

    protected ViewPager mViewPager;
    protected EasyFragmentPagerAdapter mTabsAdapter;
    protected PagerSlidingTabStrip mIndicator;

    @Override
    public void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.initView(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.ev_id_viewpager);
        mViewPager.setOffscreenPageLimit(getScreenPageLimit());
        List<TabInfo> tabInfos = new ArrayList<>();
        getTabInfos(tabInfos);
        mTabsAdapter = new EasyFragmentPagerAdapter(getChildFragmentManager(), getContext(), tabInfos) {
            @NonNull
            @Override
            public View getTabView(int position) {
                return createTabView(position, mTabsAdapter.getViewPageInfo(position));
            }
        };
        mViewPager.setAdapter(mTabsAdapter);
        mIndicator = (PagerSlidingTabStrip) findViewById(R.id.ev_id_tab_indicator);
        mIndicator.setViewPager(mViewPager);
    }


    protected Fragment getFragmentFromViewPager(int position) {
        // return getChildFragmentManager().getFragments().get(position);
        return (Fragment) mTabsAdapter.instantiateItem(mViewPager, position);
    }

    @Override
    public int getScreenPageLimit() {
        return DEFAULT_PAGE_LIMIT;
    }
}
