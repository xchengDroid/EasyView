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

    /**
     * 如用户名 密码等
     */
    String label() default "";

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
