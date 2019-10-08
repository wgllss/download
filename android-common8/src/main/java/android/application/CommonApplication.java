package android.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.utils.AuthImageDownloader;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class CommonApplication {
	protected static final boolean DEVELOPER_MODE = false;
	protected static Application mInstance;

	public static Context getContext() {
		return mInstance;
	}

	public static void initApplication(Application mApplication) {
		CommonApplication.mInstance = mApplication;
	}

	@SuppressWarnings("unused")
	public static void initImageLoader(Context context) {
		if (DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {// 初始化加载图片所需内容
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove for release app
				.imageDownloader(new AuthImageDownloader(context)).build();
		ImageLoader.getInstance().init(config);
	}
}
