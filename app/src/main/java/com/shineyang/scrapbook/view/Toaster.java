package com.shineyang.scrapbook.view;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ShineYang on 2016/12/27.
 */

public class Toaster {

    private static void showToast(Context context, String str, int duration) {
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }

    private static void showToast(Context context, String str, int duration, int gravity, int x, int y) {
        Toast toast = Toast.makeText(context, str, duration);
        toast.setGravity(gravity, x, y);
        toast.show();
    }

    //ordinary Toast
    public static void showShortToast(Context context, String str) {
        showToast(context, str, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, int res) {
        showShortToast(context, context.getString(res));
    }

    public static void showLongToast(Context context, String str) {
        showToast(context, str, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context context, int res) {
        showLongToast(context, context.getString(res));
    }

    //customized Toast
    public static void showCustomShortToast(Context context, String str, int gravity, int x, int y) {
        showToast(context,str, Toast.LENGTH_SHORT, gravity, x, y);
    }

    public static void showCustomLongToast(Context context, String str, int gravity, int x, int y) {
        showToast(context,str, Toast.LENGTH_LONG, gravity, x, y);
    }

    public static void showCustomShortToast(Context context, int res, int gravity, int x, int y) {
        showCustomShortToast(context, context.getString(res), gravity, x, y);
    }

    public static void showCustomLongToast(Context context, int res, int gravity, int x, int y) {
        showCustomLongToast(context, context.getString(res), gravity, x, y);
    }

}