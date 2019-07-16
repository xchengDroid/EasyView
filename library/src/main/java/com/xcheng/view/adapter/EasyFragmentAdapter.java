package com.xcheng.view.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class EasyFragmentAdapter extends FragmentPagerAdapter {
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

    public TabInfo getTabInfo(int position) {
        return mTabInfos.get(position);
    }

    /**
     * called {@link #notifyDataSetChanged()} if tabInfos has changed
     */
    public List<TabInfo> getTabInfos() {
        return mTabInfos;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTabInfos.size();
    }


    @Override
    public int getItemPosition(Object object) {
        /*每次 notifyDataSetChanged的时候刷新**/
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


    public void removeFragment(ViewGroup container, int index, boolean allowingStateLoss) {
        Fragment fragment = findFragment(container, index);
        if (fragment == null)
            return;
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.remove(fragment);
        if (allowingStateLoss) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }
        mFragmentManager.executePendingTransactions();
    }

    public void removeAllFragment(ViewGroup container, boolean allowingStateLoss) {
        for (int index = 0; index < getCount(); index++) {
            removeFragment(container, index, allowingStateLoss);
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
