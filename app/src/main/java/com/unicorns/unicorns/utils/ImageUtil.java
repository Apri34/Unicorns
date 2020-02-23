package com.unicorns.unicorns.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.unicorns.unicorns.R;

public class ImageUtil {

    public static Drawable getImage(Context context, String color) {
        if(color == null) return null;
        String[] array = context.getResources().getStringArray(R.array.colors);
        if(color.equals(array[0])) {
            return ContextCompat.getDrawable(context, R.drawable.unicorn_blue);
        } else if(color.equals(array[1])) {
            return ContextCompat.getDrawable(context, R.drawable.unicorn_green);
        } else if(color.equals(array[2])) {
            return ContextCompat.getDrawable(context, R.drawable.unicorn_purple);
        } else if(color.equals(array[3])) {
            return ContextCompat.getDrawable(context, R.drawable.unicorn_yellow);
        } else {
            return null;
        }
    }
}
