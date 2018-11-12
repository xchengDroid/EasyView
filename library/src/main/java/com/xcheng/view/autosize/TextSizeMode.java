package com.xcheng.view.autosize;

import android.support.annotation.IntDef;

@IntDef
public @interface TextSizeMode {
    int FOLLOW_SYSYEM = 1;
    int FOLLOW_DENSITY = 2;
    int FOLLOW_SP = 3;
}