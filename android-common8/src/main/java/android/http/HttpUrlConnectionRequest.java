/**
 *
 */
package android.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownServiceException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.reflection.ExceptionEnum.HttpIOException;
import android.reflection.ExceptionEnum.HttpProtocolException;
import android.reflection.ExceptionEnum.RefelectException;
import android.reflection.ExceptionEnum.ReflectionActivityFinished;
import android.reflection.ExceptionEnum.ReflectionTimeOutException;
import android.reflection.ExceptionEnum.ReflectionUnknownServiceException;
import android.reflection.ExceptionEnum.ReflectionUnsupportedEncodingException;
import android.utils.ShowLog;

/**
 * ****************************************************************************************************************************************************************************
 *
 * @author :Atar
 * @createTime:2015-9-14下午2:53:53
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: ****************************************************************************************************************************************************************************
 */
@SuppressLint("TrulyRandom")
public class HttpUrlConnectionRequest {

    private static String TAG = HttpUrlConnectionRequest.class.getSimpleName();
    public static final String POST = "postData";
    public static final String GET = "getResult";

    @SuppressWarnings({"static-access", "rawtypes"})
    public static String getResult(String httpurl, Map<String, String> parames, String inputEncoding, Activity activity) {
        // if (UrlParamCommon.isHasMD5 && httpurl.contains("")) {
        // parames = KGameUtil.getSignMap(parames);
        // }
        String requestUrl = httpurl;
        String result = "";
        int statusCode = 0;
        SoftReference<Activity> mSoftReference = null;
        if (activity != null) {
            mSoftReference = new SoftReference<Activity>(activity);
        }

        try {
            // 组装URL
            if (parames != null) {
                int index = httpurl.indexOf("?");
                if (index == -1) {
                    httpurl += "?";
                }
                if (parames.size() > 0) {
                    for (Map.Entry entry : parames.entrySet()) {
                        httpurl += (String) entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), inputEncoding) + "&";
                    }
                    httpurl = httpurl.substring(0, httpurl.length() - 1);
                }
                // /* 获取新浪个股时,url 不加？ */
                if (httpurl.contains("list=") && httpurl.contains("sinajs")) {
                    httpurl = requestUrl;
                }
                ShowLog.i(TAG, "HttpUrlConnectionGet-->请求URL------>:" + httpurl);
            }
            URL url = new URL(httpurl);
            HttpURLConnection conn = null;
            if ("https".equals(url.getProtocol())) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setDefaultHostnameVerifier(hnv);
                https.setHostnameVerifier(hnv);
                https.setDefaultSSLSocketFactory(mSSLSocketFactory);
                https.setSSLSocketFactory(mSSLSocketFactory);
                https.setConnectTimeout(3 * HttpRequest.connectTimeOut);
                https.setReadTimeout(3 * HttpRequest.connectTimeOut);
                conn = https;
            } else {
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(HttpRequest.connectTimeOut);
                conn.setReadTimeout(HttpRequest.connectTimeOut);
            }
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // if (httpurl.contains("gw.wmcloud.com") && AppConfigModel.getInstance().getString(HttpRequest.TLTOKIEN, "") != null
            // && AppConfigModel.getInstance().getString(HttpRequest.TLTOKIEN, "").length() > 0) {
            // conn.addRequestProperty("Authorization", AppConfigModel.getInstance().getString(HttpRequest.TLTOKIEN, ""));
            // ShowLog.i(TAG, "TLTOKIEN--->" + AppConfigModel.getInstance().getString(HttpRequest.TLTOKIEN, ""));
            // }
            // if (httpurl.contains("")) {
            // setCookie(httpurl, conn);
            // }
            conn.connect();
            statusCode = conn.getResponseCode();
            if (mSoftReference != null && mSoftReference.get() != null && mSoftReference.get().isFinishing()) {
                String err = ReflectionActivityFinished.class.getSimpleName();
                throw (RefelectException) new ReflectionActivityFinished(err, null);
            }
            BufferedReader br = null;
            InputStreamReader isr = null;
            if (statusCode == 200) {
                isr = new InputStreamReader(conn.getInputStream(), inputEncoding);
                br = new BufferedReader(isr);
                String tmp = null;
                while ((tmp = br.readLine()) != null) {
                    result += tmp;
                }
                // saveCookie(conn);
            } else {
                ShowLog.i(TAG, "HttpUrlConnectionGet---url----->" + httpurl + "---接口" + statusCode);
            }
            br.close();
            isr.close();
            conn.disconnect();
        } catch (SocketTimeoutException e) {// 联网超时
            e.printStackTrace();
            String err = ReflectionTimeOutException.class.getSimpleName();
            throw (RefelectException) new ReflectionTimeOutException(err, e.getCause());
        } catch (MalformedURLException e) {// 网络协议错误
            String err = HttpProtocolException.class.getSimpleName();
            throw (RefelectException) new HttpProtocolException(err, e.getCause());
        } catch (UnknownServiceException e) {// 服务端出错
            String err = ReflectionUnknownServiceException.class.getSimpleName();
            throw (RefelectException) new ReflectionUnknownServiceException(err, e.getCause());
        } catch (UnsupportedEncodingException e) {// 通信编码错误
            String err = ReflectionUnsupportedEncodingException.class.getSimpleName();
            throw (RefelectException) new ReflectionUnsupportedEncodingException(err, e.getCause());
        } catch (IOException e) {
            String err = HttpIOException.class.getSimpleName();
            throw (RefelectException) new HttpIOException(err, e.getCause());
        } finally {
            HttpRequest.throwExceptionByCode(statusCode);
        }
        return result;
    }

    @SuppressWarnings({"rawtypes", "static-access"})
    public static String postData(String httpurl, Map<String, String> parames, String inputEncoding, Activity activity) {
        // if (UrlParamCommon.isHasMD5 && httpurl.contains("")) {
        // parames = KGameUtil.getSignMap(parames);
        // }
        SoftReference<Activity> mSoftReference = null;
        if (activity != null) {
            mSoftReference = new SoftReference<Activity>(activity);
        }
        String result = "";
        int statusCode = 0;
        try {
            int index = httpurl.indexOf("?");
            if (index == -1) {
                httpurl += "?";
            }
            if (ShowLog.mDebug) {
                String requestUrl = httpurl;
                for (Map.Entry entry : parames.entrySet()) {
                    requestUrl += (String) entry.getKey() + "=" + entry.getValue() + "&";
                }
                ShowLog.i(TAG, "HttpUrlConnectionPost----url----->" + requestUrl + "---");
            }
            URL url = new URL(httpurl);
            HttpURLConnection conn = null;
            if ("https".equals(url.getProtocol())) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setDefaultHostnameVerifier(hnv);
                https.setHostnameVerifier(hnv);
                https.setDefaultSSLSocketFactory(mSSLSocketFactory);
                https.setSSLSocketFactory(mSSLSocketFactory);
                https.setConnectTimeout(3 * HttpRequest.connectTimeOut);
                https.setReadTimeout(3 * HttpRequest.connectTimeOut);
                conn = https;
            } else {
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(HttpRequest.connectTimeOut);
                conn.setReadTimeout(HttpRequest.connectTimeOut);
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", inputEncoding);

            // if (AppConfigModel.getInstance().getString(HttpRequest.TLTOKIEN, "") != null && AppConfigModel.getInstance().getString(HttpRequest.TLTOKIEN, "").length() > 0) {
            // conn.addRequestProperty("Authorization", AppConfigModel.getInstance().getString(HttpRequest.TLTOKIEN, ""));
            // }
            // if (httpurl.contains("")) {
            // setCookie(httpurl, conn);
            // }
            conn.connect();
            DataOutputStream dos = null;
            dos = new DataOutputStream(conn.getOutputStream());
            String paramContent = "";
            for (Map.Entry entry : parames.entrySet()) {
                paramContent += (String) entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), inputEncoding) + "&";
            }
            if (paramContent.length() > 0) {
                paramContent = paramContent.substring(0, paramContent.length() - 1);
            }
            dos.writeBytes(paramContent);
            dos.flush();
            statusCode = conn.getResponseCode();
            if (mSoftReference != null && mSoftReference.get() != null && mSoftReference.get().isFinishing()) {
                String err = ReflectionActivityFinished.class.getSimpleName();
                throw (RefelectException) new ReflectionActivityFinished(err, null);
            }
            BufferedReader br = null;
            InputStreamReader isr = null;
            if (statusCode == 200) {
                isr = new InputStreamReader(conn.getInputStream());
                br = new BufferedReader(isr);
                String tmp = null;
                while ((tmp = br.readLine()) != null) {
                    result += tmp;
                }
                // saveCookie(conn);
            } else {
                ShowLog.i(TAG, "HttpUrlConnectionPost----url----->" + httpurl + "---接口" + statusCode);
            }
            dos.close();
            br.close();
            isr.close();
            conn.disconnect();
        } catch (SocketTimeoutException e) {// 联网超时
            String err = ReflectionTimeOutException.class.getSimpleName();
            throw (RefelectException) new ReflectionTimeOutException(err, e.getCause());
        } catch (MalformedURLException e) {// 网络协议错误
            String err = HttpProtocolException.class.getSimpleName();
            throw (RefelectException) new HttpProtocolException(err, e.getCause());
        } catch (UnknownServiceException e) {// 服务端出错
            String err = ReflectionUnknownServiceException.class.getSimpleName();
            throw (RefelectException) new ReflectionUnknownServiceException(err, e.getCause());
        } catch (UnsupportedEncodingException e) {// 通信编码错误
            String err = ReflectionUnsupportedEncodingException.class.getSimpleName();
            throw (RefelectException) new ReflectionUnsupportedEncodingException(err, e.getCause());
        } catch (IOException e) {
            String err = HttpIOException.class.getSimpleName();
            throw (RefelectException) new HttpIOException(err, e.getCause());
        } finally {
            HttpRequest.throwExceptionByCode(statusCode);
        }
        return result;
    }

    public static String getResult(String httpurl, Map<String, String> parames, String inputEncoding) {
        return getResult(httpurl, parames, inputEncoding, null);
    }

    public static String postData(String httpurl, Map<String, String> parames, String inputEncoding) {
        return postData(httpurl, parames, inputEncoding, null);
    }

    // /**
    // * 设置Cookie
    // * @author :Atar
    // * @createTime:2015-9-17下午5:54:00
    // * @version:1.0.0
    // * @modifyTime:
    // * @modifyAuthor:
    // * @param httpurl
    // * @param conn
    // * @description:
    // */
    // public static void setCookie(String httpurl, HttpURLConnection conn) {
    // if (httpurl.contains("")) {
    // if (UrlParamCommon.JSESSIONID != null && UrlParamCommon.JSESSIONID.length() > 0) {
    // conn.addRequestProperty("Cookie", CookieTool.getCookieStr("JSESSIONID"));
    // ShowLog.i(TAG, "请求JSESSIONID------->" + UrlParamCommon.JSESSIONID);
    // }
    // if (!"".equals(AppConfigSetting.getInstance().getUserId())) {
    // conn.addRequestProperty("Cookie", CookieTool.getCookieStr("tgbuser"));
    // ShowLog.i(TAG, "请求tgbuser------->" + CookieTool.getCookieStr("tgbuser"));
    // if (!"".equals(AppConfigSetting.getInstance().getPassword())) {
    // conn.addRequestProperty("Cookie", CookieTool.getCookieStr("tgbpwd"));
    // ShowLog.i(TAG, "请求tgbpwd------->" + CookieTool.getCookieStr("tgbpwd"));
    // }
    // }
    // if (ImageCodeTask.Token != null && ImageCodeTask.Token.length() > 0) {
    // conn.addRequestProperty("token", ImageCodeTask.Token);
    // ImageCodeTask.Token = "";
    // ShowLog.i(TAG, "请求token---ImageCodeTask---->" + ImageCodeTask.Token);
    // }
    // if (!"".equals(AppConfigSetting.getInstance().getToken())) {
    // conn.addRequestProperty("token", AppConfigSetting.getInstance().getToken());
    // ShowLog.i(TAG, "请求token------->" + AppConfigSetting.getInstance().getToken());
    // }
    // }
    // }

    // /**
    // * 保存cookie
    // * @author :Atar
    // * @createTime:2015-9-16下午1:48:42
    // * @version:1.0.0
    // * @modifyTime:
    // * @modifyAuthor:
    // * @param conn
    // * @description:
    // */
    // public static void saveCookie(HttpURLConnection conn) {
    // if (conn != null) {
    //
    // String userId = "";
    // String userPassWord = "";
    // String sesionID = "";
    // for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
    // if (header.getKey() != null) {
    // if (header.getValue().size() >= 2) {
    // for (int i = 0; i < header.getValue().size(); i++) {
    // String cookieVal = header.getValue().get(i);
    // if (cookieVal != null && cookieVal.length() > 11 && cookieVal.contains("JSESSIONID=")) {
    // try {
    // UrlParamCommon.JSESSIONID = cookieVal.split(";")[0].substring(cookieVal.indexOf("=") + 1, cookieVal.split(";")[0].length());
    // ShowLog.i(TAG, "得到jsessionID---------->" + UrlParamCommon.JSESSIONID);
    // } catch (Exception e) {
    //
    // }
    // } else if (cookieVal != null && cookieVal.contains("tgbuser") && cookieVal.length() > 7) {
    // try {
    // userId = cookieVal.split(";")[0].substring(cookieVal.indexOf("=") + 1, cookieVal.split(";")[0].length());
    // ShowLog.i(TAG, "得到tgbuser---------->" + userId);
    // } catch (Exception e) {
    //
    // }
    // } else if (cookieVal != null && cookieVal.contains("tgbpwd") && cookieVal.length() > 7) {
    // try {
    // userPassWord = cookieVal.split(";")[0].substring(cookieVal.indexOf("=") + 1, cookieVal.split(";")[0].length());
    // ShowLog.i(TAG, "得到tgbpwd---------->" + userPassWord);
    // } catch (Exception e) {
    //
    // }
    // }
    // }
    // } else if (header.getValue().size() > 0 && header.getValue().size() < 2) {
    // for (int i = 0; i < header.getValue().size(); i++) {
    // String cookieVal = header.getValue().get(i);
    // if (cookieVal != null && cookieVal.length() > 11 && cookieVal.contains("JSESSIONID=")) {
    // try {
    // sesionID = cookieVal.split(";")[0].substring(cookieVal.indexOf("=") + 1, cookieVal.split(";")[0].length());
    // ShowLog.i(TAG, "得到jsessionID---------->" + UrlParamCommon.JSESSIONID);
    // } catch (Exception e) {
    //
    // }
    // }
    // }
    // }
    // }
    // }
    // String cookieVal = conn.getHeaderField("Set-Cookie");
    // if (cookieVal != null && cookieVal.length() > 11 && cookieVal.contains("JSESSIONID=")) {
    // try {
    // sesionID = cookieVal.split(";")[0].substring(cookieVal.indexOf("=") + 1, cookieVal.split(";")[0].length());
    // ShowLog.i(TAG, "得到jsessionID---------->" + UrlParamCommon.JSESSIONID);
    // } catch (Exception e) {
    //
    // }
    // }
    // if (!"".equals(sesionID)) {
    // UrlParamCommon.JSESSIONID = sesionID;
    // }
    // if (!"".equals(userId)) {
    // AppConfigSetting.getInstance().saveLoginUserId(userId);
    // if (!"".equals(userPassWord)) {
    // AppConfigSetting.getInstance().saveLoginPassword(userPassWord);
    // }
    // }
    // }
    // }

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
     *
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
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            }};
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
}
