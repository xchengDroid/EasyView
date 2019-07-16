package com.simple.view;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.widget.CheckView;
import com.xcheng.view.widget.ProgressView;

public class CheckViewActivity extends EasyActivity {

    public ProgressView progressView;
    CheckView cvCheckView;

    @Override
    public int getLayoutId() {
        return R.layout.ac_checkview;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //cvCheckView=findViewById(R.id.cv_checkView);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoadingLiveData.postValue(true);
    }
}
