package com.xcheng.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.xcheng.view.widget.PagerSlidingTabStrip;

import java.util.List;

public abstract class EasyFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.TabAdapter {
    private Context mContext;
    private final List<TabInfo> mTabInfos;

    public EasyFragmentPagerAdapter(FragmentManager fm, Context context,
                                    List<TabInfo> tabs) {
        super(fm);
        if (tabs == null) {
            throw new IllegalArgumentException(
                    "tabs can not be null,please initialized it");
        }
        mTabInfos = tabs;
        mContext = context;
    }

    public void addTabInfo(TabInfo tabInfo) {
        mTabInfos.add(tabInfo);
    }

    public void setTabInfo(int index, TabInfo info) {
        mTabInfos.set(index, info);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTabInfos.size();
    }

    public TabInfo getViewPageInfo(int position) {
        return mTabInfos.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        /*每此 notifyDataSetChanged的时候刷新**/
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabInfos.get(position);
        return Fragment.instantiate(mContext, info.clazz.getName(), info.args);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabInfos.get(position).title;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 注释自带的销毁方法防止页面被销毁
        super.destroyItem(container, position, object);
    }

}
