package com.xcheng.view.validator;

import java.util.List;

public interface OnValidateListener {
    void onValidateSucceeded();


    /**
     * @param passer
     * @return true 代表已经被拦截 无需后续验证，false 表示会被后续验证
     */
    boolean onValidateInterceptor(Passer passer);

    void onValidateFailed(List<Passer> passers);
}