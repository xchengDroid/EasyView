package com.xcheng.view.adapter;

import android.content.Context;
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
 * RecyclerView 所需Adapter基类，提供刷新加载更多等方法
 *
 * @param <T>
 */
public abstract class EasyRecyclerAdapter<T> extends RecyclerView.Adapter<EasyHolder> implements IAdapterDelegate<T, EasyHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private final List<T> mData;
    private final int mLayoutId;

    public EasyRecyclerAdapter(Context context, @Nullable List<T> data) {
        this(context, data, 0);
    }

    public EasyRecyclerAdapter(Context context, @Nullable List<T> data, @LayoutRes int layoutId) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.mData = data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
    }

    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getInflater() {
        return mInflater;
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
     * @param position the position insert to mData
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
     * @param position the  position insert to mData
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
     * mData数据位置开始的position之前可能有header,camera拍照按钮等等
     * 重写时注意是否需要考虑重写getItemCont方法
     *
     * @return 返回notify刷新的时候需要的起始位置
     */
    public int getHeaderCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getDataCount();
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

    /**
     * mData数据是否为空
     *
     * @return
     */
    public final boolean isEmpty() {
        return mData == null || mData.size() == 0;
    }

    @Override
    public EasyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getDelegateView(parent, viewType);
        EasyPreconditions.checkState(itemView != null, "you can set a layoutId in construct method or override getItemView(parent,viewType) and return a NonNull itemView");
        return new EasyHolder(itemView);
    }
    /**
     * holder 在onCreateViewHolder 和 onBindViewHolder时 holder.itemView no parent
     *
     * @param holder EasyHolder
     * @param position position in adapter
     */
    @Override
    public void onBindViewHolder(EasyHolder holder, int position) {
        int positionOfData = getPositionOfData(position);
        convert(holder, getItem(positionOfData), positionOfData);
    }


    @Override
    public int getItemViewType(int position) {
        int positionOfData = getPositionOfData(position);
        return getDelegateType(getItem(positionOfData), positionOfData);
    }


    public View inflater(int layoutId, ViewGroup parent) {
        return mInflater.inflate(layoutId, parent, false);
    }


    @Override
    public View getDelegateView(ViewGroup parent, int viewType) {
        return mLayoutId != 0 ? inflater(mLayoutId, parent) : null;
    }

    @Override
    public int getDelegateType(T t, int position) {
        return super.getItemViewType(position);
    }

    protected void clickToHolder(final EasyHolder easyHolder) {
        easyHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(easyHolder, easyHolder.getAdapterPosition());
                }
            }
        });
    }

    protected void longClickToHolder(final EasyHolder easyHolder) {
        easyHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mOnItemLongClickListener != null &&
                        mOnItemLongClickListener.onItemLongClick(easyHolder, easyHolder.getAdapterPosition());
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
