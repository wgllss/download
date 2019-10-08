package android.utils;
//package android.common;
//
//import java.io.File;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//
//public class PlayAudio {
//
//	// 获取语音文件格式
//	public static String getMIMEType(File f) {
//
//		String end = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length()).toLowerCase();
//		String type = "";
//		if (end.equals("mp3") || end.equals("aac") || end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
//			type = "audio";
//		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")) {
//			type = "image";
//		} else {
//			type = "*";
//		}
//		type += "/";
//		return type;
//	}
//
//	// 播放语音
//	public static void openFile(Context context, File f) {
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setAction(android.content.Intent.ACTION_VIEW);
//		String type = getMIMEType(f);
//		intent.setDataAndType(Uri.fromFile(f), type);
//		context.startActivity(intent);
//	}
//
//}
