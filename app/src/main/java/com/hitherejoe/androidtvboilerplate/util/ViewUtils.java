package com.hitherejoe.androidtvboilerplate.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ViewUtils {

    public static float convertPixelsToDp(float px, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public static float convertDpToPixel(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

}
