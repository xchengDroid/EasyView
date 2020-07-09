package com.simple.view;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.widget.CheckView;
import com.xcheng.view.widget.ProgressView;

public class CheckViewActivity extends EasyActivity {

    public ProgressView progressView;
    CheckView cvCheckView;

    @Override
    public int getLayoutId() {
        return R.layout.ac_checkview;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //cvCheckView=findViewById(R.id.cv_checkView);
        Log.e("print", "1\n2\n\n\n3");
        Log.e("print", "\n\n\n13");
        System.out.println(1);
        System.out.println(2);
        System.out.println(3);
        System.out.println();
        System.out.println();
        System.out.print(5);
      //  System.out.println(4);

        System.out.print(6);

    }

    public static void main(String[] args) {
        System.out.println(1);
        System.out.println(2);
        System.out.println(3);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.print(5);
        //  System.out.println(4);

        System.out.print(6);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
