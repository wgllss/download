/**
 * 
 */
package android.utils;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :Atar
 * @createTime:2016-7-11下午1:42:51
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class DownLoadUtil {

	@SuppressLint({ "InlinedApi", "NewApi" })
	public void downFile(Context context, String downUrl, String title, String description, String downLoadShowFileName, String downLoadFileName) {
		if (context != null && downUrl != null && downUrl.length() > 0) {
			if (VERSION.SDK_INT <= VERSION_CODES.HONEYCOMB_MR2) {
				Uri uri = Uri.parse(downUrl);
				Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
				context.startActivity(downloadIntent);
			} else {
				Uri uri = Uri.parse(downUrl);
				Request request = new Request(uri);
				// 设置允许使用的网络类型，这里是移动网络和wifi都可以
				request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
				// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
				// request.setShowRunningNotification(false);
				// 不显示下载界面
				request.setDestinationInExternalPublicDir(downLoadShowFileName, downLoadFileName);
				request.setTitle(title);
				request.setDescription(description);
				request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				request.setVisibleInDownloadsUi(true);
				/*
				 * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件 在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用, 设置了下面这个将报错，不设置，下载后的文件在/cache这个 目录下面
				 */
				// request.setDestinationInExternalFilesDir(this, null,
				// "tar.apk");
				request.setMimeType("application/vnd.android.package-archive");
				DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
				int downloadId = (int) downloadManager.enqueue(request);
				String apkFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + downLoadShowFileName + File.separator + downLoadFileName;
				AtarCompleteReceiver completeReceiver = new AtarCompleteReceiver(downloadId, apkFilePath);
				IntentFilter mIntentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
				context.registerReceiver(completeReceiver, mIntentFilter);
			}
		}
	}

	public class AtarCompleteReceiver extends BroadcastReceiver {
		private int downloadId;
		private String apkFilePath;

		public AtarCompleteReceiver(int downloadId, String apkFilePath) {
			this.downloadId = downloadId;
			this.apkFilePath = apkFilePath;
		}

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action != null) {
				if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
					long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
					if (completeDownloadId == downloadId) {
						DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
						DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
						int result = -1;
						Cursor c = null;
						try {
							c = downloadManager.query(query);
							if (c != null && c.moveToFirst()) {
								result = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
							}
						} finally {
							if (c != null) {
								c.close();
							}
						}
						if (result == DownloadManager.STATUS_SUCCESSFUL) {
							if (apkFilePath != null && apkFilePath.length() > 0 && ".apk".equals(apkFilePath.substring(apkFilePath.lastIndexOf("."), apkFilePath.length()))) {
								File file = new File(apkFilePath);
								if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
									Intent i = new Intent(Intent.ACTION_VIEW);
									i.setDataAndType(Uri.parse("file://" + apkFilePath), "application/vnd.android.package-archive");
									if (!apkFilePath.contains("")) {
										i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									}
									context.startActivity(i);
								}
							}
						}
					}
				} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
					FileUtils.deleteFile(apkFilePath);
				}
			}
		}
	}
}
