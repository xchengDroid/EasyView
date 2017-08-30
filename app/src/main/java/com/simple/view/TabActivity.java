package com.simple.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.controller.EasyFragment;
import com.xcheng.view.widget.PagerSlidingTabStrip;


public class TabActivity extends EasyActivity {

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;

    @Override
    public int getLayoutId() {
        return R.layout.ac_tab;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.ev_id_viewpager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.ev_id_tab_indicator);
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), this));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mPagerSlidingTabStrip.setIndicatorColor(Color.YELLOW);
                    mPagerSlidingTabStrip.setIndicatorHeight(20);
                    mPagerSlidingTabStrip.setDividerColor(Color.BLUE);
                } else if (position == 1) {
                    mPagerSlidingTabStrip.setIndicatorColor(Color.GREEN);
                    mPagerSlidingTabStrip.setIndicatorHeight(15);
                    mPagerSlidingTabStrip.setDividerColor(Color.CYAN);


                } else if (position == 2) {
                    mPagerSlidingTabStrip.setIndicatorColor(Color.RED);
                    mPagerSlidingTabStrip.setIndicatorHeight(10);
                    mPagerSlidingTabStrip.setDividerColor(Color.LTGRAY);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public static class TabFragment extends EasyFragment {


        @Override
        public int getLayoutId() {
            return R.layout.fr_tab;
        }

        @Override
        public void initView(Bundle savedInstanceState) {
            super.initView(savedInstanceState);
            TextView tv = (TextView) findViewById(R.id.tvTab);
            tv.setText("Tab测试");
        }
    }

    public class ViewPageFragmentAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.TabAdapter {
        protected Context mContext;

        public ViewPageFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 10;
        }

        @Override
        public int getItemPosition(Object object) {
            /**每此 notifyDataSetChanged的时候刷新**/
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(mContext, TabFragment.class.getName(), null);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "位置：" + position;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 注释自带的销毁方法防止页面被销毁
            super.destroyItem(container, position, object);
        }

        @NonNull
        @Override
        public View getTabView(int position) {
            TextView textView = new TextView(getContext());
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(-2,200);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(30, 20, 30, 20);
            textView.setText("位置：" + position);
            return textView;
        }
    }

}
