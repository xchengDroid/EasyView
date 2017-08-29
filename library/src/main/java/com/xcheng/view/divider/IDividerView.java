package com.xcheng.view.divider;

/**
 * 带分割线的的View ,实现此接口
 * Created by chengxin on 2017/8/29.
 */
public interface IDividerView {
    void setTopToLeft(int topToLeft);

    void setTopToRight(int topToRight);

    void setBottomToLeft(int bottomToLeft);

    void setBottomToRight(int bottomToRight);

    void setTopHeight(float topHeight);

    void setBottomHeight(float bottomHeight);

    void setTopColor(int topColor);

    void setBottomColor(int bottomColor);

    void setPosition(int position);
}
