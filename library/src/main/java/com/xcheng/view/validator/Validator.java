package com.xcheng.view.validator;

import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 创建时间：2018/9/29
 * 编写人： chengxin
 * 功能描述：
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
    }

    private void createPassersAndLazily() {
        if (mPassersCache == null) {
            mPassersCache = getValAnnotatedFields(mController.getClass());
        }
        if (mPassersCache.size() == 0) {
            String message = "No Val annotation found. You must have at least one Val annotation to validate.";
            throw new IllegalStateException(message);
        }
    }


    private List<Passer> getValAnnotatedFields(final Class<?> controllerClass) {
        List<Passer> passers = new ArrayList<>();
        List<Field> controllerViewFields = getControllerViewFields(controllerClass);
        for (Field field : controllerViewFields) {
            Val val = field.getAnnotation(Val.class);
            if (val == null)
                continue;
            TextView textView = getView(field);
            passers.add(new Passer(textView, val));
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
            if (TextView.class.isAssignableFrom(field.getType())) {
                viewFields.add(field);
            }
        }
        return viewFields;
    }

    private TextView getView(final Field field) {
        TextView view = null;
        try {
            field.setAccessible(true);
            view = (TextView) field.get(mController);

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
