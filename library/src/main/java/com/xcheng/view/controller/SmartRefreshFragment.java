package com.xcheng.view.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.xcheng.view.R;
import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.adapter.SpaceDecoration;
import com.xcheng.view.enums.SmartState;

import java.util.List;

import static android.support.v7.widget.RecyclerView.ItemAnimator;
import static android.support.v7.widget.RecyclerView.LayoutManager;

/**
 * 刷新列表Fragment
 * 约定大于配置，
 * SmartRefreshLayout 的id ev_id_smartRefreshLayout，
 * 设置RecyclerView 的 id为ev_id_recyclerView
 *
 * @author xincheng @date:2017-9-4
 */
public abstract class SmartRefreshFragment<T> extends EasyFragment implements IPullRefreshView<T> {
    protected SmartRefreshLayout mSmartRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected EasyAdapter<T> mAdapter;
    private boolean mHasInitView;
    private Config mConfig;

    @Override
    public int getLayoutId() {
        return R.layout.ev_smart_refresh;
    }

    @CallSuper
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mConfig = getConfig();
        mSmartRefreshLayout = findViewById(R.id.ev_id_smartRefreshLayout);
        mSmartRefreshLayout.setEnableLoadmore(mConfig.enableLoadMore);
        mRecyclerView = findViewById(R.id.ev_id_recyclerView);

        mRecyclerView.setLayoutManager(mConfig.layoutManager);
        mRecyclerView.setItemAnimator(mConfig.itemAnimator);
        ItemDecoration itemDecoration = mConfig.itemDecoration;
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);
        onSmartStateChanged(SmartState.NONE);
        mHasInitView = true;
    }

    /**
     * 创建一个HFAdapter对象
     */
    @NonNull
    protected abstract EasyAdapter<T> createAdapter();


    /**
     * 创建一个HFAdapter对象
     */
    protected void onSmartStateChanged(SmartState smartState) {

    }

    /**
     * 子类重写修改配置
     */
    @NonNull
    protected Config getConfig() {
        return new Config.Builder(getContext()).build();
    }

    @Override
    public void setListener() {
        super.setListener();
        mSmartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                requestData(true);
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                requestData(false);
            }
        });
        mSmartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                onSmartStateChanged(SmartState.REFRESHING);
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                onSmartStateChanged(SmartState.REFRESH_FINISHED);
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                onSmartStateChanged(SmartState.LOADING_MORE);

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                onSmartStateChanged(SmartState.LOADING_FINISHED);
            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
                if (newState == RefreshState.None) {
                    onSmartStateChanged(SmartState.NONE);
                }
            }
        });
        lazyLoad();
    }

    private void lazyLoad() {
        if (!getUserVisibleHint() || !mHasInitView || !mConfig.autoRefresh)
            return;
        if (mAdapter == null || mAdapter.getDataCount() != 0)
            return;
        mSmartRefreshLayout.autoRefresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }

    @Override
    public void refreshView(boolean isRefresh, List<T> data) {
        if (isRefresh) {
            mAdapter.refresh(data);
        } else {
            mAdapter.addData(data);
        }
        boolean noMoreData = data == null || data.size() < mConfig.limit;
        complete(isRefresh, true, noMoreData);
    }

    @Override
    public void complete(boolean isRefresh, boolean success, boolean noMoreData) {
        if (isRefresh) {
            mSmartRefreshLayout.finishRefresh(200, success);
            mSmartRefreshLayout.setLoadmoreFinished(noMoreData);
        } else {
            mSmartRefreshLayout.finishLoadmore(200, success, noMoreData);
        }
    }

    /**
     * 设置RecyclerView的配置，footerView headerView emptyView LayoutManager ItemAnimator ItemDecoration等
     */
    public static class Config {
        private final boolean autoRefresh;
        private final boolean enableLoadMore;

        private final int limit;
        private final LayoutManager layoutManager;
        private final ItemAnimator itemAnimator;
        private final ItemDecoration itemDecoration;

        private Config(Builder builder) {
            this.autoRefresh = builder.autoRefresh;
            this.enableLoadMore = builder.enableLoadMore;
            this.limit = builder.limit;
            this.layoutManager = builder.layoutManager;
            this.itemAnimator = builder.itemAnimator;
            this.itemDecoration = builder.itemDecoration;
        }

        public Builder newBuilder() {
            return new Builder(this);
        }

        public static class Builder {
            private boolean enableLoadMore = true;
            private boolean autoRefresh = true;
            private int limit = 10;
            private LayoutManager layoutManager;
            private ItemAnimator itemAnimator;
            private ItemDecoration itemDecoration;


            public Builder(Context context) {
                this.layoutManager = new LinearLayoutManager(context);
                DefaultItemAnimator defaultAnimator = new DefaultItemAnimator();
                // 取消notifyItemChanged动画
                defaultAnimator.setSupportsChangeAnimations(false);
                this.itemAnimator = defaultAnimator;
                this.itemDecoration = new SpaceDecoration(1);
            }

            private Builder(Config config) {
                this.enableLoadMore = config.enableLoadMore;
                this.autoRefresh = config.autoRefresh;
                this.limit = config.limit;
                this.layoutManager = config.layoutManager;
                this.itemAnimator = config.itemAnimator;
                this.itemDecoration = config.itemDecoration;
            }

            /**
             * 获取EmptyView 如果为0不设置
             */
            public Builder enableLoadMore(boolean enableLoadMore) {
                this.enableLoadMore = enableLoadMore;
                return this;
            }

            /**
             * @param limit 分页长度
             */
            public Builder limit(@IntRange(from = 1) int limit) {
                this.limit = limit;
                return this;
            }

            public Builder layoutManager(@NonNull LayoutManager layoutManager) {
                this.layoutManager = layoutManager;
                return this;
            }

            /**
             * 为null的情况下 没有ItemAnimator
             **/
            public Builder animator(@Nullable ItemAnimator animator) {
                this.itemAnimator = animator;
                return this;
            }

            /**
             * 为null的情况下 不设置ItemDecoration
             **/
            public Builder decoration(@Nullable ItemDecoration decoration) {
                this.itemDecoration = decoration;
                return this;
            }

            public Builder autoRefresh(boolean autoRefresh) {
                this.autoRefresh = autoRefresh;
                return this;
            }

            public Config build() {
                return new Config(this);
            }
        }
    }
}
