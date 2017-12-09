package com.simple.view;

import android.os.Bundle;
import android.view.View;

import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.processbtn.GenerateProcessButton;
import com.xcheng.view.processbtn.SubmitProcessButton;


public class StateSampleActivity extends EasyActivity implements View.OnClickListener {

    private GenerateProcessButton mBtnGenerate;
    private SubmitProcessButton mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_states);

        mBtnSubmit = findViewById(R.id.btnSubmit);
        mBtnGenerate = findViewById(R.id.btnGenerate);

        findViewById(R.id.btnProgressLoading).setOnClickListener(this);
        findViewById(R.id.btnProgressError).setOnClickListener(this);
        findViewById(R.id.btnProgressComplete).setOnClickListener(this);
        findViewById(R.id.btnProgressNormal).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnProgressLoading:
                mBtnSubmit.setProgress(50);
                mBtnGenerate.setProgress(50);
                break;
            case R.id.btnProgressError:
                mBtnSubmit.setProgress(-1);
                mBtnGenerate.setProgress(-1);
                break;
            case R.id.btnProgressComplete:
                mBtnSubmit.setProgress(100);
                mBtnGenerate.setProgress(100);
                break;
            case R.id.btnProgressNormal:
                mBtnSubmit.setProgress(0);
                mBtnGenerate.setProgress(0);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
