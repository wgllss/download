package android.download;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.common.CommonHandler;
import android.interfaces.HandlerListener;
import android.os.Message;
import android.utils.FileUtils;
import android.utils.ShowLog;

public class DownLoadFile {
	private String TAG = DownLoadFile.class.getSimpleName();
	/** 默认超时时长 */
	public final static int DEFAULT_WAITTIME = 5000;

	/** 下载Bean */
	private DownLoadFileBean mDownLoadBean;
	/** 下载计数器,用于同步 */
	private volatile CountDownLatch mPauseLatch;

	/**
	 * 下载文件
	 * @author :Atar
	 * @createTime:2011-8-18下午1:59:19
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param Activity:弱引用持有UI Activity 在activity finishi时自动停止下载
	 * @param HandlerListener 下载回调监听
	 * @param whcih 哪一个下载，用于同时下载多个 which值必须不相同
	 * @param fileUrl 文件url
	 * @param strDownloadFileName 本地文件名
	 * @param strDownloadDir 本地文件目录
	 * @description:
	 */
	public void downLoad(final Activity activity, HandlerListener handlerListener, int which, String fileUrl, int fileThreadNum, String strDownloadFileName, String strDownloadDir) {
		if (strDownloadDir != null) {
			FileUtils.createDir(strDownloadDir);
		}
		CountDownLatch latch = new CountDownLatch(1);
		mDownLoadBean = new DownLoadFileBean();
		if (activity != null) {
			mDownLoadBean.setWeakReference(new WeakReference<Activity>(activity));// 设置 弱引用持有UI activity
		}
		mDownLoadBean.setFileSiteURL(fileUrl);// 设置远程文件地址
		mDownLoadBean.setFileSaveName(strDownloadFileName);// 设置本地文件名
		mDownLoadBean.setFileSavePath(strDownloadDir);// 设置本地文件路径
		mDownLoadBean.setFileThreadNum(fileThreadNum);// fileThreadNum 个线程下载
		mDownLoadBean.setHandlerListenerr(handlerListener);// 下载通知消息
		mDownLoadBean.setPauseDownloadFlag(false);// 下载终止标志位设为false
		mDownLoadBean.setWhich(which);// 设置哪一个下载 区别多个同时下载

		DownLoadFileTask zipT = new DownLoadFileTask(mDownLoadBean, latch);
		ExecutorService executor = null;
		try {
			executor = Executors.newSingleThreadExecutor();
			executor.execute(zipT);
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
			// 如果不是被调用者停止,则发送成功失败消息
			if (mPauseLatch == null) {
				Message msg = new Message();
				if (mDownLoadBean != null) {
					msg.arg2 = mDownLoadBean.getWhich();
					DownLoadFileManager.getInstance().remove(msg.arg2);
					if (mDownLoadBean.getWeakReference() != null && mDownLoadBean.getWeakReference().get() != null && mDownLoadBean.getWeakReference().get().isFinishing()) {
						ShowLog.i(TAG, "---activity已经关闭---异步线程执行到此结束-------->");
						mDownLoadBean = null;
						return;
					}

					msg.what = mDownLoadBean.isDownSuccess() ? DownLoadFileBean.DOWLOAD_FLAG_SUCCESS : DownLoadFileBean.DOWLOAD_FLAG_FAIL;
					CommonHandler.getInstatnce().handerMessage(mDownLoadBean.getHandlerListener(), msg);
					mDownLoadBean = null;
				}
			} else {
				mPauseLatch.countDown();
			}
		}
	}

	/**
	 *  暂停下载
	 * @author :Atar
	 * @createTime:2017-8-18下午3:26:01
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void pauseDownload() {
		if (mDownLoadBean != null) {
			mDownLoadBean.setPauseDownloadFlag(true);
			mPauseLatch = new CountDownLatch(1);
			try {
				mPauseLatch.await();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (mDownLoadBean.getWeakReference() != null && mDownLoadBean.getWeakReference().get() != null && mDownLoadBean.getWeakReference().get().isFinishing()) {
					DownLoadFileManager.getInstance().remove(mDownLoadBean.getWhich());
					ShowLog.i(TAG, "---activity已经关闭---异步线程执行到此结束-------->");
					mPauseLatch = null;
					mDownLoadBean = null;
					return;
				}

				// 发送停止下载消息
				Message msg = new Message();
				msg.arg2 = mDownLoadBean.getWhich();
				msg.what = DownLoadFileBean.DOWLOAD_FLAG_ABORT;
				CommonHandler.getInstatnce().handerMessage(mDownLoadBean.getHandlerListener(), msg);
				mPauseLatch = null;
				mDownLoadBean = null;
			}
		}
	}

	// /**
	// * 通过文件流下载文件保存到本地strFilePath下
	// *
	// * @param strUrl
	// * 远程地址
	// * @param strFileName
	// * 本地存储地址
	// * @return
	// */
	// public boolean downFileOnStream(String strUrl, String strFilePath) {
	// OutputStream os = null;
	// try {
	// if (strFilePath != null) {
	// String strDir = strFilePath.substring(0, strFilePath.lastIndexOf("/"));
	// FileUtils.createDir(strDir);
	// }
	//
	// // 重新编码所含中文
	// String strRight = strUrl.substring(strUrl.lastIndexOf("/") + 1, strUrl.length() - 1);
	// strUrl = strUrl.replaceAll(strRight, URLEncoder.encode(strRight, "utf-8"));
	// // handle replace "+"->\\%20
	// strUrl = strUrl.replaceAll("\\+", "%20");// 处理空格
	// URL url = new URL(strUrl);
	// URLConnection conn = url.openConnection();
	// conn.setConnectTimeout(1000);
	// conn.connect();
	// InputStream is = conn.getInputStream();
	// if (is == null)
	// throw new RuntimeException("stream is null");
	// // 把文件存到path
	// os = new FileOutputStream(strFilePath);
	// byte buf[] = new byte[1024];
	// for (int numread = is.read(buf), zero = 0; numread > 0;) {
	// os.write(buf, zero, numread);
	// numread = is.read(buf);
	// }
	// os.flush();
	// is.close();
	// os.close();
	//
	// } catch (Exception e) {
	// try {
	// if (os != null)
	// os.close();
	// } catch (Exception ex) {
	// return false;
	// }
	// return false;
	// }
	//
	// return true;
	// }
}
