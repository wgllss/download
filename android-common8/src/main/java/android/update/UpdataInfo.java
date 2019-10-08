package android.update;

public class UpdataInfo {
	private String version;// 当前版本号
	private String url;// 更新apk下载地址
	private String description;// 更新说明
	private String versionmin;// 最低版本号 低于它需要强制更新

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersionmin() {
		return versionmin;
	}

	public void setVersionmin(String versionmin) {
		this.versionmin = versionmin;
	}
}