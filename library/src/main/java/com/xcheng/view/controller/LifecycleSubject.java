package com.xcheng.view.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xcheng.view.callback.FragmentLifecycleCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * 生命周期控制订阅类
 * Created by chengxin on 2017/9/29.
 */
public class LifecycleSubject {
    private static final List<FragmentLifecycleCallbacks> sFragmentLifecycleCallbacks = new ArrayList<>();

    public static void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback) {
        synchronized (sFragmentLifecycleCallbacks) {
            sFragmentLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(FragmentLifecycleCallbacks callback) {
        synchronized (sFragmentLifecycleCallbacks) {
            sFragmentLifecycleCallbacks.remove(callback);
        }
    }

    private static Object[] collectFragmentLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (sFragmentLifecycleCallbacks) {
            if (sFragmentLifecycleCallbacks.size() > 0) {
                callbacks = sFragmentLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    static void dispatchAttach(Fragment fragment, Context context) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onAttach(fragment, context);
            }
        }
    }

    static void dispatchCreate(Fragment fragment, Bundle savedInstanceState) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onCreate(fragment, savedInstanceState);
            }
        }
    }


    static void dispatchCreateView(Fragment fragment, View view, Bundle savedInstanceState) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onCreateView(fragment, view, savedInstanceState);
            }
        }
    }

    static void dispatchActivityCreated(Fragment fragment, Bundle savedInstanceState) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onActivityCreated(fragment, savedInstanceState);
            }
        }
    }


    static void dispatchStart(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onStart(fragment);
            }
        }
    }


    static void dispatchResume(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onResume(fragment);
            }
        }
    }

    static void dispatchPause(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onPause(fragment);
            }
        }
    }


    static void dispatchStop(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onStop(fragment);
            }
        }
    }


    static void dispatchSaveInstanceState(Fragment fragment, Bundle outState) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onSaveInstanceState(fragment, outState);
            }
        }
    }

    static void dispatchDestroyView(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onDestroyView(fragment);
            }
        }
    }

    static void dispatchDestroy(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onDestroy(fragment);
            }
        }
    }

    static void dispatchDetach(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onDetach(fragment);
            }
        }
    }
}
