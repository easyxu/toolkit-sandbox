package com.phoenix.common.lang;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings( { "unchecked", "deprecation" })
public class DateUtil  {
	public DateUtil() {
	}

	public static Date StrToDate(String source, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = sdf.parse(source);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}

	public static Time getTime(String source) {
		return Time.valueOf(source);
	}

	@SuppressWarnings("deprecation")
	public static Date StrToDate(String s) {
		if (s != null) {
			char c = s.charAt(4);
			int i;
			int j;
			int k;
			int l;
			int i1;
			int j1;
			if (c >= '0' && c <= '9') {
				i = parseInt(s, 0, 3) - 1900;
				j = parseInt(s, 4, 5) - 1;
				k = parseInt(s, 6, 7);
				l = parseInt(s, 9, 10);
				i1 = parseInt(s, 12, 13);
				j1 = parseInt(s, 15, 16);
			} else {
				i = parseInt(s, 0, 3) - 1900;
				j = parseInt(s, 5, 6) - 1;
				k = parseInt(s, 8, 9);
				l = parseInt(s, 11, 12);
				i1 = parseInt(s, 14, 15);
				j1 = parseInt(s, 17, 18);
			}
			return new Date(i, j, k, l, i1, j1);
		} else {
			return new Date();
		}
	}

	public static String DateTimeToStr(Date date) {
		return DateTimeToStr(date, ' ');
	}

	public static String DateTimeToISOStr(Date date) {
		return DateTimeToStr(date, 'T');
	}

	public static String DateTimeToStr(Date date, char c, char c1, char c2) {
		if (date == null)
			return null;
		StringBuffer stringbuffer = new StringBuffer(20);
		GregorianCalendar gregoriancalendar = new GregorianCalendar();
		gregoriancalendar.setTime(date);
		stringbuffer.append(gregoriancalendar.get(1));
		if (c != 0)
			stringbuffer.append(c);
		int i = gregoriancalendar.get(2) + 1;
		appendInt(stringbuffer, i);
		if (c != 0)
			stringbuffer.append(c);
		int j = gregoriancalendar.get(5);
		appendInt(stringbuffer, j);
		int k = gregoriancalendar.get(11);
		int l = gregoriancalendar.get(12);
		int i1 = gregoriancalendar.get(13);
		if (k + l + i1 > 0) {
			if (c1 != 0)
				stringbuffer.append(c1);
			appendInt(stringbuffer, k);
			if (c2 != 0)
				stringbuffer.append(c2);
			appendInt(stringbuffer, l);
			if (c2 != 0)
				stringbuffer.append(c2);
			appendInt(stringbuffer, i1);
		}
		return stringbuffer.toString();
	}

	public static String DateTimeToStr(Date date, char c) {
		if (c == ' ')
			return DateTimeToStr(date, '/', ' ', ':');
		else
			return DateTimeToStr(date, '\0', 'T', ':');
	}

	static int parseInt(String s, int i, int j) {
		int k = 0;
		int l = s.length();
		if (i >= l || j >= l)
			return 0;
		for (int i1 = i; i1 <= j; i1++) {
			int j1 = s.charAt(i1) - 48;
			if (j1 >= 0 && j1 <= 9)
				k = k * 10 + j1;
			else
				System.out.println("char not a number  at " + s + " index="
						+ i1);
		}

		return k;
	}

	private static void appendInt(StringBuffer stringbuffer, int i) {
		if (i < 10)
			stringbuffer.append("0");
		stringbuffer.append(i);
	}

	/**
	 * to_date('time','yyyy-mm-dd')
	 * 
	 * @param value
	 * @return
	 */
	public static String toDateSql(String value) {
		return "to_date( '" + trimToDay(value) + "','yyyy-mm-dd' )";
	}

	public static String trimToDay(String value) {
		if (isEmpty(value))
			return "9999-01-01";
		int idx = value.indexOf(" ");
		return idx == -1 ? value : value.substring(0, idx);
	}

	public static String preventImmit(String tmpStr) {
		if (tmpStr == null)
			return "";
		tmpStr = tmpStr.replaceAll("'", "''");
		return tmpStr;
	}

	public static String getFormatNowDateTime(String formatStr) {
		Calendar nowtime = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(
				formatStr);
		return sdf.format(nowtime.getTime());
	}

