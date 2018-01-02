package com.xcheng.view.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.xcheng.view.adapter.HFAdapter;
import com.xcheng.view.util.EasyPreconditions;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class PtrRVFrameLayout extends PtrFrameLayout {

    private UIState mState;
    private boolean mRefreshWhenDetached;
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            //ViewPager fragment onDetach 如果此时处于下拉加载中，之后重新切换回来，UI状态复原
            if (mRefreshWhenDetached && mState.canRefresh()) {
                refreshComplete();
            }
        } catch (Exception ignore) {
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRefreshWhenDetached = isRefreshing();
    }

    private void initViews() {
        setState(UIState.INIT);
        PtrCommonHeader commonHeader = new PtrCommonHeader(getContext());
        setHeaderView(commonHeader);
        addPtrUIHandler(commonHeader);
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
                            setState(UIState.LOADING_MORE);
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
        boolean canRefresh = mState.canRefresh();
        //isRefreshing()为true 如果在 onRefreshBegin(PtrFrameLayout frame) 里面调用
        if (isRefreshing() && canRefresh) {
            setState(UIState.REFRESHING);
        }
        return canRefresh;
    }

    /**
     * 满足加载的物理条件 和外在条件 是否可以加载更多
     *
     * @return true 可以加载更多,其他false
     */
    private boolean canLoadMore() {
        HFAdapter adapter = getEasyAdapter();
        if (adapter != null && adapter.hasFooter()) {
            //note: footer必须有高度否则  PtrDefaultHandlerWithLoadMore.checkContentCanBePulledUp(this, mContent, getHeaderView())返回false
            return PtrDefaultHandlerWithLoadMore.checkContentCanBePulledUp(this, mContent, getHeaderView()) && mState.canLoadMore();
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
    private HFAdapter getEasyAdapter() {
        if (mRecyclerView != null) {
            return (HFAdapter) mRecyclerView.getAdapter();
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
     * @param isRefresh 是否在刷新
     * @param state     组件状态
     */
    public void complete(boolean isRefresh, @NonNull UIState state) {
        if (isRefresh) {
            super.refreshComplete();
        }
        setState(state);
    }


    /**
     * 统一入口赋值
     */
    private void setState(UIState state) {
        EasyPreconditions.checkNotNull(state, "state==null");
        this.mState = state;
        HFAdapter adapter = getEasyAdapter();
        if (adapter != null) {
            //刷新footer和 emptyView
            adapter.notifyFooter();
            adapter.notifyEmpty();
        }
    }

    @NonNull
    public UIState getState() {
        return mState;
    }
}
