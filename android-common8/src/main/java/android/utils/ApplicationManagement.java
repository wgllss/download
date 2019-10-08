package android.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.application.CommonApplication;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Process;

public class ApplicationManagement {

	// private PackageManager pm;

	/**
	 * 判断软件是否已安装
	 *
	 * @param packagePath
	 *            apk包package路径
	 * @return true 已安装,false 未安装
	 */
	public static boolean checkIsInstalled(Context context, String packagePath) {
		PackageInfo packageInfo;
		try {
			PackageManager pm = context.getPackageManager();
			packageInfo = pm.getPackageInfo(packagePath, 0);

		} catch (NameNotFoundException e) {
			packageInfo = null;
			// e.printStackTrace();
		}
		if (packageInfo == null) {
			System.out.println("没有安装");
			return false;
		} else {
			System.out.println("已经安装");
			return true;
		}
	}

	/**
	 * 读取本地apk文件的包路径
	 *
	 * @param apkFile
	 * @return 返回包路径,如果文件不存在,则返回空字符串
	 */
	public static String getPackagePathOfApk(Context context, String apkFile) {
		String packagePath = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(apkFile, PackageManager.GET_ACTIVITIES);
			if (info != null) {
				ApplicationInfo appInfo = info.applicationInfo;
				packagePath = appInfo.packageName; // 得到安装包名称
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packagePath;
	}

	/**
	 * 安装
	 *
	 * @param context
	 * @param filePath
	 */
	public static void Intall(Context context, String filePath) {
		Intent intallIntent = new Intent(Intent.ACTION_VIEW);
		intallIntent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");

		context.startActivity(intallIntent);
	}

	/**
	 * 卸载
	 *
	 * @param context
	 * @param packageName
	 */
	public static void Unintall(Context context, String packageName) {
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}

	/**
	 * 获取apk包的信息
	 *
	 * @param context
	 * @param packageName
	 */
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return packInfo;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getApplicationName() {
		ApplicationInfo applicationInfo = null;
		try {
			PackageManager pm = CommonApplication.getContext().getPackageManager();
			applicationInfo = pm.getApplicationInfo(CommonApplication.getContext().getPackageName(), 0);
			String applicationName = (String) pm.getApplicationLabel(applicationInfo);
			return applicationName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public static String getPackageName() {
		return CommonApplication.getContext().getPackageName();
	}

	public synchronized static String getVersionName() {
		return getPackageInfo().versionName;
	}

	public synchronized static PackageInfo getPackageInfo() {
		try {
			PackageManager pm = CommonApplication.getContext().getPackageManager();
			PackageInfo packInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			return packInfo;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static void doStartApplicationWithPackageName(Context context, String packagename) {

		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
		PackageInfo packageinfo = null;
		PackageManager pm = context.getPackageManager();
		try {
			packageinfo = pm.getPackageInfo(packagename, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return;
		}

		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// 通过getPackageManager()的queryIntentActivities方法遍历
		List<ResolveInfo> resolveinfoList = pm.queryIntentActivities(resolveIntent, 0);

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			// 设置ComponentName参数1:packagename参数2:MainActivity路径
			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			context.startActivity(intent);
		}
	}

	/***
	 * 启动app
	 * @param context
	 * @param appPackageName
	 */
	public static void startAppLaunchIntentForPackage(Context context, String appPackageName) {
		try {
			PackageManager pm = context.getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(appPackageName);
			context.startActivity(intent);
		} catch (Exception e) {

		}
	}

	public static boolean shouldInit() {
		ActivityManager am = ((ActivityManager) CommonApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();
		int myPid = Process.myPid();
		for (RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}
}