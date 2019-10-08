package android.http;

import android.app.Activity;
import android.reflection.ExceptionEnum;
import android.utils.ShowLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownServiceException;

/**
 * @author：atar
 * @date: 2019/10/8
 * @description:
 */
public class HttpReadServerConnection {

    private static String TAG = HttpReadServerConnection.class.getSimpleName();

    public static final String HttpReadServerFile = "HttpReadServerFile";

    //读取服务端配置
    public static String HttpReadServerFile(String httpurl, String inputEncoding, Activity activity) {
        SoftReference<Activity> mSoftReference = null;
        if (activity != null) {
            mSoftReference = new SoftReference<Activity>(activity);
        }
        String result = "";
        int statusCode = 0;
        HttpURLConnection httpConnection = null;
        try {
            URL url = new URL(httpurl);
            httpConnection = HttpRequest.getHttpURLConnection(url, 5000);
            HttpRequest.setConHead(httpConnection);
            httpConnection.connect();
            statusCode = httpConnection.getResponseCode();
            if (mSoftReference != null && mSoftReference.get() != null && mSoftReference.get().isFinishing()) {
                String err = ExceptionEnum.ReflectionActivityFinished.class.getSimpleName();
                throw (ExceptionEnum.RefelectException) new ExceptionEnum.ReflectionActivityFinished(err, null);
            }
            if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_PARTIAL) {
                InputStream instream = httpConnection.getInputStream();
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream, inputEncoding);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    // 分行读取
                    while ((line = buffreader.readLine()) != null) {
                        result += line;
                    }
                    inputreader.close();
                    instream.close();
                    buffreader.close();
                }
            }
        } catch (SocketTimeoutException e) {// 联网超时
            e.printStackTrace();
            String err = ExceptionEnum.ReflectionTimeOutException.class.getSimpleName();
            throw (ExceptionEnum.RefelectException) new ExceptionEnum.ReflectionTimeOutException(err, e.getCause());
        } catch (MalformedURLException e) {// 网络协议错误
            String err = ExceptionEnum.HttpProtocolException.class.getSimpleName();
            throw (ExceptionEnum.RefelectException) new ExceptionEnum.HttpProtocolException(err, e.getCause());
        } catch (UnknownServiceException e) {// 服务端出错
            String err = ExceptionEnum.ReflectionUnknownServiceException.class.getSimpleName();
            throw (ExceptionEnum.RefelectException) new ExceptionEnum.ReflectionUnknownServiceException(err, e.getCause());
        } catch (UnsupportedEncodingException e) {// 通信编码错误
            String err = ExceptionEnum.ReflectionUnsupportedEncodingException.class.getSimpleName();
            throw (ExceptionEnum.RefelectException) new ExceptionEnum.ReflectionUnsupportedEncodingException(err, e.getCause());
        } catch (IOException e) {
            String err = ExceptionEnum.HttpIOException.class.getSimpleName();
            throw (ExceptionEnum.RefelectException) new ExceptionEnum.HttpIOException(err, e.getCause());
        } finally {
            HttpRequest.throwExceptionByCode(statusCode);
        }
        ShowLog.e(TAG, "result-->" + result);
        return result;
    }

    //读取服务端配置
    public static String HttpReadServerFile(String httpurl, String inputEncoding) {
        return HttpReadServerFile(httpurl, inputEncoding, null);
    }
}
