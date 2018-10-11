package com.xcheng.view.controller;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建时间：2018/10/11
 * 编写人： chengxin
 * 功能描述：装配View的布局 支持{@link EasyActivity}{@link EasyFragment}
 */
@Target(ElementType.TYPE)//表示用在成员变量
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewLayout {
    //布局文件
    @LayoutRes
    int value();
}
