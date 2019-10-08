package android.appconfig;

import android.application.CommonApplication;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 *****************************************************************************************************************************************************************************
 * 基于应用级别 保存数据状态（不受用户切换账号影响）
 * @author :Atar
 * @createTime:2017-1-6下午4:09:40
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class AppConfigModel {
	private final static String TAG = AppConfigModel.class.getCanonicalName();
	public final static String SHARED_PREF_NAME = TAG;
	// network
	public final static String LOGIN_ACCOUNT = TAG + ".LOGIN_ACCOUNT";
	public final static String LOGIN_PSW = TAG + ".LOGIN_PSW";
	public final static String VERSION_KEY = TAG + "VERSION_KEY";

	private SharedPreferences mSettings;
	private SharedPreferences.Editor mSettingsEditor;

	/** 应用程序配置 */
	private static AppConfigModel mAppConfigModel = null;

	public AppConfigModel() {
		try {
			Context context = CommonApplication.getContext();
			if (context != null) {
				mSettings = context.getSharedPreferences(AppConfigModel.SHARED_PREF_NAME, 0);
				mSettingsEditor = mSettings.edit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AppConfigModel getInstance() {
		if (mAppConfigModel == null) {
			mAppConfigModel = new AppConfigModel();
		}

		return mAppConfigModel;
	}

	public boolean putString(final String entry, String value, boolean commit) {
		if (mSettingsEditor == null) {
			return false;
		}
		mSettingsEditor.putString(entry.toString(), value);
		if (commit) {
			return mSettingsEditor.commit();
		}
		return true;
	}

	public boolean putString(final String entry, String value) {
		return putString(entry, value, false);
	}

	public boolean putInt(final String entry, int value, boolean commit) {
		if (mSettingsEditor == null) {
			return false;
		}
		mSettingsEditor.putInt(entry.toString(), value);
		if (commit) {
			return mSettingsEditor.commit();
		}
		return true;
	}

	public boolean putInt(final String entry, int value) {
		return putInt(entry, value, false);
	}

	public boolean putFloat(final String entry, float value, boolean commit) {
		if (mSettingsEditor == null) {
			return false;
		}
		mSettingsEditor.putFloat(entry.toString(), value);
		if (commit) {
			return mSettingsEditor.commit();
		}
		return true;
	}

	public boolean putFloat(final String entry, float value) {
		return putFloat(entry, value, false);
	}

	public boolean putBoolean(final String entry, boolean value, boolean commit) {
		if (mSettingsEditor == null) {
			return false;
		}
		mSettingsEditor.putBoolean(entry.toString(), value);
		if (commit) {
			return mSettingsEditor.commit();
		}
		return true;
	}

	public boolean putBoolean(final String entry, boolean value) {
		return putBoolean(entry, value, false);
	}

	public String getString(final String entry, String defaultValue) {
		if (mSettingsEditor == null) {
			return defaultValue;
		}
		try {
			return mSettings.getString(entry.toString(), defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public int getInt(final String entry, int defaultValue) {
		if (mSettingsEditor == null) {
			return defaultValue;
		}
		try {
			return mSettings.getInt(entry.toString(), defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public float getFloat(final String entry, float defaultValue) {
		if (mSettingsEditor == null) {
			return defaultValue;
		}
		try {
			return mSettings.getFloat(entry.toString(), defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public boolean getBoolean(final String entry, boolean defaultValue) {
		if (mSettingsEditor == null) {
			return defaultValue;
		}
		try {
			return mSettings.getBoolean(entry.toString(), defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * 清除所有
	 * @author :Atar
	 * @createTime:2015-7-10下午12:57:26
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void cleanAll() {
		if (mSettingsEditor != null) {
			mSettingsEditor.clear();
			mSettingsEditor.commit();
		}
	}

	public boolean commit() {
		if (mSettingsEditor == null) {
			return false;
		}
		return mSettingsEditor.commit();
	}
}
