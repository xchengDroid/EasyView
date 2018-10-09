package com.xcheng.view.validator;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

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
public @interface Valid {

    /**
     * 如用户名 密码等 用于拼装错误信息
     */
    String label() default "";

    @StringRes
    int labelResId() default -1;


    /**
     * @return View所包含的TextView的ID
     */
    @IdRes
    int textViewId() default -1;

    /**
     * 是否需要去除空格
     */
    boolean trim() default false;

    /**
     * 如 loginName password 等用于http提交保存等
     */
    String key() default "";

    /**
     * 最小长度,为0 表示可以为空
     */
    int min() default 1;

    /**
     * 验证顺序
     */
    int order() default -1;
}
