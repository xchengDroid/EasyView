package com.simple.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.processbtn.ActionProcessButton;


public class SignInActivity extends EasyActivity implements ProgressGenerator.OnCompleteListener {

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
//./gradlew clean build bintrayUpload -PbintrayUser=xcheng -PbintrayKey=4f8465004b752b103ea2e374ad6cb73ec38d8601 -PdryRun=false

    @Override
    public int getLayoutId() {
        return R.layout.ac_sign_in;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        final EditText editEmail = findViewById(R.id.editEmail);
        final EditText editPassword =  findViewById(R.id.editPassword);

        final ProgressGenerator progressGenerator = new ProgressGenerator(this);
        final ActionProcessButton btnSignIn =  findViewById(R.id.btnSignIn);
        final ActionProcessButton btnSignIn2 = findViewById(R.id.btnSignIn2);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressGenerator.start(btnSignIn);
                editEmail.setEnabled(false);
                editPassword.setEnabled(false);
            }
        });
        btnSignIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressGenerator.start(btnSignIn2);
                editEmail.setEnabled(false);
                editPassword.setEnabled(false);
            }
        });
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, R.string.Loading_Complete, Toast.LENGTH_LONG).show();
    }

}
