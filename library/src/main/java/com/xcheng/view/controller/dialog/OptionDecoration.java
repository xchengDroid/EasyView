package com.xcheng.view.controller.dialog;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class OptionDecoration extends RecyclerView.ItemDecoration {
    private int mDividerColor;
    private int mDividerHeight;
    private Paint mPaint;

    public OptionDecoration(int dividerColor, int dividerHeight) {
        this.mDividerColor = dividerColor;
        this.mDividerHeight = dividerHeight;
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
        if (position != parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, mDividerHeight);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int count = parent.getChildCount();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (i < count - 1)
                c.drawLine(0, view.getBottom() + mDividerHeight / 2, parent.getRight(), view.getBottom() + mDividerHeight / 2, mPaint);
        }
    }
}