package com.simple.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.adapter.EasyRecyclerAdapter;
import com.xcheng.view.adapter.HFRecyclerAdapter;
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
        mHFAdapter.setOnItemClickListener(new EasyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EasyHolder holder, int position) {
                ToastLess.showToast("position:"+mHFAdapter.getPositionOfData(position));
            }
        });
    }

    @Override
    public void requestData(final boolean isRefresh) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> data = new ArrayList<>();
                for (int index = 0; index < 20; index++) {
                    data.add("数据：" + index);
                }
                refreshView(isRefresh, data);
            }
        },2000);
    }

    @Nullable
    @Override
    public View getHeaderView() {
        TextView textView=new TextView(getContext());
        textView.setText("这是Header");
        return textView;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<String> getHFAdapter() {
        return new HFRecyclerAdapter<String>(getContext(),R.layout.ev_item_text) {
            @Override
            protected void convert(EasyHolder holder, String s, int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(s);
                clickToHolder(holder);
            }
        };
    }
}
