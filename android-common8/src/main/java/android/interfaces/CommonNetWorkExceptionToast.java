/**
 * 
 */
package android.interfaces;

import java.util.Date;

import android.application.CommonApplication;
import android.application.CrashHandler;
import android.content.Context;
import android.http.HttpRequest;
import android.reflection.ErrorMsgEnum;
import android.reflection.NetWorkMsg;
import android.utils.ShowLog;
import android.widget.CommonToast;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :Atar
 * @createTime:2017-1-19上午10:34:09
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class CommonNetWorkExceptionToast {
	private static String TAG = CommonNetWorkExceptionToast.class.getSimpleName();
	// 提示时间间隔4秒钟
	private int toastPeriod = 4;
	private static Date lastDate = null;
	private static String toastError[];
	private static boolean isShowError;

	/**
	 * 是否显示错误异常提示
	 * @author :Atar
	 * @createTime:2016-5-13下午4:34:36
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param isShowError
	 * @description:
	 */
	public static void setIsShowErrorToast(boolean isShowError0) {
		isShowError = isShowError0;
	}

	/**
	 *setToastError: 从数组里获取String型 错误提示信息
	 *@atour: Atar
	 *@createTime:2014-5-20上午12:29:53
	 *@modifyTime:
	 *@modifyAtour:
	 *@version: 1.0.0
	 *@param id: array资源数组id
	 *@description: 
	 */
	public static void initToastError(Context context, int id) {
		toastError = context.getResources().getStringArray(id);
	}

	public void NetWorkCall(NetWorkMsg msg, NetWorkCallListener mNetWorkCallListener) {
		try {
			if (mNetWorkCallListener != null) {
				mNetWorkCallListener.NetWorkCall(msg);
			}
			switch (msg.what) {
			case ErrorMsgEnum.EHttpIO_Msg:
				String remark = (String) msg.obj;
				if (remark != null && remark.length() > 0) {
					if (HttpRequest.IsUsableNetWork(CommonApplication.getContext())) {
						toast(msg.arg3, toastError[0]);
						ShowLog.i(TAG, toastError[0]);
					} else {
						if (!isInToastPeriod()) {
							CommonToast.show(toastError[1]);
						}
						ShowLog.i(TAG, toastError[1]);
					}
				} else {
					toast(msg.arg3, toastError[2]);
					ShowLog.i(TAG, toastError[2]);
				}
				break;
			case ErrorMsgEnum.ENotFoundMethods_Msg:
				toast(msg.arg3, toastError[3]);
				ShowLog.i(TAG, toastError[3]);
				break;
			case ErrorMsgEnum.EHttpProtocol_Msg:
				toast(msg.arg3, toastError[4]);
				ShowLog.i(TAG, toastError[4]);
				break;
			case ErrorMsgEnum.EXmlParser_Msg:
				toast(msg.arg3, toastError[5]);
				ShowLog.i(TAG, toastError[5]);
				break;
			case ErrorMsgEnum.EXmlIO_Msg:
				toast(msg.arg3, toastError[6]);
				ShowLog.i(TAG, toastError[6]);
				break;
			case ErrorMsgEnum.EClassNotFound_Msg:
				toast(msg.arg3, toastError[7]);
				ShowLog.i(TAG, toastError[7]);
				break;
			case ErrorMsgEnum.EIllegalAccess_Msg:
				toast(msg.arg3, toastError[8]);
				ShowLog.i(TAG, toastError[8]);
				break;
			case ErrorMsgEnum.ESecurity_Msg:
				toast(msg.arg3, toastError[9]);
				ShowLog.i(TAG, toastError[9]);
				break;
			case ErrorMsgEnum.EMobileNetUseless_Msg:
				if (!isInToastPeriod()) {
					CommonToast.show(toastError[1]);
				}
				ShowLog.i(TAG, toastError[1]);
				break;
			case ErrorMsgEnum.EConnectTimeout_Msg:
				toast(msg.arg3, toastError[10]);
				ShowLog.i(TAG, toastError[10]);
				break;
			case ErrorMsgEnum.EParamHasNull_Msg:
				toast(msg.arg3, toastError[11]);
				ShowLog.i(TAG, toastError[11]);
				break;
			case ErrorMsgEnum.EParamUnInvalid_Msg:
				toast(msg.arg3, toastError[12]);
				ShowLog.i(TAG, toastError[12]);
				break;
			case ErrorMsgEnum.EJsonParser_Msg:
				toast(msg.arg3, toastError[13]);
				ShowLog.i(TAG, toastError[13]);
				break;
			case ErrorMsgEnum.ENotDefine_Msg:
				toast(msg.arg3, toastError[14]);
				ShowLog.i(TAG, toastError[14]);
				break;
			case ErrorMsgEnum.EHttpRequestFail400:
				toast(msg.arg3, toastError[15]);
				ShowLog.i(TAG, toastError[15]);
				break;
			case ErrorMsgEnum.EHttpRequestFail403:
				toast(msg.arg3, toastError[16]);
				ShowLog.i(TAG, toastError[16]);
				break;
			case ErrorMsgEnum.EHttpRequestFail404:
				toast(msg.arg3, toastError[17]);
				ShowLog.i(TAG, toastError[17]);
				break;
			case ErrorMsgEnum.EHttpRequestFail502:
				toast(msg.arg3, toastError[18]);
				ShowLog.i(TAG, toastError[18]);
				break;
			case ErrorMsgEnum.EHttpRequestFail503:
				toast(msg.arg3, toastError[19]);
				ShowLog.i(TAG, toastError[19]);
				break;
			case ErrorMsgEnum.EHttpRequestFail504:
				toast(msg.arg3, toastError[20]);
				ShowLog.i(TAG, toastError[20]);
				break;
			case ErrorMsgEnum.EUnknownHost_msg:
				toast(msg.arg3, toastError[21]);
				ShowLog.i(TAG, toastError[21]);
				break;
			case ErrorMsgEnum.EUnknownService_msg:
				toast(msg.arg3, toastError[21]);
				ShowLog.i(TAG, toastError[22]);
				break;
			case ErrorMsgEnum.EUnsupportedEncoding_msg:
				toast(msg.arg3, toastError[21]);
				ShowLog.i(TAG, toastError[23]);
				break;
			case ErrorMsgEnum.EHttpRequestFail500:
				toast(msg.arg3, toastError[24]);
				ShowLog.i(TAG, toastError[24]);
				break;
			case ErrorMsgEnum.EHttpRequestFail405:
				toast(msg.arg3, toastError[25]);
				ShowLog.i(TAG, toastError[25]);
				break;
			}
		} catch (ClassCastException e) {
			toast(msg.arg3, toastError[13]);
			ShowLog.i(TAG, toastError[13]);
			ShowLog.e(TAG, "handleMessage--e--->" + CrashHandler.crashToString(e));
		} catch (Exception e) {
			ShowLog.e(TAG, "handleMessage--e--->" + CrashHandler.crashToString(e));
		}
	}

	/**
	 * toast提示内容
	 * @author :Atar
	 * @createTime:2014-12-12下午4:37:56
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param msg1
	 * @param strToastContent
	 * @description:
	 */
	public void toast(int arg3, String strToastContent) {
		if (isShowError) {
			if (arg3 == ErrorMsgEnum.NetWorkMsgWhithToast && !isInToastPeriod()) {
				CommonToast.show(strToastContent);
			}
		}
	}

	/**
	 * 是否在提示时间间隔之内
	 * @author :Atar
	 * @createTime:2014-11-12上午11:26:54
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param startDate
	 * @param endDate
	 * @return
	 * @description:
	 */
	@SuppressWarnings("unused")
	public boolean isInToastPeriod() {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		if (curDate != null) {
			long lastTime = 0;
			if (lastDate == null) {
				lastTime = 0;
			} else {
				lastTime = lastDate.getTime();
			}
			long timeLong = curDate.getTime() - lastTime;
			if (timeLong < toastPeriod * 1000) {
				return true;
			} else {
				lastDate = curDate;
				return false;
			}
		} else {
			return false;
		}
	}
}
