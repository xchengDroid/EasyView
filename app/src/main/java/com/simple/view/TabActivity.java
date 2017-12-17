package com.simple.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.controller.EasyFragment;
import com.xcheng.view.controller.EasyPagerActivity;

import java.util.List;


public class TabActivity extends EasyPagerActivity {


    @Override
    public int getLayoutId() {
        return R.layout.ac_tab;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mIndicator.setIndicatorColor(Color.YELLOW);
                    mIndicator.setIndicatorHeight(20);
                    mIndicator.setDividerColor(Color.BLUE);
                } else if (position == 1) {
                    mIndicator.setIndicatorColor(Color.GREEN);
                    mIndicator.setIndicatorHeight(15);
                    mIndicator.setDividerColor(Color.CYAN);


                } else if (position == 2) {
                    mIndicator.setIndicatorColor(Color.RED);
                    mIndicator.setIndicatorHeight(10);
                    mIndicator.setDividerColor(Color.LTGRAY);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void getTabInfos(List<TabInfo> tabInfos) {
        tabInfos.add(new TabInfo("0", "新12312闻", TabFragment.class));
        tabInfos.add(new TabInfo("1", "咨123213询", TabFragment.class));
        tabInfos.add(new TabInfo("2", "视频", TabFragment.class));
        tabInfos.add(new TabInfo("3", "本123地", TabFragment.class));
        tabInfos.add(new TabInfo("4", "小说", TabFragment.class));
        tabInfos.add(new TabInfo("5", "阅读", TabFragment.class));
        tabInfos.add(new TabInfo("6", "本地", TabFragment.class));
        tabInfos.add(new TabInfo("7", "贴吧", TabFragment.class));
        tabInfos.add(new TabInfo("8", "评论", TabFragment.class));

    }

    @NonNull
    @Override
    public View createTabView(int position, TabInfo tabInfo) {
        TextView textView = new TextView(getContext());
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, 200);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(30, 20, 30, 20);
        textView.setText(tabInfo.title);
        return textView;
    }


    public static class TabFragment extends EasyFragment {


        @Override
        public int getLayoutId() {
            return R.layout.fr_tab;
        }

        @Override
        public void initView(Bundle savedInstanceState) {
            super.initView(savedInstanceState);
            TextView tv = findViewById(R.id.tvTab);
            tv.setText(getArguments().getString("tag"));
        }
    }

}
