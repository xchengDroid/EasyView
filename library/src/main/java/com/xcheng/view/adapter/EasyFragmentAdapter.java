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

    public TabInfo getTabInfo(int position) {
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

    /**
     * 当ViewPager调用setAdapter的时候是否重新创建之前原位置的Fragment
     *
     * @return false
     */
    public boolean isRecreateWhenSetAdapter(int position, TabInfo tabInfo) {
        return false;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        if (isRecreateWhenSetAdapter(position, getTabInfo(position)))
            removeFragment(container, position);
        return super.instantiateItem(container, position);
    }

    private void removeFragment(ViewGroup container, int index) {
        Fragment fragment = findFragment(container, index);
        if (fragment == null)
            return;
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.remove(fragment);
        ft.commit();
        mFragmentManager.executePendingTransactions();
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
