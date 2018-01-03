package com.simple.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.controller.SmartRefreshFragment;
import com.xcheng.view.pullrefresh.UIState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxin on 2017/9/3.
 */
public class RefreshSmtFragment extends SmartRefreshFragment<String> {
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mAdapter.setOnItemClickListener(new EasyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EasyHolder holder, int position) {
//                ToastLess.showToast(position + "==position:");
//                mAdapter.notifyFooter();
                int beforeAp = holder.getAdapterPosition();
                Log.e("print", "beforeAp:" + beforeAp);
                int beforeLp = holder.getLayoutPosition();
                Log.e("print", "beforeLp:" + beforeLp);


                int afterAp = holder.getAdapterPosition();
                Log.e("print", "afterAp:" + afterAp);

                int afterLp = holder.getLayoutPosition();
                Log.e("print", "afterLp:" + afterLp);
            }
        });
    }

    @NonNull
    @Override
    protected Config getConfig() {
        return super.getConfig().newBuilder()
                .limit(5)
                .build();
    }

    @Override
    public void requestData(final boolean isRefresh) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    List<String> data = new ArrayList<>();
                    for (int index = 0; index < 5; index++) {
                        data.add("数据：" + index);
                    }
                    refreshView(true, data);
                } else {
                    List<String> data = new ArrayList<>();
                    if (mAdapter.getDataCount() < 25) {
                        for (int index = 0; index < 5; index++) {
                            data.add("数据：" + index);
                        }
                    } else {
                        for (int index = 0; index < 2; index++) {
                            data.add("数据：" + index);
                        }
                    }
                    refreshView(false, data);
                }
            }
        }, 500);
    }

    @Override
    public void complete(boolean isRefresh, UIState state) {

    }

    @NonNull
    @Override
    public EasyAdapter<String> createAdapter() {
        return new EasyAdapter<String>(getContext(), R.layout.ev_item_text) {
            @Override
            public void convert(EasyHolder holder, String s, int position) {
                // Log.e("print", "adapterPosition:" + holder.getAdapterPosition());
                TextView textView = (TextView) holder.itemView;
                textView.setText(s);
            }
        };
    }
}
