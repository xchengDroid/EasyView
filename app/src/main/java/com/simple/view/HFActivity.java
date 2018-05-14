package com.simple.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.adapter.HFAdapter;
import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.util.Router;

import java.util.ArrayList;
import java.util.List;


public class HFActivity extends EasyActivity {

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
//./gradlew clean build bintrayUpload -PbintrayUser=xcheng -PbintrayKey=4f8465004b752b103ea2e374ad6cb73ec38d8601 -PdryRun=false

    @Override
    public int getLayoutId() {
        return R.layout.ac_hfadapter;
    }

    EasyAdapter<String> easyAdapter;
    RecyclerView mRecyclerView;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView = (RecyclerView) findViewById(R.id.ev_id_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        EasyAdapter<String> adapter = getEasyAdapter();
        adapter.setOnItemClickListener(new EasyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EasyHolder holder, int adapterPosition) {
                Log.e("print", "adapterPosition:" + adapterPosition);
            }
        });
        mRecyclerView.setAdapter(adapter);
//        ARouter.getInstance().build("");
        Router.build("").navigation(this);
    }

    private EasyAdapter<String> getEasyAdapter() {
        List<String> data = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            data.add("第" + index + "条数据");
        }
        return new EasyAdapter<String>(this, data, R.layout.ev_item_text) {
            @Override
            public void convert(EasyHolder holder, String s, int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(s);
            }

            @Override
            public View getDelegateView(ViewGroup parent, int viewType) {
                TextView textView = (TextView) super.getDelegateView(parent, viewType);
                textView.setTextSize(16);
                return textView;
            }
        };
    }

    private HFAdapter<String> getHFAdapter() {
        List<String> data = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            data.add("第" + index + "条数据");
        }
        HFAdapter<String> hfAdapter = new HFAdapter<String>(this, data, R.layout.ev_item_text) {
            @Override
            public void convert(EasyHolder holder, String s, int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(s);
            }

            @Override
            public View getDelegateView(ViewGroup parent, int viewType) {
                TextView textView = (TextView) super.getDelegateView(parent, viewType);
                textView.setTextSize(16);
                return textView;
            }
        };
        hfAdapter.setHeader(R.layout.ev_item_text);
        hfAdapter.setFooter(R.layout.ev_item_text, true);
        hfAdapter.setOnHolderBindListener(new HFAdapter.OnBindHolderListener() {
            @Override
            public void onBindHeader(EasyHolder holder, boolean isCreate) {
                if (isCreate) {
                    TextView textView = (TextView) holder.itemView;
                    textView.setText("这是HEADER");
                    textView.setTextSize(25);
                }
            }

            @Override
            public void onBindEmpty(EasyHolder holder, boolean isCreate) {

            }

            @Override
            public void onBindFooter(EasyHolder holder, boolean isCreate) {
                if (isCreate) {
                    TextView textView = (TextView) holder.itemView;
                    textView.setText("这是FOOTER");
                    textView.setTextSize(25);
                }
            }
        });
        return hfAdapter;
    }
}
