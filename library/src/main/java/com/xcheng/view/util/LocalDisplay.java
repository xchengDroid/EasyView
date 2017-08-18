package com.xcheng.view.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 获取屏幕宽高的工具类，忽略屏幕的方向，宽度为较小的一边，高度为较长的一边
 */
public class LocalDisplay {

    public static int SCREEN_WIDTH_PIXELS;
    public static int SCREEN_HEIGHT_PIXELS;
    public static float SCREEN_DENSITY;
    public static int SCREEN_WIDTH_DP;
    public static int SCREEN_HEIGHT_DP;
    private static boolean sInitialed;

    public static void init(Context context) {
        if (sInitialed || context == null) {
            return;
        }
        sInitialed = true;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        boolean widthLessThanHeight = dm.widthPixels <= dm.heightPixels;
        SCREEN_WIDTH_PIXELS = widthLessThanHeight ? dm.widthPixels : dm.heightPixels;
        SCREEN_HEIGHT_PIXELS = widthLessThanHeight ? dm.heightPixels : dm.widthPixels;
        SCREEN_WIDTH_DP = (int) (SCREEN_WIDTH_PIXELS / dm.density);
        SCREEN_HEIGHT_DP = (int) (SCREEN_HEIGHT_PIXELS / dm.density);
        SCREEN_DENSITY = dm.density;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dp) {
        final float scale = SCREEN_DENSITY;
        return (int) (dp * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        final float scale = SCREEN_DENSITY;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 单位转换
     */
    public static int applyDimension(int unit, float value, Context context) {
        // TypedValue.COMPLEX_UNIT_SP
        return (int) TypedValue.applyDimension(unit, value, context.getResources()
                .getDisplayMetrics());
    }
}
