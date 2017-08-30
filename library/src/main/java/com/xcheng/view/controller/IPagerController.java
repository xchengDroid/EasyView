package com.xcheng.view.controller;

import android.support.annotation.NonNull;
import android.view.View;

import com.xcheng.view.adapter.ViewPageInfo;

import java.util.List;

/**
 * @author xincheng
 */
public interface IPagerController {
    int DEFAULT_PAGE_LIMIT = 1;

    /**
     * 设置ViewPager预加载页数
     */
    int getScreenPageLimit();

    /**
     * 设置Tab页面加载的每个页面
     */
    void getViewPageInfos(final List<ViewPageInfo> viewPageInfos);

    /**
     * @param position     当前Tab指针的位置
     * @param viewPageInfo 对应Tab的信息
     * @return
     */
    @NonNull
    View createTabView(int position, ViewPageInfo viewPageInfo);

}
