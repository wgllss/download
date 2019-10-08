package android.update;

import android.appconfig.moudle.UpdateApkInfo;
import android.common.CommonHandler;
import android.interfaces.HandlerListener;
import android.os.Environment;


/**
 * @author：atar
 * @date: 2019/9/27
 * @description:
 */
public class UpdateApkTools {

    /**
     * SD卡目录 下载
     */
    public static String strDownloadDir = Environment.getExternalStorageDirectory() + "/.cache/.apk/";
    public final static String fileName = "saas_pos_zhuizhi_apk";
    /**
     * 没有新的版本
     */
    public final static int UPDATA_NO_NEW_VERSION = 0;
    /**
     * 有新的版本
     */
    public final static int UPDATA_IS_NEW_VERSION = 1;
    /**
     * 检测版本超时
     */
    public final static int GET_UPDATA_INFO_TIME_OUT = 2;

//    public static UpdateApkTools newInstance() {
//        return new UpdateApkTools();
//    }

    //
    public static void update(UpdateApkInfo updateApkInfo, String localVersion, HandlerListener handlerListener) {
        if (updateApkInfo != null) {
            if (updateApkInfo.getVersion().compareToIgnoreCase(localVersion) > 0) {
                CommonHandler.getInstatnce().handerMessage(handlerListener, UPDATA_IS_NEW_VERSION, 0, 0, updateApkInfo);
            } else {
                CommonHandler.getInstatnce().handerMessage(handlerListener, UPDATA_NO_NEW_VERSION, 0, 0, null);
            }
        }
    }
}
