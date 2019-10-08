package android.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import android.activity.ActivityManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.appconfig.AppConfigModel;
import android.appconfig.AppConfigSetting;
import android.application.CrashHandler;
import android.common.CommonHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.interfaces.HandlerListener;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.Message;
import android.util.Xml;
import android.utils.ApplicationManagement;
import android.utils.FileUtils;
import android.utils.ShowLog;
import android.view.KeyEvent;
import android.widget.CommonToast;

@TargetApi(VERSION_CODES.HONEYCOMB)
@SuppressLint("HandlerLeak")
public class AutoUpdateApkRunnable implements Runnable, HandlerListener {

	public static final String TAG = AutoUpdateApkRunnable.class.getSimpleName();

	public static final String _APP_DOWNLOAD_URL_KEY = "_APP_DOWNLOAD_URL_KEY";
	/**没有新的版本*/
	private final int UPDATA_NO_NEW_VERSION = 0;
	/**有新的版本*/
	private final int UPDATA_IS_NEW_VERSION = 1;
	/**检测版本超时*/
	private final int GET_UPDATA_INFO_TIME_OUT = 2;
	/**SD卡不能用*/
	private final int SD_CARD_NO_USEED = 3;
	/**下载出错*/
	private final int DOWN_ERROR = 4;
	/**取消下载*/
	private final int CANCLE_DOWNLOAD_APK = 5;
	private Context mContext;
	/** xml 服务器地址*/
	private String serverVersionXmlPath;
	/**apk 服务器域名 不需要可传空*/
	private String apkServerPath;
	/**下载到本地文件路径*/
	private String downLoadLocalPath;

	private UpdataInfo info;
	private String localVersion;

	public AutoUpdateApkRunnable(Context mContext, String serverVersionXmlPath, String apkServerPath, String downLoadLocalPath) {
		this.mContext = mContext;
		this.serverVersionXmlPath = serverVersionXmlPath;
		this.apkServerPath = apkServerPath;
		this.downLoadLocalPath = downLoadLocalPath;
		if (AppConfigModel.getInstance().getString(AppConfigModel.VERSION_KEY, "") != null && AppConfigModel.getInstance().getString(AppConfigModel.VERSION_KEY, "").length() > 0) {
			localVersion = AppConfigModel.getInstance().getString(AppConfigModel.VERSION_KEY, "");
		} else {
			try {
				localVersion = ApplicationManagement.getVersionName();
			} catch (Exception e) {
				ShowLog.e(TAG, CrashHandler.crashToString(e));
			}
		}
	}

