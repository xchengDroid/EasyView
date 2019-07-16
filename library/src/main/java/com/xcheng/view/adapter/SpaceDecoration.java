package com.xcheng.view.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private int mSpaceColor;
    private int mSpaceSize;
    private boolean hasEndSpace;
    private Paint mPaint;

    public SpaceDecoration(int spaceSize) {
        this(Color.parseColor("#dae1e5"), spaceSize);
    }

    public SpaceDecoration(@ColorInt int spaceColor, int spaceSize) {
        this(spaceColor, spaceSize, false);
    }

    /**
     * @param spaceColor  分割线颜色
     * @param spaceSize   分割线高度
     * @param hasEndSpace 最后一项是否有分割线
     */
    public SpaceDecoration(@ColorInt int spaceColor, int spaceSize, boolean hasEndSpace) {
        this.mSpaceColor = spaceColor;
        this.mSpaceSize = spaceSize;
        this.hasEndSpace = hasEndSpace;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mSpaceColor);
        mPaint.setStrokeWidth(mSpaceSize);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (hasEndSpace || position != parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, mSpaceSize);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        final int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (hasEndSpace || position != itemCount - 1)
                c.drawLine(0, view.getBottom() + mSpaceSize / 2, parent.getRight(), view.getBottom() + mSpaceSize / 2, mPaint);
        }
    }
}