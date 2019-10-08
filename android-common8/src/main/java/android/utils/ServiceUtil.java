package android.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class ServiceUtil {
	/**
	 * 判断某个服务是否存在
	 * 
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean isServiceExisted(Context context, String className) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

		if (serviceList == null || !(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;

			if (serviceName.getClassName().equals(className)) {
				return true;
			}
		}
		return false;
	}

	public static void startService(Context packageContext, Class<?> cls) {
		Intent intent = new Intent(packageContext, cls);
		packageContext.startService(intent);
	}

	public static void stopService(Context packageContext, Class<?> cls) {
		Intent intent = new Intent(packageContext, cls);
		packageContext.stopService(intent);
	}

}
