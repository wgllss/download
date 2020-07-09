package android.appconfig.moudle;

import android.text.TextUtils;

/**
 * @author：atar
 * @date: 2019/10/12
 * @description:
 */
public class DownloadApkBean {
    private int progress;
    private String downloadName;
    private String loadName;
    private String downloadFileName;
    private String url;

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public String getLoadName() {
        return downloadName;
    }

    public String getUrl() {
        return url;
    }

    public String getDownloadFileName() {
        if (!TextUtils.isEmpty(url)) {
            return url.hashCode() + "";
        }
        return downloadFileName;
    }
}
