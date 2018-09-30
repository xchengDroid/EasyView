package com.xcheng.view.validator;

import java.util.List;

public interface OnValidateListener {

    /**
     * 验证成功
     */
    void onValidateSucceeded(List<Passer> passers);

    /**
     * 验证规则 true代表成功
     */
    boolean isValidRule(Passer passer);
}