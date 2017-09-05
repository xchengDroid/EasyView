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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.R;
import com.xcheng.view.adapter.DividerDecoration;
import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.adapter.HFRecyclerAdapter;
import com.xcheng.view.pullrefresh.LoadingState;
import com.xcheng.view.pullrefresh.PtrDefaultHandlerWithLoadMore;
import com.xcheng.view.pullrefresh.PtrRVFrameLayout;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.xcheng.view.pullrefresh.LoadingState.LOADINGMORE;
import static com.xcheng.view.pullrefresh.LoadingState.REFRESHING;

/**
 * 刷新列表Fragment
 *
 * @author xincheng @date:2014-8-4
 */
public abstract class EasyRefreshFragment<T> extends EasyFragment implements IPullRefreshView<T> {
    protected PtrRVFrameLayout mPtrFrameLayout;
    protected RecyclerView mRecyclerView;
    protected HFRecyclerAdapter<T> mHFAdapter;
    private boolean mHasInitView;

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
        mHFAdapter = getHFAdapter();
        //设置header和footer监听
        mHFAdapter.setOnHolderBindListener(this);
        View headerView = getHeaderView(mRecyclerView);
        if (headerView != null) {
            mHFAdapter.setHeader(headerView);
        }
        View emptyView = getEmptyView(mRecyclerView);
        if (emptyView != null) {
            mHFAdapter.setEmpty(emptyView);
        }
        View footerView = getFooterView(mRecyclerView);
        if (footerView != null) {
            mHFAdapter.setFooter(footerView);
        }
        mRecyclerView.setAdapter(mHFAdapter);
        mHasInitView = true;
    }

    @Override
    public void setListener() {
        super.setListener();
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
    }

    @Override
    public void refreshView(boolean isRefresh, List<T> data) {
        if (isRefresh) {
            mHFAdapter.refresh(data);
        } else {
            mHFAdapter.addData(data);
        }
        LoadingState loadingState = LoadingState.INIT;
        if (data == null || data.size() < mHFAdapter.getLength()) {
            loadingState = LoadingState.NOMORE;
        }
        complete(isRefresh, loadingState);
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

    @Nullable
    @Override
    public View getHeaderView(ViewGroup parent) {
        return null;
    }

    @Nullable
    @Override
    public View getEmptyView(ViewGroup parent) {
        return null;
    }

    @Nullable
    @Override
    public View getFooterView(ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(R.layout.ev_footer_load_more, parent, false);
    }

    @Override
    public void onBindHeader(EasyHolder holder) {

    }

    @Override
    public void onBindEmpty(EasyHolder holder) {
        Log.e("print", "onBindEmpty");
    }

    @Override
    public void onBindFooter(EasyHolder holder) {
        LoadingState loadingState = mPtrFrameLayout.getLoadingState();
        if (loadingState == LOADINGMORE || loadingState == REFRESHING) {
            holder.setVisible(R.id.ev_id_progressBarLoadMore, true);
        } else {
            holder.setVisible(R.id.ev_id_progressBarLoadMore, false, false);
        }
        holder.setText(R.id.ev_id_textLoadMore, loadingState.getText());
    }
}