	@Override
	public void run() {
		try {
			if (serverVersionXmlPath == null || serverVersionXmlPath.length() == 0) {
				return;
			}
			URL url = new URL(serverVersionXmlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			InputStream is = conn.getInputStream();
			info = getUpdataInfo(is);
			if (info != null) {
				AppConfigSetting.getInstance().putString(_APP_DOWNLOAD_URL_KEY, apkServerPath + info.getUrl());
				if (info.getVersion().compareToIgnoreCase(localVersion) > 0) {
					CommonHandler.getInstatnce().handerMessage(this, UPDATA_IS_NEW_VERSION, 0, 0, "");
				} else {
					CommonHandler.getInstatnce().handerMessage(this, UPDATA_NO_NEW_VERSION, 0, 0, "");
				}
			}
		} catch (Exception e) {
			ShowLog.e(TAG, CrashHandler.crashToString(e));
			CommonHandler.getInstatnce().handerMessage(this, GET_UPDATA_INFO_TIME_OUT, 0, 0, "");
		}
	}

	@Override
	public void onHandlerData(Message msg) {
		switch (msg.what) {
		case UPDATA_NO_NEW_VERSION:
			ShowLog.i(TAG, "当前已是最新版本");
			break;
		case UPDATA_IS_NEW_VERSION:
			showUpdataDialog();
			break;
		case GET_UPDATA_INFO_TIME_OUT:
			ShowLog.i(TAG, "联网超时");
			break;
		case SD_CARD_NO_USEED:
			ShowLog.i(TAG, "SD卡不可用");
			CommonToast.show("SD卡不可用");
		case DOWN_ERROR:
			ShowLog.i(TAG, "下载失败");
			CommonToast.show("下载失败");
			break;
		}
	}

	private void showUpdataDialog() {
		try {
			if (info != null) {
				Builder builer = null;
				if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
					builer = new Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
				} else {
					builer = new Builder(mContext);
				}
				builer.setTitle("有新的版本，请升级");
				builer.setMessage(info.getDescription());
				builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (dialog != null) {
							dialog.dismiss();
						}
						downLoadApk();
					}
				});
				builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (dialog != null) {
							dialog.dismiss();
						}
						if (localVersion != null && localVersion.length() > 0 && info.getVersionmin().compareToIgnoreCase(localVersion) >= 0) {
							ActivityManager.getActivityManager().exitApplication();
						} else {
							CommonHandler.getInstatnce().handerMessage(AutoUpdateApkRunnable.this, CANCLE_DOWNLOAD_APK, 0, 0, "");
						}
					}
				});
				AlertDialog dialog = builer.create();
				dialog.show();
				dialog.setOnKeyListener(keyListener);
				dialog.setCanceledOnTouchOutside(false);
			}
		} catch (Exception e) {
			ShowLog.e(TAG, CrashHandler.crashToString(e));
		}
	}

	private void downLoadApk() {
		if (mContext == null) {
			return;
		}
		final ProgressDialog pd;
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			pd = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
		} else {
			pd = new ProgressDialog(mContext);
		}
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载");
		pd.setCanceledOnTouchOutside(false);
		pd.setOnKeyListener(keyListener);
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			CommonHandler.getInstatnce().handerMessage(this, SD_CARD_NO_USEED, 0, 0, "");
		} else {
			pd.show();
			new Thread() {
				@Override
				public void run() {
					try {
						if (info != null) {
							String apkUrl = apkServerPath + info.getUrl();
							if (apkUrl == null || apkUrl.length() == 0) {
								return;
							}
							File file = getFileFromServer(apkUrl, pd);
							if (file != null) {
								sleep(1000);
								Intent intent = new Intent();
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setAction(Intent.ACTION_VIEW);
								intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
								mContext.startActivity(intent);
								pd.dismiss();
								android.os.Process.killProcess(android.os.Process.myPid());
							}
						}
					} catch (Exception e) {
						CommonHandler.getInstatnce().handerMessage(AutoUpdateApkRunnable.this, DOWN_ERROR, 0, 0, "");
						ShowLog.e(TAG, CrashHandler.crashToString(e));
					}
				}
			}.start();
		}
	}

	private File getFileFromServer(String path, ProgressDialog pd) {
		try {
			if (path != null && path.length() > 0 && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				pd.setMax(conn.getContentLength());
				InputStream is = conn.getInputStream();
				downLoadLocalPath = Environment.getExternalStorageDirectory() + "/" + downLoadLocalPath;
				if (!FileUtils.exists(downLoadLocalPath)) {
					FileUtils.createDir(downLoadLocalPath);
				}
				File file = new File(downLoadLocalPath, "_updata.apk");
				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len;
				int total = 0;
				while ((len = bis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					total += len;
					pd.setProgress(total);
				}
				fos.close();
				bis.close();
				is.close();
				return file;
			} else {
				return null;
			}
		} catch (Exception e) {
			ShowLog.e(TAG, CrashHandler.crashToString(e));
			return null;
		}
	}

	private UpdataInfo getUpdataInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int type = parser.getEventType();
		UpdataInfo info = new UpdataInfo();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText());
				} else if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText());
				} else if ("description".equals(parser.getName())) {
					info.setDescription(parser.nextText());
				} else if ("versionmin".equals(parser.getName())) {
					info.setVersionmin(parser.nextText());
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}

	OnKeyListener keyListener = new OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			return true;
		}
	};
}
