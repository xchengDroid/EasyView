package com.simple.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.controller.EasyRefreshFragment;
import com.xcheng.view.util.ToastLess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxin on 2017/9/3.
 */
public class RefreshTextFragment extends EasyRefreshFragment<String> {
    @Override
    public int getLayoutId() {
        return R.layout.fr_refresh;
    }

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
    public void onBindHeader(EasyHolder holder) {
        super.onBindHeader(holder);
        Log.e("print", "onBindHeader:"+holder.getAdapterPosition());

    }

    @Override
    public void onBindEmpty(EasyHolder holder) {
        super.onBindEmpty(holder);
        Log.e("print", "onBindEmpty:"+holder.getAdapterPosition());

    }
    @Override
    public void onBindFooter(EasyHolder holder) {
        super.onBindFooter(holder);
        Log.e("print", "footerBind:"+holder.getAdapterPosition());
    }

    @Nullable
    @Override
    public View getHeaderView(ViewGroup parent) {
        TextView textView = new TextView(getContext());
        textView.setText("这是Header");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.notifyHeader();
            }
        });
        return textView;
    }

    @Nullable
    @Override
    public View getEmptyView(ViewGroup parent) {
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(Color.RED);
        textView.setText("这是EmptyView");
        textView.setTextSize(30);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.notifyEmpty();
            }
        });
        return textView;
    }

    @NonNull
    @Override
    public EasyAdapter<String> getEasyAdapter() {
        return new EasyAdapter<String>(getContext(), null, R.layout.ev_item_text, 3) {
            @Override
            public void convert(EasyHolder holder, String s, int position) {
                Log.e("print", "adapterPosition:" + holder.getAdapterPosition());
                TextView textView = (TextView) holder.itemView;
                textView.setText(s);
                clickToHolder(holder);
            }

//            @Override
//            public int getDataOffset() {
//                return getHeaderCount()+3;
//            }
        };
    }
}
