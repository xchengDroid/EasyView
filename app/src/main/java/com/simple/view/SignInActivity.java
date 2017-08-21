package com.simple.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcheng.view.processbtn.ActionProcessButton;


public class SignInActivity extends Activity implements ProgressGenerator.OnCompleteListener {

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sign_in);

        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);

        final ProgressGenerator progressGenerator = new ProgressGenerator(this);
        final ActionProcessButton btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        final ActionProcessButton btnSignIn2 = (ActionProcessButton) findViewById(R.id.btnSignIn2);
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
