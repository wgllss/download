/**
 * 
 */
package android.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;

/**
 ******************************************************************************************
 * TimeFormatUtil:
 * @author: Atar 
 * @createTime:2014年7月20日下午2:57:16
 * @modifyTime:
 * @version: 1.0.0
 * @description:
 ******************************************************************************************
 */
@SuppressLint("SimpleDateFormat")
public class TimeFormatUtil {

	/**
	 * 如此<--1404377575000--->时间转换为时间格式
	 * @author :Atar
	 * @createTime:2014-7-8下午1:47:09
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param time
	 * @return
	 * @description:
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime(String time) {
		if (time == null || time.length() == 0 || (time != null && "null".equals(time))) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mTime = time.toString();
		mTime = formatter.format(Long.valueOf(mTime));
		return mTime;
	}

	/**
	 * 得到时间不做几分钟之前处理
	 * @author :Atar
	 * @createTime:2015-7-9上午11:03:12
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param time
	 * @return
	 * @description:
	 */
	public static String getStringTimeFromLong(long time) {
		String strTime = "- -";
		try {
			if (time > 0) {
				SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
				strTime = formatter.format(Long.valueOf(time));
			}
		} catch (Exception e) {

		}
		return strTime;
	}

	public static String getStringTimeFromLongTimeFormat(long time, String timeFormat) {
		String strTime = "- -";
		try {
			if (time > 0) {
				if (!isSameYear(System.currentTimeMillis(), time)) {
					timeFormat = "yy-MM-dd HH:mm";
				}
				SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
				strTime = formatter.format(Long.valueOf(time));
			}
		} catch (Exception e) {

		}
		return strTime;
	}

	public static String getStringTimeFromLongCustomFormat(long time, String timeFormat) {
		String strTime = "- -";
		try {
			if (time > 0) {
				SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
				strTime = formatter.format(Long.valueOf(time));
			}
		} catch (Exception e) {

		}
		return strTime;
	}

