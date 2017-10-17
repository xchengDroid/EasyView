package com.xcheng.view.controller;

import android.support.annotation.NonNull;
import android.view.View;

import com.xcheng.view.adapter.TabInfo;

import java.util.List;

/**
 * @author xincheng
 */
public interface IPagerView {
    int DEFAULT_PAGE_LIMIT = 1;

    /**
     * 设置ViewPager预加载页数
     */
    int getScreenPageLimit();

    /**
     * 设置Tab页面加载的每个页面
     */
    void getTabInfos(final List<TabInfo> tabInfos);


    /**
     * @param position 当前Tab指针的位置
     * @param tabInfo  对应Tab的信息
     * @return
     */
    @NonNull
    View createTabView(int position, TabInfo tabInfo);

    /**
     * 当ViewPager调用setAdapter的时候是否重新创建之前原位置的Fragment
     *
     * @return true 重新创建,否则 false
     */
    boolean isRecreateWhenSetAdapter(int position, TabInfo tabInfo);
}
