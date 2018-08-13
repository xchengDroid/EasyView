package com.xcheng.view.controller.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcheng.view.R;
import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.adapter.SpaceDecoration;
import com.xcheng.view.divider.DividerTextView;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BottomOptionDialog extends BottomDialog {
    private final Builder mBuilder;

    private BottomOptionDialog(Builder builder) {
        super(builder.context);
        this.mBuilder = builder;
    }

    @Override
    public int getLayoutId() {
        return mBuilder.layoutId;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TextView topTipTextView = findViewById(R.id.ev_id_optionDialog_topTip);
        if (mBuilder.tipText != null) {
            //如果tipText有内容而此时没有对应的TextView 则需要抛出异常
            Preconditions.checkState(topTipTextView != null, "layout res must have a TextView with id named ev_id_optionDialog_topTip");

            topTipTextView.setTextSize(mBuilder.tipTextSize);
            topTipTextView.setTextColor(mBuilder.tipTextColor);
            topTipTextView.setText(mBuilder.tipText);
            ViewGroup.LayoutParams lp = topTipTextView.getLayoutParams();
            //lp.height = mBuilder.optionHeight;
            topTipTextView.setBackgroundColor(mBuilder.itemColor);
            //topTipTextView.setLayoutParams(lp);
            //设置分割线的颜色
            if (topTipTextView instanceof DividerTextView) {
                DividerTextView dividerTextView = (DividerTextView) topTipTextView;
                dividerTextView.setBottomColor(mBuilder.dividerColor);
            }
        } else {
            if (topTipTextView != null) {
                topTipTextView.setVisibility(View.GONE);
            }
        }

        TextView bottomTextView = (TextView) findViewById(R.id.ev_id_optionDialog_bottom);
        if (mBuilder.bottomText != null) {
            Preconditions.checkState(bottomTextView != null, "layout res must have a TextView with id named ev_id_optionDialog_bottom");
            bottomTextView.setTextSize(mBuilder.textSize);
            bottomTextView.setTextColor(mBuilder.bottomTextColor);
            bottomTextView.setText(mBuilder.bottomText);
            ViewGroup.LayoutParams lp = bottomTextView.getLayoutParams();
            lp.height = mBuilder.optionHeight;
            bottomTextView.setLayoutParams(lp);
            bottomTextView.setOnClickListener(this);
            bottomTextView.setBackgroundColor(mBuilder.itemColor);
        } else {
            if (bottomTextView != null) {
                bottomTextView.setVisibility(View.GONE);
            }
        }
        RecyclerView recyclerView = findViewById(R.id.ev_id_recyclerView);
        Preconditions.checkState(recyclerView != null, "layout res must have a RecyclerView with id named ev_id_recyclerView");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceDecoration(mBuilder.dividerColor, 1));
        recyclerView.setAdapter(new OptionAdapter(mBuilder.context, new ArrayList<>(Arrays.asList(mBuilder.optionTexts))));
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.ev_id_optionDialog_bottom) {
            if (mBuilder.onSelectListener != null) {
                mBuilder.onSelectListener.onBottomSelect(v);
            }
        }
    }

    public static class Builder {
        private Context context;
        private int layoutId;
        private String[] optionTexts;
        private String tipText;
        private int tipTextColor;
        private int tipTextSize;
        private String bottomText;
        private OnSelectListener onSelectListener;
        private int itemColor;
        private int dividerColor;
        private int textSize;
        private int bottomTextColor;
        private int optionTextColor;
        private int optionHeight;

        public Builder(Context context) {
            this.context = context;
            this.layoutId = R.layout.ev_dialog_option_bottom;
            this.tipTextSize = 14;
            this.tipTextColor = ContextCompat.getColor(context, R.color.ev_text_grey);
            this.bottomText = "取消";
            this.itemColor = Color.WHITE;
            this.bottomTextColor = ContextCompat.getColor(context, R.color.ev_text_grey);
            this.optionTextColor = ContextCompat.getColor(context, R.color.ev_light_blue);
            this.dividerColor = ContextCompat.getColor(context, R.color.ev_divider_color);
            this.textSize = 18;
            this.optionHeight = LocalDisplay.dp2px(45);
        }

        /**
         * 自定义布局必须有一个RecyclerView其id为 ev_id_recyclerView，
         * * @param layoutId 布局的id
         */
        public Builder layoutId(@LayoutRes int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder tipText(String tipText) {
            this.tipText = tipText;
            return this;
        }

        public Builder tipTextColor(@ColorInt int tipTextColor) {
            this.tipTextColor = tipTextColor;
            return this;
        }

        public Builder tipTextSize(int tipTextSize) {
            this.tipTextSize = tipTextSize;
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

        public Builder optionTexts(@Size(min = 1) String... optionTexts) {
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

        public Builder itemColor(@ColorInt int itemColor) {
            this.itemColor = itemColor;
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
            Preconditions.checkState(optionTexts != null, "optionTexts has not been init");
            return new BottomOptionDialog(this);
        }

        public BottomOptionDialog show() {
            BottomOptionDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    private class OptionAdapter extends EasyAdapter<String> {
        private OptionAdapter(Context context, @Nullable List<String> data) {
            super(context, data);
        }

        @Override
        public View getDelegateView(ViewGroup parent, int viewType) {
            TextView optionText = new TextView(getContext());
            optionText.setGravity(Gravity.CENTER);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(-1, mBuilder.optionHeight);
            optionText.setTextColor(mBuilder.optionTextColor);
            optionText.setTextSize(mBuilder.textSize);
            optionText.setLayoutParams(lp);
            optionText.setBackgroundColor(mBuilder.itemColor);
            return optionText;
        }

        @Override
        public void convert(final EasyHolder holder, String s, int position) {
            TextView optionText = (TextView) holder.itemView;
            optionText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mBuilder.onSelectListener != null) {
                        mBuilder.onSelectListener.onOptionSelect(v, holder.getAdapterPosition());
                    }
                }
            });
            optionText.setText(mBuilder.optionTexts[position]);
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
