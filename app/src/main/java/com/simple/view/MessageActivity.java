package com.simple.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.xcheng.view.EasyView;
import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.validator.OnValidateListener;
import com.xcheng.view.validator.Passer;
import com.xcheng.view.validator.Valid;
import com.xcheng.view.validator.Validator;

import es.dmoral.toasty.Toasty;

public class MessageActivity extends EasyActivity {
    @Valid(order = 1)
    EditText loginName;
    @Valid(label = "密码", order = 2, min = 3)
    EditText password;
    Validator validator;
    @Valid(label = "名字呢", textViewId = R.id.tvName, min = 20)
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_message);
        Toasty.Config.getInstance().setWarningColor(Color.RED).apply();
        Toasty.info(this, "测试一般信息", 1, false).show();
        EasyView.success("登录成功");
        findViewById(R.id.tv_uncheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate(false);
            }
        });
        loginName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        frameLayout = findViewById(R.id.frameLayout);
        validator = new Validator(this);
        validator.setOnValidateListener(new OnValidateListener() {
            @Override
            public void onValidateSucceeded(Passer[] passers) {
            }

            @Override
            public boolean isValidRule(Passer passer) {
                Log.e("print", "isValidRule:" + passer.label + "====" + passer.getText());
                if (passer.isEmpty()) {
                    Toasty.warning(getContext(), passer.label + "不能为空").show();
                    return false;
                }
                if (passer.isLessThanMin()) {
                    Toasty.warning(getContext(), passer.label + "长度不能小于" + passer.min).show();
                    return false;
                }
                return true;
            }
        });

    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
