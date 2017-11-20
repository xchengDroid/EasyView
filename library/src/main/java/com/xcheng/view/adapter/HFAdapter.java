package com.xcheng.view.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.xcheng.view.util.EasyPreconditions;

import java.util.Collection;
import java.util.List;

/**
 * 支持HeaderView FooterView emptyView
 *
 * @param <T>
 */
public abstract class HFAdapter<T> extends EasyAdapter<T> {
    /**
     * param to be used to query number of spans occupied by each item
     */
    private SpanSizeLookup mSpanSizeLookup;
    private OnBindHolderListener mOnBindHolderListener;

    //-1为 INVALID_TYPE,不要设置为-1
    public static final int TYPE_HEADER = Integer.MAX_VALUE - 101;
    public static final int TYPE_FOOTER = Integer.MAX_VALUE - 102;
    public static final int TYPE_EMPTY = Integer.MAX_VALUE - 103;
    @LayoutRes
    private int mHeaderId;
    @LayoutRes
    private int mFooterId;
    @LayoutRes
    private int mEmptyId;

    /**
     * 控制是否显示footer
     */
    private boolean mShowFooter = true;

    private boolean mHasFooterIfEmpty;

    /**
     * 标记是否绑定到RecyclerView
     */
    private boolean mAttachToRecycler = false;

    public HFAdapter(Context context, @Nullable List<T> data) {
        super(context, data);
    }

    public HFAdapter(Context context, @LayoutRes int layoutId) {
        super(context, layoutId);
    }

    public HFAdapter(Context context, @Nullable List<T> data, @LayoutRes int layoutId) {
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
            mData.addAll(position, loadMore);
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
            mData.add(position, item);
            if (oldFlag == HEFViewFlag()) {
                notifyItemInserted(getHeaderCount() + position);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void remove(T item) {
        if (item != null) {
            int index = mData.indexOf(item);
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
            mData.remove(position);
            if (oldFlag == HEFViewFlag()) {
                notifyItemRemoved(getHeaderCount() + position);
            } else {
                //compatible mEmptyView mHeaderView and mFooterView control
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return TYPE_HEADER;
        } else if (isEmptyPosition(position)) {
            return TYPE_EMPTY;
        } else if (isFooterPosition(position)) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(getPositionOfData(position));
    }

    @Override
    public EasyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                EasyHolder headerHolder = new EasyHolder(inflater(mHeaderId, parent));
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindHeader(headerHolder, true);
                }
                return headerHolder;
            case TYPE_EMPTY:
                EasyHolder emptyHolder = new EasyHolder(inflater(mEmptyId, parent));
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindEmpty(emptyHolder, true);
                }
                return emptyHolder;
            case TYPE_FOOTER:
                EasyHolder footerHolder = new EasyHolder(inflater(mFooterId, parent));
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindFooter(footerHolder, true);
                }
                return footerHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(EasyHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case TYPE_HEADER:
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindHeader(holder, false);
                }
                break;
            case TYPE_EMPTY:
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindEmpty(holder, false);
                }
                break;
            case TYPE_FOOTER:
                if (mOnBindHolderListener != null) {
                    mOnBindHolderListener.onBindFooter(holder, false);
                }
                break;
            default:
                super.onBindViewHolder(holder, getPositionOfData(position));
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
     * @param headerId LayoutResId,如果为0则没有Header
     */
    public void setHeader(@LayoutRes int headerId) {
        EasyPreconditions.checkState(mHeaderId == 0, "the mHeaderView already has been set");
        mHeaderId = headerId;
        if (mAttachToRecycler && hasHeader()) {
            notifyDataSetChanged();
        }
    }

    /**
     * @param emptyId LayoutResId,如果为0则没有Empty
     */
    public void setEmpty(@LayoutRes int emptyId) {
        EasyPreconditions.checkState(mEmptyId == 0, "the mEmptyView already has been set");
        mEmptyId = emptyId;
        if (mAttachToRecycler && hasEmpty()) {
            notifyDataSetChanged();
        }
    }

    /**
     * @param footerId         LayoutResId,如果为0则没有Footer
     * @param hasFooterIfEmpty 如果为true,则表示 {@link #isEmpty()} 为true时也显示footer
     */
    public void setFooter(@LayoutRes int footerId, boolean hasFooterIfEmpty) {
        EasyPreconditions.checkState(mFooterId == 0, "the mFooterView already has been set");
        mFooterId = footerId;
        mHasFooterIfEmpty = hasFooterIfEmpty;
        if (mAttachToRecycler && hasFooter()) {
            notifyDataSetChanged();
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
        this.mShowFooter = true;
        notifyDataSetChanged();
    }

    /**
     * Call this method to hide footer.
     */
    public void hideFooter() {
        this.mShowFooter = false;
        notifyDataSetChanged();
    }

    public boolean isHeaderPosition(int position) {
        return hasHeader() && position == 0;
    }

    public boolean isEmptyPosition(int position) {
        return hasEmpty() && ((hasHeader() && position == 1) || (!hasHeader() && position == 0));
    }
    
    public boolean isFooterPosition(int position) {
        int lastPosition = getItemCount() - 1;
        return hasFooter() && position == lastPosition;
    }


    public boolean hasHeader() {
        return mHeaderId != 0;
    }


    public boolean hasEmpty() {
        return mEmptyId != 0 && isEmpty();
    }

    public boolean hasFooter() {
        return mFooterId != 0 && mShowFooter && (!isEmpty() || mHasFooterIfEmpty);
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

        /**
         * @param isCreate 是否是第一次创建即onCreateViewHolder中执行的
         *                 此参数的目的是为了避免View一些重复的初始化操作
         */
        void onBindHeader(EasyHolder holder, boolean isCreate);

        void onBindEmpty(EasyHolder holder, boolean isCreate);

        void onBindFooter(EasyHolder holder, boolean isCreate);
    }

    public static class SimpleBindHolderListener implements OnBindHolderListener {

        @Override
        public void onBindHeader(EasyHolder holder, boolean isCreate) {

        }

        @Override
        public void onBindEmpty(EasyHolder holder, boolean isCreate) {

        }

        @Override
        public void onBindFooter(EasyHolder holder, boolean isCreate) {

        }
    }
}
