package com.phoenix.common.web;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.phoenix.common.lang.StringUtil;

public class UtilDate {
	/** 默认日期格式 */
	private static final String PATTERN_DEFAUTL = "yyyy-MM-dd";

	/**
	 * 返回某个日期与当前日期的月份的差值
	 */
	public static int getMonthDiffToNow(Date date) {
		int mDiff = 0;
		int tmpM = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		mDiff = cal.get(Calendar.MONTH);
		cal.setTime(Calendar.getInstance().getTime());
		tmpM = cal.get(Calendar.MONTH);
		if (mDiff <= tmpM) {
			mDiff = tmpM - mDiff;
		} else {
			mDiff = tmpM - mDiff + 12;
		}

		return mDiff;
	}

	/**
	 * 返回某个日期与当前日期的年份的差值
	 */
	public static int getYearDiffToNow(Date date) {
		int yDiff = 0;
		int mDiff = 0;
		int tmpY = 0;
		int tmpM = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		yDiff = cal.get(Calendar.YEAR);
		mDiff = cal.get(Calendar.MONTH);
		cal.setTime(Calendar.getInstance().getTime());
		tmpY = cal.get(Calendar.YEAR);
		tmpM = cal.get(Calendar.MONTH);
		if (tmpY <= yDiff) {
			yDiff = yDiff - tmpY;
			mDiff = mDiff - tmpM;
		} else {
			yDiff = tmpY - yDiff;
			mDiff = tmpM - mDiff;
		}

		if (mDiff < 0)
			yDiff -= 1;

		return yDiff;
	}

	/**
	 * 取得当前日期.
	 * 
	 * @param pattern
	 *            日期格式
	 * @return String 格式化日期
	 */
	public static String getCurDate(String pattern) {
		return new SimpleDateFormat(pattern).format(Calendar.getInstance()
				.getTime());
	}

	/**
	 * 取得当前时间.
	 * 
	 * @return Date
	 */
	public static Date getCurDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 将日期格式化指定形式.
	 * 
	 * @param pattern
	 *            格式
	 * @param dateFormat
	 * @param dateTime
	 * @return String
	 */
	public static String doFormatDate(String pattern, String dateFormat,
			String dateTime) {
		try {
			return doFormatDate(pattern, doParseDate(dateFormat, dateTime));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 日期型格式成字符型.
	 * 
	 * @param pattern
	 * @param dateTime
	 * @return String
	 */
	public static String doFormatDate(String pattern, Date dateTime) {
		try {
			if (dateTime == null) {
				return null;
			}

			return new SimpleDateFormat(pattern).format(dateTime);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符型转换成日期型.
	 * 
	 * @param pattern
	 * @param dateTime
	 * @return
	 */
	public static Date doParseDate(String pattern, String dateTime) {
		try {
			return new SimpleDateFormat(pattern).parse(dateTime,
					new ParsePosition(0));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 加减时间.
	 * 
	 * @param dateTime
	 * @param pattern
	 * @param amount
	 * @param field
	 * @return
	 */
	public static String modifyDate(Date dateTime, String pattern, int amount,
			String field) {
		return doFormatDate(pattern, modifyDate(dateTime, amount, field));
	}

	/**
	 * modify the given date
	 * 
	 * @param dateTime
	 *            the date
	 * @param amout
	 *            the count
	 * @param field
	 *            the flag of "day" or "houre"
	 * 
	 * @return the date modified
	 */
	public static Date modifyDate(Date dateTime, int amount, String field) {
		if (dateTime == null) {
			return null;
		}

		long dateGiven = dateTime.getTime();
		long lRetn = 0;

		if (field.equals("day")) {
			lRetn = dateGiven + (amount * 24 * 60 * 60 * 1000L);
		}

		if (field.equals("hour")) {
			lRetn = dateGiven + (amount * 60 * 60 * 1000L);
		}

		Date dateRetn = new Date(lRetn);
		return dateRetn;
	}

	/**
	 * 得到当前时间的前一个月份.
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getCurrentPreMonthString(String pattern) {
		return getPreMonthString(pattern, getCurDate());
	}

	/**
	 * 得到某个时间的前一个月份.
	 * 
	 * @param pattern
	 * @param dateTime
	 * @return
	 */
	public static String getPreMonthString(String pattern, Date dateTime) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dateTime);

		// 得到前一个月的最后一天
		return doFormatDate(pattern, modifyDate(dateTime, -(cal
				.get(Calendar.DAY_OF_MONTH) + 1), "day"));
	}

	/**
	 * 得到当前月份的最后一天
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String getMonthLastDay(Date dateTime) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dateTime);
		int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		String reDate = doFormatDate("yyyy-MM-", cal.getTime());
		return reDate + day;

	}

	/**
	 * 返回指定日期是星期几
	 * 
	 * @param pattern
	 * @param date
	 * @return 周几
	 */
	public static int getWeekDay(String pattern, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 取得两个时间相差的天数.
	 * 
	 * @param startDate
	 *            开始日期,默认格式"yyyy-MM-dd"
	 * @param endDate
	 *            结束日期,默认格式"yyyy-MM-dd"
	 * @return Long 天数
	 */
	public static Long getIntervalDays(String startDate, String endDate) {
		return (parseDate(endDate, null).getTime() - parseDate(startDate, null)
				.getTime())
				/ (1000L * 60 * 60 * 24);
	}

	/**
	 * 日期型转换成字符型.
	 * 
	 * @param theDate
	 *            输入日期
	 * @param pattern
	 *            转换格式,可以为null ,如默认格式"yyyy-MM-dd"
	 * @return 字符型日期
	 */
	public static String formatDate(Date theDate, String pattern) {
		if (null == theDate) {
			return null;
		}

		return StringUtil.isNotBlank(pattern) ? new SimpleDateFormat(pattern)
				.format(theDate) : new SimpleDateFormat(PATTERN_DEFAUTL)
				.format(theDate);
	}

	/**
	 * 格式化成指定日期类型.
	 * 
	 * @param theDate
	 *            字符型日期
	 * @param pattern
	 *            转换格式,可以为null ,如默认格式"yyyy-MM-dd"
	 * @return Date 格式化后日期
	 */
	public static Date parseDate(String theDate, String pattern) {
		if (StringUtil.isBlank(theDate)) {
			return null;
		}

		SimpleDateFormat format = StringUtil.isNotBlank(pattern) ? new SimpleDateFormat(
				pattern)
				: new SimpleDateFormat(PATTERN_DEFAUTL);
		try {
			return format.parse(theDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
