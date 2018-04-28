package com.xcheng.view.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

//包裹Fragment页面
public class WrapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String clazzName = bundle.getString("fragmentClass");
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(clazzName);
        if (fragment == null) {
            fragment = Fragment.instantiate(this, clazzName, bundle);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment, clazzName).commit();
        }
    }

    public static Bundle clazzBundle(Class<? extends Fragment> clazz) {
        Bundle bundle = new Bundle();
        bundle.putString("fragmentClass", clazz.getName());
        return bundle;
    }
}
