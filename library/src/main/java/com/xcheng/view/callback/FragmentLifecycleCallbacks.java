package com.xcheng.view.callback;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * fragment生命周期回调接口
 */
public interface FragmentLifecycleCallbacks {
    String TAG = "FragmentLifecycleCallbacks";

    //Created
    void onAttach(Fragment fragment, Context context);

    void onCreate(Fragment fragment, Bundle savedInstanceState);

    void onCreateView(Fragment fragment, View view, Bundle savedInstanceState);

    void onActivityCreated(Fragment fragment, Bundle savedInstanceState);

    //Started
    void onStart(Fragment fragment);

    //Resumed
    void onResume(Fragment fragment);

    //Paused
    void onPause(Fragment fragment);

    //Stopped
    void onStop(Fragment fragment);

    //saveInstanceState
    void onSaveInstanceState(Fragment fragment, Bundle outState);

    //Destroyed
    void onDestroyView(Fragment fragment);

    void onDestroy(Fragment fragment);

    void onDetach(Fragment fragment);

}