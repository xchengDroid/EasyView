package com.xcheng.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcheng.view.util.EasyPreconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 所需Adapter基类，提供刷新加载更多等方法
 *
 * @param <T>
 */
abstract class EasyAdapter<T> extends RecyclerView.Adapter<EasyHolder> implements IAdapterDelegate<T, EasyHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<T> mData;
    private final int mLayoutId;

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

    public View inflater(int layoutId, ViewGroup parent) {
        return mInflater.inflate(layoutId, parent, false);
    }

    @Override
    public int getItemViewType(int position) {
        return getDelegateType(getItem(position), position);
    }

    @Override
    public EasyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getDelegateView(parent, viewType);
        EasyPreconditions.checkState(itemView != null, "you can set a layoutId in construct method or override getItemView(parent,viewType) and return a NonNull itemView");
        return new EasyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EasyHolder holder, int position) {
        convert(holder, getItem(position), position);
    }

    @Override
    public int getDelegateType(T t, int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getDelegateView(ViewGroup parent, int viewType) {
        return mLayoutId != 0 ? inflater(mLayoutId, parent) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return getDataCount();
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

}
