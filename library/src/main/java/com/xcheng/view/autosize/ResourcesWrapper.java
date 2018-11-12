package com.xcheng.view.autosize;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 今日头条的适配方案
 */
public class ResourcesWrapper extends Resources {
    private final AutoSize autoSize;
    private float targetDensity;
    private float targetScaledDensity;
    private int targetDensityDpi;
    private float targetXdpi;

    public ResourcesWrapper(Resources resources, AutoSize autoSize) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.autoSize = autoSize;
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = super.getDisplayMetrics();
        initValue(displayMetrics);
        autoSize(displayMetrics);
        return displayMetrics;
    }

    private void initValue(DisplayMetrics displayMetrics) {
        if (targetDensity == 0) {
            float nonCompatDensity = displayMetrics.density;
            float nonCompatScaledDensity = displayMetrics.scaledDensity;
            float designSizeInDp = autoSize.designSizeInDp;
            if (designSizeInDp > 0) {
                targetDensity = displayMetrics.widthPixels / designSizeInDp;
            } else {
                targetDensity = displayMetrics.heightPixels / -designSizeInDp;
            }
            targetScaledDensity = targetDensity * (nonCompatScaledDensity / nonCompatDensity);
            targetDensityDpi = (int) (160 * targetDensity);
            targetXdpi = targetDensity;
        }
    }

    private void autoSize(DisplayMetrics displayMetrics) {
        if (autoSize.isSupportDip) {
            displayMetrics.density = targetDensity;
            displayMetrics.densityDpi = targetDensityDpi;
        }
        if (autoSize.isSupportSp) {
            displayMetrics.scaledDensity = targetScaledDensity;
        }
        switch (autoSize.subUnit) {
            case TypedValue.COMPLEX_UNIT_PT:
                displayMetrics.xdpi = targetXdpi * 72f;
                break;
            case TypedValue.COMPLEX_UNIT_IN:
                displayMetrics.xdpi = targetXdpi;
                break;
            case TypedValue.COMPLEX_UNIT_MM:
                displayMetrics.xdpi = targetXdpi * 25.4f;
                break;
            default:
        }
    }
}

