package com.xcheng.view.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * 创建时间：2018/10/24
 * 编写人： chengxin
 * 功能描述：今日头条适配方案  在{@link Activity#onCreate(Bundle)} 中执行
 */
public class TouTiaoUIAdapter {
    private static float sNonCompatDensity;
    private static float sNonCompatScaledDensity;

    /**
     * @param activity        当前的activity对象
     * @param application     全局的application
     * @param designWidthInDp 设计的宽度
     */
    public static void setCustomDensity(Activity activity, final Application application, float designWidthInDp) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNonCompatDensity == 0) {
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        final float targetDensity = appDisplayMetrics.widthPixels / designWidthInDp;
        final float targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }
}
