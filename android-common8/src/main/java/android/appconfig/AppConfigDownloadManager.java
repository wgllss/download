/**
 *
 */
package android.appconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.appconfig.moudle.ConfigJson;
import android.application.CrashHandler;
import android.download.DownLoadFileManager;
import android.http.HttpReadServerConnection;
import android.http.HttpRequest;
import android.http.UrlParamCommon;
import android.interfaces.HandlerListener;
import android.interfaces.NetWorkCallListener;
import android.os.Message;
import android.reflection.ThreadPoolTool;
import android.utils.ApplicationManagement;
import android.utils.ShowLog;

/**
 * ****************************************************************************************************************************************************************************
 * app 配置下载管理器，
 *
 * @author :Atar
 * @createTime:2017-9-22上午10:08:39
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:如下载 配置动态皮肤，配置动态文件 根据版本号下载,并且根据配置最低多少版本才下载 等
 * ****************************************************************************************************************************************************************************
 */
public class AppConfigDownloadManager {

    private static String TAG = AppConfigDownloadManager.class.getSimpleName();

    private static AppConfigDownloadManager instance;
    private String defaultVersion = "1.0";

    public synchronized static AppConfigDownloadManager getInstance() {
        if (instance == null) {
            instance = new AppConfigDownloadManager();
            try {
                instance.defaultVersion = ApplicationManagement.getVersionName();
            } catch (Exception e) {
                instance.defaultVersion = "1.0";
            }
        }
        return instance;
    }

    /**
     * 配置文件按照版本下载
     *
     * @param activity
     * @param handlerListener
     * @param newVersion          配置文件版本
     * @param replaceMinVersion   配置文件在 >= 多少版本 开始下载替换
     * @param which
     * @param fileUrl
     * @param fileThreadNum
     * @param deleteOnExit
     * @param strDownloadFileName
     * @param strDownloadDir
     * @author :Atar
     * @createTime:2017-9-22下午2:36:45
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void downLoadAppConfigFile(Activity activity, final HandlerListener handlerListener, final String newVersion, String replaceMinVersion, final int which, final String fileUrl,
                                      final int fileThreadNum, final boolean deleteOnExit, final String strDownloadFileName, final String strDownloadDir) {
        try {
            if (newVersion == null || replaceMinVersion == null || newVersion.length() == 0 || replaceMinVersion.length() == 0) {
                return;
            }
            File downloadFile = new File(strDownloadDir + strDownloadFileName);
            if (downloadFile.exists()) {// 存在下载文件
                if (!"0.0000000".equals(AppConfigModel.getInstance().getString(fileUrl, "0.0000000"))) {
                    //该链接下载过
                    if (newVersion.compareToIgnoreCase(AppConfigModel.getInstance().getString(fileUrl, defaultVersion)) > 0
                            && AppConfigModel.getInstance().getString(fileUrl, defaultVersion).compareToIgnoreCase(replaceMinVersion) >= 0) {
                        // 配置新版本比本地版本大 同时 允许替换的版本比本地版本大

                    } else {
                        ShowLog.i(TAG, fileUrl + ":不下载替换");
                        return;
                    }
                }
            }
            DownLoadFileManager.getInstance().downLoad(activity, new HandlerListener() {
                @Override
                public void onHandlerData(Message msg) {
                    switch (msg.what) {
                        case android.download.DownLoadFileBean.DOWLOAD_FLAG_FAIL:
                            ShowLog.i(TAG, fileUrl + ":下载失败");
                            break;
                        case android.download.DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                            AppConfigModel.getInstance().putString(fileUrl, newVersion, true);
                            ShowLog.i(TAG, fileUrl + ":下载成功");
                            break;
                    }
                    if (handlerListener != null) {
                        handlerListener.onHandlerData(msg);
                    }
                }
            }, which, fileUrl, fileThreadNum, deleteOnExit, strDownloadFileName, strDownloadDir);
        } catch (Exception e) {
            ShowLog.e(TAG, CrashHandler.crashToString(e));
        }
    }

    /**
     * 读取服务端配置son
     *
     * @param FileUrl
     * @param what                 回调接口标识
     * @param mNetWorkCallListener 回调
     * @author :Atar
     * @createTime:2017-9-22下午3:08:01
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void getServerJson(final String FileUrl, int what, int showToast, NetWorkCallListener mNetWorkCallListener, Activity activity) {
        Object[] params = null;
        if (activity == null) {
            params = new Object[]{FileUrl, UrlParamCommon.UTF_8};
        } else {
            params = new Object[]{FileUrl, UrlParamCommon.UTF_8, activity};
        }
        ThreadPoolTool.getInstance().setAsyncTask(what, 0, 0, showToast, mNetWorkCallListener, activity, HttpReadServerConnection.class.getName(), HttpReadServerConnection.HttpReadServerFile, params, ConfigJson.class);
    }

    /**
     * 读取服务端配置son
     *
     * @param FileUrl
     * @param mHttpCallBackResult
     * @author :Atar
     * @createTime:2017-9-22下午3:08:01
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void getServerJson(final String FileUrl, final HttpCallBackResult mHttpCallBackResult) {
        ThreadPoolTool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                String result = "";
                HttpURLConnection httpConnection = null;
                try {
                    URL url = new URL(FileUrl);
                    httpConnection = HttpRequest.getHttpURLConnection(url, 5000);
                    HttpRequest.setConHead(httpConnection);
                    httpConnection.connect();
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {
                        InputStream instream = httpConnection.getInputStream();
                        if (instream != null) {
                            InputStreamReader inputreader = new InputStreamReader(instream);
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
                } catch (Exception e) {
                    ShowLog.w(TAG, CrashHandler.crashToString(e));
                } finally {
                    if (httpConnection != null)
                        httpConnection.disconnect();// 关闭连接
                }
                if (mHttpCallBackResult != null) {
                    mHttpCallBackResult.onResult(result);
                }
            }
        });
    }

    /**
     * 读取服务端json成功回调
     *
     * @author :Atar
     * @createTime:2017-9-22下午3:05:54
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public interface HttpCallBackResult {
        /**
         * 返回数据
         *
         * @param result
         * @author :Atar
         * @createTime:2017-9-22下午3:06:30
         * @version:1.0.0
         * @modifyTime:
         * @modifyAuthor:
         * @description:
         */
        void onResult(String result);
    }
}
