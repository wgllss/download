/**
 * 
 */
package android.interfaces;

import android.os.Message;

/**
 *****************************************************************************************************************************************************************************
 * 监听 全局 CommonHandler 回收消息
 * @author :Atar
 * @createTime:2017-8-11下午2:01:38
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public interface HandlerListener {
	/**
	 * CommonHandler 回收消息
	 * @author :Atar
	 * @createTime:2017-8-11下午2:03:10
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param msg
	 * @description:
	 */
	void onHandlerData(Message msg);
}
