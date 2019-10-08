package android.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class ShowLog {
    public static boolean mDebug = true;

    /**
     * 设置日志状态
     *
     * @param debug 当debug为ture时显示日志，false时不显示日志
     */
    public static void setDebug(boolean debug) {
        mDebug = debug;
    }

    /**
     * Send a DEBUG log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (mDebug)
            android.util.Log.d(tag, msg);
    }

    /**
     * Send an ERROR log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (mDebug)
            android.util.Log.e(tag, msg);
    }

    public static void e(String tag, Throwable throwable) {
        if (mDebug) {
            StringBuffer buffer = new StringBuffer();
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            buffer.append(result);
            android.util.Log.e(tag, buffer.toString());
        }
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (mDebug)
            android.util.Log.e(tag, msg, tr);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (mDebug)
            android.util.Log.v(tag, msg);
    }

    /**
     * Send a WARN log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if (mDebug)
            android.util.Log.w(tag, msg);
    }

    /**
     * Send a INFO log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if (mDebug)
            android.util.Log.i(tag, msg);
    }

    public static void d(String tag, Map<String, String> parames) {
        if (mDebug)
            for (Map.Entry entry : parames.entrySet()) {
                String strKey = (String) entry.getKey();
                String strValue = entry.getValue().toString();
                android.util.Log.d(tag, strKey);
                android.util.Log.d(tag, strValue);
            }
    }

    public static void d(String tag, List<String> parames) {
        if (mDebug)
            for (String strContent : parames) {
                android.util.Log.d(tag, strContent);
            }
    }
}
