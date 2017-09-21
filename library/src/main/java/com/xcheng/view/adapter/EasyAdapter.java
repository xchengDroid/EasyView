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
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.util.EasyPreconditions;

import java.util.Collection;
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
public abstract class EasyAdapter<T> extends TAdapter<T> {
    /**
     * param to be used to query number of spans occupied by each item
     */
    private SpanSizeLookup mSpanSizeLookup;
    private OnBindHolderListener mOnBindHolderListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    //-1为 INVALID_TYPE,不要设置为-1
    public static final int TYPE_HEADER = 0x00000111;
    public static final int TYPE_FOOTER = 0x00000222;
    public static final int TYPE_EMPTY = 0x00000333;
    @LayoutRes
    private int mHeaderId;
    @LayoutRes
    private int mFooterId;
    @LayoutRes
    private int mEmptyId;

    private boolean mShowFooter = true;

    /**
     * 标记是否绑定到RecyclerView
     */
    private boolean mAttachToRecycler = false;

    public EasyAdapter(Context context, @Nullable List<T> data) {
        super(context, data);
    }

    public EasyAdapter(Context context, @LayoutRes int layoutId) {
        super(context, layoutId);
    }

    public EasyAdapter(Context context, @Nullable List<T> data, @LayoutRes int layoutId) {
        super(context, data, layoutId);
    }

    /**
     * 获取在data数据列表上的位置
     *
     * @param adapterPosition onBindViewHolder 所指定的position
     * @return position in mData
     */
    public int getPositionOfData(@IntRange(from = 0) int adapterPosition) {
        return adapterPosition - getHeaderCount();
    }

    /**
     * 刷新数据
     */
    public void refresh(@Nullable Collection<? extends T> newData) {
        getData().clear();
        if (newData != null && newData.size() > 0) {
            /** 刷新数据 */
            getData().addAll(newData);
        }
        notifyDataSetChanged();
    }

    /**
     * notifyItemRangeInserted notifyItemRemoved notifyItemChanged 时必须要保持数据修改和操作的一致性，否则会越位时会报错，
     * // boolean validateViewHolderForOffsetPosition(ViewHolder holder),每次都会检测
     * if (holder.mPosition < 0 || holder.mPosition >= mAdapter.getItemCount()) {
     * throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder "
     * + "adapter position" + holder);
     * }
     * <p>
     * add new data to the end of mData
     *
     * @param loadMore the new data collection
     */
    public void addData(Collection<? extends T> loadMore) {
        addData(getDataCount(), loadMore);
    }

