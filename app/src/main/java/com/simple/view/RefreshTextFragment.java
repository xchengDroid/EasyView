package com.simple.view;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.adapter.HFRecyclerAdapter;
import com.xcheng.view.controller.EasyRefreshFragment;

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
    public void requestData(boolean isRefresh) {
        List<String> data = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            data.add("数据：" + index);

        }
        refreshView(isRefresh, data);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<String> getHFAdapter() {
        return new HFRecyclerAdapter<String>(getContext(), null, 10) {
            @Override
            protected void convert(EasyHolder holder, String s, int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(s);
            }

            @Override
            protected int getLayoutId(int viewType) {
                return R.layout.ev_text_option;
            }
        };
    }
}
