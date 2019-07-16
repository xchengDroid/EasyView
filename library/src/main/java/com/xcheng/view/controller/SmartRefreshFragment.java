package com.xcheng.view.controller;

import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.xcheng.view.R;
import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.enums.SmartState;

import java.util.List;

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
    protected Config mConfig;

    @Override
    public int getLayoutId() {
        return R.layout.ev_smart_refresh;
    }

    @CallSuper
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //TODO 重写此方法设置RecyclerView 和 SmartRefreshLayout的属性
        mConfig = getConfig();
        mSmartRefreshLayout = findViewById(R.id.ev_id_smartRefreshLayout);
        mRecyclerView = findViewById(R.id.ev_id_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = createAdapter());
        onSmartStateChanged(SmartState.NONE);
    }

    /**
     * 子类重写修改配置
     */
    @NonNull
    protected Config getConfig() {
        return Config.DEFAULT;
    }

    /**
     * 创建一个HFAdapter对象
     */
    @NonNull
    protected abstract EasyAdapter<T> createAdapter();


    /**
     * 子类如果需要监听状态可以重写此函数
     */
    protected void onSmartStateChanged(SmartState smartState) {
    }


    @Override
    public void setListener() {
        super.setListener();
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                requestData(true);
            }

            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
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
            public void onLoadMore(RefreshLayout refreshlayout) {
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
    }

    @Override
    protected void onLazyLoad() {
        if (!mConfig.autoRefresh)
            return;
        if (mAdapter == null || mAdapter.getDataCount() != 0)
            return;
        mSmartRefreshLayout.autoRefresh(mConfig.autoRefreshDelayed);
    }

    @Override
    public void refreshView(boolean isRefresh, List<? extends T> data) {
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
            mSmartRefreshLayout.finishRefresh(0, success);
            mSmartRefreshLayout.setNoMoreData(noMoreData);
        } else {
            mSmartRefreshLayout.finishLoadMore(0, success, noMoreData);
        }
    }

    public static class Config {
        public static final Config DEFAULT = new Config();
        public final boolean autoRefresh;
        public final int autoRefreshDelayed;
        public final int limit;

        public Config(boolean autoRefresh, int autoRefreshDelayed, @IntRange(from = 1) int limit) {
            this.autoRefresh = autoRefresh;
            this.autoRefreshDelayed = autoRefreshDelayed;
            this.limit = limit;
        }

        public Config(int autoRefreshDelayed, @IntRange(from = 1) int limit) {
            this(true, autoRefreshDelayed, limit);
        }

        public Config() {
            this(true, 200, 10);
        }
    }
}
