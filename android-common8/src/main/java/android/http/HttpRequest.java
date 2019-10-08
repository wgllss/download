package android.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.reflection.ExceptionEnum.HttpRequestFalse400;
import android.reflection.ExceptionEnum.HttpRequestFalse403;
import android.reflection.ExceptionEnum.HttpRequestFalse404;
import android.reflection.ExceptionEnum.HttpRequestFalse405;
import android.reflection.ExceptionEnum.HttpRequestFalse500;
import android.reflection.ExceptionEnum.HttpRequestFalse502;
import android.reflection.ExceptionEnum.HttpRequestFalse503;
import android.reflection.ExceptionEnum.HttpRequestFalse504;
import android.reflection.ExceptionEnum.RefelectException;

/**
 * 
 ******************************************************************************************
 * Http网络请求
 * @author: Atar 
 * @createTime:2014年5月27日下午7:56:48
 * @modifyTime:
 * @version: 1.0.0
 * @description:
 ******************************************************************************************
 */
public class HttpRequest {
	public static int connectTimeOut = 30000; // 连接超时时间毫秒

	// @SuppressWarnings("rawtypes")
	// public static String getRequest(String url, Map<String, String> parames, String inputEncoding) {
	// String response = "";
	// String httpurl = url;
	// HttpGet request = null;
	// try {
	// // 组装URL
	// if (parames != null && parames.size() > 0) {
	// int index = httpurl.indexOf("?");
	// if (index == -1) {
	// httpurl += "?";
	// }
	//
	// for (Map.Entry entry : parames.entrySet()) {
	// httpurl += (String) entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), inputEncoding) + "&";
	// }
	// httpurl = httpurl.substring(0, httpurl.length() - 1);
	// Log.d("URL", "请求URL------>:" + httpurl);
	// }
	//
	// // 创建 HttpParams 设置 HTTP 参数
	// HttpParams httpParams = new BasicHttpParams();
	// // 设置连接超时和 Socket 超时，以及 Socket 缓存大小
	// HttpConnectionParams.setConnectionTimeout(httpParams, connectTimeOut);
	// HttpConnectionParams.setSoTimeout(httpParams, connectTimeOut);
	// HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
	//
	// // 创建一个默认的HttpClient
	// HttpClient httpclient = new DefaultHttpClient(httpParams);
	// // 创建一个GET请求
	// request = new HttpGet(httpurl);
	// // 向服务器发送请求并获取服务器返回的结果
	// HttpResponse httpresponse = httpclient.execute(request);
	// int statusCode = httpresponse.getStatusLine().getStatusCode();
	// if (statusCode == 200) {
	// response = EntityUtils.toString(httpresponse.getEntity(), inputEncoding);
	// }
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// String err = HttpProtocolException.class.getSimpleName();
	// throw (RefelectException) new HttpProtocolException(err, e.getCause());
	//
	// } catch (ConnectTimeoutException e) {
	// e.printStackTrace();
	// String err = ReflectionTimeOutException.class.getSimpleName();
	// throw (RefelectException) new ReflectionTimeOutException(err, e.getCause());
	// } catch (IOException e) {
	// e.printStackTrace();
	// String err = HttpIOException.class.getSimpleName();
	// throw (RefelectException) new HttpIOException(err, e.getCause());
	// } finally {
	// request.abort();
	// }
	//
	// return response;
	// }
	//
	// public static String postData(String reqUrl, Map<String, ?> parames, String recvEncoding) {
	// String result = "";
	// // 创建 HttpParams 设置 HTTP 参数
	// HttpParams httpParams = new BasicHttpParams();
	// // 设置连接超时和 Socket 超时，以及 Socket 缓存大小
	// HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000);
	// HttpConnectionParams.setSoTimeout(httpParams, 40 * 1000);
	// HttpConnectionParams.setSocketBufferSize(httpParams, 2 * 8192);
	//
	// HttpClient client = new DefaultHttpClient(httpParams);
	// HttpPost httpPost = new HttpPost(reqUrl);
	// try {
	// if (parames != null && parames.size() > 0) {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// Set<String> keys = parames.keySet();
	// for (String key : keys) {
	// params.add(new BasicNameValuePair(key, (String) parames.get(key)));
	// }
	// HttpEntity httpentity = new UrlEncodedFormEntity(params, recvEncoding);
	// httpPost.setEntity(httpentity);
	// }
	// HttpResponse httpResponse = client.execute(httpPost);
	// int statusCode = httpResponse.getStatusLine().getStatusCode();
	// if (statusCode == 200) {
	// result = retrieveInputStream(httpResponse.getEntity(), recvEncoding);
	// }
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// String err = HttpProtocolException.class.getSimpleName();
	// throw (RefelectException) new HttpProtocolException(err, e.getCause());
	// } catch (ConnectTimeoutException e) {
	// e.printStackTrace();
	// String err = ReflectionTimeOutException.class.getSimpleName();
	// throw (RefelectException) new ReflectionTimeOutException(err, e.getCause());
	// } catch (IOException e) {
	// e.printStackTrace();
	// String err = HttpIOException.class.getSimpleName();
	// throw (RefelectException) new HttpIOException(err, e.getCause());
	// } finally {
	// httpPost.abort();
	// }
	// return result;
	// }
	//
	// /**
	// * 处理httpResponse信息,返回String
	// *
	// * @param httpEntity
	// * @return String
	// * @throws UnsupportedEncodingException
	// */
	// public static String retrieveInputStream(HttpEntity httpEntity, String recvEncoding) throws UnsupportedEncodingException {
	// int length = (int) httpEntity.getContentLength();
	// if (length < 0) {
	// length = 10000;
	// }
	// StringBuffer stringBuffer = new StringBuffer(length);
	// try {
	// InputStreamReader inputStreamReader = new InputStreamReader(httpEntity.getContent(), recvEncoding);
	// char buffer[] = new char[length];
	// int count;
	// while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {
	// stringBuffer.append(buffer, 0, count);
	// }
	// } catch (UnsupportedEncodingException e) {
	// } catch (IllegalStateException e) {
	// } catch (IOException e) {
	// }
	// return new String(stringBuffer.toString());
	// }

