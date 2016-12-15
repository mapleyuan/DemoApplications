package com.maple.yuanweinan.demoapplication.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

/**
 * @author yuanweinan
 */
public class LogUtils {
    public static boolean sIsLog = true;
    public static String sTag = "William";
    private static final String[] LOGLEVELS = new String[]{"VERBOSE", "DEBUG  ", "INFO   ", "WARN   ", "ERROR  "};

    public LogUtils() {
    }

    public static void v(String msg) {
        v(sTag, msg);
    }

    public static void d(String msg) {
        d(sTag, msg);
    }

    public static void i(String msg) {
        i(sTag, msg);
    }

    public static void w(String msg) {
        w(sTag, msg);
    }

    public static void e(String msg) {
        e(sTag, msg);
    }

    public static void v(String tag, String msg) {
        if(sIsLog) {
            Log.v(tag, msg);
        }

    }

    public static void v(String tag, String msg, Throwable tr) {
        if(sIsLog) {
            Log.v(tag, msg, tr);
        }

    }

    public static void d(String tag, String msg) {
        if(sIsLog) {
            Log.d(tag, msg);
        }

    }

    public static void d(String tag, String msg, Throwable tr) {
        if(sIsLog) {
            Log.d(tag, msg, tr);
        }

    }

    public static void i(String tag, String msg) {
        if(sIsLog) {
            Log.i(tag, msg);
        }

    }

    public static void i(String tag, String msg, Throwable tr) {
        if(sIsLog) {
            Log.i(tag, msg, tr);
        }

    }

    public static void w(String tag, String msg) {
        if(sIsLog) {
            Log.w(tag, msg);
        }

    }

    public static void w(String tag, String msg, Throwable tr) {
        if(sIsLog) {
            Log.w(tag, msg, tr);
        }

    }

    public static void w(String tag, Throwable tr) {
        if(sIsLog) {
            Log.w(tag, tr);
        }

    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
        if(sIsLog) {
        }

    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
        if(sIsLog) {
        }

    }

    public static void showToast(Context context, CharSequence text, int duration) {
        if(sIsLog) {
            Toast.makeText(context, text, duration).show();
        }

    }

    public static void showToast(Context context, int resId, int duration) {
        if(sIsLog) {
            Toast.makeText(context, resId, duration).show();
        }

    }

    public static String getCurrentStackTraceString() {
        return Log.getStackTraceString(new Throwable());
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }



    private static String getLogString(String tag, String msg, Throwable tr, int level) {
        boolean seprator = true;
        StringBuilder sb = new StringBuilder();
        sb.append(DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString());
        sb.append('\t');
        sb.append(LOGLEVELS[level]);
        sb.append('\t');
        sb.append(tag);
        sb.append('\t');
        sb.append(msg);
        if(tr != null) {
            sb.append("\r\n");
            sb.append(getStackTraceString(tr));
        }

        sb.append("\r\n");
        return sb.toString();
    }
}
