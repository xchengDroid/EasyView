package com.xcheng.view.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.xcheng.view.adapter.EasyAdapter;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class PtrRVFrameLayout extends PtrFrameLayout {

    private PtrCommonHeader mPtrCommonHeader;
    private LoadingState mState;
    private PtrHandlerWithLoadMore mPtrHandlerWithLoadMore;
    private RecyclerView mRecyclerView;

    public PtrRVFrameLayout(Context context) {
        this(context, null);
    }

    public PtrRVFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrRVFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        setState(LoadingState.INIT);
        mPtrCommonHeader = new PtrCommonHeader(getContext());
        setHeaderView(mPtrCommonHeader);
        addPtrUIHandler(mPtrCommonHeader);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mContent instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) mContent;
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //  StaggeredGridLayoutManager sp= (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
                    //  sp.invalidateSpanAssignments();
                    if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                        if (canLoadMore() && mPtrHandlerWithLoadMore != null) {
                            setState(LoadingState.LOADINGMORE);
                            mPtrHandlerWithLoadMore.onLoadMore();
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        } else {
            throw new IllegalStateException("PtrFrameLayout must only have a RecyclerView child");
        }
    }

    /**
     * 正在刷新和正在加载更多的情况下都不可以刷新
     * 下拉刷新 mStatus 必须为PTR_STATUS_PREPARE
     * 强制刷新 mStatus 必须为PTR_STATUS_INIT
     * 的情况下才会触发 onRefreshBegin(PtrFrameLayout frame)
     *
     * @return
     */
    public boolean canRefresh() {
        boolean canRefresh = (mState == LoadingState.INIT || mState == LoadingState.NOMORE);
        if (isRefreshing()) {
            if (!canRefresh) {
                super.refreshComplete();
            } else {
                setState(LoadingState.REFRESHING);
            }
        }
        return canRefresh;
    }

    /**
     * 满足加载的物理条件 和外在条件 是否可以加载更多
     *
     * @return
     */
    private boolean canLoadMore() {
        EasyAdapter adapter = getEasyAdapter();
        if (adapter != null && adapter.hasFooter()) {
            //note: footer必须有高度否则  PtrDefaultHandlerWithLoadMore.checkContentCanBePulledUp(this, mContent, getHeaderView())返回false
            return PtrDefaultHandlerWithLoadMore.checkContentCanBePulledUp(this, mContent, getHeaderView()) && (mState == LoadingState.INIT);
        }
        return false;
    }

    /**
     * 强制刷新设置滚动到顶部
     *
     * @param atOnce
     * @param duration
     */
    @Override
    public void autoRefresh(boolean atOnce, int duration) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
        }
        super.autoRefresh(atOnce, duration);
    }

    /**
     * 不设为成员变量 防止在外部被重新设置adapter
     *
     * @return
     */
    @Nullable
    private EasyAdapter getEasyAdapter() {
        if (mRecyclerView != null) {
            return (EasyAdapter) mRecyclerView.getAdapter();
        }
        return null;
    }

    /**
     * 通过对ptrHandler类型的判断 决定是否是需要下拉加载更多操作
     *
     * @param ptrHandler
     */
    @Override
    public void setPtrHandler(PtrHandler ptrHandler) {
        super.setPtrHandler(ptrHandler);
        if (mRecyclerView != null && ptrHandler instanceof PtrHandlerWithLoadMore) {
            mPtrHandlerWithLoadMore = (PtrHandlerWithLoadMore) ptrHandler;
        }
    }

    /**
     * 恢复UI
     *
     * @param isRefresh    是否在刷新
     * @param loadingState 加载状态
     */
    public void complete(boolean isRefresh, LoadingState loadingState) {
        if (isRefresh) {
            super.refreshComplete();
        }
        setState(loadingState);
    }

    private void setState(LoadingState state) {
        this.mState = state;
        EasyAdapter adapter = getEasyAdapter();
        if (adapter != null) {
            //刷新footer和 emptyView
            adapter.notifyFooter();
            adapter.notifyEmpty();
        }
    }

    @NonNull
    public LoadingState getLoadingState() {
        return mState;
    }
}
