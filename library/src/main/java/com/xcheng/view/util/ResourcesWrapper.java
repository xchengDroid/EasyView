package com.xcheng.view.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * This extends Resources but delegates the calls to another Resources object. This enables
 * any customization done by some subclass of Resources to be also picked up.
 */
public class ResourcesWrapper extends Resources {
    private float targetDensity;
    private float targetScaledDensity;
    private int targetDensityDpi;

    public ResourcesWrapper(Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = super.getDisplayMetrics();
        if (targetDensity == 0) {
            float nonCompatDensity = displayMetrics.density;
            float nonCompatScaledDensity = displayMetrics.scaledDensity;
            targetDensity = displayMetrics.widthPixels / 1280f;
            targetScaledDensity = targetDensity * (nonCompatScaledDensity / nonCompatDensity);
            targetDensityDpi = (int) (160 * targetDensity);
        }
        displayMetrics.density = targetDensity;
        displayMetrics.scaledDensity = targetScaledDensity;
        displayMetrics.densityDpi = targetDensityDpi;
        return displayMetrics;
    }
}

