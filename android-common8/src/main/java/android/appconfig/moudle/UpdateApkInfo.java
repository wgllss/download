package android.appconfig.moudle;

/**
 * @author：atar
 * @date: 2019/9/27
 * @description:
 */
public class UpdateApkInfo {
    private String version;//apk 最新版本
    private String url;//下载apk的url
    private String description;//更新说明
    private String versionMin;//小于该版本号强制升级

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getVersionMin() {
        return versionMin;
    }
}
