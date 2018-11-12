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
    public final boolean isSupportSp;

    /**
     * @param designSizeInDp 设计宽度货高度
     * @param isSupportSp    是否支持sp
     */
    public AutoSize(float designSizeInDp, boolean isSupportSp) {
        if (designSizeInDp == 0) {
            throw new IllegalArgumentException("designSizeInDp==0");
        }
        this.designSizeInDp = designSizeInDp;
        this.isSupportSp = isSupportSp;
    }
}
