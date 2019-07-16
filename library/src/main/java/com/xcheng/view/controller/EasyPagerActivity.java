package com.xcheng.view.controller;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

import com.xcheng.view.R;
import com.xcheng.view.adapter.EasyFragmentAdapter;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.widget.smarttab.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 约定大于配置，
 * 设置Viewpager 的id 为ev_id_viewpager，
 * 设置PagerSlidingTabStrip 的 id为ev_id_tab_indicator
 *
 * @author chengxin @date:2017-8-30
 */
public abstract class EasyPagerActivity extends EasyActivity {

    protected ViewPager mViewPager;
    protected EasyFragmentAdapter mTabAdapter;
    protected SmartTabLayout mTabLayout;

    @Override
    public int getLayoutId() {
        return R.layout.ev_pager;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.initView(savedInstanceState);
        mViewPager = findViewById(R.id.ev_id_viewpager);
        List<TabInfo> tabInfos = new ArrayList<>();
        getTabInfos(tabInfos);
        mTabAdapter = new EasyFragmentAdapter(getSupportFragmentManager(), getContext(), tabInfos);
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout = findViewById(R.id.ev_id_tabLayout);
        /**if call {@link SmartTabLayout#setCustomTabView(SmartTabLayout.TabProvider)}
         * {@link SmartTabLayout#setCustomTabView(int, int)},you must call{@link SmartTabLayout#setViewPager(ViewPager)} again
         */
        mTabLayout.setViewPager(mViewPager);
    }

    /**
     * 设置Tab页面加载的每个页面
     */
    protected void getTabInfos(final List<TabInfo> tabInfos) {

    }
}
