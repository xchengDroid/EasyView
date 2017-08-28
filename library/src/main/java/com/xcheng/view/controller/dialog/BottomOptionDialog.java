package com.xcheng.view.controller.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcheng.view.R;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.ShapeBinder;

public class BottomOptionDialog extends BottomDialog {
    private final Builder builder;

    private BottomOptionDialog(Builder builder) {
        super(builder.context);
        this.builder = builder;
    }

    @Override
    public int getLayoutId() {
        return builder.layoutId;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TextView bottomTextView = (TextView) findViewById(R.id.ev_id_optionDialog_bottom);
        if (builder.bottomText != null) {
            bottomTextView.setTextSize(builder.textSize);
            bottomTextView.setTextColor(builder.bottomTextColor);
            bottomTextView.setText(builder.bottomText);
            ViewGroup.LayoutParams lp = bottomTextView.getLayoutParams();
            lp.height = builder.optionHeight;
            bottomTextView.setLayoutParams(lp);
            ShapeBinder.with(builder.solidColor).radius(builder.radius).drawableStateTo(bottomTextView);
            bottomTextView.setOnClickListener(this);
        } else {
            bottomTextView.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ev_id_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new OptionDecoration(builder.dividerColor, 1));
        recyclerView.setAdapter(new OptionAdapter());
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.ev_id_optionDialog_bottom) {
            if (builder.onSelectListener != null) {
                builder.onSelectListener.onBottomSelect(v);
            }
        }
    }

    public static class Builder {
        private Context context;
        private int layoutId;
        private String[] optionTexts;
        private String bottomText;
        private OnSelectListener onSelectListener;
        private int solidColor;
        private int dividerColor;
        private int textSize;
        private int bottomTextColor;
        private int optionTextColor;
        private int radius;
        private int optionHeight;

        public Builder(Context context) {
            this.context = context;
            this.layoutId = R.layout.ev_dialog_option_bottom;
            this.bottomText = "取消";
            this.solidColor = Color.WHITE;
            this.bottomTextColor = ContextCompat.getColor(context, R.color.ev_text_grey);
            this.optionTextColor = ContextCompat.getColor(context, R.color.ev_light_blue);
            this.dividerColor = ContextCompat.getColor(context, R.color.ev_divider_color);
            this.textSize = 18;
            this.radius = LocalDisplay.dp2px(8);
            this.optionHeight = LocalDisplay.dp2px(45);
        }

        /**
         * 自定义布局必须有一个RecyclerView其id为 ev_id_recyclerView，
         * 底部有一个 TextView其id为 ev_id_recyclerView
         *
         * @param context  上下文
         * @param layoutId 布局的id
         */
        public Builder(Context context, @LayoutRes int layoutId) {
            this(context);
            this.layoutId = layoutId;
        }

        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder optionHeight(int optionHeight) {
            this.optionHeight = optionHeight;
            return this;
        }

        public Builder bottomTextColor(@ColorInt int bottomTextColor) {
            this.bottomTextColor = bottomTextColor;
            return this;
        }

        public Builder optionTextColor(@ColorInt int optionTextColor) {
            this.optionTextColor = optionTextColor;
            return this;
        }

        public Builder optionTexts(@Size(min = 1) String[] optionTexts) {
            this.optionTexts = optionTexts;
            return this;
        }

        /**
         * @param bottomText if null, do not show bottomBtn
         * @return Builder
         */
        public Builder bottomText(String bottomText) {
            this.bottomText = bottomText;
            return this;
        }

        public Builder onSelectListener(OnSelectListener l) {
            this.onSelectListener = l;
            return this;
        }

        public Builder solidColor(@ColorInt int solidColor) {
            this.solidColor = solidColor;
            return this;
        }

        public Builder dividerColor(@ColorInt int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        /**
         * @param textSize 单位为sp
         * @return Builder
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public BottomOptionDialog create() {
            if (optionTexts == null) {
                throw new IllegalStateException("optionTexts has not been init");
            }
            return new BottomOptionDialog(this);
        }

        public BottomOptionDialog show() {
            BottomOptionDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    private class OptionAdapter extends RecyclerView.Adapter<OptionHolder> {

        @Override
        public OptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView optionText = new TextView(getContext());
            optionText.setGravity(Gravity.CENTER);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(-1, builder.optionHeight);
            optionText.setTextColor(builder.optionTextColor);
            optionText.setTextSize(builder.textSize);
            optionText.setLayoutParams(lp);
            ShapeBinder.with(builder.solidColor).radius(builder.radius).drawableStateTo(optionText);
            return new OptionHolder(optionText);
        }

        @Override
        public void onBindViewHolder(final OptionHolder holder, final int position) {
            TextView optionText = (TextView) holder.itemView;

            int size = getItemCount();
            int radius = builder.radius;
            if (size == 1) {
                ShapeBinder.with(builder.solidColor).radius(radius).drawableStateTo(optionText);
            } else {
                if (position == 0) {
                    ShapeBinder.with(builder.solidColor).radii(new float[]{radius, radius, 0, 0}).drawableStateTo(optionText);
                } else if (position == size - 1) {
                    ShapeBinder.with(builder.solidColor).radii(new float[]{0, 0, radius, radius}).drawableStateTo(optionText);
                } else {
                    ShapeBinder.with(builder.solidColor).radii(new float[]{0, 0, 0, 0}).drawableStateTo(optionText);
                }
            }

            optionText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (builder.onSelectListener != null) {
                        builder.onSelectListener.onOptionSelect(v, holder.getAdapterPosition());
                    }
                }
            });
            optionText.setText(builder.optionTexts[position]);
        }

        @Override
        public int getItemCount() {
            return builder.optionTexts.length;
        }
    }

    private class OptionHolder extends RecyclerView.ViewHolder {

        private OptionHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnSelectListener {
        void onBottomSelect(View view);

        /**
         * @param view     对应的view
         * @param position 设置在数组中的位置
         */
        void onOptionSelect(View view, int position);
    }
}
