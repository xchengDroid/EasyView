package com.simple.view;

import android.os.Bundle;
import android.widget.EditText;

import com.xcheng.view.controller.EasyActivity;


public class MessageActivity extends EasyActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_message);

        final EditText editMessage = (EditText) findViewById(R.id.editMessage);

    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
