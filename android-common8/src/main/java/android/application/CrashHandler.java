package android.application;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.utils.ShowLog;

/**
 * 
 *****************************************************************************************************************************************************************************
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * @author :Atar
 * @createTime:2015-7-16上午11:32:42
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: 本类只接收错误异常不做处理 只为防止闪退现象
 *****************************************************************************************************************************************************************************
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = CrashHandler.class.getSimpleName();

	/* 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	/* CrashHandler实例 */
	private static CrashHandler INSTANCE = new CrashHandler();

	// 保证只有一个CrashHandler实例
	private CrashHandler() {
	}

	// 获取CrashHandler实例 ,单例模式
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		/* 获取系统默认的UncaughtException处理器 */
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		/* 设置该CrashHandler为程序的默认处理器 */
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			/* 如果用户没有处理则让系统默认的异常处理器来处理 */
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			if (ex != null) {
				ShowLog.e(TAG, "ex----->" + crashToString(ex));
			}
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * @author :Atar
	 * @createTime:2015-7-16上午11:29:55
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param ex
	 * @return
	 * @description:
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		return true;
	}

	/**
	 * 打印具体信息
	 * @author :Atar
	 * @createTime:2015-12-15上午9:44:28
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param ex
	 * @return
	 * @description:
	 */
	public static String crashToString(Throwable ex) {
		StringBuffer buffer = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		buffer.append(result);
		return buffer.toString();
	}
}
