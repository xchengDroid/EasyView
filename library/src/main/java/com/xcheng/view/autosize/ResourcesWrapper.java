package com.xcheng.view.autosize;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.xcheng.view.util.Preconditions;

/**
 * 今日头条的适配方案
 */
public class ResourcesWrapper extends Resources {
    private float targetDensity;
    private float targetScaledDensity;
    private int targetDensityDpi;
    private final float designSizeInDp;

    public ResourcesWrapper(Resources resources, int designSizeInDp) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        Preconditions.checkArgument(designSizeInDp != 0, "designSizeInDp==0");
        this.designSizeInDp = designSizeInDp;
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = super.getDisplayMetrics();
        if (targetDensity == 0) {
            float nonCompatDensity = displayMetrics.density;
            float nonCompatScaledDensity = displayMetrics.scaledDensity;
            if (designSizeInDp > 0) {
                targetDensity = displayMetrics.widthPixels / designSizeInDp;
            } else {
                targetDensity = displayMetrics.heightPixels / -designSizeInDp;
            }
            targetScaledDensity = targetDensity * (nonCompatScaledDensity / nonCompatDensity);
            targetDensityDpi = (int) (160 * targetDensity);
        }
        displayMetrics.density = targetDensity;
        displayMetrics.scaledDensity = targetScaledDensity;
        displayMetrics.densityDpi = targetDensityDpi;
        return displayMetrics;
    }

    @Override
    public Drawable getDrawableForDensity(int id, int density, @Nullable Theme theme) {
        return super.getDrawableForDensity(id, density, theme);
    }

    @Override
    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
        return super.getDrawableForDensity(id, density);
    }
}

