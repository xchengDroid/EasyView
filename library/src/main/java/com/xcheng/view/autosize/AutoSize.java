package com.xcheng.view.autosize;

import android.support.annotation.Size;
import android.util.TypedValue;

/**
 * 创建时间：2018/11/12
 * 编写人： chengxin
 * 功能描述：适配实体类
 */
public class AutoSize {

    public static final int DEFAULT_DISPLAY_UNITS[] = {TypedValue.COMPLEX_UNIT_DIP, TypedValue.COMPLEX_UNIT_SP};
    /**
     * 默认的设计尺寸
     * >0 设置宽度
     * <0 设置高度
     */
    public final float designSizeInDp;
    /**
     * 默认支持的适配单位
     *
     * @see android.util.TypedValue#COMPLEX_UNIT_DIP
     * @see android.util.TypedValue#COMPLEX_UNIT_SP
     * @see android.util.TypedValue#COMPLEX_UNIT_PT
     * @see android.util.TypedValue#COMPLEX_UNIT_IN
     * @see android.util.TypedValue#COMPLEX_UNIT_MM
     */
    public final int[] displayUnits;

    public AutoSize(float designSizeInDp, @Size(min = 1) int... displayUnits) {
        if (designSizeInDp == 0) {
            throw new IllegalArgumentException("designSizeInDp==0");
        }
        if (displayUnits == null || displayUnits.length == 0) {
            throw new IllegalArgumentException("displayUnits==null||displayUnits.length==0");
        }
        this.designSizeInDp = designSizeInDp;
        this.displayUnits = displayUnits;
    }
}
