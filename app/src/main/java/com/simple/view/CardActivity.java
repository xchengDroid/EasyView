package com.simple.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.widget.CommonView;
import com.xcheng.view.widget.ProgressView;


public class CardActivity extends EasyActivity {

    public CommonView commonView;
    public ProgressView progressView;

    @Override
    public int getLayoutId() {
        return R.layout.ac_card;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        commonView = findViewById(R.id.cv);
        commonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = commonView.getMode();
                if (mode == CommonView.INPUT) {
                    commonView.setMode(CommonView.DISPLAY);
                } else {
                    commonView.setMode(CommonView.INPUT);
                }
            }
        });
        progressView = findViewById(R.id.progressView);
        progressView.setProgressViewTextGenerator(new ProgressView.ProgressViewTextGenerator() {
            @Override
            public String generateText(ProgressView progressBar, int value, int maxValue) {
                return "测试text";
            }
        });
    }
}
