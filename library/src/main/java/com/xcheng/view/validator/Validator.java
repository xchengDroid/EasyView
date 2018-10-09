package com.xcheng.view.validator;

import android.view.View;
import android.widget.TextView;

import com.xcheng.view.util.Preconditions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 创建时间：2018/9/29
 * 编写人： chengxin
 * 功能描述：验证表单信息
 */
public class Validator {
    private Object mController;
    private OnValidateListener mOnValidateListener;
    private List<Passer> mPassersCache;

    public Validator(Object controller) {
        this.mController = controller;
    }

    public void setOnValidateListener(OnValidateListener onValidateListener) {
        this.mOnValidateListener = onValidateListener;
    }

    public void validate() {
        validate(false);
    }

    public void validate(boolean isAll) {
        createPassersAndLazily();
        Preconditions.checkNotNull(mOnValidateListener);
        boolean hasFailed = false;
        for (Passer passer : mPassersCache) {
            if (!mOnValidateListener.isValidRule(passer)) {
                hasFailed = true;
                if (!isAll) {
                    break;
                }
            }
        }
        if (!hasFailed) {
            Passer[] passers = new Passer[mPassersCache.size()];
            mOnValidateListener.onValidateSucceeded(mPassersCache.toArray(passers));
        }
    }

    public Passer findPasserByKey(String key) {
        createPassersAndLazily();
        for (Passer passer : mPassersCache) {
            if (passer.key.equals(key)) {
                return passer;
            }
        }
        return null;
    }

    public Passer findPasserByOrder(int order) {
        createPassersAndLazily();
        for (Passer passer : mPassersCache) {
            if (passer.order == order) {
                return passer;
            }
        }
        return null;
    }

    private void createPassersAndLazily() {
        if (mPassersCache == null) {
            mPassersCache = getValidAnnotatedFields(mController.getClass());
        }
        if (mPassersCache.size() == 0) {
            String message = "No Val annotation found. You must have at least one Val annotation to validate.";
            throw new IllegalStateException(message);
        }
    }


    private List<Passer> getValidAnnotatedFields(final Class<?> controllerClass) {
        List<Passer> passers = new ArrayList<>();
        List<Field> controllerViewFields = getControllerViewFields(controllerClass);
        for (Field field : controllerViewFields) {
            Valid val = field.getAnnotation(Valid.class);
            if (val == null)
                continue;

            View view = getView(field);
            //找到对应的TextView
            /*1、判断当前的view是不是一个TextView;2、判断获取textViewId获取对应的TextView*/
            TextView textView;
            if (view instanceof TextView) {
                textView = (TextView) view;
            } else if (val.textViewId() != -1) {
                textView = view.findViewById(val.textViewId());
            } else {
                throw new IllegalStateException(field.getName() + " must be a TextView or Valid annotation must have a textViewId.");
            }
            passers.add(new Passer(textView, val, field.getName()));
        }

        // Sort
        PassersComparator comparator = new PassersComparator();
        Collections.sort(passers, comparator);
        return passers;
    }

    private List<Field> getControllerViewFields(final Class<?> controllerClass) {

        // Fields declared in the controller
        List<Field> controllerViewFields = new ArrayList<>(getViewFields(controllerClass));
        // Inherited fields
        Class<?> superClass = controllerClass.getSuperclass();
        while (!superClass.equals(Object.class)) {
            List<Field> viewFields = getViewFields(superClass);
            if (viewFields.size() > 0) {
                controllerViewFields.addAll(viewFields);
            }
            superClass = superClass.getSuperclass();
        }

        return controllerViewFields;
    }

    private List<Field> getViewFields(final Class<?> clazz) {
        List<Field> viewFields = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (View.class.isAssignableFrom(field.getType())) {
                viewFields.add(field);
            }
        }
        return viewFields;
    }

    private View getView(final Field field) {
        View view = null;
        try {
            field.setAccessible(true);
            view = (View) field.get(mController);
            if (view == null) {
                String message = String.format("'%s %s' is null.",
                        field.getType().getSimpleName(), field.getName());
                throw new IllegalStateException(message);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return view;
    }
}
