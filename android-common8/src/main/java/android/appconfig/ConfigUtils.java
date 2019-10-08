package android.appconfig;

import android.app.Activity;
import android.appconfig.moudle.ConfigJson;
import android.common.CommonHandler;
import android.content.Context;
import android.interfaces.HandlerListener;
import android.os.Build;
import android.reflection.ThreadPoolTool;
import android.text.TextUtils;
import android.update.UpdateApkTools;
import android.utils.ApplicationManagement;
import android.utils.GsonUtils;
import android.utils.ShowLog;


/**
 * @author：atar
 * @date: 2019/9/27
 * @description:
 */
public class ConfigUtils {

    private static String TAG = ConfigUtils.class.getSimpleName();

    private static ConfigUtils instance;
    private String defaultApkVersion = "1.0.00";

    public static synchronized ConfigUtils getInstance() {
        if (instance == null) {
            synchronized (ConfigUtils.class) {
                if (instance == null) {
                    instance = new ConfigUtils();
                }
            }
            instance = new ConfigUtils();
        }
        return instance;
    }

    public void init(Context context, String defaultApkVersion) {
        this.defaultApkVersion = defaultApkVersion;
        AppConfigModel.getInstance().putString(Constans.LOCAL_APK_VERSION_KEY, ApplicationManagement.getVersionName(), true);
    }

    //设置config 数据 进行验证
    public void setConfigResult(final Activity activity, final ConfigJson configJson, final HandlerListener handlerListener) {
        ThreadPoolTool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (configJson == null) {
                        return;
                    }
                    if (!TextUtils.isEmpty(configJson.getTestSn()) && !configJson.getTestSn().contains(Build.SERIAL)) {
                        if (activity != null && !activity.isFinishing()) {
                            CommonHandler.getInstatnce().handerMessage(handlerListener, UpdateApkTools.UPDATA_NO_NEW_VERSION, 0, 0, null);
                        }
                        ShowLog.e(TAG, "灰度测试设备SN号为：" + configJson.getTestSn());
                        return;
                    }
                    String selfVersion = "";
                    String localVersion = "";
                    String replacaeMinSelfVersion = "";
                    if (!TextUtils.isEmpty(AppConfigModel.getInstance().getString(Constans.LOCAL_APK_VERSION_KEY, ""))) {
                        localVersion = AppConfigModel.getInstance().getString(Constans.LOCAL_APK_VERSION_KEY, defaultApkVersion);
                    } else {
                        try {
                            localVersion = ApplicationManagement.getVersionName();
                        } catch (Exception e) {
                            localVersion = defaultApkVersion;
                        }
                    }
                    selfVersion = configJson.getSelfVersion();
                    replacaeMinSelfVersion = configJson.getReplacaeMinApkVersion();

                    UpdateApkTools.update(configJson.getUpdateApkInfo(), localVersion, handlerListener);

                    if (!TextUtils.isEmpty(AppConfigModel.getInstance().getString(Constans.CONFIG_FILE_VERSION_KEY, ""))) {
                        //本地存在该文件的版本
                        if (selfVersion.compareToIgnoreCase(AppConfigModel.getInstance().getString(Constans.CONFIG_FILE_VERSION_KEY, "")) > 0) {
                            //服务器版本大于本地该文件的版本
                            AppConfigModel.getInstance().putString(Constans.CONFIG_FILE_VERSION_KEY, selfVersion, true);
                        } else {
                            ShowLog.e(TAG, "服务器版本小于等于本地该文件的版本");
                            return;
                        }
                    } else {
                        AppConfigModel.getInstance().putString(Constans.CONFIG_FILE_VERSION_KEY, selfVersion, true);
                    }

                    if (selfVersion.compareToIgnoreCase(localVersion) > 0) {
                        if (localVersion.compareToIgnoreCase(replacaeMinSelfVersion) >= 0) {
                            //小于等于该版本替换本配置json数据
                            AppConfigModel.getInstance().putString(Constans.SAVE_TO_SHARED_PREFERENCES_KEY, GsonUtils.beanToJson(configJson), true);
                        }
                    } else {
                        AppConfigModel.getInstance().putString(Constans.SAVE_TO_SHARED_PREFERENCES_KEY, GsonUtils.beanToJson(configJson), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
