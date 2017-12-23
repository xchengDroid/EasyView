package com.xcheng.view.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;
import android.view.ViewTreeObserver;

import com.xcheng.view.R;
import com.xcheng.view.adapter.DividerDecoration;
import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.adapter.HFAdapter;
import com.xcheng.view.pullrefresh.LoadingState;
import com.xcheng.view.pullrefresh.PtrDefaultHandlerWithLoadMore;
import com.xcheng.view.pullrefresh.PtrRVFrameLayout;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

import static android.support.v7.widget.RecyclerView.*;
import static com.xcheng.view.pullrefresh.LoadingState.LOADINGMORE;
import static com.xcheng.view.pullrefresh.LoadingState.REFRESHING;

/**
 * 刷新列表Fragment
 * 约定大于配置，
 * 设置PtrRVFrameLayout 的id 为ev_id_ptrRVFrameLayout，
 * 设置RecyclerView 的 id为ev_id_recyclerView
 *
 * @author xincheng @date:2017-9-4
 */
public abstract class EasyRefreshFragment<T> extends EasyFragment implements IPullRefreshView<T>, HFAdapter.OnBindHolderListener {
    protected PtrRVFrameLayout mPtrFrameLayout;
    protected RecyclerView mRecyclerView;
    protected HFAdapter<T> mAdapter;
    private boolean mHasInitView;
    private Config mConfig;

    @Override
    public int getLayoutId() {
        return R.layout.ev_ptr_refresh;
    }

    @CallSuper
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mPtrFrameLayout = findViewById(R.id.ev_id_ptrRVFrameLayout);
        mRecyclerView = findViewById(R.id.ev_id_recyclerView);

        mConfig = getConfig();
        mRecyclerView.setLayoutManager(mConfig.layoutManager);
        mRecyclerView.setItemAnimator(mConfig.itemAnimator);
        ItemDecoration itemDecoration = mConfig.itemDecoration;
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
        mAdapter = createAdapter();
        mAdapter.setHeader(mConfig.headerId);
        mAdapter.setEmpty(mConfig.emptyId);
        mAdapter.setFooter(mConfig.footerId, false);
        mRecyclerView.setAdapter(mAdapter);
        mHasInitView = true;
    }

    /**
     * 创建一个HFAdapter对象
     */
    @NonNull
    protected abstract HFAdapter<T> createAdapter();

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
        //设置header和footer监听
        mAdapter.setOnHolderBindListener(this);
        mPtrFrameLayout.setPtrHandler(new PtrDefaultHandlerWithLoadMore() {
            @Override
            public void onLoadMore() {
                requestData(false);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mPtrFrameLayout.canRefresh()) {
                    requestData(true);
                }
            }
        });
        lazyLoad();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPtrFrameLayout.complete(true, LoadingState.INIT);
    }


    private void lazyLoad() {
        if (!mConfig.autoRefresh || !getUserVisibleHint() || !mHasInitView)
            return;
        if (mAdapter == null || mAdapter.getDataCount() != 0)
            return;
        if (mPtrFrameLayout.getWidth() != 0) {
            mPtrFrameLayout.autoRefresh(true, 1000);
        } else {
            mPtrFrameLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mPtrFrameLayout.autoRefresh(true, 1000);
                    mPtrFrameLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
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
        LoadingState loadingState = LoadingState.INIT;
        if (data == null || data.size() < mConfig.limit) {
            loadingState = LoadingState.NOMORE;
        }
        complete(isRefresh, loadingState);
    }

    @Override
    public void complete(boolean isRefresh, LoadingState state) {
        mPtrFrameLayout.complete(isRefresh, state);
    }


    @Override
    public void onBindHeader(EasyHolder holder, boolean isCreate) {

    }

    @Override
    public void onBindEmpty(EasyHolder holder, boolean isCreate) {

    }

    @Override
    public void onBindFooter(EasyHolder holder, boolean isCreate) {
        //防止不是此布局的情况下报空指针
        if (mConfig.footerId == R.layout.ev_footer_load_more) {
            LoadingState loadingState = mPtrFrameLayout.getLoadingState();
            if (loadingState == LOADINGMORE || loadingState == REFRESHING) {
                holder.setVisible(R.id.ev_id_progressBarLoadMore, View.VISIBLE);
            } else {
                holder.setVisible(R.id.ev_id_progressBarLoadMore, View.INVISIBLE);
            }
            holder.setText(R.id.ev_id_textLoadMore, loadingState.getText());
        }
    }

    /**
     * 设置RecyclerView的配置，footerView headerView emptyView LayoutManager ItemAnimator ItemDecoration等
     */
    public static class Config {
        private final int footerId;
        private final int emptyId;
        private final int headerId;
        private final boolean autoRefresh;
        private final int limit;
        private final LayoutManager layoutManager;
        private final ItemAnimator itemAnimator;
        private final ItemDecoration itemDecoration;

        private Config(Builder builder) {
            this.footerId = builder.footerId;
            this.emptyId = builder.emptyId;
            this.headerId = builder.headerId;
            this.autoRefresh = builder.autoRefresh;
            this.limit = builder.limit;
            this.layoutManager = builder.layoutManager;
            this.itemAnimator = builder.itemAnimator;
            this.itemDecoration = builder.itemDecoration;
        }

        public Builder newBuilder() {
            return new Builder(this);
        }

        public static class Builder {
            private int footerId = R.layout.ev_footer_load_more;
            private int emptyId = 0;
            private int headerId = 0;
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
                this.itemDecoration = new DividerDecoration(ContextCompat.getColor(context, R.color.ev_divider_color), 1);
            }

            private Builder(Config config) {
                this.footerId = config.footerId;
                this.emptyId = config.emptyId;
                this.headerId = config.headerId;
                this.autoRefresh = config.autoRefresh;
                this.limit = config.limit;
                this.layoutManager = config.layoutManager;
                this.itemAnimator = config.itemAnimator;
                this.itemDecoration = config.itemDecoration;
            }

            /**
             * 获取EmptyView 如果为0不设置
             */
            public Builder emptyId(@LayoutRes int emptyId) {
                this.emptyId = emptyId;
                return this;
            }

            /**
             * 获取HeaderView ,如果为0不设置
             */
            public Builder headerId(@LayoutRes int headerId) {
                this.headerId = headerId;
                return this;
            }

            /**
             * 获取FooterView,如果为0不设置
             */
            public Builder footerId(@LayoutRes int footerId) {
                this.footerId = footerId;
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