    /**
     * add new data in to certain locations
     *
     * @param position the position insert into mData
     */
    public void addData(@IntRange(from = 0) int position, Collection<? extends T> loadMore) {
        if (loadMore != null && loadMore.size() > 0) {
            byte oldFlag = HEFViewFlag();
            getData().addAll(position, loadMore);
            if (oldFlag == HEFViewFlag()) {
                notifyItemRangeInserted(getHeaderCount() + position, loadMore.size());
            } else {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * mData的变化会影响HeaderView mFooterView mEmptyView 是否显示问题，
     * 导致getItemCount发生变化
     *
     * @return flag
     */
    private byte HEFViewFlag() {
        byte flag = 0x0;
        if (hasHeader()) {
            flag |= 0x1;
        }
        if (hasEmpty()) {
            flag |= 0x2;
        }
        if (hasFooter()) {
            flag |= 0x4;
        }
        return flag;
    }

    public void add(T item) {
        add(getDataCount(), item);
    }

    /**
     * 追加数据
     *
     * @param position the  position insert into mData
     * @param item     要追加的数据
     */
    public void add(@IntRange(from = 0) int position, T item) {
        if (item != null) {
            byte oldFlag = HEFViewFlag();
            getData().add(position, item);
            if (oldFlag == HEFViewFlag()) {
                notifyItemInserted(getHeaderCount() + position);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void remove(T item) {
        if (item != null) {
            int index = getData().indexOf(item);
            remove(index);
        }
    }

    /**
     * 删除某条数据
     *
     * @param position the  position in mData
     */
    public void remove(@IntRange(from = 0) int position) {
        if (position >= 0 && position < getDataCount()) {
            byte oldFlag = HEFViewFlag();
            getData().remove(position);
            if (oldFlag == HEFViewFlag()) {
                notifyItemRemoved(getHeaderCount() + position);
            } else {
                //compatible mEmptyView mHeaderView and mFooterView control
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return TYPE_HEADER;
        } else if (isEmptyPosition(position)) {
            return TYPE_EMPTY;
        } else if (isFooterPosition(position)) {
            return TYPE_FOOTER;
        }
        int positionOfData = getPositionOfData(position);
        return getDelegateType(getItem(positionOfData), positionOfData);
    }

    /**
     * Invokes onCreateHeaderViewHolder, onCreateItemViewHolder or onCreateFooterViewHolder methods
     * based on the view type param.
     */
    @Override
    public final EasyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new EasyHolder(inflater(mHeaderId, parent));
            case TYPE_EMPTY:
                return new EasyHolder(inflater(mEmptyId, parent));
            case TYPE_FOOTER:
                return new EasyHolder(inflater(mFooterId, parent));
            default:
                View itemView = getDelegateView(parent, viewType);
                EasyPreconditions.checkState(itemView != null, "you can set a layoutId in construct method or override getDelegateView(parent,viewType) and return a NonNull itemView");
                EasyHolder holder = new EasyHolder(itemView);
                bindClickListener(holder);
                return holder;
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
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case TYPE_HEADER:
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindHeader(holder);
                }
                break;
            case TYPE_EMPTY:
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindEmpty(holder);
                }
                break;
            case TYPE_FOOTER:
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindFooter(holder);
                }
                break;
            default:
                int positionOfData = getPositionOfData(position);
                convert(holder, getItem(positionOfData), positionOfData);
                break;
        }
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
     * If you need a header, you should set header data in the adapter initialization code.
     *
     * @param headerId header data
     */
    public void setHeader(@LayoutRes int headerId) {
        EasyPreconditions.checkState(mHeaderId == 0, "the mHeaderView already has been set");
        mHeaderId = headerId;
        if (mAttachToRecycler && hasHeader()) {
            notifyItemInserted(0);
        }
    }

    /**
     * If you need a mEmptyView, you should set header data in the adapter initialization code.
     *
     * @param emptyId header data
     */
    public void setEmpty(@LayoutRes int emptyId) {
        EasyPreconditions.checkState(mEmptyId == 0, "the mEmptyView already has been set");
        mEmptyId = emptyId;
        if (mAttachToRecycler && hasEmpty()) {
            notifyItemInserted(getHeaderCount());
        }
    }

    /**
     * If you need a footer, you should set footer data in the adapter initialization code.
     */
    public void setFooter(@LayoutRes int footerId) {
        EasyPreconditions.checkState(footerId == 0, "the mFooterView already has been set");
        mFooterId = footerId;
        if (mAttachToRecycler && hasFooter()) {
            //如果没有数据添加的情况下调用会crash
            notifyItemInserted(0);
        }
    }

    public void notifyHeader() {
        if (hasHeader()) {
            notifyItemChanged(0);
        }
    }

    public void notifyEmpty() {
        if (hasEmpty()) {
            notifyItemChanged(getHeaderCount());
        }
    }

    public void notifyFooter() {
        if (hasFooter()) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * Call this method to show hiding footer.
     */
    public void showFooter() {
        if (!hasFooter()) {
            this.mShowFooter = true;
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
        this.mShowFooter = false;
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
     * Returns true if the header configured is not null.
     */
    public boolean hasHeader() {
        return mHeaderId != 0;
    }


    public boolean hasEmpty() {
        return mEmptyId != 0 && isEmpty();
    }

    /**
     * Returns true if the footer configured is not null.
     */
    public boolean hasFooter() {
        return mFooterId != 0 && mShowFooter && !isEmpty();
    }

    private void validateItems(List<T> data) {
        EasyPreconditions.checkNotNull(data, "You can't use a null List<Item> instance.");
    }

    private boolean isFullSpan(int position) {
        return isHeaderPosition(position) || isFooterPosition(position) || isEmptyPosition(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachToRecycler = true;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridSpanSizeLookup(gridManager));
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAttachToRecycler = false;
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

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }


    public void setOnHolderBindListener(OnBindHolderListener onBindHolderListener) {
        this.mOnBindHolderListener = onBindHolderListener;
    }

    /**
     * 当 onBindViewHolder 调用的时候回调此函数中的方法
     */
    public interface OnBindHolderListener {
        void onBindHeader(EasyHolder holder);

        void onBindEmpty(EasyHolder holder);

        void onBindFooter(EasyHolder holder);
    }


    private void bindClickListener(final EasyHolder easyHolder) {
        if (mOnItemClickListener != null) {
            easyHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(easyHolder, getPositionOfData(easyHolder.getAdapterPosition()));
                    }
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            easyHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener != null &&
                            mOnItemLongClickListener.onItemLongClick(easyHolder, getPositionOfData(easyHolder.getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(EasyHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(EasyHolder holder, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
