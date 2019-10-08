package android.utils;
//package android.common;
//
//import java.io.File;
//
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//import android.widget.Toast;
//
//public class OpenFile {
//
//	// 获取文件格式
//	public static String getMIMEType(File f) {
//
//		String end = f
//				.getName()
//				.substring(f.getName().lastIndexOf(".") + 1,
//						f.getName().length()).toLowerCase();
//		String type = "";
//		if (end.equals("mp3") || end.equals("aac") || end.equals("amr")
//				|| end.equals("mpeg") || end.equals("mp4")) {
//			type = "audio";
//		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
//				|| end.equals("jpeg")) {
//			type = "image";
//		} else if (end.equals("pdf")) {
//			type = "application/pdf";
//		} else if (end.equals("doc")) {
//			type = "application/msword";
//		} else if (end.equals("ppt")) {
//			type = "application/vnd.ms-powerpoint";
//		} else if (end.equals("xls")) {
//			type = "application/vnd.ms-excel";
//		} else if (end.equals("txt")) {
//			// type = "application/msword";
//			type = "text/plain";
//		} else {
//			type = "*";
//		}
//		type += "/";
//		return type;
//	}
//
//	// 打开文件
//	public static void openFile(Context context, File f) {
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setAction(android.content.Intent.ACTION_VIEW);
//		String type = getMIMEType(f);
//		intent.setDataAndType(Uri.fromFile(f), type);
//		try {
//			context.startActivity(intent);
//		} catch (ActivityNotFoundException e) {
//			Log.d("wg", "打不开了,没有装");
//			Toast.makeText(context, "对不起,没有对应的软件可以打开！", Toast.LENGTH_SHORT)
//					.show();
//		}
//	}
//}
