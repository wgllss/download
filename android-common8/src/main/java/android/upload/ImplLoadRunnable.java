package android.upload;

import java.io.File;
import java.util.Map;

import android.interfaces.HandlerListener;

/**
 * 
 *****************************************************************************************************************************************************************************
 * 上传异步 Runnable
 * @author :Atar
 * @createTime:2017-8-16下午2:03:54
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class ImplLoadRunnable implements Runnable {

	private String strServerPath;
	private Map<String, String> params;
	private Map<String, File> files;
	private HandlerListener mHandlerListener;
	private int whichThread;

	public ImplLoadRunnable(String strServerPath, Map<String, String> params, Map<String, File> files, HandlerListener mHandlerListener, int whichThread) {
		this.strServerPath = strServerPath;
		this.params = params;
		this.files = files;
		this.mHandlerListener = mHandlerListener;
		this.whichThread = whichThread;
	}

	@Override
	public void run() {
		UploadFile.upLoad(strServerPath, params, files, mHandlerListener, whichThread);
	}

}
