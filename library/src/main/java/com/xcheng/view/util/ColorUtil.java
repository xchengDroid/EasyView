package com.xcheng.view.util;

import android.graphics.Color;
import androidx.annotation.ColorInt;

/**
 * 颜色转换工具类
 * Created by cx on 17/8/18.
 */
public class ColorUtil {
    private static final int DISABLED_ALPHA_FILL = 186;//136
    private static final float ACTIVE_OPACITY_FACTOR_FILL = 0.125f;

    /**
     * 将颜色转换为16位字符串
     *
     * @param color
     * @return
     */
    public static String getHexColor(@ColorInt int color) {
        return String.format("#%s", Integer.toHexString(color));
    }

    /**
     * 按下填充显色
     *
     * @param color 将color经过转换计算成为pressedColor
     * @return
     */
    public static int pressed(int color) {
        return decreaseRgbChannel(color, ACTIVE_OPACITY_FACTOR_FILL);
    }

    /**
     * disable填充显色
     *
     * @param color 将color经过转换计算成为disabledColor
     * @return
     */
    public static int disabled(int color) {
        return increaseOpacity(color, DISABLED_ALPHA_FILL);
    }

    /**
     * 颜色加深处理
     * from: http://blog.csdn.net/jdsjlzx/article/details/41441083
     *
     * @param color RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *              Android中我们一般使用它的16进制，
     *              例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *              red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *              所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    public static int decreaseRgbChannel(@ColorInt int color, float percent) {
        // reduce rgb channel values to produce box shadow effect
        int red = (Color.red(color));
        red -= (red * percent);
        red = red > 0 ? red : 0;

        int green = (Color.green(color));
        green -= (green * percent);
        green = green > 0 ? green : 0;

        int blue = (Color.blue(color));
        blue -= (blue * percent);
        blue = blue > 0 ? blue : 0;

        return Color.argb(Color.alpha(color), red, green, blue);
    }

    public static int increaseOpacity(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}