	/**
	 * 得到时间以 最后以特定格可显示 前面做几分钟之前处理
	 * @author :Atar
	 * @createTime:2015-7-9上午11:03:12
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param time
	 * @return
	 * @description:
	 */
	public static String getStringTimeFromLong(long time, String timeFormat) {
		String strTime = "- -";
		try {
			if (time > 0) {
				long timeLong = System.currentTimeMillis() - time;
				if (timeLong < 60 * 1000) {
					if (timeLong > 0) {
						return "1分钟前";
					} else {
						SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
						strTime = formatter.format(Long.valueOf(time));
					}
				} else if (timeLong < 60 * 60 * 1000) {
					timeLong = timeLong / 1000 / 60;
					return timeLong + "分钟前";
				} else if (timeLong < 60 * 60 * 24 * 1000) {
					timeLong = timeLong / 1000 / 60 / 60;
					// SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
					// strTime = formatter.format(Long.valueOf(time));
					// timeLong = timeLong / 60 / 60 / 1000;
					return timeLong + "小时前";
					// } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
					// timeLong = timeLong / 1000 / 60 / 60 / 24;
					// return timeLong + "天前";
					// } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
					// timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
					// return timeLong + "周前";
				} else {
					SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
					strTime = formatter.format(Long.valueOf(time));
				}
				// SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
				// strTime = formatter.format(Long.valueOf(time));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strTime;
	}

	/**
	 * 如此<--1404377575000--->时间转换为yyyyMMddHH 类型格式int型时间
	 * @author :Atar
	 * @createTime:2015-5-21下午4:36:11
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param time
	 * @return
	 * @description:
	 */
	public static String getTimeFromlong(long time) {
		String strTime = "- -";
		try {
			if (time > 0) {
				long timeLong = System.currentTimeMillis() - time;
				if (timeLong < 60 * 1000) {
					if (timeLong > 0) {
						return "1分钟前";
					} else {
						SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm");
						strTime = formatter.format(Long.valueOf(time));
					}
				} else if (timeLong < 60 * 60 * 1000) {
					timeLong = timeLong / 1000 / 60;
					return timeLong + "分钟前";
				} else if (timeLong < 60 * 60 * 24 * 1000) {
					String timeFormate = "";
					if (isSameDayOfMillis(time)) {
						timeFormate = "HH:mm";
					} else {
						if (isSameYear(System.currentTimeMillis(), time)) {
							timeFormate = "MM-dd";
						} else {
							timeFormate = "yy-MM-dd";
						}
					}
					SimpleDateFormat formatter = new SimpleDateFormat(timeFormate);
					strTime = formatter.format(Long.valueOf(time));
					return strTime;
					// timeLong = timeLong / 60 / 60 / 1000;
					// } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
					// timeLong = timeLong / 1000 / 60 / 60 / 24;
					// return timeLong + "天前";
					// } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
					// timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
					// return timeLong + "周前";
				} else {
					String timeFormate = "";
					if (isSameYear(System.currentTimeMillis(), time)) {
						timeFormate = "MM-dd";
					} else {
						timeFormate = "yy-MM-dd";
					}
					SimpleDateFormat formatter = new SimpleDateFormat(timeFormate);
					strTime = formatter.format(Long.valueOf(time));
				}
			}
		} catch (Exception e) {

		}
		return strTime;
	}

	public static final int SECONDS_IN_DAY = 60 * 60 * 24;
	public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

	/**
	 * 两时间是否在当前天之内
	 * @author :Atar
	 * @createTime:2016-11-2上午10:51:20
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param ms1
	 * @param ms2
	 * @return
	 * @description:
	 */
	public static boolean isSameDayOfMillis(final long ms2) {
		long ms1 = System.currentTimeMillis();
		final long interval = ms1 - ms2;
		return interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY && toDay(ms1) == toDay(ms2);
	}

	private static long toDay(long millis) {
		return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
	}

	/**
	 * time1（当前时间） 和 time2 是否在同一年
	 * @author :Atar
	 * @createTime:2016-11-3上午11:08:42
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param time1
	 * @param time2
	 * @return
	 * @description:
	 */
	private static boolean isSameYear(long time1, long time2) {
		try {
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy");
			return Integer.valueOf(formatter1.format(Long.valueOf(time1))) <= Integer.valueOf(formatter1.format(Long.valueOf(time2)));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断时间间隔大于某值
	 * @param currenttime 当前时间
	 * @param lasttime 比较的时间
	 * @param intervaltime 时间间隔min
	 * @return int 0:不过期；1:过期
	 */
	public static int expire(long currenttime, long lasttime, int intervaltime) {
		int isexpire = 0;
		long difftime = (currenttime - lasttime) / 1000 / 60;
		if (difftime > intervaltime) {
			isexpire = 1;
		} else {
			isexpire = 0;
		}
		// ShowLog.d("TimeFormatUtil", "expire   difftime = " + difftime);
		return isexpire;
	}

	/**
	 * 是否在交易时间内 周一到周五，09：15~15：00
	 * @author :Atar
	 * @createTime:2017-2-17上午10:36:24
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param time
	 * @return
	 * @description:
	 */
	public static boolean isJiaoYiTime(long time) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time);
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				String strTimeyyyyMMddStart = getStringTimeFromLongCustomFormat(time, "yyyy-MM-dd") + " 09:15:00";
				String strTimeyyyyMMddEnd = getStringTimeFromLongCustomFormat(time, "yyyy-MM-dd") + " 15:00:00";
				long start = TimeUtils.getTime(strTimeyyyyMMddStart);// 当天09:15:00的时间戳
				long end = TimeUtils.getTime(strTimeyyyyMMddEnd);// 当天15:00:00的时间戳
				if (time > start && time < end) {
					return true;
				}
			}
		} catch (Exception e) {

		}
		return false;
	}
}
