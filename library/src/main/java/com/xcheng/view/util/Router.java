package com.xcheng.view.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 创建时间：2018/5/14
 * 编写人： chengxin
 * 功能描述：路由器
 */
public final class Router {
    private Class<?> mClazz;
    private String mClassName;
    private String mAction;  // action of route
    private Uri mUri; // data of route

    private int mFlags = -1; // Flags of route
    @NonNull
    private Bundle mBundle = new Bundle();   // Data to transform
    // Animation
    private Bundle mOptionsCompat; // The transition animation of activity
    private int mEnterAnim;
    private int mExitAnim;

    private boolean mFinishAfterNav;//after start with finished;

    public static Router build(String className) {
        return new Router(className);
    }

    public static Router build(Class<?> clazz) {
        return new Router(clazz);
    }

    public static Router build() {
        return new Router();
    }

    public Router setClassName(String className) {
        this.mClassName = className;
        return this;
    }

    public Router setClass(Class<?> clazz) {
        this.mClazz = clazz;
        return this;
    }

    public Router setAction(String action) {
        this.mAction = action;
        return this;
    }

    public Router setUri(Uri uri) {
        this.mUri = uri;
        return this;
    }

    private Router(Class<?> clazz) {
        this.mClazz = clazz;
    }

    private Router(String className) {
        this.mClassName = className;
    }

    private Router() {
    }

    public Router setBundle(Bundle bundle) {
        if (bundle != null) {
            mBundle = bundle;
        }
        return this;
    }

    /**
     * Set options compat
     *
     * @param compat compat
     * @return this
     */
    @RequiresApi(16)
    public Router setOptionsCompat(ActivityOptionsCompat compat) {
        if (null != compat) {
            this.mOptionsCompat = compat.toBundle();
        }
        return this;
    }

    public Router setFlags(int flags) {
        mFlags = flags;
        return this;
    }

    public Router addFlags(int flags) {
        mFlags |= flags;
        return this;
    }

    public Router clearTop() {
        mFlags = Intent.FLAG_ACTIVITY_CLEAR_TOP// 注意本行的FLAG设置
                | Intent.FLAG_ACTIVITY_NO_ANIMATION;//设置NO_ANIMATION在set之后才有效
        return this;
    }

    // 多个Activity的值传递。ActivityA到达ActivityB再到达ActivityC，
    // 但ActivityB为过渡页可以finish了，此时ActivityC将值透传至ActivityA。
    public Router forwardResult() {
        mFlags = Intent.FLAG_ACTIVITY_FORWARD_RESULT;
        mFinishAfterNav = true;
        return this;
    }

    public Router clearTopWithState() {
        mFlags = Intent.FLAG_ACTIVITY_CLEAR_TOP// 注意本行的FLAG设置
                | Intent.FLAG_ACTIVITY_SINGLE_TOP;
        return this;
    }

    public Router beRoot() {
        mFlags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK;
        return this;
    }


    public Router finishAfterNav() {
        mFinishAfterNav = true;
        return this;
    }

    public Router transitionAnim(int enterAnim, int exitAnim) {
        this.mEnterAnim = enterAnim;
        this.mExitAnim = exitAnim;
        return this;
    }

    public void navigation(@NonNull final Context context, final int requestCode) {
        // Build intent
        Intent intent = new Intent(mAction/*ignore null*/, mUri/*ignore null*/);
        if (mClazz != null) {
            intent.setClass(context, mClazz);
        } else if (mClassName != null) {
            intent.setClassName(context, mClassName);
        }

        final int flags = mFlags;
        if (!(context instanceof Activity) && flags == -1/*未设置Flag*/) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (flags != -1) {
            intent.setFlags(flags);
        }
        intent.putExtras(mBundle);

        if (requestCode >= 0) {  // Need start for result
            //noinspection ConstantConditions
            ActivityCompat.startActivityForResult((Activity) context, intent, requestCode, mOptionsCompat);
        } else {
            ActivityCompat.startActivity(context, intent, mOptionsCompat);
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (mFinishAfterNav) {
                activity.finish();
            }
            if (0 != mEnterAnim || 0 != mExitAnim) {    // Old version.
                activity.overridePendingTransition(mEnterAnim, mExitAnim);
            }
        }
    }

    public void navigation(@NonNull final Fragment fragment, int requestCode) {
        final Context context = fragment.getContext();
        // Build intent
        Intent intent = new Intent(mAction/*ignore null*/, mUri/*ignore null*/);
        if (mClazz != null) {
            intent.setClass(context, mClazz);
        } else if (mClassName != null) {
            intent.setClassName(context, mClassName);
        }
        if (mFlags != -1) {
            intent.setFlags(mFlags);
        }
        intent.putExtras(mBundle);

        if (requestCode >= 0) {  // Need start for result
            fragment.startActivityForResult(intent, requestCode, mOptionsCompat);
        } else {
            fragment.startActivity(intent, mOptionsCompat);
        }
        Activity activity = fragment.getActivity();
        if (mFinishAfterNav) {
            activity.finish();
        }
        if (0 != mEnterAnim || 0 != mExitAnim) {    // Old version.
            activity.overridePendingTransition(mEnterAnim, mExitAnim);
        }
    }

    public void navigation(Context context) {
        navigation(context, -1);
    }

    public void navigation(Fragment fragment) {
        navigation(fragment, -1);
    }


    // Follow api copy from #{Bundle}======================================

    public Router withString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return this;
    }

    public Router withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public Router withShort(@Nullable String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }

    public Router withInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public Router withLong(@Nullable String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }

    public Router withDouble(@Nullable String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    public Router withByte(@Nullable String key, byte value) {
        mBundle.putByte(key, value);
        return this;
    }

    public Router withChar(@Nullable String key, char value) {
        mBundle.putChar(key, value);
        return this;
    }

    public Router withFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public Router withCharSequence(@Nullable String key, @Nullable CharSequence value) {
        mBundle.putCharSequence(key, value);
        return this;
    }

    public Router withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    public Router withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    public Router withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public Router withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        mBundle.putSparseParcelableArray(key, value);
        return this;
    }

    public Router withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public Router withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

    public Router withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        mBundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public Router withSerializable(@Nullable String key, @Nullable Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public Router withByteArray(@Nullable String key, @Nullable byte[] value) {
        mBundle.putByteArray(key, value);
        return this;
    }

    public Router withShortArray(@Nullable String key, @Nullable short[] value) {
        mBundle.putShortArray(key, value);
        return this;
    }

    public Router withCharArray(@Nullable String key, @Nullable char[] value) {
        mBundle.putCharArray(key, value);
        return this;
    }

    public Router withFloatArray(@Nullable String key, @Nullable float[] value) {
        mBundle.putFloatArray(key, value);
        return this;
    }

    public Router withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        mBundle.putCharSequenceArray(key, value);
        return this;
    }

    public Router withBundle(@Nullable String key, @Nullable Bundle value) {
        mBundle.putBundle(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    @CheckResult
    public static <T extends Serializable> T getSerializable(@NonNull Intent intent, String bundleKey) {
        return (T) intent.getSerializableExtra(bundleKey);
    }

    @CheckResult
    public static <T extends Parcelable> T getParcelable(@NonNull Intent intent, String bundleKey) {
        return intent.getParcelableExtra(bundleKey);
    }

}
