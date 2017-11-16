package com.xcheng.view.controller;

import android.os.Bundle;
import android.support.annotation.CallSuper;
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
import com.xcheng.view.adapter.HFAdapter;
import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.pullrefresh.LoadingState;
import com.xcheng.view.pullrefresh.PtrDefaultHandlerWithLoadMore;
import com.xcheng.view.pullrefresh.PtrRVFrameLayout;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

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
public abstract class EasyRefreshFragment<T> extends EasyFragment implements IPullRefreshView<T> {
    protected PtrRVFrameLayout mPtrFrameLayout;
    protected RecyclerView mRecyclerView;
    protected HFAdapter<T> mAdapter;
    private boolean mHasInitView;

    @Override
    public int getLayoutId() {
        return R.layout.ev_ptr_refresh;
    }

    @CallSuper
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //子类如果要自己实现Presenter,可以在onCreate方法里面调用setPresenter方法
        mPtrFrameLayout = (PtrRVFrameLayout) findViewById(R.id.ev_id_ptrRVFrameLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.ev_id_recyclerView);

        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setItemAnimator(getItemAnimator());
        ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
        mAdapter = getHFAdapter();
        mAdapter.setHeader(getHeaderId());
        mAdapter.setEmpty(getEmptyId());
        mAdapter.setFooter(getFooterId(), false);
        mRecyclerView.setAdapter(mAdapter);
        mHasInitView = true;
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

    @Override
    public boolean isAutoRefresh() {
        return true;
    }

    private void lazyLoad() {
        if (!isAutoRefresh() || !getUserVisibleHint() || !mHasInitView)
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
        if (data == null || data.size() < getLimit()) {
            loadingState = LoadingState.NOMORE;
        }
        complete(isRefresh, loadingState);
    }

    @Override
    public int getLimit() {
        return 10;
    }

    @Override
    public void complete(boolean isRefresh, LoadingState state) {
        mPtrFrameLayout.complete(isRefresh, state);
    }

    @NonNull
    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Nullable
    @Override
    public ItemDecoration getItemDecoration() {
        return new DividerDecoration(ContextCompat.getColor(getContext(), R.color.ev_divider_color), 1);
    }

    @Nullable
    @Override
    public RecyclerView.ItemAnimator getItemAnimator() {
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        // 取消notifyItemChanged动画
        defaultItemAnimator.setSupportsChangeAnimations(false);
        return defaultItemAnimator;
    }

    @Override
    public int getHeaderId() {
        return 0;
    }

    @Override
    public int getEmptyId() {
        return 0;
    }

    @Override
    public int getFooterId() {
        return R.layout.ev_footer_load_more;
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
        if (getFooterId() == R.layout.ev_footer_load_more) {
            LoadingState loadingState = mPtrFrameLayout.getLoadingState();
            if (loadingState == LOADINGMORE || loadingState == REFRESHING) {
                holder.setVisible(R.id.ev_id_progressBarLoadMore, true);
            } else {
                holder.setVisible(R.id.ev_id_progressBarLoadMore, false, false);
            }
            holder.setText(R.id.ev_id_textLoadMore, loadingState.getText());
        }
    }
}
