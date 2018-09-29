package com.xcheng.view.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chengxin
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Val {
    String name() default "";

    //最小长度
    int min() default 1;

    //最小长度
    int max() default Integer.MAX_VALUE;

    //顺序
    int order() default -1;

    int messageResId() default -1;

    String message() default "This field is required";

    Scheme scheme() default Scheme.ANY;

    enum Scheme {
        ANY, ALPHA, ALPHA_MIXED_CASE,
        NUMERIC, ALPHA_NUMERIC, ALPHA_NUMERIC_MIXED_CASE,
        ALPHA_NUMERIC_SYMBOLS, ALPHA_NUMERIC_MIXED_CASE_SYMBOLS
    }
}
