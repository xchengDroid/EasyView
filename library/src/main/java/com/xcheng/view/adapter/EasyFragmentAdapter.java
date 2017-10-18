package com.xcheng.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.xcheng.view.widget.PagerSlidingTabStrip;

import java.util.List;

public abstract class EasyFragmentAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.TabAdapter {
    private Context mContext;
    private final List<TabInfo> mTabInfos;
    private FragmentManager mFragmentManager;

    public EasyFragmentAdapter(FragmentManager fm, Context context,
                               List<TabInfo> tabs) {
        super(fm);
        if (tabs == null) {
            throw new IllegalArgumentException(
                    "tabs can not be null,please initialized it");
        }
        mFragmentManager = fm;
        mTabInfos = tabs;
        mContext = context;
    }

    /**
     * called {@link #notifyDataSetChanged()} after this method
     */
    public void addTabInfo(TabInfo tabInfo) {
        mTabInfos.add(tabInfo);
    }

    /**
     * called {@link #notifyDataSetChanged()} after this method
     */
    public void removeTabInfo(int index) {
        mTabInfos.remove(index);
    }

    /**
     * called {@link #notifyDataSetChanged()} after this method
     */
    public void setTabInfo(int index, TabInfo info) {
        mTabInfos.set(index, info);
    }

    public TabInfo getTabInfo(int position) {
        return mTabInfos.get(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTabInfos.size();
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


    public void removeFragment(ViewGroup container, int index) {
        Fragment fragment = findFragment(container, index);
        if (fragment == null)
            return;
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.remove(fragment);
        ft.commit();
        mFragmentManager.executePendingTransactions();
    }

    public void removeAllFragment(ViewGroup container) {
        for (int index = 0; index < getCount(); index++) {
            removeFragment(container, index);
        }
    }

    /**
     * @param container ViewPager
     * @param position  位置
     * @return Fragment
     */
    @Nullable
    public Fragment findFragment(ViewGroup container, int position) {
        String tag = "android:switcher:" + container.getId() + ":" + getItemId(position);
        return mFragmentManager.findFragmentByTag(tag);
    }
}
