package com.unicorns.unicorns.utils;

import android.content.Context;

import com.unicorns.unicorns.R;

import androidx.core.content.ContextCompat;

/**
 * ColorUtil class created by Andreas Pribitzer 13.01.2020
 * Used for data binding in xml files
 */

public class ColorUtil {
    //Returns the corresponding color(int) for input color(String)
    public static int getColor(Context context, String color) {
        String[] array = context.getResources().getStringArray(R.array.colors);
        if(color.equals(array[0])) {
            return ContextCompat.getColor(context, android.R.color.holo_blue_bright);
        } else if(color.equals(array[1])) {
            return ContextCompat.getColor(context, android.R.color.holo_green_light);
        } else if(color.equals(array[2])) {
            return ContextCompat.getColor(context, android.R.color.holo_purple);
        } else if(color.equals(array[3])) {
            return ContextCompat.getColor(context, R.color.yellow);
        } else {
            return android.R.color.transparent;
        }
    }
}
