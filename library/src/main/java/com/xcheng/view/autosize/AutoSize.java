package com.xcheng.view.autosize;

/**
 * 创建时间：2018/11/12
 * 编写人： chengxin
 * 功能描述：适配实体类
 */
public class AutoSize {
    /**
     * 默认的设计尺寸
     * >0 设置宽度
     * <0 设置高度
     */
    public final float designSizeInDp;
    public final boolean isSupportDip;
    public final boolean isSupportSp;
    /**
     * 支持的适配的副单位
     *
     * @see android.util.TypedValue#COMPLEX_UNIT_PT
     * @see android.util.TypedValue#COMPLEX_UNIT_IN
     * @see android.util.TypedValue#COMPLEX_UNIT_MM
     */
    public final int subUnit;

    /**
     * @param designSizeInDp 设计宽度货高度
     * @param isSupportDip   是否支持dip
     * @param isSupportSp    是否支持sp
     * @param subUnit        副单位 如果不支持传-1
     */
    public AutoSize(float designSizeInDp, boolean isSupportDip, boolean isSupportSp, int subUnit) {
        if (designSizeInDp == 0) {
            throw new IllegalArgumentException("designSizeInDp==0");
        }
        this.designSizeInDp = designSizeInDp;
        this.isSupportDip = isSupportDip;
        this.isSupportSp = isSupportSp;
        this.subUnit = subUnit;
    }
}
