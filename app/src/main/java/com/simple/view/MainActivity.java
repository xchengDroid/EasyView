package com.simple.view;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xcheng.view.EasyView;
import com.xcheng.view.controller.dialog.BottomOptionDialog;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyView.init(this);
        String[] items = getResources().getStringArray(R.array.sample_list);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0:
                startSignInActivity();
                break;
            case 1:
                startMessageActivity();
                break;
            case 2:
                startUploadActivity();
                break;
            case 3:
                startStateSampleActivity();
                break;
            case 4:
                BottomOptionDialog dialog = new BottomOptionDialog.Builder(this)
                      //  .bottomText(null)
                        .tipText("请先登录或注册")
                        .tipTextColor(Color.RED)
//                        .bottomTextColor(Color.YELLOW)
//                        .optionTextColor(Color.RED)
//                        .dividerColor(Color.GREEN)
//                        .solidColor(Color.MAGENTA)
//                        .textSize(30)
//                        .optionHeight(200)
//                        .radius(LocalDisplay.dp2px(10))
//                        .bottomText("底部测试")
                        .optionTexts(new String[]{"登录", "注册", "忘记密码",})
                        .onSelectListener(new BottomOptionDialog.OnSelectListener() {
                            @Override
                            public void onBottomSelect(View view) {
                                Log.e("print", "底部点击");
                            }

                            @Override
                            public void onOptionSelect(View view, int position) {
                                Log.e("print", "列表点击:" + position);
                            }
                        })
                        .create();
                dialog.show();
                break;
        }
    }

    private void startStateSampleActivity() {
        Intent intent = new Intent(this, StateSampleActivity.class);
        startActivity(intent);
    }

    private void startUploadActivity() {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

    private void startSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void startMessageActivity() {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
}
