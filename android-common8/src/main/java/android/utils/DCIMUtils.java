package android.utils;

import java.io.File;

import android.app.Activity;
import android.application.CommonApplication;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/** 调用手机相册和拍照功能方法 */
public class DCIMUtils {
	/** 调用系统图库,获取图片 */
	public final static int TYPE_LOAD_IMAGE = 100;
	/** 调用系统图库,获取视频 */
	public final static int TYPE_LOAD_VIDEO = 101;
	/** 调用系统照相 */
	public final static int TYPE_CAPTURE_IMAGE = 102;
	/** 调用系统录像 */
	public final static int TYPE_CAPTURE_VIDEO = 103;
	public final static int TYPE__CROP_ICON = 104;
	private static final String IMAGE_CAPTURE_NAME = PubTool.getCurrTime()
			+ "tmp.png"; // 照片名称
	protected final static String master_Dir = ApplicationManagement
			.getApplicationName() + "/microVideo/";// 主目录
	protected final static String mv_Camera_Dir = master_Dir + ".mv_camera/";// 微视拍照缓存目录

	/**
	 * 调用系统图库,获取图片
	 * 
	 * @param activity
	 */
	public static void startLoadImage(Activity activity) {
		if (activity == null)
			return;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/jpeg");
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, TYPE_LOAD_IMAGE);

	}

	/**
	 * 调用系统图库,获取视频
	 * 
	 * @param activity
	 */
	public static void startLoadVideo(Activity activity) {
		if (activity == null)
			return;
		Intent inent = new Intent(Intent.ACTION_PICK,
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
		activity.startActivityForResult(inent, TYPE_LOAD_VIDEO);
	}

	public static String getCacheDir() {
		String strHomeDir = "";// 主目录
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {// 有SD卡
			String str = android.os.Environment.getExternalStorageDirectory()
					.toString();
			if (!str.endsWith("/") && !str.endsWith("\\")) {
				str += '/';
			}
			strHomeDir = str;
		} else {
			strHomeDir = CommonApplication.getContext().getFilesDir()
					.getAbsolutePath();
		}

		String strDir = "";
		strDir = String.format("%s%s", strHomeDir, mv_Camera_Dir);
		if (strDir != null && strDir.equals("") == false) {
			FileUtils.createDir(strDir);
		}
		return strDir;
	}

	/**
	 * 调用系统照相
	 * 
	 * @param activity
	 */
	public static void startCamera(Activity activity) {
		if (activity == null)
			return;

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		String strDir = getCacheDir();
		File file = new File(strDir, IMAGE_CAPTURE_NAME);
		if (file != null && !file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				return;
			}
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(strDir, IMAGE_CAPTURE_NAME)));

		activity.startActivityForResult(intent, TYPE_CAPTURE_IMAGE);
	}

	/**
	 * 调用系统摄像
	 * 
	 * @param activity
	 */
	public static void startCaptureVideo(Activity activity) {
		if (activity == null)
			return;
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		activity.startActivityForResult(intent, TYPE_CAPTURE_VIDEO);
	}

	/**
	 * 获取结果
	 * 
	 * @param requestCode
	 *            值为：RESULT_LOAD_IMAGE、REQUEST_CAMERA
	 */

	public static ResultObject getResult(int requestCode, int resultCode,
			Intent data) {
		ResultObject object = new ResultObject();
		Context context = CommonApplication.getContext();
		if (resultCode == Activity.RESULT_OK && null != data) {
			switch (requestCode) {
			case TYPE_LOAD_IMAGE: {
				try {
					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };

					Cursor cursor = context.getContentResolver().query(
							selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					object.resoucePath = cursor.getString(columnIndex);
					object.type = TYPE_LOAD_IMAGE;
					cursor.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case TYPE_LOAD_VIDEO: {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Video.Media.DATA };

				Cursor cursor = context.getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				object.resoucePath = cursor.getString(columnIndex);
				object.type = TYPE_LOAD_VIDEO;
				cursor.close();
				break;
			}
			case TYPE_CAPTURE_IMAGE: {

				break;
			}
			case TYPE_CAPTURE_VIDEO: {
				Uri uriVideo = data.getData();
				Cursor cursor = context.getContentResolver().query(uriVideo,
						null, null, null, null);
				if (cursor.moveToNext()) {
					/** _data：文件的绝对路径 ，_display_name：文件名 */
					object.resoucePath = cursor.getString(cursor
							.getColumnIndex("_data"));
					object.type = TYPE_CAPTURE_VIDEO;
				}
				cursor.close();
				break;
			}
			default:
				return null;
			}
		} else if (resultCode == Activity.RESULT_OK
				&& TYPE_CAPTURE_IMAGE == requestCode) {
			String strDir = getCacheDir();
			File file = new File(strDir, IMAGE_CAPTURE_NAME);
			if (file.exists()) {
				object.resoucePath = file.getAbsolutePath();
				object.type = TYPE_CAPTURE_IMAGE;
			} else {
				object = null;
			}
		} else
			return null;

		return object;
	}

	public static class ResultObject {
		public String resoucePath;
		public int type;
	}

}
