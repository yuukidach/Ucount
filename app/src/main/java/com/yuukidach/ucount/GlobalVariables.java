package com.yuukidach.ucount;

import android.app.Application;

/**
 * Created by yuukidach on 17-3-21.
 */

public class GlobalVariables {
    private static String mDate = "";
    private static boolean mHasDot = false;
    private static String mInputMoney = "";
    private static String mDescription = "";

    public static void setmDate(String date)      { mDate = date;     }
    public static void setHasDot(boolean hasDot)  { mHasDot = hasDot; }
    public static void setmInputMoney(String a)   { mInputMoney = a;  }
    public static void setmDescription(String a ) { mDescription = a; }

    public static String getmDate()        { return mDate;        }
    public static boolean getmHasDot()     { return mHasDot;      }
    public static String getmInputMoney()  { return mInputMoney;  }
    public static String getmDescription() { return mDescription; }
}
