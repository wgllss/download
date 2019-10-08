/**
 *
 */
package android.skin;

import android.app.Activity;
import android.appconfig.AppConfigDownloadManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.interfaces.HandlerListener;
import android.os.Environment;
import android.os.Message;
import android.reflection.ThreadPoolTool;
import android.utils.FileUtils;
import android.utils.MDPassword;
import android.utils.ShowLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * ****************************************************************************************************************************************************************************
 * 皮肤资源管理器
 *
 * @author :Atar
 * @createTime:2017-9-18上午10:33:21
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: ****************************************************************************************************************************************************************************
 */
public class SkinResourcesManager {
    private String TAG = SkinResourcesManager.class.getSimpleName();

    /**
     * 是否加载apk资源 false加载library下资源
     */
    public static boolean isLoadApkSkin = true;

    private String download_skin_Url = "";
    /**
     * 主工程包名
     */
    private String main_project_packname = "";
    /**
     * 皮肤工程包名
     */
    private String skin_project_packname = "";
    /**
     * assets根目录下资源文件 默认皮肤资源
     */
    private String DEFAULT_ASSETS_SKIN_NAME = "skin.so";
    /**
     * SD卡目录 下载 资源文件 皮肤资源
     */
    private String SD_PATH = Environment.getExternalStorageDirectory() + "/.Android/.cache/.";
    /**
     * sd下默认皮肤资源
     */
    private String DEFAULT_SD_SKIN_NAME = "default_skin";
    /**
     * sd下载皮肤资源
     */
    private String DOWNLOAD_SD_SKIN_NAME = "download_skin";

    private static SkinResourcesManager mInstance;
    private Context mContext;
    private Resources mResources;

