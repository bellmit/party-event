package com.sunsharing.party.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Tom
 */
public class DateUtils {
	
	/***
	 * 字符串格式化为指定日期格式
	 * @author chenhao 2016年11月9日 上午11:10:16
	 * @param date -- 日期字符串
	 * @param pattern -- 日期字符串格式
	 * @param toPatten --转换的格式
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public static final String dateStrToFamtStr(String date, String pattern, String toPatten) throws Exception {
		Date now = StringToDate(date, pattern);
		format.applyPattern("".equals(toPatten) ? DATE_PATTERN_YYYYMMDDHHMMSS : toPatten);
		return format.format(now);
	}
	
	public static String DateToString(Date date, String pattern) {
		if ("".equals(pattern)) {
			pattern = DATE_PATTERN_YYYY_MM_DD;
		}
		format.applyPattern(pattern);
		return format.format(date);
	}
	
	private static Calendar getCalendar() {
		return Calendar.getInstance();
	}
	
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		
		if (month < 10) {
			if (day < 10)
				return year + "-0" + month + "-0" + day + " " + hour + ":" + minute + ":" + second;
			else
				return year + "-0" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
		} else {
			if (day < 10)
				return year + "-" + month + "-0" + day + " " + hour + ":" + minute + ":" + second;
			else
				return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
		}
	}
	
	private static int getDateField(Date date, int field) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c.get(field);
	}
	
	public static DateUtils getDateInstance() {
		if (date == null) {
			date = new DateUtils();
		}
		return date;
	}
	
	/**
	 * 获取当天年月日 格式：yyyy-MM-dd
	 * @return
	 */
	public static String getDayOfThisMonth() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (month < 10) {
			if (day < 10)
				return year + "-0" + month + "-0" + day;
			else
				return year + "-0" + month + "-" + day;
		} else {
			if (day < 10)
				return year + "-" + month + "-0" + day;
			else
				return year + "-" + month + "-" + day;
		}
		
	}
	
	public static int getDaysBetweenDate(Date begin, Date end) {
		int bDay = getDateField(begin, Calendar.DAY_OF_YEAR);
		int eDay = getDateField(end, Calendar.DAY_OF_YEAR);
		return eDay - bDay;
	}
	
	/**
	 * 得到指定日期的一天的的最后时刻23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFinallyDate(Date date) {
		String temp = format.format(date);
		temp += " 23:59:59";
		
		try {
			return format1.parse(temp);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static int getMonthsBetweenDate(Date begin, Date end) {
		int bMonth = getDateField(begin, Calendar.MONTH);
		int eMonth = getDateField(end, Calendar.MONTH);
		return eMonth - bMonth;
	}
	
	public static Date getSpecficDateStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return getStartDate(cal.getTime());
	}
	
	/**
	 * 获取当前自然月后的amount月的最后一天的终止时间
	 * 
	 * @param amount 可正、可负
	 * @return
	 */
	public static Date getSpecficMonthEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getSpecficMonthStart(date, amount + 1));
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFinallyDate(cal.getTime());
	}
	
	/**
	 * 获取date月后的amount月的第一天的开始时间
	 * 
	 * @param amount 可正、可负
	 * @return
	 */
	public static Date getSpecficMonthStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, amount);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getStartDate(cal.getTime());
	}
	
	/**
	 * 获取date周后的第amount周的最后时间（这里星期日为一周的最后一天）
	 * 
	 * @param amount 可正、可负
	 * @return
	 */
	public static Date getSpecficWeekEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
		cal.add(Calendar.WEEK_OF_MONTH, amount);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return getFinallyDate(cal.getTime());
	}
	
	/**
	 * 获取date周后的第amount周的开始时间（这里星期一为一周的开始）
	 * 
	 * @param amount 可正、可负
	 * @return
	 */
	public static Date getSpecficWeekStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
		cal.add(Calendar.WEEK_OF_MONTH, amount);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return getStartDate(cal.getTime());
	}
	
	/**
	 * 获取date年后的amount年的最后一天的终止时间
	 * 
	 * @param amount 可正、可负
	 * @return
	 */
	public static Date getSpecficYearEnd(Date date, int amount) {
		Date temp = getStartDate(getSpecficYearStart(date, amount + 1));
		Calendar cal = Calendar.getInstance();
		cal.setTime(temp);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFinallyDate(cal.getTime());
	}
	
	/**
	 * 获取date年后的amount年的第一天的开始时间
	 * 
	 * @param amount 可正、可负
	 * @return
	 */
	public static Date getSpecficYearStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, amount);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return getStartDate(cal.getTime());
	}
	
	/**
	 * 得到指定日期的一天的开始时刻00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDate(Date date) {
		String temp = format.format(date);
		temp += " 00:00:00";
		
		try {
			return format1.parse(temp);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static int getWeeksBetweenDate(Date begin, Date end) {
		int bWeek = getDateField(begin, Calendar.WEEK_OF_YEAR);
		int eWeek = getDateField(end, Calendar.WEEK_OF_YEAR);
		return eWeek - bWeek;
	}
	
	/**
	 * 获取有效工作日（去除周末）
	 * @param startD 起始时间
	 * @param endD 结束时间
	 * @return int[总天数,工作日天数]
	 */
	@SuppressWarnings("deprecation")
	public static int[] getWorkingDay(Date startD, Date endD) {
		Calendar start = new GregorianCalendar(startD.getYear(), startD.getMonth() - 1, startD.getDay(), 0, 0);
		Calendar end = new GregorianCalendar(endD.getYear(), endD.getMonth() - 1, endD.getDay(), 0, 0);
		long day = 86400000;// 一天的millis
		long mod = (end.getTimeInMillis() - start.getTimeInMillis()) / day;
		int n = start.get(Calendar.DAY_OF_WEEK);// 起始是周几
		// 周日到周六分别是1~7
		long cnt = mod / 7;// 几个整周
		cnt = cnt * 2;
		long yushu = mod % 7;
		if (n + yushu > 7)
			cnt = cnt + 2;// 过了周六
		if (n + yushu == 7)
			cnt++;// 正好是周六
		if (n == 7)
			cnt--;// 起始正好是周六
		return new int[] {(int)mod, (int)(mod - cnt)};
	}
	
	public static int getYearsBetweenDate(Date begin, Date end) {
		int bYear = getDateField(begin, Calendar.YEAR);
		int eYear = getDateField(end, Calendar.YEAR);
		return eYear - bYear;
	}
	
	/**
	 * 
	 * @param date 指定比较日期
	 * @param compareDate
	 * @return
	 */
	public static boolean isInDate(Date date, Date compareDate) {
		if (compareDate.after(getStartDate(date)) && compareDate.before(getFinallyDate(date))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String args[]) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = format.parse("20161114182300");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 字符串转时间
	 * @param dateString
	 * @param pattern 只能传入和字符相同格式的转换格式
	 * @return
	 * @throws Exception
	 */
	public static Date StringToDate(String dateString, String pattern) throws Exception {
		if ("".equals(pattern)) {
			pattern = DATE_PATTERN_YYYY_MM_DD;
		}
		format.applyPattern(pattern);
		return format.parse(dateString);
	}
	
	private final StringBuffer buffer = new StringBuffer();
	
	private static String ZERO = "0";
	
	private static DateUtils date;
	
	public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	
	public static SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	
	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	
	public static final String DATE_PATTERN_YYYY_MM = "yyyy-MM";
	
	public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	
	private int getDate(Calendar calendar) {
		return calendar.get(Calendar.DATE);
	}
	
	private int getHour(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	private int getMinute(Calendar calendar) {
		return calendar.get(Calendar.MINUTE);
	}
	
	private int getMonth(Calendar calendar) {
		return calendar.get(Calendar.MONDAY) + 1;
	}
	
	public String getNowString() {
		Calendar calendar = getCalendar();
		buffer.delete(0, buffer.capacity());
		buffer.append(getYear(calendar));
		
		if (getMonth(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getMonth(calendar));
		
		if (getDate(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getDate(calendar));
		if (getHour(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getHour(calendar));
		if (getMinute(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getMinute(calendar));
		if (getSecond(calendar) < 10) {
			buffer.append(ZERO);
		}
		buffer.append(getSecond(calendar));
		return buffer.toString();
	}
	
	private int getSecond(Calendar calendar) {
		return calendar.get(Calendar.SECOND);
	}
	
	private int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}
	
}
