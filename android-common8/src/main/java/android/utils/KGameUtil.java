/**
 * 
 */
package android.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.appconfig.AppConfigModel;
import android.application.CrashHandler;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/**
 ***************************************************************************************************************************************************************************** 
 * K线游戏 计算工具
 * 
 * @author :Atar
 * @createTime:2016-1-6下午4:18:50
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 ***************************************************************************************************************************************************************************** 
 */
public class KGameUtil {
	private static String TAG = KGameUtil.class.getSimpleName();

	/**
	 * 计算每三位加一个逗号，保留两位小数
	 * 
	 * @author :Atar
	 * @createTime:2016-1-19下午2:54:40
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param data
	 * @return
	 * @description:
	 */
	public static String formatTosepara(double data) {
		try {
			if (data == 0 || data == 0.00) {
				return "0.00";
			}
			DecimalFormat df = new DecimalFormat("#,###");
			if (Math.abs(data) >= 10000000) {
				data = data / 10000;
				if (Math.abs(data) >= 1000000) {
					data = data / 10000;
					df = new DecimalFormat("#,###.##");
					if (Math.abs(data) >= 1000000) {
						data = data / 10000;
						if (Math.abs(data) >= 1000000) {
							data = data / 10000;
							if (Math.abs(data) >= 1000000) {
								DecimalFormat formater = new DecimalFormat("#.##E0");
								if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
									formater.setMaximumFractionDigits(2);
									formater.setMinimumFractionDigits(2);
								}
								return formater.format(data * 10E15);
							}
							return df.format(data) + "亿亿";
						}
						return df.format(data) + "万亿";
					}
					return df.format(data) + " 亿";
				}
				return df.format(data) + " 万";
			}
			return df.format(data);
		} catch (Exception e) {
			ShowLog.i(TAG, CrashHandler.crashToString(e));
			return "0.00";
		}
	}

	/**
	 * 百分比 到倍数到万倍 到可科学计数法倍
	 * 
	 * @author :Atar
	 * @createTime:2016-5-24下午2:12:49
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param data
	 * @return
	 * @description:
	 */
	public static String dataformatRate(double data) {
		try {
			if (data == 0 || data == 0.00) {
				return "0.00%";
			}
			DecimalFormat df = new DecimalFormat("#,###.##");
			if (Math.abs(data) >= 10000) {
				data = data / 10000;
				if (Math.abs(data) >= 10000) {
					data = data / 10000;
					if (Math.abs(data) >= 10000) {
						DecimalFormat formater = new DecimalFormat("#.##E0");
						if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
							formater.setMaximumFractionDigits(2);
							formater.setMinimumFractionDigits(2);
						}
						return formater.format(data * 10E7) + "倍";
					} else {
						if (Math.abs(data) >= 100) {
							df = new DecimalFormat("#,###.#");
							return df.format(data) + "亿倍";
						} else {
							return df.format(data) + "亿倍";
						}
					}
				} else {
					if (Math.abs(data) >= 100) {
						df = new DecimalFormat("#,###.#");
						return df.format(data) + "万倍";
					} else {
						return df.format(data) + "万倍";
					}
				}
			} else {
				return df.format(100 * data) + "%";
			}
		} catch (Exception e) {
			ShowLog.i(TAG, CrashHandler.crashToString(e));
			return "0.00%";
		}
	}

	/**
	 * 截取取两位小数
	 * 
	 * @author :Atar
	 * @createTime:2016-5-19下午3:49:50
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param d
	 * @return
	 * @description:
	 */
	@SuppressLint("NewApi")
	public static String dataformat(double d) {
		DecimalFormat formater = new DecimalFormat();
		if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
			formater.setMaximumFractionDigits(2);
			formater.setMinimumFractionDigits(2);
		}
		formater.setGroupingSize(0);
		formater.setRoundingMode(RoundingMode.DOWN);
		return formater.format(d);
	}

	/**
	 * 得到加密map
	 * 
	 * @author :Atar
	 * @createTime:2016-3-28上午11:38:41
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param parames
	 * @return
	 * @description:
	 */
	public static Map<String, String> getSignMap(Map<String, String> parames) {
		try {
			parames.put("version", AppConfigModel.getInstance().getString(AppConfigModel.VERSION_KEY, "5.27"));
			if (parames != null && parames.size() > 0) {
				String privateKey = "7enca3a8zrxcjt5j0l08b9tj2dart8pw";
				// Map<String, String> mapSign = new HashMap<String, String>();
				// for (Map.Entry entry : parames.entrySet()) {
				// String valueStr = "";
				// String value = (String) entry.getValue();
				// for (int i = 0; i < value.length(); i++) {
				// valueStr = (i == value.length() - 1) ? valueStr +
				// value.substring(i, i + 1) : valueStr + value.substring(i, i +
				// 1) + ",";
				// }
				// mapSign.put(((String) entry.getKey()), valueStr);
				// }
				List<String> keys = new ArrayList<String>(parames.keySet());
				Collections.sort(keys);
				String content = "";
				for (int i = 0; i < keys.size(); i++) {
					String key = (String) keys.get(i);
					String value = parames.get(key);
					content += (i == 0 ? "" : "&") + key + "=" + value;
				}
				String signBefore = content + privateKey;
				parames.put("sign", MDPassword.getPassword32(signBefore));
				parames.put("sign_type", "MD5");
			}
			return parames;
		} catch (Exception e) {
			return parames;
		}
	}

	/**
	 * 计算每三位加一个逗号，大于1万1亿 汉字单位1位固定小数， 小于1万的自定义格式： accuracy 小于10000的格式 小数位数（0、1、2位） 和 format 同步一起自定义
	 * 
	 * @author :Atar
	 * @createTime:2016-1-19下午2:54:40
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param data
	 * @param format 小于10000的格式
	 * @param accuracy 小于10000的格式 小数位数（0、1、2位） 和 format 同步一起自定义
	 * @return
	 * @description:
	 */
	@SuppressLint("NewApi")
	public static String formatShipan(double data, int accuracy, String format) {
		try {
			if (Math.abs(data) >= 10000) {
				DecimalFormat df = new DecimalFormat("#,###.#");
				if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
					df.setMaximumFractionDigits(1);
					df.setMinimumFractionDigits(1);
				}
				df.setGroupingSize(0);
				df.setRoundingMode(RoundingMode.DOWN);
				data = data / 10000;
				if (Math.abs(data) >= 10000) {
					data = data / 10000;
					return df.format(data) + "亿";
				}
				return df.format(data) + "万";
			}
			DecimalFormat df = new DecimalFormat(format);
			if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
				df.setMaximumFractionDigits(accuracy);
				df.setMinimumFractionDigits(accuracy);
			}
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.DOWN);
			return df.format(data);
		} catch (Exception e) {
			ShowLog.i(TAG, CrashHandler.crashToString(e));
			return data + "";
		}
	}

	/**
	 * double 不保留小数方法
	 * @author :Atar
	 * @createTime:2017-6-7上午9:39:59
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param data
	 * @return
	 * @description:
	 */
	@SuppressLint("NewApi")
	public static String getFormateDouble(double data) {
		try {
			DecimalFormat df = new DecimalFormat("#,###");
			if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
				df.setMaximumFractionDigits(0);
				df.setMinimumFractionDigits(0);
			}
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.DOWN);
			return df.format(data);
		} catch (Exception e) {
			return data + "";
		}
	}
}
