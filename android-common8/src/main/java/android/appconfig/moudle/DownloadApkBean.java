package android.appconfig.moudle;

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
        return loadName;
    }

    public String getUrl() {
        return url;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }
}
