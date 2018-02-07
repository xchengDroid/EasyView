package com.xcheng.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.util.EasyPreconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RecyclerView 所需Adapter基类，抽离一些基础方法
 *
 * @param <T>
 */
public abstract class EasyAdapter<T> extends RecyclerView.Adapter<EasyHolder> implements IAdapterDelegate<T, EasyHolder> {
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    protected final Context mContext;
    protected final Resources mResources;
    protected final LayoutInflater mInflater;
    //默认布局ID
    @LayoutRes
    protected final int mLayoutId;
    protected final List<T> mData;

    public EasyAdapter(Context context, @Nullable List<T> data) {
        this(context, data, 0);
    }

    public EasyAdapter(Context context, @LayoutRes int layoutId) {
        this(context, null, layoutId);
    }

    public EasyAdapter(Context context, @Nullable List<T> data, @LayoutRes int layoutId) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mData = data;
        mContext = context;
        mResources = context.getResources();
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
    }

    /**
     * 刷新数据
     */
    public void refresh(@Nullable Collection<? extends T> newData) {
        mData.clear();
        if (newData != null && newData.size() > 0) {
            /*刷新数据 */
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
        addData(getDataCount(), loadMore);
    }

    /**
     * add new data in to certain locations
     *
     * @param position the position insert into mData
     */
    public void addData(@IntRange(from = 0) int position, Collection<? extends T> loadMore) {
        if (loadMore != null && loadMore.size() > 0) {
            mData.addAll(position, loadMore);
            notifyItemRangeInserted(position, loadMore.size());
        }
    }

    @Override
    public int getItemCount() {
        return getDataCount();
    }

    /**
     * 如果需要覆盖此方法，请重写{@link #getDelegateType(T, int)}
     */
    @Override
    public int getItemViewType(int position) {
        return getDelegateType(getItem(position), position);
    }

    /**
     * 如果需要覆盖此方法，请重写{@link #getDelegateView(ViewGroup, int)},
     * holder 在onCreateViewHolder 和 onBindViewHolder时 holder.itemView no parent
     */
    @Override
    public EasyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getDelegateView(parent, viewType);
        EasyPreconditions.checkState(itemView != null, "you can set a layoutId in construct method or override getDelegateView(parent,viewType) and return a NonNull itemView");
        EasyHolder holder = new EasyHolder(itemView);
        bindClickListener(holder);
        return holder;
    }

    /**
     * 如果需要覆盖此方法，请重写{@link #convert(RecyclerView.ViewHolder, T, int)}}
     */
    @Override
    public void onBindViewHolder(EasyHolder holder, int position) {
        convert(holder, getItem(position), position);
    }

    @Override
    public int getDelegateType(T t, int position) {
        return 0;
    }

    @Override
    public View getDelegateView(ViewGroup parent, int viewType) {
        return mLayoutId != 0 ? inflater(mLayoutId, parent) : null;
    }

    public View inflater(int layoutId, ViewGroup parent) {
        return mInflater.inflate(layoutId, parent, false);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position 在mData列表中的位置，注意如果有header或camera按钮等的话处理后调用
     * @return bindData
     */
    public final T getItem(int position) {
        if (position >= 0 && position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    /**
     * 获取mData数据的长度
     *
     * @return
     */
    public final int getDataCount() {
        return mData.size();
    }

    public List<T> getData() {
        return mData;
    }

    /**
     * mData数据是否为空
     *
     * @return
     */
    public final boolean isEmpty() {
        return mData == null || mData.size() == 0;
    }

    private void bindClickListener(final EasyHolder easyHolder) {
        if (mOnItemClickListener != null) {
            easyHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(easyHolder, easyHolder.getAdapterPosition());
                    }
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            easyHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener != null &&
                            mOnItemLongClickListener.onItemLongClick(easyHolder, easyHolder.getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(EasyHolder holder, int adapterPosition);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(EasyHolder holder, int adapterPosition);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
