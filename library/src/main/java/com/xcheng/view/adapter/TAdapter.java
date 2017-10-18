package com.xcheng.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 所需Adapter基类，抽离一些基础方法
 *
 * @param <T>
 */
abstract class TAdapter<T> extends RecyclerView.Adapter<EasyHolder> implements IAdapterDelegate<T, EasyHolder> {

    private final Context mContext;
    private final Resources mResources;
    private final LayoutInflater mInflater;
    //默认布局ID
    @LayoutRes
    private final int mLayoutId;
    private final List<T> mData;

    TAdapter(Context context, @Nullable List<T> data) {
        this(context, data, 0);
    }

    TAdapter(Context context, @LayoutRes int layoutId) {
        this(context, null, layoutId);
    }

    TAdapter(Context context, @Nullable List<T> data, @LayoutRes int layoutId) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mData = data;
        mContext = context;
        mResources = context.getResources();
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
    }

    public final int getLayoutId() {
        return mLayoutId;
    }

    public Resources getResources() {
        return mResources;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public Context getContext() {
        return mContext;
    }

    public View inflater(int layoutId, ViewGroup parent) {
        return mInflater.inflate(layoutId, parent, false);
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
