package android.utils;

import java.io.BufferedInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 
 * 
 * <p>
 * Title:图片功能基本操作
 * </p>
 * 
 * 
 * @histroy 修改历史
 * 
 *          <li>版本号 修改日期 修改人 修改说明
 * 
 *          <li>
 * 
 *          <li>
 */
public class ImageUtils {

	// static int i = 0;
	//
	// private static final int HashMap = 0;
	//
	// /**
	// * 根据文件名获取其drawable值,如果本地此文件不存在
	// *
	// * @param absolutePath
	// * 完整路径
	// * @return
	// */
	// public static Drawable getDrawableByFilePath(String absolutePath) {
	// // System.out.println("getDrawableByFileName()" + absolutePath);
	// Bitmap bitmap = null;
	// Drawable drawable = null;
	// try {
	// if (absolutePath != null && !absolutePath.equals("")) {
	// if (FileUtils.exists(absolutePath)) {// 文件存在
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// // options.inJustDecodeBounds = true;
	// options.inSampleSize = 4;
	// bitmap = BitmapFactory.decodeFile(absolutePath, options);
	//
	// // options.inSampleSize = computeSampleSize(options, -1,
	// // 128 * 128);
	// // options.inJustDecodeBounds = false;
	// // try {
	// // bitmap = BitmapFactory
	// // .decodeFile(absolutePath, options);
	// //
	// drawable = new BitmapDrawable(bitmap);
	// // } catch (OutOfMemoryError err) {
	// // // TODO: handle exception
	// // }
	//
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return drawable;
	// }
	//
	// public static Drawable myGetDrawableByFilePath(String absolutePath) {
	// // System.out.println("getDrawableByFileName()" + absolutePath);
	// Bitmap bitmap = null;
	// Drawable drawable = null;
	// try {
	// if (absolutePath != null && !absolutePath.equals("")) {
	// if (FileUtils.exists(absolutePath)) {// 文件存在
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// // options.inJustDecodeBounds = true;
	// options.inSampleSize = 2;
	// bitmap = BitmapFactory.decodeFile(absolutePath, options);
	//
	// // options.inSampleSize = computeSampleSize(options, -1,
	// // 128 * 128);
	// // options.inJustDecodeBounds = false;
	// // try {
	// // bitmap = BitmapFactory
	// // .decodeFile(absolutePath, options);
	// //
	// drawable = new BitmapDrawable(bitmap);
	// // } catch (OutOfMemoryError err) {
	// // // TODO: handle exception
	// // }
	//
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return drawable;
	// }
	//
	// public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
	// int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
	//
	// int roundedSize;
	// if (initialSize <= 8) {
	// roundedSize = 1;
	// while (roundedSize < initialSize) {
	// roundedSize <<= 1;
	// }
	// } else {
	// roundedSize = (initialSize + 7) / 8 * 8;
	// }
	//
	// return roundedSize;
	// }
	//
	// private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
	// double w = options.outWidth;
	// double h = options.outHeight;
	//
	// int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	// int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
	//
	// if (upperBound < lowerBound) {
	// // return the larger one when there is no overlapping zone.
	// return lowerBound;
	// }
	//
	// if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
	// return 1;
	// } else if (minSideLength == -1) {
	// return lowerBound;
	// } else {
	// return upperBound;
	// }
	// }
	//
	// private static int computeSampleSize(BitmapFactory.Options options, int target) {
	//
	// int w = options.outWidth;
	// int h = options.outHeight;
	// int candidateW = w / target;
	// int candidateH = h / target;
	// int candidate = Math.max(candidateW, candidateH);
	// if (candidate == 0)
	// return 1;
	// if (candidate > 1) {
	// if ((w > target) && (w / candidate) < target)
	// candidate -= 1;
	// }
	//
	// if (candidate > 1) {
	// if ((h > target) && (h / candidate) < target)
	// candidate -= 1;
	// }
	// return candidate;
	// }
	//
	// /**
	// * 从assets目录下读取小图片
	// *
	// * @param context
	// * @param fileName
	// * @param nScale
	// * 缩小到原来的1/nScale
	// * @return
	// */
	// public static synchronized Drawable getSmallDrawableFromFile(Context context, String fileName, int nScale) {
	// Bitmap bitmap = null;
	// Drawable drawable = null;
	// try {
	// AssetManager am = context.getAssets();
	// BufferedInputStream buf = new BufferedInputStream(am.open(fileName));
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inJustDecodeBounds = false;
	// opts.inSampleSize = nScale;
	// bitmap = BitmapFactory.decodeStream(buf, null, opts);
	// drawable = new BitmapDrawable(bitmap);
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// return drawable;
	// }

	/**
	 * 从assets目录下读取图片
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */

	public static synchronized Drawable getDrawableFromAssets(Context context, String fileName) {
		Bitmap bitmap = null;
		Drawable drawable = null;
		try {
			AssetManager am = context.getAssets();
			BufferedInputStream buf = new BufferedInputStream(am.open(fileName));
			bitmap = BitmapFactory.decodeStream(buf);
			buf.close();
			drawable = new BitmapDrawable(bitmap);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return drawable;
	}

	// /**
	// * 从assets目录下读取图片
	// *
	// * @param context
	// * @param fileName
	// * @return
	// */
	//
	// public static synchronized Drawable getDrawableFromAssetsByID(Context context, String fileName, int nIndex) {
	// Bitmap bitmap = null;
	// Drawable drawable = null;
	// try {
	// AssetManager am = context.getAssets();
	// BufferedInputStream buf = new BufferedInputStream(am.open(fileName));
	// bitmap = BitmapFactory.decodeStream(buf);
	// buf.close();
	// drawable = new BitmapDrawable(bitmap);
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// return drawable;
	// }
	//
	// /**
	// * 从assets目录下读取图片
	// *
	// * @param context
	// * @param fileName
	// * @return
	// */
	//
	// public static synchronized Drawable getDrawableFromAssetsByID(Context context, String fileName) {
	// Bitmap bitmap = null;
	// Drawable drawable = null;
	// try {
	// AssetManager am = context.getAssets();
	// BufferedInputStream buf = new BufferedInputStream(am.open(fileName));
	// bitmap = BitmapFactory.decodeStream(buf);
	// buf.close();
	// drawable = new BitmapDrawable(bitmap);
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// return drawable;
	// }
	//
	// public static synchronized Drawable getDrawableFromUrl(String url) {
	// // if(true)
	// // return null;
	// Bitmap bitmap = null;
	// Drawable drawable = null;
	// try {
	// // bitmap = getHttpBitmap(url);
	// // drawable = new BitmapDrawable(bitmap);
	// URL myFileUrl = null;
	// myFileUrl = new URL(url);
	// HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	// conn.setConnectTimeout(5000);
	// conn.setDoInput(true);
	// conn.connect();
	// InputStream is = conn.getInputStream();
	// drawable = Drawable.createFromStream(is, "src");
	// is.close();
	// //
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// return drawable;
	// }
	//
	// /**
	// * 从服务器取图片
	// *
	// * @param url
	// * @return
	// */
	// public static Bitmap getHttpBitmap(String url) {
	// URL myFileUrl = null;
	// Bitmap bitmap = null;
	// try {
	// myFileUrl = new URL(url);
	// HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	// conn.setConnectTimeout(20000);
	// conn.setDoInput(true);
	// conn.connect();
	// InputStream is = conn.getInputStream();
	// bitmap = BitmapFactory.decodeStream(is);
	// is.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return bitmap;
	// }

}
