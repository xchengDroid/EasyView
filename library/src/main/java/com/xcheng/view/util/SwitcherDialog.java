package com.xcheng.view.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xcheng.view.R;
import com.xcheng.view.adapter.EasyAdapter;
import com.xcheng.view.adapter.EasyHolder;
import com.xcheng.view.controller.EasyDialog;
import com.xcheng.view.widget.CheckView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2018/10/31
 * 编写人： chengxin
 * 功能描述：切换环境使用
 */
public class SwitcherDialog extends EasyDialog {
    private static final List<Module> MODULES = new ArrayList<>();
    public static String TITLE = "请选择测试环境";
    private EasyAdapter<Module> mAdapter;
    private OnSwitcherListener mOnSwitcherListener;

    public static void addModule(String name, String... environments) {
        MODULES.add(new Module(name, null));
        for (String environment : environments) {
            MODULES.add(new Module(name, environment));
        }
    }

    public static void removeModule(String name) {
        List<Module> temp = new ArrayList<>();
        for (Module module : MODULES) {
            if (module.name.equals(name)) {
                temp.add(module);
            }
        }
        MODULES.removeAll(temp);
    }

    public SwitcherDialog(@NonNull Context context) {
        super(context, R.style.ev_dialog_commonStyle);
        setCancelable(false);
        //如果有title会屏掉掉
        // getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    public void setOnSwitcherListener(OnSwitcherListener onSwitcherListener) {
        this.mOnSwitcherListener = onSwitcherListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ev_dialog_switcher;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.ev_id_recyclerView);
        TextView title = findViewById(R.id.ev_id_titleView);
        title.setText(TITLE);
        final int blueColor = ContextCompat.getColor(getContext(), R.color.ev_light_blue);
        final int greyColor = ContextCompat.getColor(getContext(), R.color.ev_text_grey);

        mAdapter = new EasyAdapter<Module>(getContext(), MODULES, R.layout.ev_item_module) {

            @Override
            public void convert(EasyHolder holder, Module module, int position) {
                TextView tvNameOrEnvironment = holder.getView(R.id.tv_id_name_or_environment);
                CheckView checkView = holder.getView(R.id.cv_isChecked);
                if (module.environment == null) {
                    tvNameOrEnvironment.setText(module.name);
                    tvNameOrEnvironment.setTextSize(26);
                    checkView.setVisibility(View.GONE);
                    tvNameOrEnvironment.setTextColor(blueColor);
                } else {
                    tvNameOrEnvironment.setTextSize(24);
                    tvNameOrEnvironment.setTextColor(greyColor);
                    tvNameOrEnvironment.setText(module.environment);
                    checkView.setVisibility(View.VISIBLE);
                    checkView.setChecked(false);
                    if (mOnSwitcherListener != null) {
                        String environment = mOnSwitcherListener.getCurrentEnvironment(module.name);
                        if (environment != null && environment.equals(module.environment)) {
                            checkView.setChecked(true);
                            tvNameOrEnvironment.setTextColor(blueColor);
                        }
                    }
                }
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setListener() {
        super.setListener();
        findViewById(R.id.ev_id_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnSwitcherListener != null) {
                    mOnSwitcherListener.onSure();
                }
            }
        });
        mAdapter.setOnItemClickListener(new EasyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EasyHolder holder, int adapterPosition) {
                Module module = MODULES.get(adapterPosition);
                if (module.environment == null)
                    return;
                if (mOnSwitcherListener != null) {
                    mOnSwitcherListener.onSwitcher(module.name, module.environment);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public interface OnSwitcherListener {

        void onSwitcher(String name, String environment);

        String getCurrentEnvironment(String name);

        void onSure();
    }

    public static class Module {
        public final String name;
        //如果为null标识只显示标题
        public final String environment;

        public Module(String name, @Nullable String environment) {
            this.name = name;
            this.environment = environment;
        }
    }
}
