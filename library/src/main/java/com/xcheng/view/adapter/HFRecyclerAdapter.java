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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.util.EasyPreconditions;

import java.util.ArrayList;
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
public abstract class HFRecyclerAdapter<T> extends RecyclerView.Adapter<EasyHolder> implements IAdapterDelegate<T, EasyHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private final List<T> mData;
    private final int mLayoutId;

    //-1为 INVALID_TYPE,不要设置为-1
    public static final int TYPE_HEADER = 0x00000111;
    public static final int TYPE_FOOTER = 0x00000222;
    public static final int TYPE_EMPTY = 0x00000333;
    private View headerView;
    private View footerView;
    private View emptyView;
    private boolean showFooter = true;
    /**
     * 刷新加载每页的长度
     */
    private final int mLength;
    /**
     * 标记是否绑定到RecyclerView
     */
    private boolean isAttachToRecycler = false;

    public HFRecyclerAdapter(Context context) {
        this(context, 0);
    }

    public HFRecyclerAdapter(Context context, @LayoutRes int layoutId) {
        this(context, layoutId, 10);
    }

    public HFRecyclerAdapter(Context context, @LayoutRes int layoutId, @IntRange(from = 1) int length) {
        this(context, null, layoutId, 10);
    }

    public HFRecyclerAdapter(Context context, @Nullable List<T> data, @LayoutRes int layoutId) {
        this(context, data, layoutId, 10);
    }

    public HFRecyclerAdapter(Context context, @Nullable List<T> data, @LayoutRes int layoutId, @IntRange(from = 1) int length) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.mData = data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mLength = length;
    }

    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public View inflater(int layoutId, ViewGroup parent) {
        return mInflater.inflate(layoutId, parent, false);
    }

    public final List<T> getData() {
        return mData;
    }

    /**
     * 获取mData数据的长度
     *
     * @return
     */
    public final int getDataCount() {
        return mData.size();
    }


    public int getLength() {
        return mLength;
    }

    /**
     * mData数据是否为空
     *
     * @return
     */
    public final boolean isEmpty() {
        return mData == null || mData.size() == 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position the  position in mData
     * @return T
     */
    public T getItem(@IntRange(from = 0) int position) {
        if (position >= 0 && position < mData.size()) {
            return mData.get(position);
        }
        return null;
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
        mData.clear();
        if (newData != null && newData.size() > 0) {
            /** 刷新数据 */
            mData.addAll(newData);
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
        if (loadMore != null && loadMore.size() > 0) {
            mData.addAll(loadMore);
            notifyItemRangeInserted(getHeaderCount() + mData.size() - loadMore.size(), loadMore.size());
        }
    }

    /**
     * add new data in to certain locations
     *
     * @param position the position insert into mData
     */
    public void addData(@IntRange(from = 0) int position, Collection<? extends T> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(position, data);
            notifyItemInserted(getHeaderCount() + position);
        }
    }

    public void add(T data) {
        if (data != null) {
            mData.add(data);
            notifyItemInserted(getHeaderCount() + mData.size() - 1);
        }
    }

    /**
     * 追加数据
     *
     * @param position the  position insert into mData
     * @param data     要追加的数据
     */
    public void add(@IntRange(from = 0) int position, T data) {
        if (data != null) {
            mData.add(position, data);
            notifyItemInserted(getHeaderCount() + position);
        }
    }

    /**
     * 删除某条数据
     *
     * @param position the  position in mData
     */
    public void remove(@IntRange(from = 0) int position) {
        if (position >= 0 && position < mData.size()) {
            mData.remove(position);
            notifyItemRemoved(getHeaderCount() + position);
        }
    }

    public void remove(T item) {
        int index = mData.indexOf(item);
        remove(index);
    }

    /**
     * Invokes onCreateHeaderViewHolder, onCreateItemViewHolder or onCreateFooterViewHolder methods
     * based on the view type param.
     */
    @Override
    public final EasyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new EasyHolder(headerView);
            case TYPE_EMPTY:
                return new EasyHolder(emptyView);
            case TYPE_FOOTER:
                return new EasyHolder(footerView);
            default:
                View itemView = getDelegateView(parent, viewType);
                EasyPreconditions.checkState(itemView != null, "you can set a layoutId in construct method or override getItemView(parent,viewType) and return a NonNull itemView");
                return new EasyHolder(itemView);
        }
    }

    @Override
    public View getDelegateView(ViewGroup parent, int viewType) {
        return mLayoutId != 0 ? inflater(mLayoutId, parent) : null;
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

    @Override
    public int getDelegateType(T t, int position) {
        return super.getItemViewType(position);
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
        EasyPreconditions.checkState(this.headerView == null, "the headerView already has been set");
        EasyPreconditions.checkNotNull(headerView != null, "the headerView can not be null");
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

        EasyPreconditions.checkState(this.emptyView == null, "the emptyView already has been set");
        EasyPreconditions.checkNotNull(emptyView != null, "the emptyView can not be null");
        this.emptyView = emptyView;
        if (isAttachToRecycler && hasEmpty()) {
            notifyItemInserted(getHeaderCount());
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
        EasyPreconditions.checkState(this.footerView == null, "the footerView already has been set");
        EasyPreconditions.checkNotNull(footerView != null, "the footerView can not be null");

        this.footerView = footerView;
        if (isAttachToRecycler && hasFooter()) {
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
        EasyPreconditions.checkNotNull(data, "You can't use a null List<Item> instance.");
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
                return isFullSpan ? layoutManager.getSpanCount() : mSpanSizeLookup.getSpanSize(layoutManager, position);
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

    private OnBindHolderListener mOnBindHolderListener;

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

    protected void clickToHolder(final EasyHolder easyHolder) {
        easyHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(easyHolder, getPositionOfData(easyHolder.getAdapterPosition()));
                }
            }
        });
    }

    protected void longClickToHolder(final EasyHolder easyHolder) {
        easyHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mOnItemLongClickListener != null &&
                        mOnItemLongClickListener.onItemLongClick(easyHolder, getPositionOfData(easyHolder.getAdapterPosition()));
            }
        });
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(EasyHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemLongClickListener {
        boolean onItemLongClick(EasyHolder holder, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
