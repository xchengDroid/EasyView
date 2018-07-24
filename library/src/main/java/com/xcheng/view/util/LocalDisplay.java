package com.xcheng.view.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 获取屏幕宽高的工具类，忽略屏幕的方向，宽度为较小的一边，高度为较长的一边
 */
public class LocalDisplay {

    private static int WIDTH_PIXEL;
    private static int HEIGHT_PIXEL;
    private static float DENSITY;
    private static int WIDTH_DP;
    private static int HEIGHT_DP;
    //default false
    private static volatile boolean sInitialized;

    public static void init(Context context) {
        if (sInitialized) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        DENSITY = dm.density;
        boolean widthLessThanHeight = dm.widthPixels <= dm.heightPixels;
        WIDTH_PIXEL = widthLessThanHeight ? dm.widthPixels : dm.heightPixels;
        HEIGHT_PIXEL = widthLessThanHeight ? dm.heightPixels : dm.widthPixels;
        WIDTH_DP = (int) (WIDTH_PIXEL / DENSITY);
        HEIGHT_DP = (int) (HEIGHT_PIXEL / DENSITY);
        sInitialized = true;
    }

    /**
     * 获取屏幕较窄的一边
     *
     * @return WIDTH_PIXEL
     */
    public static int widthPixel() {
        checkInit();
        return WIDTH_PIXEL;
    }

    /**
     * 获取屏幕较宽的一边
     *
     * @return HEIGHT_PIXEL
     */
    public static int heightPixel() {
        checkInit();
        return HEIGHT_PIXEL;
    }

    public static int widthDp() {
        checkInit();
        return WIDTH_DP;
    }

    public static int heightDp() {
        checkInit();
        return HEIGHT_DP;
    }

    public static float density() {
        checkInit();
        return DENSITY;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dp) {
        checkInit();
        final float scale = DENSITY;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        checkInit();
        final float scale = DENSITY;
        return (int) (pxValue / scale + 0.5f);
    }

    private static void checkInit() {
        if (!sInitialized)
            throw new IllegalStateException("LocalDisplay has not init");
    }

    /**
     * 单位转换
     */
    public static int convert(int unit, float value, Context context) {
        // TypedValue.COMPLEX_UNIT_SP
        return (int) TypedValue.applyDimension(unit, value, context.getResources()
                .getDisplayMetrics());
    }
}
