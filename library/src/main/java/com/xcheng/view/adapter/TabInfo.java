package com.xcheng.view.adapter;

import android.os.Bundle;
import android.support.annotation.DrawableRes;

public final class TabInfo {
    public CharSequence title;
    public int iconResId = 0;
    /**
     * 指示器的背景
     */
    public int bgResId = 0;

    public final String tag;
    public final Class<?> clazz;
    public final Bundle args;

    public TabInfo(String _tag, CharSequence _title, Class<?> _clazz, Bundle bundle) {
        tag = _tag;
        title = _title;
        clazz = _clazz;
        args = bundle != null ? bundle : new Bundle();
        args.putString("tag", _tag);
        args.putCharSequence("title", _title);
    }

    public TabInfo(String _tag, CharSequence _title, Class<?> _clazz) {
        this(_tag, _title, _clazz, null);
    }

    public TabInfo(String _tag, @DrawableRes int _iconResId, CharSequence _title, Class<?> _clazz) {
        this(_tag, _title, _clazz);
        iconResId = _iconResId;
    }

    public TabInfo(String _tag, CharSequence _title, Class<?> _clazz, @DrawableRes int _bgResId) {
        this(_tag, _title, _clazz);
        bgResId = _bgResId;
    }

    public TabInfo(String _tag, CharSequence _title, Class<?> _clazz, @DrawableRes int _bgResId, Bundle bundle) {
        this(_tag, _title, _clazz, bundle);
        bgResId = _bgResId;
    }
}