    public static SkinResourcesManager getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new SkinResourcesManager();
            mInstance.mContext = mContext;
            mInstance.main_project_packname = mContext.getPackageName();
            // mInstance.SD_PATH = mContext.getCacheDir() + "/";
            mInstance.SD_PATH = mInstance.SD_PATH + MDPassword.getPassword32(mInstance.main_project_packname) + "/";
            //ShowLog.i(mInstance.TAG, " mInstance.SD_PATH -->" + mInstance.SD_PATH);
        }
        return mInstance;
    }

    /**
     * 初始化皮肤资源
     * @param isDownLoadApkSkin     是否加载apk资源皮肤
     * @param skin_project_packname 皮肤工程包名
     * @param download_skin_Url     下载皮肤url
     * @author :Atar
     * @createTime:2017-9-18下午5:19:23
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void initSkinResources(boolean isDownLoadApkSkin, String skin_project_packname, final String download_skin_Url) {
        isLoadApkSkin = isDownLoadApkSkin;
        this.skin_project_packname = skin_project_packname;
        if (isLoadApkSkin) {
            this.download_skin_Url = download_skin_Url;
            ThreadPoolTool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        File file = new File(SD_PATH);
                        if (!FileUtils.exists(SD_PATH)) {
                            FileUtils.createDir(SD_PATH);
                        }
                        final File downloadFile = new File(file.getAbsolutePath(), MDPassword.getPassword32(DOWNLOAD_SD_SKIN_NAME));
                        if (downloadFile.exists()) {// 存在下载皮肤文件
                            loadSkinResources(downloadFile.getAbsolutePath(), null);
                            return;
                        }
                        File defaultFile = new File(file.getAbsolutePath(), MDPassword.getPassword32(DEFAULT_SD_SKIN_NAME));
                        if (!defaultFile.exists()) {
                            copyfileFromAssetsToSD(mContext);
                        }
                        loadSkinResources(defaultFile.getAbsolutePath(), null);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    /**
     * 从assets中复制apk到sd中
     *
     * @param context
     * @return
     * @author :Atar
     * @createTime:2017-9-18上午10:01:43
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    private boolean copyfileFromAssetsToSD(Context context) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(DEFAULT_ASSETS_SKIN_NAME);
            File file = new File(SD_PATH + MDPassword.getPassword32(DEFAULT_SD_SKIN_NAME));
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i); // 写入到文件
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    /**
     * 同步 加载皮肤资源
     *
     * @param skinFilePath
     * @param callback
     * @author :Atar
     * @createTime:2017-9-18上午11:08:05
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    private void loadSkinResources(final String skinFilePath, final loadSkinCallBack callback) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, skinFilePath);
            Resources superRes = mContext.getResources();
            mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
            if (callback != null) {
                callback.loadSkinSuccess(mResources);
            }
            ShowLog.i(TAG, "皮肤加载成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步 加载皮肤资源
     *
     * @param callback
     * @author :Atar
     * @createTime:2017-9-18上午11:08:30
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void loadSkinResources(final loadSkinCallBack callback) {
        if (isLoadApkSkin) {
            ThreadPoolTool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    String newest_path = SD_PATH + MDPassword.getPassword32(DEFAULT_SD_SKIN_NAME);
                    File file = new File(SD_PATH);
                    if (!FileUtils.exists(SD_PATH)) {
                        FileUtils.createDir(SD_PATH);
                    }
                    File downloadFile = new File(file.getAbsolutePath(), MDPassword.getPassword32(DOWNLOAD_SD_SKIN_NAME));
                    if (downloadFile.exists()) {// 存在下载皮肤文件
                        newest_path = downloadFile.getAbsolutePath();
                    } else {
                        File defaultFile = new File(file.getAbsolutePath(), MDPassword.getPassword32(DEFAULT_SD_SKIN_NAME));
                        if (defaultFile.exists()) {
                            newest_path = defaultFile.getAbsolutePath();
                        } else {
                            return;
                        }
                    }
                    loadSkinResources(newest_path, callback);
                }
            });
        }
    }

    /**
     * 下载皮肤
     *
     * @param activity
     * @param newVersion      皮肤新版本号
     * @param replaceMinVersion 皮肤在多少版本以上( >= )下载替换
     * @author :Atar
     * @createTime:2017-9-22下午2:42:49
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void downLoadSkin(Activity activity, final String newVersion, String replaceMinVersion) {
        if (isLoadApkSkin) {
            AppConfigDownloadManager.getInstance().downLoadAppConfigFile(activity, handlerListener, newVersion, replaceMinVersion, 0, download_skin_Url, 0, true,
                    MDPassword.getPassword32(DOWNLOAD_SD_SKIN_NAME) + "0", SD_PATH);
        }
    }

    HandlerListener handlerListener = new HandlerListener() {
        @Override
        public void onHandlerData(Message msg) {
            switch (msg.what) {
                case android.download.DownLoadFileBean.DOWLOAD_FLAG_FAIL:
                    ShowLog.i(TAG, "皮肤下载失败");
                    break;
                case android.download.DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                    ShowLog.i(TAG, "皮肤下载成功");
                    final File oldDownloadFile = new File(SD_PATH + MDPassword.getPassword32(DOWNLOAD_SD_SKIN_NAME));
                    oldDownloadFile.deleteOnExit();
                    FileUtils.copyFile(SD_PATH + MDPassword.getPassword32(DOWNLOAD_SD_SKIN_NAME) + "0", SD_PATH + MDPassword.getPassword32(DOWNLOAD_SD_SKIN_NAME));
                    ShowLog.i(TAG, "皮肤文件替换成功");
                    break;
                case android.download.DownLoadFileBean.DOWLOAD_FLAG_ING:
                    ShowLog.i(TAG, "皮肤正在下载:" + msg.arg2 + "%");
                    break;
            }
        }
    };

    public Resources getResources() {
        if (isLoadApkSkin) {
            return mResources;
        } else {
            return mContext.getResources();
        }
    }

    public String getSkinPackName() {
        return isLoadApkSkin ? skin_project_packname : main_project_packname;
    }

    public interface loadSkinCallBack {
        void loadSkinSuccess(Resources mResources);
    }
}
