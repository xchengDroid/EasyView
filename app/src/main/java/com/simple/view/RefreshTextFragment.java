package com.simple.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.adapter.HFAdapter;
import com.xcheng.view.controller.EasyRefreshFragment;
import com.xcheng.view.util.ToastLess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxin on 2017/9/3.
 */
public class RefreshTextFragment extends EasyRefreshFragment<String> {
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mAdapter.setOnItemClickListener(new EasyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EasyHolder holder, int position) {
                ToastLess.showToast(position + "==position:");
                mAdapter.notifyFooter();
            }
        });
    }

    @Override
    public int getLimit() {
        return 3;
    }

    private boolean isFirst = true;

    @Override
    public void requestData(final boolean isRefresh) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirst) {
                    isFirst = false;
                    refreshView(true, null);

                } else {
                    if (isRefresh) {
                        List<String> data = new ArrayList<>();
                        for (int index = 0; index < 3; index++) {
                            data.add("数据：" + index);
                        }
                        refreshView(true, data);
                    } else {
                        List<String> data = new ArrayList<>();
                        for (int index = 0; index < 3; index++) {
                            data.add("数据：" + index);
                        }
                        if (mAdapter.getDataCount() > 12) {
                            refreshView(true, data);
                        } else {
                            refreshView(false, data);

                        }
                    }
                }
            }
        }, 500);
    }

    @Override
    public void onBindHeader(EasyHolder holder, boolean isCreate) {
        super.onBindHeader(holder, isCreate);
        // Log.e("print","Header isCreate:"+isCreate);
        if (isCreate) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastLess.showToast("点击头部");
                    mAdapter.notifyHeader();
                }
            });
        }
    }

    @Override
    public void onBindEmpty(EasyHolder holder, boolean isCreate) {
        super.onBindEmpty(holder, isCreate);
        // Log.e("print","Empty isCreate:"+isCreate);
        if (isCreate) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastLess.showToast("点击空Empty");
                    mAdapter.notifyEmpty();
                }
            });
        }
    }

    @Override
    public int getEmptyId() {
        return R.layout.empty;
    }

    @Override
    public int getHeaderId() {
        return R.layout.header;
    }

    @NonNull
    @Override
    public HFAdapter<String> getHFAdapter() {
        return new HFAdapter<String>(getContext(), R.layout.ev_item_text) {
            @Override
            public void convert(EasyHolder holder, String s, int position) {
                // Log.e("print", "adapterPosition:" + holder.getAdapterPosition());
                TextView textView = (TextView) holder.itemView;
                textView.setText(s);
            }
        };
    }
}
