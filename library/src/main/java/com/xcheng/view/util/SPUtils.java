package com.xcheng.view.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.xcheng.view.EasyView;

import java.util.Map;
import java.util.Set;

public class SPUtils {
    /**
     * 保存在手机里面的文件名,不要轻易改变会影响之前的数据
     */
    public static final String FILE_NAME = "share_data";

    public static void putString(String key, @Nullable String value) {
        SharedPreferences.Editor editor = getSps().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static void putStringSet(String key, @Nullable Set<String> value) {
        SharedPreferences.Editor editor = getSps().edit();
        editor.putStringSet(key, value);
        apply(editor);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = getSps().edit();
        editor.putInt(key, value);
        apply(editor);
    }


    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = getSps().edit();
        editor.putLong(key, value);
        apply(editor);
    }


    public static void putFloat(String key, float value) {
        SharedPreferences.Editor editor = getSps().edit();
        editor.putFloat(key, value);
        apply(editor);
    }


    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSps().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }


    @Nullable
    public static String getString(String key, @Nullable String defValue) {
        return getSps().getString(key, defValue);
    }

    @Nullable
    public static Set<String> getStringSet(String key, @Nullable Set<String> defValue) {
        return getSps().getStringSet(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getSps().getInt(key, defValue);
    }


    public static long getLong(String key, long defValue) {
        return getSps().getLong(key, defValue);
    }


    public static float getFloat(String key, float defValue) {
        return getSps().getFloat(key, defValue);
    }


    public static boolean getBoolean(String key, boolean defValue) {
        return getSps().getBoolean(key, defValue);
    }

    public static <T> void putBean(String key, @NonNull T t, @NonNull TypeToken<T> token) {
        String json = JSONUtils.toJson(t, token.getType());
        if (!json.equals(JSONUtils.EMPTY_JSON)) {
            putString(key, json);
        }
    }

    @CheckResult
    public static <T> T getBean(String key, @NonNull TypeToken<T> token) {
        String json = getString(key, null);
        return JSONUtils.fromJson(json, token);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = getSps().edit();
        editor.remove(key);
        apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences.Editor editor = getSps().edit();
        editor.clear();
        apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return getSps().contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        return getSps().getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     * 如果找到则使用apply执行，否则使用commit
     *
     * @param editor
     */
    public static void apply(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    private static SharedPreferences getSps() {
        return EasyView.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
    }
}
