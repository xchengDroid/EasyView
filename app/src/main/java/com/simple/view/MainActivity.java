package com.simple.view;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.xcheng.view.EasyView;
import com.xcheng.view.autosize.AutoSize;
import com.xcheng.view.controller.dialog.BottomOptionDialog;
import com.xcheng.view.util.Router;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MainActivity extends ListActivity {
    static {
        //设置全局的Header构建器o
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                ClassicsHeader header = new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
                header.setPrimaryColorId(R.color.ev_holo_red_light);
                header.setTextSizeTitle(14);
                header.setFinishDuration(200);
                //header.setAccentColorId(android.R.color.white);
                return header;//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });

        EasyView.AUTOSIZE = new AutoSize(740, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] items = getResources().getStringArray(R.array.sample_list);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);

    }

    private Map<String, String> values = new HashMap<>();

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toasty.info(this, "点击操作").show();

        switch (position) {
            case 0:
                // startSignInActivity();
                EasyView.success("123");
                EasyView.error("456");
                EasyView.info("789");
                EasyView.warning("123");
                break;
            case 1:
                Router.build(CheckViewActivity.class).navigation(this);
                break;
            case 3:
                break;
            case 4:
                BottomOptionDialog dialog = new BottomOptionDialog.Builder(this)
                        //  .bottomText(null)
                        .tipText("请先登录或注册")
                        .tipTextColor(Color.RED)
                        .dividerColor(Color.GREEN)
//                        .bottomTextColor(Color.YELLOW)
//                        .optionTextColor(Color.RED)
//                        .dividerColor(Color.GREEN)
//                        .solidColor(Color.MAGENTA)
//                        .textSize(30)
//                        .optionHeight(200)
//                        .radius(LocalDisplay.dp2px(10))
//                        .bottomText("底部测试")
                        .optionTexts("登录", "注册", "忘记密码")
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
            case 5:
                startTabActivity();
                break;
            case 6:
                Router.build(RefreshActivity.class).navigation(this);
                break;
            case 7:
                Router.build(CardActivity.class).navigation(this);

                break;
            case 8:

                break;
        }
    }

    private void startTabActivity() {
        Router.build(TabSmartActivity.class).navigation(this);
    }

    public void testAndroidR() {
        try {
            Class c = Class.forName("android.R$styleable");
            int[] value = (int[]) c.getField("ProgressBar").get(null);

            Log.e("print", "value" + Arrays.toString(value));
            Log.e("print", "maxHeight:" + android.R.attr.maxHeight);

            Class c2 = Class.forName("com.android.internal.R$styleable");
            int[] value2 = (int[]) c.getField("ProgressBar").get(null);
            Object value3 = Class.forName("com.android.internal.R$attr").getField("maxHeight").get(null);

            Log.e("print", "value2" + Arrays.toString(value2));
            Log.e("print", "maxHeight2:" + value3);

        } catch (ClassNotFoundException e) {
            Log.e("print", "", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
