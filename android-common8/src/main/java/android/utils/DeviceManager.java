package android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.application.CommonApplication;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

public class DeviceManager {

	/*
	 * 获取手机MAC地址
	 */
	public static String getMacAddress() {
		String result = "";
		WifiManager wifiManager = (WifiManager) CommonApplication.getContext().getSystemService(CommonApplication.getContext().WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return result;
	}

	public static int getScreenWidth(Activity mActivity) {
		int screenWidth = mActivity.getWindow().getWindowManager().getDefaultDisplay().getWidth();
		return screenWidth;
	}

	public static int getScreenHeight(Activity mActivity) {
		int screenHeight = mActivity.getWindow().getWindowManager().getDefaultDisplay().getHeight();
		return screenHeight;
	}

	@SuppressLint("NewApi")
	public static boolean checkDeviceHasNavigationBar(Context activity) {

		// 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
		boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

		if (!hasMenuKey && !hasBackKey) {
			// 做任何你需要做的,这个设备有一个导航栏
			return true;
		}
		return false;
	}
}
