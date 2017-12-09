package com.xcheng.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcheng.view.util.JumpUtil;

/**
 * 简单的ViewHolder对象
 * Created by cc on 2016/10/8.
 */
public class EasyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    /**
     * Views indexed with their IDs
     */
    private final SparseArray<View> mViews;

    public EasyHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
    }

    public Context getContext() {
        return itemView.getContext();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 返回itemView最外层容器
     **/
    public ViewGroup getRootView() {
        if (itemView instanceof ViewGroup) {
            return (ViewGroup) itemView;
        }
        throw new IllegalStateException("itemView is not a ViewGroup");
    }

    public String getString(@StringRes int id) {
        return getContext().getString(id);
    }

    public int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    public int getDimen(@DimenRes int id) {
        return itemView.getResources().getDimensionPixelSize(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public EasyHolder setOnClickListener(@IdRes int viewId, View.OnClickListener l) {
        View view = getView(viewId);
        view.setOnClickListener(l);
        return this;
    }

    public EasyHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener l) {
        View view = getView(viewId);
        view.setOnLongClickListener(l);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    public EasyHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    public EasyHolder setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public EasyHolder setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The EasyHolder for chaining.
     */
    public EasyHolder linkify(@IdRes int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    public EasyHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseViewHolder for chaining.
     */
    public EasyHolder setBackgroundColor(@IdRes int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseViewHolder for chaining.
     */
    public EasyHolder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseViewHolder for chaining.
     */
    public EasyHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public EasyHolder setTextColorRes(int viewId, @ColorRes int colorId) {
        TextView view = getView(viewId);
        view.setTextColor(ContextCompat.getColorStateList(getContext(), colorId));
        return this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseViewHolder for chaining.
     */
    public EasyHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public EasyHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public EasyHolder setAlpha(@IdRes int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseViewHolder for chaining.
     */
    public EasyHolder setVisible(@IdRes int viewId, boolean visible) {
        return setVisible(viewId, visible, true);
    }


    /**
     * @param viewId
     * @param visible
     * @param isGone  visible 为false时是否占位 如果为 true为View.GONE else View.INVISIBLE
     * @return
     */
    public EasyHolder setVisible(@IdRes int viewId, boolean visible, boolean isGone) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : (isGone ? View.GONE : View.INVISIBLE));
        return this;
    }

    public EasyHolder setEnabled(@IdRes int viewId, boolean enabled) {
        View view = getView(viewId);
        view.setEnabled(enabled);
        return this;
    }

    public void toActivity(Class cls, Bundle bundle) {
        JumpUtil.toActivity((Activity) getContext(), cls, bundle);
    }
}
