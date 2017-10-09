/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xcheng.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.xcheng.view.R;
import com.xcheng.view.util.LocalDisplay;

public class PagerSlidingTabStrip extends HorizontalScrollView {
    public interface TabAdapter {
        @NonNull
        View getTabView(int position);

        /**
         * 点击tab切换时是否有滚动动画
         */
        boolean smoothScroll();
    }

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager viewPager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;
    private boolean shouldExpand;
    private int scrollOffset;
    private int indicatorHeight;
    private int underlineHeight;
    private int dividerPadding;
    private int lastScrollX = 0;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        // get custom attrs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_ev_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_ev_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_ev_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_ev_pstsIndicatorHeight, LocalDisplay.dp2px(5));
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_ev_pstsUnderlineHeight, LocalDisplay.dp2px(2));
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_ev_pstsDividerPadding, LocalDisplay.dp2px(12));
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_ev_pstsShouldExpand, false);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_ev_pstsScrollOffset, LocalDisplay.dp2px(52));
        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(LocalDisplay.dp2px(1));

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT/*解决如果设置为0在android 7.0上测量错误的bug*/, LayoutParams.MATCH_PARENT, 1.0f);
    }

    public void setViewPager(ViewPager pager) {
        this.viewPager = pager;
        if (getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        if (!(getAdapter() instanceof TabAdapter)) {
            throw new IllegalStateException("PagerAdapter:" + getAdapter().getClass().getName() + " must implements TabAdapter");
        }
        pager.addOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    public PagerAdapter getAdapter() {
        if (viewPager == null) {
            throw new IllegalStateException("called  setViewPager before  getAdapter");
        }
        return viewPager.getAdapter();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = getAdapter().getCount();
        TabAdapter adapter = (TabAdapter) getAdapter();
        for (int i = 0; i < tabCount; i++) {
            addTab(i, adapter.getTabView(i));
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                /*解决shouldExpand 为true 时 如果 当前PagerSlidingTabStrip
                 * 宽度不足够的情况下导致每个ITEM宽度不一致的bug
                 */
                if (shouldExpand && tabCount > 0) {
                    int maxTabWidth = 0;
                    for (int index = 0; index < tabCount; index++) {
                        int tempWidth = tabsContainer.getChildAt(index).getWidth();
                        if (tempWidth > maxTabWidth) {
                            maxTabWidth = tempWidth;
                        }
                    }
                    if (maxTabWidth > 0) {
                        for (int index = 0; index < tabCount; index++) {
                            View view = tabsContainer.getChildAt(index);
                            view.setMinimumWidth(maxTabWidth);
                        }
                    }
                }
                /*end bug**/
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = viewPager.getCurrentItem();
                setSelected(currentPosition);
                scrollToChild(currentPosition, 0);
            }
        });
    }

    public void setSelected(int position) {
        int childCount = tabsContainer.getChildCount();
        for (int index = 0; index < childCount; index++) {
            if (index == position) {
                tabsContainer.getChildAt(index).setSelected(true);
            } else {
                tabsContainer.getChildAt(index).setSelected(false);
            }
        }
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消翻页动画
                viewPager.setCurrentItem(position, ((TabAdapter) getAdapter()).smoothScroll());
            }
        });
        //设置在setBackgroundResource后面防止当tabBackgroundResId是shape的时候导致padding被覆盖
        //tab.setPadding(tabPaddingLeftRight, tabPaddingTopBottom, tabPaddingLeftRight, tabPaddingTopBottom);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }
        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int height = getHeight();
        // draw indicator line

        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }
        // draw indicatorHeight
        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw divider
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;
            //tabCount为0的时候 添加tabInfo然后调用 notifyDataSetChanged 执行会调用此方法导致奔溃(tabsContainer没有child),故加此判断
            if (tabCount != 0) {
                scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            }
            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(viewPager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            setSelected(position);
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        notifyDataSetChanged();
        requestLayout();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    private static class SavedState extends BaseSavedState {
        int currentPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
