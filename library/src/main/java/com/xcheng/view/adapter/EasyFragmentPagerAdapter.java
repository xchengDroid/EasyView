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
    private final List<ViewPageInfo> mTabInfos;

    public EasyFragmentPagerAdapter(FragmentManager fm, Context context,
                                    List<ViewPageInfo> tabs) {
        super(fm);
        if (tabs == null) {
            throw new IllegalArgumentException(
                    "tabs can not be null,please initialized it");
        }
        mTabInfos = tabs;
        mContext = context;
    }

    public void addInfo(String title, String tag, Class<?> clazz) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, clazz);
        mTabInfos.add(viewPageInfo);
    }

    public void setInfo(int index, ViewPageInfo info) {
        mTabInfos.set(index, info);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTabInfos.size();
    }

    public ViewPageInfo getViewPageInfo(int position) {
        return mTabInfos.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        /*每此 notifyDataSetChanged的时候刷新**/
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = mTabInfos.get(position);
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
