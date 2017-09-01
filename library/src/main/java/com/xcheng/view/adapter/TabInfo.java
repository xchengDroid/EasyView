package com.xcheng.view.adapter;

import android.os.Bundle;
import android.support.annotation.DrawableRes;

public final class TabInfo {
    public final CharSequence title;
    public final String tag;
    public final Class<?> clazz;
    public final Bundle args;

    public final int iconResId;
    /**
     * Tab背景
     */
    public final int bgResId;

    public TabInfo(String _tag, @DrawableRes int _iconResId, CharSequence _title, @DrawableRes int _bgResId, Class<?> _clazz, Bundle bundle) {
        tag = _tag;
        title = _title;
        clazz = _clazz;
        iconResId = _iconResId;
        bgResId = _bgResId;
        args = bundle != null ? bundle : new Bundle();
        args.putString("tag", _tag);
        args.putCharSequence("title", _title);
    }

    public TabInfo(String _tag, CharSequence _title, Class<?> _clazz, Bundle _args) {
        this(_tag, 0, _title, 0, _clazz, _args);
    }

    public TabInfo(String _tag, CharSequence _title, Class<?> _clazz) {
        this(_tag, 0, _title, 0, _clazz, null);
    }
    /**
     * 如果没有DrawableRes 传入 0
     *
     * @param _iconResId icon资源
     */
    public TabInfo(String _tag, @DrawableRes int _iconResId, CharSequence _title, Class<?> _clazz) {
        this(_tag, _iconResId, _title, 0, _clazz, null);
    }

    /**
     * 如果没有DrawableRes 传入 0
     *
     * @param _bgResId 背景资源
     */
    public TabInfo(String _tag, CharSequence _title, @DrawableRes int _bgResId, Class<?> _clazz) {
        this(_tag, 0, _title, _bgResId, _clazz, null);
    }
}