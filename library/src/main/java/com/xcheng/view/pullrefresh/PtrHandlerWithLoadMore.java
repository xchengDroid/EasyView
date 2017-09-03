package com.xcheng.view.pullrefresh;

import android.view.View;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by cc on 2016/9/26.
 */
public interface PtrHandlerWithLoadMore extends PtrHandler {
    void onLoadMore();
    /**
     * Check can do onLoadMore or not. For example the content is empty or the first child is in view.
     */
    boolean checkCanDoLoadMore(final PtrFrameLayout frame, final View content, final View header);

}
