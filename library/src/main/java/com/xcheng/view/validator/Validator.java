package com.xcheng.view.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2018/9/29
 * 编写人： chengxin
 * 功能描述：
 */
public class Validator {
    private Object mController;
    private final List<Passer> mPassersCache = new ArrayList<>();
    // true will validate all View
    private boolean mImmediate;

    public Validator(Object controller) {
        this.mController = controller;
        this.mImmediate = true;
    }

    public void setImmediate(boolean immediate) {
        this.mImmediate = immediate;
    }

}
