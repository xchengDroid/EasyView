package com.xcheng.view.validator;

import android.util.Pair;
import android.widget.TextView;

import java.util.List;

public interface OnValidateListener {
    void onValidateSucceeded();

    //拦截验证
    void onValidateInterceptor();

    void onValidateFailed(List<Pair<TextView, Val>> errors);
}