package android.appconfig;

import android.application.CommonApplication;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 *****************************************************************************************************************************************************************************
 * 用SharedPreferences 保存一些常用系统设置的 基于用户级别 保存数据状态 （用户切换账号受影响）
 * @author :Atar
 * @createTime:2014-6-18上午10:15:29
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:如:第一次登陆标志，已经登陆标志，帐号，密码 等，此类已经做了除此之外的其它可以按用户保存的操作; 可分用户存
 *****************************************************************************************************************************************************************************
 */
public class AppConfigSetting {

	private final static String TAG = AppConfigSetting.class.getSimpleName();
	private final static String SHARED_PREF_NAME = TAG;
	@SuppressWarnings("unused")
	private final static String LOGIN_SN = TAG + ".LOGIN_SN";
	public final static String LOGIN_NAME = TAG + ".LOGIN_NAME";
	public final static String LOGIN_NAME_PSW = ".LOGIN_NAME_PSW";
	private final static String LOGIN_USER_ID = TAG + ".LOGIN_USER_ID";
	private final static String LOGIN_USER_ID_PSW = TAG + ".LOGIN_USER_ID_PSW";

	@SuppressWarnings("unused")
	private final static String SAVE_PSW = TAG + ".SAVE_PSW";
	@SuppressWarnings("unused")
	private final static String AUTO_LOGIN = TAG + ".AUTO_LOGIN";
	private final static String IS_FIRST = TAG + ".IS_FIRST";
	private final static String IS_Login = TAG + ".IS_Login";
	private final static String Token = TAG + ".Token";
	@SuppressWarnings("unused")
	private final static String REMEMBER_PASSWORD = TAG + ".REMEMBER_PASSWORD";
	private final static String IS_YingDao = TAG + ".IS_YingDao";
	private final static String IS_BING_SUCCESS = TAG + "IS_BING_SUCCESS";// 绑定极光推送系统打开
	private final static String IS_BING_OPEN = TAG + "IS_BING_OPEN";// 是否绑定推送系统是否打开
	private final static String IS_VISITOR_LOGIN = TAG + "IS_VISITOR_LOGIN";// 是否浏览用户登陆

	private SharedPreferences mSettings;
	private SharedPreferences.Editor mSettingsEditor;

	private static AppConfigSetting mAppConfigSetting = null;

