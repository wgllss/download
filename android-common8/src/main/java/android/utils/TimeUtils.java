package android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 得到当前时间 自定义格式
	 * @author :Atar
	 * @createTime:2016-12-13下午3:29:48
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param format
	 * @return
	 * @description:
	 */
	public static String getCurrentTimeIn2String(String format) {
		SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(format);
		return getTime(System.currentTimeMillis(), DATE_FORMAT_DATE);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 *yyyy-MM-dd HH:mm:ss 转化为时间戳
	 * @author :Atar
	 * @createTime:2014-12-29上午11:07:47
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param yyyymmdd
	 * @param strTime
	 * @return
	 * @description:
	 */
	public static long getTime(String yyyymmddHHmmss) {
		Date date = null;
		long time = 0;
		try {
			date = DEFAULT_DATE_FORMAT.parse(yyyymmddHHmmss);
			time = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 计算两个时间差
	 * @author :Atar
	 * @createTime:2014-11-12上午11:20:23
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @param startDate
	 * @param endDate
	 * @return
	 * @description:
	 */
	public static String twoDateDistance(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 60 * 1000)
			return timeLong / 1000 + "秒前";
		else if (timeLong < 60 * 60 * 1000) {
			timeLong = timeLong / 1000 / 60;
			return timeLong + "分钟前";
		} else if (timeLong < 60 * 60 * 24 * 1000) {
			timeLong = timeLong / 60 / 60 / 1000;
			return timeLong + "小时前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
			timeLong = timeLong / 1000 / 60 / 60 / 24;
			return timeLong + "天前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
			timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
			return timeLong + "周前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(startDate);
		}
	}
}
