package android.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("deprecation")
public class PubTool {
	// /**
	// * 验证手机号码、电话号码是否有效 手机号前面加86的情况也考虑 新联通 　　 *（中国联通+中国网通）手机号码开头数字
	// * 130、131、132、145、155、156、185、186 　　 * 新移动 　　 * （中国移动+中国铁通）手机号码开头数字
	// * 134、135、136、137、138、139、147、150、151、152、157、158、159、182、183、187、188 　　 *
	// * 新电信 　　 * （中国电信 < http://baike.baidu.com/view/3214.htm>+中国卫通）手机号码开头数字
	// * 133、153、189、180 座机： 3/4位区号（数字）+ “-” + 7/8位（数字）+ “-”+数字位数不限
	// * 说明：“-”+数字位数不限；这段可有可无
	// */
	// public static boolean checkphone(String photo) {
	// if (null != photo) {
	// String reisphoto = photo.replace(",", ",").replace(";", ",").replace(";", ",").replace("　", ",").replace(" ", ",").replace("/", ",").replace("\\", ",");
	// String[] photo1 = reisphoto.split(",");
	// String[] photo2 = new String[photo1.length];
	// boolean isfirst;
	// if (null != photo1 && photo1.length > 0) {
	// for (int i = 0; i < photo1.length; i++) {
	// isfirst = false;
	// if (photo1[i].matches("(^[0-9]{3,4}-[0-9]{3,8}$)|^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|2|3|5|6|7|8|9]) \\d{8}$")) {
	// photo2[i] = photo1[i];
	// isfirst = true;
	// }
	// // 第二规则 “-”+数字位数不限 和手机号前面加86的情况也考虑
	// if (!isfirst) {
	// if (photo1[i].matches("(^[0-9]{3,4}-[0-9]{3,8}-[0-9]{0,100}$)|^((\\+86)|(86))?(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|2|3|5|6|7|8|9]) \\d{8}$")) {
	// photo2[i] = photo1[i];
	// }
	// }
	// }
	// // 如果两个电话 只用一个
	// if (photo2.length > 0) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }
	//
	// /**
	// * 下载文件
	// *
	// * @param url
	// * 下载地址
	// * @param path
	// * 保存地址
	// * @return 是否成功
	// */
	// public static boolean downLoadFile(String url, File file, Handler mh) {
	// boolean res = false;
	// InputStream is = null;
	// FileOutputStream fileOutputStream = null;
	// try {
	// BasicHttpParams httpParams = new BasicHttpParams();
	// HttpConnectionParams.setConnectionTimeout(httpParams, 60 * 1000);
	// HttpConnectionParams.setSoTimeout(httpParams, 120 * 1000);
	// HttpClient client = new DefaultHttpClient(httpParams);
	// HttpGet get = new HttpGet(url);
	// HttpResponse response;
	// response = client.execute(get);
	// HttpEntity entity = response.getEntity();
	// long length = entity.getContentLength();
	// is = entity.getContent();
	// if (is != null) {
	// if (file.exists()) {
	// file.delete();
	// }
	// file.createNewFile();
	// fileOutputStream = new FileOutputStream(file);
	// byte[] buf = new byte[1024];
	// int ch = -1;
	// Long nReceiveLength = 0l;
	// while ((ch = is.read(buf)) != -1) {
	// fileOutputStream.write(buf, 0, ch);
	// if (length > 0) {
	// //
	// if (mh != null) {
	//
	// int nPer = (int) (nReceiveLength * 100l / length);
	// nReceiveLength += ch;
	// int nNextPer = (int) (nReceiveLength * 100l / length);
	// if (nPer != nNextPer) {
	// Message msg = mh.obtainMessage(1, nNextPer);
	// mh.sendMessage(msg);
	// }
	// }
	// }
	// }
	// }
	//
	// if (length != 0l) {
	// res = true;
	// } else {
	// file.delete();
	// res = false;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (is != null)
	// try {
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// if (fileOutputStream != null)
	// try {
	// fileOutputStream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return res;
	// }

	// public static String getCurrentTime() {
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH：mm：ss");
	// Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
	// String time = formatter.format(curDate);
	// System.out.println("当前时间" + time);
	// return time;
	// }

	public static String getCurrTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH：mm：ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		System.out.println("当前时间" + time);
		return time;
	}

}