	public AppConfigSetting() {
		try {
			Context context = CommonApplication.getContext();
			if (context != null) {
				mSettings = context.getSharedPreferences(AppConfigSetting.SHARED_PREF_NAME, 0);
				mSettingsEditor = mSettings.edit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AppConfigSetting getInstance() {
		if (mAppConfigSetting == null) {
			mAppConfigSetting = new AppConfigSetting();
		}

		return mAppConfigSetting;
	}

	public void putString(String entry, String value) {
		if (mSettingsEditor != null) {
			mSettingsEditor.putString((getUserId() + TAG + entry).toString().trim(), value);
			mSettingsEditor.commit();
		}
	}

	public void putInt(String entry, int value) {
		if (mSettingsEditor != null) {
			mSettingsEditor.putInt((getUserId() + TAG + entry).toString().trim(), value);
			mSettingsEditor.commit();
		}
	}

	public void putFloat(String entry, float value) {
		if (mSettingsEditor != null) {
			mSettingsEditor.putFloat((getUserId() + TAG + entry).toString().trim(), value);
			mSettingsEditor.commit();
		}
	}

	public void putBoolean(String entry, Boolean value) {
		if (mSettingsEditor != null) {
			mSettingsEditor.putBoolean((getUserId() + TAG + entry).toString().trim(), value);
			mSettingsEditor.commit();
		}
	}

	public String getString(String entry, String defaultValue) {
		if (mSettingsEditor == null) {
			return defaultValue;
		}
		try {
			return mSettings.getString((getUserId() + TAG + entry).toString().trim(), defaultValue);
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
			return mSettings.getInt((getUserId() + TAG + entry).toString().trim(), defaultValue);
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
			return mSettings.getFloat((getUserId() + TAG + entry).toString().trim(), defaultValue);
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
			return mSettings.getBoolean((getUserId() + TAG + entry).toString().trim(), defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public void cleanAll() {
		if (mSettingsEditor != null) {
			mSettingsEditor.clear();
			mSettingsEditor.commit();
		}
	}

	public void remove(String key) {
		if (mSettingsEditor != null) {
			mSettingsEditor.remove(getUserId() + TAG + key);
			mSettingsEditor.commit();
		}
	}

	/*----------------------------特别处理几个参数 登陆用户ID,密码，Sesstion,是否登陆（IS_LOGIN），是否第一次（IS_FIRST）第一次是否引导过----------------------------------------------*/

	/**
	 * 保存token
	 * @author :Atar
	 * @createTime:2015-9-29下午2:55:33
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param strToken
	 * @description:
	 */
	public void saveToken(String strToken) {
		if (mSettingsEditor != null) {
			mSettingsEditor.putString(TAG + Token, strToken);
			mSettingsEditor.commit();
		}
	}

	/**
	 * 设置是否浏览用户登陆
	 * @author :Atar
	 * @createTime:2015-11-4下午1:15:50
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param isVisitor
	 * @description:
	 */
	public void setIsVisitorLogin(boolean isVisitor) {
		putBoolean(IS_VISITOR_LOGIN, isVisitor);
	}

	/**
	 * 保存登陆用户ID
	 * @author :Atar
	 * @createTime:2014-7-3上午9:25:31
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param userId
	 * @description:
	 */
	public void saveLoginUserId(String userId) {
		if (mSettingsEditor != null) {
			mSettingsEditor.putString(TAG + LOGIN_USER_ID, userId);
			mSettingsEditor.commit();
		}
	}

	/**
	 * 保存登陆用户密码
	 * @author :Atar
	 * @createTime:2014-7-3上午9:25:52
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param strPassword
	 * @description:
	 */
	public void saveLoginPassword(String strPassword) {
		putString(LOGIN_USER_ID_PSW, strPassword);
	}

	/**
	 * 设置当前用户是否登陆
	 * @author :Atar
	 * @createTime:2014-7-3上午9:26:18
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param islogin
	 * @description:
	 */
	public void setIsLogin(boolean islogin) {
		putBoolean(IS_Login, islogin);
	}

	/**
	 * 设置当前用户是否第一次使用
	 * @author :Atar
	 * @createTime:2014-7-3上午9:26:44
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param isFirst
	 * @description:
	 */
	public void setIsFirst(boolean isFirst) {
		putBoolean(IS_FIRST, isFirst);
	}

	/**
	 * 设置是否绑定推送系统成功
	 * @author :Atar
	 * @createTime:2014-8-11上午9:35:22
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param isBindSuccess
	 * @description:
	 */
	public void setIsBindSuccess(boolean isBindSuccess) {
		putBoolean(IS_BING_SUCCESS, isBindSuccess);
	}

	/**
	 * 设置推送系统打开状态
	 * @author :Atar
	 * @createTime:2015-7-10上午11:54:58
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param isBindOpen
	 * @description:
	 */
	public void setIsBindOpen(boolean isBindOpen) {
		putBoolean(IS_BING_OPEN, isBindOpen);
	}

	/**
	 * 设置是否引导过
	 * @author :Atar
	 * @createTime:2014-7-3上午10:06:10
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param isYdFlag
	 * @description:
	 */
	public void setIsYdFlag(boolean isYdFlag) {
		putBoolean(IS_YingDao, isYdFlag);
	}

	/**
	 * 得到登陆用户ID
	 * @author :Atar
	 * @createTime:2014-7-3上午9:27:34
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return 没有得到情况下，为空情况下返回 ""
	 * @description:
	 */
	public String getUserId() {
		if (mSettingsEditor == null) {
			return "";
		}
		try {
			return mSettings.getString(TAG + LOGIN_USER_ID, "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 得到登陆用户密码
	 * @author :Atar
	 * @createTime:2014-7-3上午9:27:57
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return 没有得到情况下，为空情况下返回 ""
	 * @description:
	 */
	public String getPassword() {
		return getString(LOGIN_USER_ID_PSW, "");
	}

	/**
	 * 判断是否登陆 
	 * @author :Atar
	 * @createTime:2014-7-3上午9:30:27
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:（没有设置情况下）默认false
	 */
	public boolean isLogin() {
		return getBoolean(IS_Login, false);
	}

	/**
	 * 判断是否第一次打开软件
	 * @author :Atar
	 * @createTime:2014-7-3上午9:31:20
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:（没有设置情况下）默认true
	 */
	public boolean isFirst() {
		return getBoolean(IS_FIRST, true);
	}

	/**
	 * 是否成功绑定推送系统
	 * @author :Atar
	 * @createTime:2014-8-11上午9:37:06
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:
	 */
	public boolean IsBindSuccess() {
		return getBoolean(IS_BING_SUCCESS, false);
	}

	/**
	 * 推送系统是否打开
	 * @author :Atar
	 * @createTime:2015-7-10上午11:55:56
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description: 默认情况下打开
	 */
	public boolean IsBindOpen() {
		return getBoolean(IS_BING_OPEN, true);
	}

	/**
	 * 判断是否引导过
	 * @author :Atar
	 * @createTime:2014-7-3上午10:07:07
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return 没有设置情况下）默认false
	 * @description: 
	 */
	public boolean isYdFlag() {
		return getBoolean(IS_YingDao, false);
	}

	/**
	 * 得到Token
	 * @author :Atar
	 * @createTime:2015-9-29下午2:57:56
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:
	 */
	public String getToken() {
		if (mSettingsEditor == null) {
			return "";
		}
		try {
			return mSettings.getString(TAG + Token, "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 得到是否浏览用户登陆
	 * @author :Atar
	 * @createTime:2015-11-4下午1:15:25
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:
	 */
	public boolean IsVisitorLogin() {
		return getBoolean(IS_VISITOR_LOGIN, false);
	}

	/*----------------------------特别处理几个参数 登陆用户ID,密码，Sesstion,是否登陆（IS_LOGIN），是否第一次（IS_FIRST）----------------------------------------------*/
}