	@SuppressLint("InlinedApi")
	public static boolean IsUsableNetWork(Context myContext) {
		boolean netSataus = false;
		if (myContext != null) {
			try {
				ConnectivityManager conMan = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (conMan != null) {
					if (conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != null) {
						State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
						// 移动网络
						if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
							netSataus = true;
						}
					}
					if (conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != null) {
						State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
						// wifi连接
						if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
							netSataus = true;
						}
					}
					if (conMan.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH) != null && conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != null) {
						State blueTooth = conMan.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH).getState();
						// 蓝牙连接
						if (blueTooth == State.CONNECTED || blueTooth == State.CONNECTING) {
							netSataus = true;
						}
					}
				}
			} catch (Exception e) {
				netSataus = false;
			}
		}
		return netSataus;
	}

	/**
	 * 根据code抛出异常
	 * @author :Atar
	 * @createTime:2014-12-10上午10:31:18
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param statusCode
	 * @description:
	 */
	public static void throwExceptionByCode(int statusCode) {
		// ShowLog.i(TAG, "statusCode---->" + statusCode);
		if (statusCode == 400) {
			String err = HttpRequestFalse400.class.getSimpleName();
			throw (RefelectException) new HttpRequestFalse400(err, new Throwable());
		} else if (statusCode == 403) {
			String err = HttpRequestFalse403.class.getSimpleName();
			throw (RefelectException) new HttpRequestFalse403(err, new Throwable());
		} else if (statusCode == 404) {
			String err = HttpRequestFalse404.class.getSimpleName();
			throw (RefelectException) new HttpRequestFalse404(err, new Throwable());
		} else if (statusCode == 405) {
			String err = HttpRequestFalse405.class.getSimpleName();
			throw (RefelectException) new HttpRequestFalse405(err, new Throwable());
		} else if (statusCode == 502) {
			String err = HttpRequestFalse502.class.getSimpleName();
			throw (RefelectException) new HttpRequestFalse502(err, new Throwable());
		} else if (statusCode == 503) {
			String err = HttpRequestFalse503.class.getSimpleName();
			throw (RefelectException) new HttpRequestFalse503(err, new Throwable());
		} else if (statusCode == 504) {
			String err = HttpRequestFalse504.class.getSimpleName();
			throw (RefelectException) new HttpRequestFalse504(err, new Throwable());
		} else if (statusCode == 500) {
			String err = HttpRequestFalse500.class.getSimpleName();
			throw (RefelectException) new HttpRequestFalse500(err, new Throwable());
		}
	}

	// /**
	// * 判断当前网络状态，未连接网络则跳至网络设置页面
	// * @author :Atar
	// * @createTime:2014-12-30下午9:58:26
	// * @version:1.0.0
	// * @modifyTime:
	// * @modifyAuthor:
	// * @param myContext
	// * @param myActivity
	// * @return
	// * @description:
	// */
	// public static boolean NetWorkStatus(final Context myContext, Activity myActivity) {
	// boolean netSataus = false;
	// if (!IsUsableNetWork(myContext)) {
	// Builder b = new AlertDialog.Builder(myActivity).setTitle("没有可用的网络").setMessage("是否对网络进行设置？");
	// b.setPositiveButton("是", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// Intent mIntent = new Intent("/");
	// ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
	// mIntent.setComponent(comp);
	// mIntent.setAction("android.intent.action.VIEW");
	// mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// myContext.startActivity(mIntent);
	// }
	// }).setNeutralButton("否", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// dialog.cancel();
	// }
	// }).show();
	// }
	// return netSataus;
	// }

	@SuppressWarnings("static-access")
	public static HttpURLConnection getHttpURLConnection(URL url, int connectTimeOut) {
		try {
			if ("https".equals(url.getProtocol())) {
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				https.setDefaultHostnameVerifier(hnv);
				https.setHostnameVerifier(hnv);
				https.setDefaultSSLSocketFactory(mSSLSocketFactory);
				https.setSSLSocketFactory(mSSLSocketFactory);
				https.setConnectTimeout(3 * connectTimeOut);
				https.setReadTimeout(3 * connectTimeOut);
				return https;
			} else {
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setConnectTimeout(connectTimeOut);
				httpURLConnection.setReadTimeout(connectTimeOut);
				return httpURLConnection;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 信任所有host
	 */
	public static HostnameVerifier hnv = new HostnameVerifier() {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * 设置https 
	 * @author :Atar
	 * @createTime:2015-9-17下午4:57:39
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	@SuppressLint("TrulyRandom")
	public static void trustAllHosts() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[] {};
				}

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			} };
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			if (mSSLSocketFactory == null) {
				mSSLSocketFactory = sc.getSocketFactory();
			}
			HttpsURLConnection.setDefaultHostnameVerifier(hnv);
			HttpsURLConnection.setDefaultSSLSocketFactory(mSSLSocketFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SSLSocketFactory mSSLSocketFactory;

	/**
	 * 设置请求头
	 * 
	 * @param httpConnection
	 */
	public static void setConHead(HttpURLConnection httpConnection) {
		httpConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
		httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36");
		httpConnection.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		httpConnection.setRequestProperty("Accept-Encoding", "aa");
		httpConnection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		httpConnection.setRequestProperty("Keep-Alive", "300");
		httpConnection.setRequestProperty("Connection", "keep-alive");
		httpConnection.setRequestProperty("Cache-Control", "max-age=0");
	}
}
