package com.xcheng.view.pullrefresh;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public abstract class PtrDefaultHandlerWithLoadMore extends PtrDefaultHandler implements PtrHandlerWithLoadMore {

    /**如果child底部有内容滚动被隐藏了 返回true
     * @param childView
     * @return
     */
    public static boolean canChildScrollDown(View childView) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (childView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) childView;
                Adapter adapter = absListView.getAdapter();
                if (adapter != null && adapter.getCount() != 0) {
                    int itemCount = adapter.getCount();
                    View lastChild = absListView.getChildAt(absListView
                            .getChildCount() - 1);
                    return absListView.getLastVisiblePosition() < itemCount - 1 || lastChild.getBottom() > absListView.getHeight() - absListView
                            .getPaddingBottom();
                }
                return false;
            } else {
                return ViewCompat.canScrollVertically(childView, 1) || childView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(childView, 1);
        }
    }

    /**
     * Default implement for check can perform pull to refresh
     *
     * @param content
     * @param header
     * @return
     */
    public static boolean checkContentCanBePulledUp(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollDown(content);
    }

    @Override
    public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View header) {
        return checkContentCanBePulledUp(frame, content, header);
    }
}