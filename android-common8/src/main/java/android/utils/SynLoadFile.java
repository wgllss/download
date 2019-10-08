package android.utils;
//package android.common;
//
//
//import java.io.File;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import android.download.DownLoadFileBean;
//import android.download.DownLoadFileTask;
//
//
//
///**
// * 同步读取网络文件
// */
//public class SynLoadFile {
//	public File run(String url,String localDir,String fileName) {
//		try {
//			File fileObject= null;				
//			if(fileName==null||fileName.equals(""))
//				return null;			
//			String strFileLocalPath = localDir+fileName;			
//			if (FileUtils.exists(strFileLocalPath)) {// 存在
//				fileObject = new File(strFileLocalPath);
//			} else {
//				// 下载
//				String strTempFileName = fileName+ ".suffix";
//				String strTempFilePath = localDir+strTempFileName;
//				CountDownLatch latch = new CountDownLatch(1);
//				DownLoadFileBean downLoadBean = new DownLoadFileBean();
//				ExecutorService executor = null;
//				try {
//					executor = Executors.newCachedThreadPool();
//					downLoadBean.setFileSiteURL(url);// 设置远程文件地址
//					downLoadBean.setFileSaveName(strTempFileName);// 设置本地文件名
//					downLoadBean.setFileSavePath(localDir);// 设置本地文件路径
//					downLoadBean.setFileThreadNum(1);// 单线程下载
//					downLoadBean.setIsRange(false);//设置关闭断点续传
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
//					if (downLoadBean.isDownSuccess()) {// 下载成功
//						File fl = new File(strTempFilePath);
//						fileObject = new File(strFileLocalPath);
//						fl.renameTo(fileObject);						
//					}else{
//						//drawable = ImageUtils.getDrawableFromUrl(url);
//					}
//					downLoadBean = null;					
//				}
//			}
//			return fileObject;
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			// e.printStackTrace();
//		}
//		return null;
//	}
// }