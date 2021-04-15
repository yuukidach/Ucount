package com.yuukidach.ucount.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class ImgUtils {
    private final Context context;
    private SharedPreferences pref;

    public ImgUtils(Context context) {
        this.context = context;
    }

    public void find(int id) {
        pref = context.getSharedPreferences("image" + id, Context.MODE_PRIVATE);
    }

    public void update(Uri uri) {
        if (pref == null) return;

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("uri", uri.toString());
        editor.apply();
    }
}
