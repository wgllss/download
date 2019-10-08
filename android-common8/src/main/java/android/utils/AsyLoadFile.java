package android.utils;
//package android.common;
//
//import java.io.File;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import android.content.Context;
//import android.download.DownLoadFileBean;
//import android.download.DownLoadFileTask;
//import android.os.AsyncTask;
//import android.os.Handler;
//
///**
// * 异步打开文件，主要用于异步下载
// */
//public class AsyLoadFile extends AsyncTask<Object, Object, Void> {
//	private LoadFinishListener mLoadFinish;
//	private boolean finished = false;
//
//	public AsyLoadFile(Context context) {
//	}
//
//	public AsyLoadFile() {
//	}
//
//	@Override
//	protected Void doInBackground(Object... params) {
//		try {
//			if (finished == true)
//				return null;
//
//			File fileObject = null;
//			final Object tag = params[0];
//			String url = (String) params[1];
//			String strCacheDir = (String) params[2];
//			String strName = (String) params[3];
//			Handler handler = (Handler) params[4];
//			if (strName == null || strName.equals(""))
//				return null;
//			String strFileName = strName;
//			String strFileLocalPath = strCacheDir + strFileName;
//			if (finished == true)
//				return null;
//			if (FileUtils.exists(strFileLocalPath)) {// 存在
//				fileObject = new File(strFileLocalPath);
//			} else {
//				// 下载
//				String strTempFileName = strName + ".suffix";
//				String strTempFilePath = strCacheDir + strTempFileName;
//
//				CountDownLatch latch = new CountDownLatch(1);
//				DownLoadFileBean downLoadBean = new DownLoadFileBean();
//				ExecutorService executor = null;
//				try {
//					executor = Executors.newCachedThreadPool();
//					downLoadBean.setFileSiteURL(url);// 设置远程文件地址
//					downLoadBean.setFileSaveName(strTempFileName);// 设置本地文件名
//					downLoadBean.setFileSavePath(strCacheDir);// 设置本地文件路径
//					downLoadBean.setFileThreadNum(1);// 单线程下载
//					downLoadBean.setIsRange(true);// 设置关闭断点续传
//					// downLoadBean.setHandler(handler);//下载通知消息
//					downLoadBean.setPauseDownloadFlag(false);// 下载终止标志位设为false
//					DownLoadFileTask zipT = new DownLoadFileTask(downLoadBean,
//							latch);
//
//					executor.execute(zipT);
//					latch.await();
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					executor.shutdown();
//					// 下载结束
//					if (finished == true)
//						return null;
//					if (downLoadBean.isDownSuccess()) {// 下载成功
//						File fl = new File(strTempFilePath);
//						fileObject = new File(strFileLocalPath);
//						fl.renameTo(fileObject);
//					} else {
//						// drawable = ImageUtils.getDrawableFromUrl(url);
//					}
//					downLoadBean = null;
//				}
//			}
//
//			if (mLoadFinish != null && finished == false) {
//				mLoadFinish.onLoadFinish(tag, fileObject, handler);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			// e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	protected void onCancelled() {
//		// TODO Auto-generated method stub
//		finished = true;
//		super.onCancelled();
//	}
//
//	public void setLoadFinishListener(LoadFinishListener ls) {
//		this.mLoadFinish = ls;
//	}
//
//	public static interface LoadFinishListener {
//		public void onLoadFinish(final Object tag, final File object,
//				Handler handler);
//	}
//
//}