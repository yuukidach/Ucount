package com.yuukidach.ucount;

import android.app.Application;

/**
 * Created by yuukidach on 17-3-21.
 */

public class GlobalVariables {
    private static String mDate = "";

    public static void setmDate(String date) { mDate = date; }
    public static String getmDate() { return mDate; }
}
