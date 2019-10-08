package android.appconfig.moudle;

/**
 * @author：atar
 * @date: 2019/9/27
 * @description:
 */
public class ConfigJson {

    private String selfVersion;   //服务端配置文件版本
    private String replacaeMinApkVersion;//服务端配置文件 小于等于该版本替换本配置json数据
    private String testSn;  // 如：188FBC210622,S201D89F70115      //逗号隔开配置该字段则为指定某台设备生效灰度测试，不配置该字段为全部设备生效

    //更改以下配置数据 需要 升级 selfVersion版本
    private UpdateApkInfo updateApkInfo;//升级apk信息

    public String getSelfVersion() {
        return selfVersion;
    }

    public String getReplacaeMinApkVersion() {
        return replacaeMinApkVersion;
    }

    public UpdateApkInfo getUpdateApkInfo() {
        return updateApkInfo;
    }

    public String getTestSn() {
        return testSn;
    }
}