	public static String getFormatDateTime(String formatStr, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				formatStr);
		return sdf.format(date);
	}

	public static String getDate() {
		return getFormatNowDateTime("yyyy-MM-dd");
	}

	public static String getDate(Date date) {
		return getFormatDateTime("yyyy-MM-dd", date);
	}

	public static String getHour() {
		return getFormatNowDateTime("HH");
	}

	public static String getHour(Date date) {
		return getFormatDateTime("HH", date);
	}

	public static String getMinutes() {
		return getFormatNowDateTime("mm");
	}

	public static String getMinutes(Date date) {
		return getFormatDateTime("mm", date);
	}

	public static String getSeconds() {
		return getFormatNowDateTime("ss");
	}

	public static String getSeconds(Date date) {
		return getFormatDateTime("ss", date);
	}

	public static String getYear() {
		return getFormatNowDateTime("yyyy");
	}

	public static String getYear(Date date) {
		return getFormatDateTime("yyyy", date);
	}

	public static String getMonth() {
		return getFormatNowDateTime("MM");
	}

	public static String getMonth(Date date) {
		return getFormatDateTime("MM", date);
	}

	public static String getDay() {
		return getFormatNowDateTime("dd");
	}

	public static String getDay(Date date) {
		return getFormatDateTime("dd", date);
	}

	public static String getNowTime() {
		return getFormatNowDateTime("HH:mm:ss");
	}

	public static String getNowDateTime() {
		return getFormatNowDateTime("yyyy-MM-dd HH:mm:ss");
	}

	public static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 返回当前时间 Hmmss格式
	 * 
	 * @param date
	 * @return
	 */
	public static String toHMMSS() {

		return getFormatNowDateTime("Hmmss");
	}

	public static String toHMMSS(Date date) {

		return getFormatDateTime("Hmmss", date);
	}

	public static String toYYYYMMDD() {

		return getFormatNowDateTime("yyyyMMdd");
	}

	public static String toYYYYMMDD(Date date) {

		return getFormatDateTime("yyyyMMdd", date);
	}

	/**
	 * 返回当前时间 H:mm:ss格式
	 * 
	 * @param date
	 * @return
	 */
	public static String toH_MM_SS() {
		return getFormatNowDateTime("H:mm:ss");
	}

	public static String toH_MM_SS(Date date) {
		return getFormatDateTime("H:mm:ss", date);
	}

	/**
	 * 将日期转化为标准的8位显示
	 * 
	 * @param yyyyMMdd
	 * @return YYYYMMDD
	 */
	public static String toYYYYMMDD(String yyyyMMdd) {

		String temp = yyyyMMdd;
		String tDate[];

		if (temp == null) {
			return "";
		}

		if (temp.indexOf("/") != -1) {
			tDate = temp.split("/");
			if (tDate[1].length() != 2) {
				tDate[1] = "0" + tDate[1];
			}
			if (tDate[2].length() != 2) {
				tDate[2] = "0" + tDate[2];
			}
			temp = tDate[0] + tDate[1] + tDate[2];
		} else if (temp.indexOf("-") != -1) {
			tDate = temp.split("-");
			if (tDate[1].length() != 2) {
				tDate[1] = "0" + tDate[1];
			}
			if (tDate[2].length() != 2) {
				tDate[2] = "0" + tDate[2];
			}
			temp = tDate[0] + tDate[1] + tDate[2];
		}

		if (!checkDate(temp)) {
			return "";
		}

		return temp;
	}

	/**
	 * 对日期的合法性进行检查检查
	 * 
	 * @param yyyyMMdd
	 * @return true of false
	 */
	private static boolean checkDate(String yyyyMMdd) {

		if (yyyyMMdd == null) {
			return false;
		}

		if (yyyyMMdd.length() != 8) {
			return false;
		}

		int intYear = Integer.parseInt(yyyyMMdd.substring(0, 4));
		int intMonth = Integer.parseInt(yyyyMMdd.substring(4, 6));
		int intDay = Integer.parseInt(yyyyMMdd.substring(6, yyyyMMdd.length()));
		if (intYear < 1) {
			return false;
		}

		int[] wkMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if ((intMonth > 12) || (intMonth < 1)) {
			return false;
		}
		if (isLeapYear(intYear)) {
			wkMonth[1] = 29;
		}

		if ((intDay > wkMonth[intMonth - 1]) || intDay < 1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断是否是LeapYear
	 * 
	 * @param year
	 * @return true or false
	 */
	private static boolean isLeapYear(int year) {

		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 讲日期格式由“yyyyMMdd”转化为“YYYY-MM-DD”
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public static String toYYYYMMDDWithSlash(String yyyyMMdd) {

		String temp = toYYYYMMDD(yyyyMMdd);
		if (temp.equals("")) {
			return "";
		}

		String yyyy = temp.substring(0, 4);
		String MM = temp.substring(4, 6);
		String dd = temp.substring(6, temp.length());

		return yyyy + "-" + MM + "-" + dd;
	}

	/**
	 * 取得01－12月份列表
	 * 
	 * @return list 01－12月份列表
	 */

	public static ArrayList getMonths() {
		ArrayList list = new ArrayList();
		for (int i = 1; i <= 12; i++) {
			Map map = new HashMap();
			String temp = String.valueOf(i);
			if (temp.length() == 1) {
				temp = "0" + temp;
			}
			map.put("label", temp);
			map.put("value", temp);
			list.add(map);
		}
		return list;
	}

	/**
	 * 取得系统年的前后十年列表
	 * 
	 * @return list 系统年前后十年列表
	 */
	public static ArrayList getYears() {
		ArrayList list = new ArrayList();
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(1, -6);

		for (int i = 0; i <= 10; i++) {
			gc.add(1, +1);
			Map map = new HashMap();
			map.put("label", df.format(gc.getTime()));
			map.put("value", df.format(gc.getTime()));
			list.add(map);
		}
		return list;
	}

	// 取得当前年的前后几年列表
	public static ArrayList getListYear() {
		ArrayList list = new ArrayList();
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(1, -6);

		for (int i = 0; i <= 6; i++) {
			gc.add(1, +1);
			list.add(df.format(gc.getTime()));
		}
		return list;

	}

	// 取得每年的12个月份列表
	public static List getListMonth() {
		List list = new ArrayList();
		for (int i = 1; i <= 12; i++) {
			String temp = String.valueOf(i);
			if (temp.length() == 1) {
				temp = "0" + temp;
			}
			list.add(temp);
		}
		return list;
	}

	// 对null值进行转化
	public static String convertNull(String str) {
		if (str == null) {
			str = "";
		}
		return str;
	}

	/**
	 * 得到某年某月的天数
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */

	public static int getDayNum(int year, int month, int day) {
		Date date = new Date(year, month, day);
		Calendar cal = Calendar.getInstance();
		System.out.println(date);
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1); // 前个月
		int month_day_score = cal.getActualMaximum(Calendar.DAY_OF_MONTH);// 最后一天
		return month_day_score;
	}

	/**
	 * 得到某年某月的天数
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int getDayNum(String year, String month, String day) {
		return getDayNum(Integer.valueOf(year), Integer.valueOf(month), Integer
				.valueOf(day));
	}

	public static final String DATE_DIVISION = "-";

	public static final String TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTON_DEFAULT = "yyyy-MM-dd";
	public static final String DATA_PATTON_YYYYMMDD = "yyyyMMdd";
	public static final String TIME_PATTON_HHMMSS = "HH:mm:ss";

	public static final int ONE_SECOND = 1000;
	public static final int ONE_MINUTE = 60 * ONE_SECOND;
	public static final int ONE_HOUR = 60 * ONE_MINUTE;
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * Return the current date
	 * 
	 * @return － DATE<br>
	 */
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return currDate;
	}

	/**
	 * Return the current date string
	 * 
	 * @return － 产生的日期字符串<br>
	 */
	public static String getCurrentDateStr() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate);
	}

	/**
	 * Return the current date in the specified format
	 * 
	 * @param strFormat
	 * @return
	 */
	public static String getCurrentDateStr(String strFormat) {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();

		return format(currDate, strFormat);
	}

	/**
	 * Parse a string and return a date value
	 * 
	 * @param dateValue
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(String dateValue) {
		return parseDate(DATE_PATTON_DEFAULT, dateValue);
	}

	/**
	 * Parse a string and return a date value
	 * 
	 * @param dateValue
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(Object object) {
		return parseDate(DATE_PATTON_DEFAULT, (String) object);
	}

	/**
	 * Parse a strign and return a datetime value
	 * 
	 * @param dateValue
	 * @return
	 */
	public static Date parseDateTime(String dateValue) {
		return parseDate(TIME_PATTON_DEFAULT, dateValue);
	}

	/**
	 * Parse a string and return the date value in the specified format
	 * 
	 * @param strFormat
	 * @param dateValue
	 * @return
	 * @throws java.text.ParseException
	 * @throws Exception
	 */
	public static Date parseDate(String strFormat, String dateValue) {
		if (dateValue == null)
			return null;

		if (strFormat == null)
			strFormat = TIME_PATTON_DEFAULT;

		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		Date newDate = null;

		try {
			newDate = dateFormat.parse(dateValue);
		} catch (ParseException pe) {
			newDate = null;
		}

		return newDate;
	}

	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String format(Date aTs_Datetime) {
		return format(aTs_Datetime, DATE_PATTON_DEFAULT);
	}

	/**
	 * 将Timestamp类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 *            需要转换的日期。
	 * @return 转换后符合给定格式的日期字符串
	 */
	public static String formatTime(Date aTs_Datetime) {
		return format(aTs_Datetime, TIME_PATTON_DEFAULT);
	}

	/**
	 * 将Date类型的日期转换为系统参数定义的格式的字符串。
	 * 
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Date aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * @param aTs_Datetime
	 * @param as_Format
	 * @return
	 */
	public static String formatTime(Date aTs_Datetime, String as_Format) {
		if (aTs_Datetime == null || as_Format == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Format);

		return dateFromat.format(aTs_Datetime);
	}

	public static String getFormatTime(Date dateTime) {
		return formatTime(dateTime, TIME_PATTON_HHMMSS);
	}

	/**
	 * @param aTs_Datetime
	 * @param as_Pattern
	 * @return
	 */
	public static String format(Timestamp aTs_Datetime, String as_Pattern) {
		if (aTs_Datetime == null || as_Pattern == null)
			return null;

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(as_Pattern);

		return dateFromat.format(aTs_Datetime);
	}

	/**
	 * 取得指定日期N天后的日期
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_MONTH, days);

		return cal.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static int daysBetweenPlus(Date date1, Date date2)
			throws ParseException {
		String dateStr1 = format(date1);
		String dateStr2 = format(date2);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTON_DEFAULT);
		Date date1New = sdf.parse(dateStr1);
		Date date2New = sdf.parse(dateStr2);
		return daysBetween(date1New, date2New);
	}

	/**
	 * 计算当前日期相对于"1977-12-01"的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long getRelativeDays(Date date) {
		Date relativeDate = DateUtil.parseDate("yyyy-MM-dd", "1977-12-01");

		return DateUtil.daysBetween(relativeDate, date);
	}

	public static Date getDateBeforTwelveMonth() {
		String date = "";
		Calendar cla = Calendar.getInstance();
		cla.setTime(getCurrentDate());
		int year = cla.get(Calendar.YEAR) - 1;
		int month = cla.get(Calendar.MONTH) + 1;
		if (month > 9) {
			date = String.valueOf(year) + DATE_DIVISION + String.valueOf(month)
					+ DATE_DIVISION + "01";
		} else {
			date = String.valueOf(year) + DATE_DIVISION + "0"
					+ String.valueOf(month) + DATE_DIVISION + "01";
		}

		Date dateBefore = parseDate(date);
		return dateBefore;
	}

	/**
	 * 传入时间字符串,加一天后返回Date
	 * 
	 * @param date
	 *            时间 格式 YYYY-MM-DD
	 * @return
	 */
	public static Date addDate(String date) {
		if (date == null) {
			return null;
		}

		Date tempDate = parseDate(DATE_PATTON_DEFAULT, date);
		String year = format(tempDate, "yyyy");
		String month = format(tempDate, "MM");
		String day = format(tempDate, "dd");

		GregorianCalendar calendar = new GregorianCalendar(Integer
				.parseInt(year), Integer.parseInt(month) - 1, Integer
				.parseInt(day));

		calendar.add(GregorianCalendar.DATE, 1);
		return calendar.getTime();
	}
}
