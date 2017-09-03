/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xcheng.view.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

/**
 * RecyclerView.Adapter extension created to add header capability support and a generic List of
 * mData really useful most of the cases. You should extend from this class and override
 * onCreateViewHolder to create your ViewHolder instances and onBindViewHolder methods to draw your
 * user interface as you wish.
 * <p/>
 * The usage of List<T> mData member is not mandatory. If you are going to provide your custom
 * implementation remember to override getItemCount method.
 */
public abstract class HFRecyclerAdapter<T> extends EasyRecyclerAdapter<T> {
    //-1为 INVALID_TYPE,不要设置为-1
    public static final int TYPE_HEADER = 0x00000111;
    public static final int TYPE_FOOTER = 0x00000222;
    public static final int TYPE_EMPTY = 0x00000333;
    private View headerView;
    private View footerView;
    private View emptyView;
    private boolean showFooter = true;
    private final int mLength;
    /**
     * 标记是否绑定到RecyclerView
     ***/
    private boolean isAttachToRecycler = false;

    public HFRecyclerAdapter(Context context, List<T> data) {
        this(context, data, 10);
    }

    public HFRecyclerAdapter(Context context, List<T> data, @IntRange(from = 1) int length) {
        super(context, data);
        mLength = length;
    }

    public int getLength() {
        return mLength;
    }

