package com.xcheng.view.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerDecoration extends RecyclerView.ItemDecoration {
    private int mDividerColor;
    private int mDividerHeight;
    private boolean hasEndDivider;
    private Paint mPaint;

    public DividerDecoration(int dividerHeight) {
        this(Color.parseColor("#dae1e5"), dividerHeight);
    }

    public DividerDecoration(@ColorInt int dividerColor, int dividerHeight) {
        this(dividerColor, dividerHeight, false);
    }

    /**
     * @param dividerColor  分割线颜色
     * @param dividerHeight 分割线高度
     * @param hasEndDivider 最后一项是否有分割线
     */
    public DividerDecoration(@ColorInt int dividerColor, int dividerHeight, boolean hasEndDivider) {
        this.mDividerColor = dividerColor;
        this.mDividerHeight = dividerHeight;
        this.hasEndDivider = hasEndDivider;
        init();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mDividerColor);
        mPaint.setStrokeWidth(mDividerHeight);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (hasEndDivider || position != parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, mDividerHeight);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        final int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (hasEndDivider || position != itemCount - 1)
                c.drawLine(0, view.getBottom() + mDividerHeight / 2, parent.getRight(), view.getBottom() + mDividerHeight / 2, mPaint);
        }
    }
}