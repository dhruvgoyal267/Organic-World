package com.organic.organicworld.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class UtilityFunctions {

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .into(imageView);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