    /**
     * Invokes onCreateHeaderViewHolder, onCreateItemViewHolder or onCreateFooterViewHolder methods
     * based on the view type param.
     */
    @Override
    public final EasyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderType(viewType) || isFooterType(viewType) || isEmptyType(viewType)) {
            // create a new FrameLayout, or inflate from a resource
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            // make sure it fills the space
            int height = isEmptyType(viewType) ? ViewGroup
                    .LayoutParams.MATCH_PARENT : ViewGroup
                    .LayoutParams.WRAP_CONTENT;
            frameLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            return new EasyHolder(frameLayout);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    /**
     * holder 在onCreateViewHolder 和 onBindViewHolder时 holder.itemView no parent
     *
     * @param holder
     * @param position
     */
    @Override
    public final void onBindViewHolder(EasyHolder holder, int position) {
        if (isHeaderPosition(position)) {
            prepareHeaderFooter(holder, headerView);
            if (mOnHolderBindListener != null) {
                mOnHolderBindListener.onHeaderBind(holder);
            }
        } else if (isFooterPosition(position)) {
            prepareHeaderFooter(holder, footerView);
            if (mOnHolderBindListener != null) {
                mOnHolderBindListener.onFooterBind(holder);
            }
        } else if (isEmptyPosition(position)) {
            prepareHeaderFooter(holder, emptyView);
            if (mOnHolderBindListener != null) {
                mOnHolderBindListener.onEmptyBind(holder);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    /**
     * safe deal,keep a same headerView 、footerView or emptyView
     *
     * @param vh
     * @param view
     */
    private void prepareHeaderFooter(EasyHolder vh, View view) {
        ViewGroup viewGroup = vh.getRootView();
        if (viewGroup.getChildCount() == 1 && viewGroup.getChildAt(0) == view) {
            return;
        }
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        // empty out our FrameLayout and replace with our header/footer
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        viewGroup.addView(view);
    }

    @Override
    public final int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return TYPE_HEADER;
        } else if (isFooterPosition(position)) {
            return TYPE_FOOTER;
        } else if (isEmptyPosition(position)) {
            return TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }


    @Override
    public final int getDataOffset() {
        return getHeaderCount();
    }

    @Override
    public int getItemCount() {
        return getDataCount() + getHeaderCount() + getFooterCount() + getEmptyCount();
    }

    public int getHeaderCount() {
        return hasHeader() ? 1 : 0;
    }

    public int getFooterCount() {
        return hasFooter() ? 1 : 0;
    }

    public int getEmptyCount() {
        return hasEmpty() ? 1 : 0;
    }


    /**
     * Get header data in this adapter, you should previously use {@link #setHeader(View header)}
     * in the adapter initialization code to set header data.
     *
     * @return header data
     */
    public View getHeader() {
        return footerView;
    }

    /**
     * If you need a header, you should set header data in the adapter initialization code.
     *
     * @param headerView header data
     */
    public void setHeader(View headerView) {
        if (headerView == null) {
            throw new NullPointerException("the headerView can not be null");
        }
        if (this.headerView != null) {
            throw new IllegalStateException("the headerView already has been set");
        }
        this.headerView = headerView;
        if (isAttachToRecycler && hasHeader()) {
            notifyItemInserted(0);
        }
    }

    public View getEmpty() {
        return emptyView;
    }

    /**
     * If you need a emptyView, you should set header data in the adapter initialization code.
     *
     * @param emptyView header data
     */
    public void setEmpty(View emptyView) {
        if (emptyView == null) {
            throw new NullPointerException("the emptyView can not be null");
        }
        if (this.emptyView != null) {
            throw new IllegalStateException("the emptyView already has been set");
        }
        this.emptyView = emptyView;
        if (isAttachToRecycler && hasEmpty()) {
            notifyItemInserted(getDataOffset());
        }
    }

    public void notifyHeader() {
        if (hasHeader()) {
            notifyItemChanged(0);
        }
    }

    public void notifyEmpty() {
        if (hasEmpty()) {
            notifyItemChanged(getDataOffset());
        }
    }

    public void notifyFooter() {
        if (hasFooter()) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * Get footer data in this adapter, you should previously use {@link #setFooter}
     * in the adapter initialization code to set footer data.
     *
     * @return footer data
     */
    public View getFooter() {
        return footerView;
    }

    /**
     * If you need a footer, you should set footer data in the adapter initialization code.
     */
    public void setFooter(View footerView) {
        if (footerView == null) {
            throw new NullPointerException("the footerView can not be null");
        }
        if (this.footerView != null) {
            throw new IllegalStateException("the footerView already has been set");
        }
        this.footerView = footerView;
        if (isAttachToRecycler && hasFooter()) {
            //如果没有数据添加的情况下调用会crash
            notifyItemInserted(0);
        }
    }


    /**
     * Call this method to show hiding footer.
     */
    public void showFooter() {
        if (!hasFooter()) {
            this.showFooter = true;
            if (hasFooter()) {
                //添加footer之后 count+1,追加至最后一项的后面
                notifyItemInserted(getItemCount() - 1);
            }
        }
    }

    /**
     * Call this method to hide footer.
     */
    public void hideFooter() {
        boolean hasFooter = hasFooter();
        //会影响 hasFooter()的结果，故之后赋值
        this.showFooter = false;
        if (hasFooter) {
            notifyItemRemoved(getItemCount());
        }
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to 0 and the adapter
     * has a not null header already configured.
     */
    public boolean isHeaderPosition(int position) {
        return hasHeader() && position == 0;
    }

    public boolean isEmptyPosition(int position) {
        return hasEmpty() && ((hasHeader() && position == 1) || (!hasHeader() && position == 0));
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to
     * <code>getItemCount() - 1</code>
     * and the adapter has a not null header already configured.
     */
    public boolean isFooterPosition(int position) {
        int lastPosition = getItemCount() - 1;
        return hasFooter() && position == lastPosition;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_HEADER.
     */
    protected boolean isHeaderType(int viewType) {
        return viewType == TYPE_HEADER;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_FOOTER.
     */
    protected boolean isFooterType(int viewType) {
        return viewType == TYPE_FOOTER;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_FOOTER.
     */
    protected boolean isEmptyType(int viewType) {
        return viewType == TYPE_EMPTY;
    }

    /**
     * Returns true if the header configured is not null.
     */
    public boolean hasHeader() {
        return headerView != null;
    }


    public boolean hasEmpty() {
        return emptyView != null && isEmpty();
    }

    /**
     * Returns true if the footer configured is not null.
     */
    public boolean hasFooter() {
        return footerView != null && showFooter && !isEmpty() && (getDataCount() >= mLength)/*不满一屏不加载*/;
    }

    private void validateItems(List<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("You can't use a null List<Item> instance.");
        }
    }

    private boolean isFullSpan(int position) {
        return isHeaderPosition(position) || isFooterPosition(position) || isEmptyPosition(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        isAttachToRecycler = true;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridSpanSizeLookup(gridManager));
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        isAttachToRecycler = false;
    }

    @Override
    public void onViewAttachedToWindow(EasyHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isFullSpan(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sp = (StaggeredGridLayoutManager.LayoutParams) lp;
                sp.setFullSpan(true);
            }
        }
    }

    public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        private final GridLayoutManager layoutManager;

        public GridSpanSizeLookup(GridLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            boolean isFullSpan = isFullSpan(position);
            int type = getItemViewType(position);
            if (mSpanSizeLookup == null) {
                return isFullSpan ? layoutManager.getSpanCount() : 1;
            } else {
                return isFullSpan ? layoutManager.getSpanCount() : mSpanSizeLookup.getSpanSize(layoutManager, getPositionOfData(position));
            }
        }
    }

    /**
     * param to be used to query number of spans occupied by each item
     */
    private SpanSizeLookup mSpanSizeLookup;

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    private OnHolderBindListener mOnHolderBindListener;

    public void setOnHolderBindListener(OnHolderBindListener onHolderBindListener) {
        this.mOnHolderBindListener = onHolderBindListener;
    }

    /**
     * 当 onBindViewHolder 调用的时候回调此函数中的方法
     */
    public interface OnHolderBindListener {
        void onHeaderBind(EasyHolder holder);

        void onEmptyBind(EasyHolder holder);

        void onFooterBind(EasyHolder holder);
    }
}
