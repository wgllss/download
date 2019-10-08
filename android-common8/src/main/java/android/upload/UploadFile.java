package android.upload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.Map;

import android.appconfig.AppConfigSetting;
import android.common.CommonHandler;
import android.http.HttpRequest;
import android.interfaces.HandlerListener;
import android.utils.ShowLog;

/**
 * 
 *****************************************************************************************************************************************************************************
 *同步上传专属类
 * @author :Atar
 * @createTime:2014-8-12下午3:19:58
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class UploadFile {
	public final static String TAG = UploadFile.class.getSimpleName();

	/** 上传失败 */
	public final static int UPLOAD_FLAG_FAIL = 98765860;
	/** 上传成功 */
	public final static int UPLOAD_FLAG_SUCCESS = 98765861;
	/** 正在上传 */
	public final static int UPLOAD_FLAG_ING = 98765862;
	/** 上传错误 */
	public final static int UPLOAD_FLAG_ABORT = 98765863;
	/** 上传到web完毕，具体web返回。。。 */
	public final static int UPLOAD_WEB_CALLBACK = 98765864;
	/** 上传异常 */
	public final static int UPLOAD_SERIVER_ERROR = 98765865;
	/** 上传联网超时 */
	public final static int UPLOAD_CONNNECT_TIME_OUT = 98765866;
	/** 上传HTTP协议出错 */
	public final static int UPLOAD_HTTP_ERROR = 98765867;
	/** 通信编码错误 */
	public final static int UPLOAD_ENCODING_ERROR = 98765868;
	/** 上传文件木有找到 */
	public final static int UPLOAD_FILE_NOT_FIND = 98765869;
	/**上传文件有错误 */
	public final static int UPLOAD_FILE_ERROR = 987658610;
	/** 上传文件最大20M */
	public final static int UPLOAD_FILE_MAX_20 = 987658611;
	/** 上传文件返回code不为200 */
	public final static int UPLOAD_FILE_RESPONSECODE = 987658612;

	/**
	 * 多文件按队列上传文件 主方法（可带参数）
	 * @author :Atar
	 * @createTime:2014-8-12下午5:41:31
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param strServerPath 上传到服务器路径
	 * @param params 参数
	 * @param files 文件Map
	 * @param mHandler 操作上传状态
	 * @param whichUpload 上传的哪一个 //用于异步多线程上传回传进度值 对号入座
	 * @description:
	 */
	@SuppressWarnings("unused")
	public static void upLoad(String strServerPath, Map<String, String> params, Map<String, File> files, HandlerListener mHandlerListener, int whichUpload) {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		try {
			ShowLog.i(TAG, "strServerPath--->" + strServerPath);
			URL uri = new URL(strServerPath);
			HttpURLConnection httpURLConnection = (HttpURLConnection) uri.openConnection();

			httpURLConnection.setConnectTimeout(HttpRequest.connectTimeOut);// 10秒钟连接超时
			httpURLConnection.setReadTimeout(HttpRequest.connectTimeOut); // 缓存的最长时间
			httpURLConnection.setDoInput(true);// 允许输入
			httpURLConnection.setDoOutput(true);// 允许输出
			httpURLConnection.setUseCaches(false); // 不允许使用缓存
			httpURLConnection.setInstanceFollowRedirects(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "keep-alive");
			httpURLConnection.setRequestProperty("Charsert", CHARSET);
			httpURLConnection.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
			// setCookie();
			if (!"".equals(AppConfigSetting.getInstance().getToken())) {
				httpURLConnection.addRequestProperty("token", AppConfigSetting.getInstance().getToken());
				ShowLog.i(TAG, "请求token------->" + AppConfigSetting.getInstance().getToken());
			}

			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
				// sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			DataOutputStream outStream = new DataOutputStream(httpURLConnection.getOutputStream());
			outStream.write(sb.toString().getBytes());
			InputStream in = null;
			// 发送文件数据
			if (files != null && files.size() > 0) {
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
					sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());
					try {
						Thread.sleep(500);// 由于目前上传接口中支持多次上传单张图片 防止多次导致服务器500 强行增加时间间隔
					} catch (InterruptedException e) {
						CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_FLAG_FAIL, 0, whichUpload, e.getMessage());
					}
					InputStream is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					int m = 0;
					int nFileLength = is.available();
					ShowLog.d(TAG, "开始上传");
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						m += len;
						int percent = (int) m * 100 / nFileLength; // 上传进度值
						if (percent >= 1) {
							percent = 100;
							CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_FLAG_ING, percent, whichUpload, null);
						}
					}
					is.close();
					outStream.write(LINEND.getBytes());
				}
				ShowLog.i(TAG, "is.close()");
				// 请求结束标志
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
				try {
					Thread.sleep(400);// 由于目前上传接口中支持多次上传单张图片 防止多次导致服务器500 强行增加时间间隔
				} catch (InterruptedException e) {
					CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_FLAG_FAIL, 0, whichUpload, e.getMessage());
				}
				outStream.write(end_data);
				outStream.flush();
				// 得到响应码
				int resCode = httpURLConnection.getResponseCode();
				ShowLog.i(TAG, "resCode---->" + resCode);
				if (resCode == HttpURLConnection.HTTP_OK) {
					InputStream mInputStream = null;
					mInputStream = httpURLConnection.getInputStream();
					InputStreamReader isr = null;
					isr = new InputStreamReader(mInputStream, CHARSET);
					BufferedReader br = new BufferedReader(isr);
					String result = br.readLine();
					ShowLog.i(TAG, "result:" + result);
					CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_WEB_CALLBACK, 0, whichUpload, result);
					// saveCookie();
				} else {
					CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_FILE_RESPONSECODE, resCode, whichUpload, null);
				}
				outStream.close();
				httpURLConnection.disconnect();
			}
		} catch (SocketTimeoutException e) {// 联网超时
			CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_CONNNECT_TIME_OUT, 0, whichUpload, e.getMessage());
		} catch (MalformedURLException e) {// 网络协议错误
			CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_HTTP_ERROR, 0, whichUpload, e.getMessage());
		} catch (UnknownServiceException e) {// 上传的服务端出错
			CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_SERIVER_ERROR, 0, whichUpload, e.getMessage());
		} catch (UnsupportedEncodingException e) {// 通信编码错误
			CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_ENCODING_ERROR, 0, whichUpload, e.getMessage());
		} catch (FileNotFoundException e) {// 上传文件木有找到
			CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_FILE_NOT_FIND, 0, whichUpload, e.getMessage());
		} catch (IOException e) {// 上传失败
			CommonHandler.getInstatnce().handerMessage(mHandlerListener, UPLOAD_FLAG_FAIL, 0, whichUpload, e.getMessage());
		} finally {

		}
	}

	// private static void setCookie(HttpURLConnection httpURLConnection) {
	/* 请求前带上cookie */
	// if (UrlParamCommon.JSESSIONID != null && UrlParamCommon.JSESSIONID.length() > 0) {
	// httpURLConnection.setRequestProperty("Cookie", CookieTool.getCookieStr("JSESSIONID"));
	// ShowLog.i(TAG, "请求JSESSIONID---->" + CookieTool.getCookieStr("JSESSIONID"));
	// }
	// if (!"".equals(AppConfigSetting.getInstance().getUserId())) {
	// httpURLConnection.addRequestProperty("Cookie", CookieTool.getCookieStr("tgbuser"));
	// ShowLog.i(TAG, "请求tgbuser---->" + CookieTool.getCookieStr("tgbuser"));
	// if (!"".equals(AppConfigSetting.getInstance().getPassword())) {
	// httpURLConnection.addRequestProperty("Cookie", CookieTool.getCookieStr("tgbpwd"));
	// ShowLog.i(TAG, "请求tgbpwd---->" + CookieTool.getCookieStr("tgbpwd"));
	// }
	// }
	/* 请求前带上cookie */
	// }

	// private static void saveCookie(HttpURLConnection httpURLConnection) {
	/* 请求回来保存cookie */
	// // List<Cookie> cookies = ((DefaultHttpClient) httpURLConnection).getCookieStore().getCookies();
	// String cookieskey = "Set-Cookie";
	// Map<String, List<String>> maps = httpURLConnection.getHeaderFields();
	// List<Cookie> cookies = httpURLConnection.getHeaderFields().getCookieStore().getCookies();
	// List<String> coolist = maps.get(cookieskey);
	// String userId = "";
	// String userPassWord = "";
	// String sessionId = "";
	// for (int i = 0; i < cookies.size(); i++) {
	// String cookieName = cookies.get(i).getName();
	// if ("JSESSIONID".equals(cookieName)) {
	// sessionId = cookies.get(i).getValue();
	// AppConfigSetting.getInstance().saveSesstion(sessionId);
	// ShowLog.d(TAG, "得到jsessionID--" + cookies.get(i).getValue());
	// }
	// if ("tgbuser".equals(cookieName)) {
	// userId = cookies.get(i).getValue();
	// ShowLog.d(TAG, "得到tgbuser--" + cookies.get(i).getValue());
	// }
	// if ("tgbpwd".equals(cookieName)) {
	// userPassWord = cookies.get(i).getValue();
	// ShowLog.d(TAG, "得到tgbpwd--" + cookies.get(i).getValue());
	// }
	// }
	// if (!"".equals(userId)) {
	// AppConfigSetting.getInstance().saveLoginUserId(userId);
	// if (!"".equals(userPassWord)) {
	// AppConfigSetting.getInstance().saveLoginPassword(userPassWord);
	// }
	// }
	/* 请求回来保存cookie */
	// }
}
