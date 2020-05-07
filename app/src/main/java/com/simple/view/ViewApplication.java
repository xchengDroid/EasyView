package com.simple.view;

import android.app.Application;

import com.xcheng.view.util.LocalDisplay;

/**
 * 创建时间：2020-05-07
 * 编写人： chengxin
 * 功能描述：
 */
public class ViewApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalDisplay.init(this);
    }
}
