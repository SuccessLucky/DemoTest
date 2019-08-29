package com.project.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

	private static Log logger = LogFactory.getLog(DateUtil.class);
	
	private DateUtil() {
	}

	/** 获取日期格式 yyyy/MM/dd */
	public static final int PATTERN_1 = 1;
	/** 获取日期格式 yyyy/MM/dd HH:mm:ss */
	public static final int PATTERN_2 = 2;
	/** 获取日期格式 yyyy/MM/dd HH:mm:ss:sss */
	public static final int PATTERN_3 = 3;
	/** 获取日期格式 yyyy-MM-dd */
	public static final int PATTERN_4 = 4;
	/** 获取日期格式 yyyy-MM-dd HH:mm:ss */
	public static final int PATTERN_5 = 5;
	/** 获取日期格式 yyyy-MM-dd HH:mm:ss:sss */
	public static final int PATTERN_6 = 6;
	/** 获取年 */
	public static final int PATTERN_7 = 7;
	/** 获取年终的月份 */
	public static final int PATTERN_8 = 8;
	/** 获取年终的周数 */
	public static final int PATTERN_9 = 9;
	/** 获取月份中的周数 */
	public static final int PATTERN_10 = 10;
	/** 获取年中的天数 */
	public static final int PATTERN_11 = 11;
	/** 获取月份中的天数 */
	public static final int PATTERN_12 = 12;
	/** 获取月份中的星期 */
	public static final int PATTERN_13 = 13;
	/** 获取星期中的天数 */
	public static final int PATTERN_14 = 14;
	/** 获取一天中的小时数(0-23) */
	public static final int PATTERN_15 = 15;
	/** 获取一天中的小时数(1-24) */
	public static final int PATTERN_16 = 16;
	/** 获取am/pm 中的小时数(0-11) */
	public static final int PATTERN_17 = 17;
	/** 获取am/pm 中的小时数(1-11) */
	public static final int PATTERN_18 = 18;
	/** 获取小时中的分钟数 */
	public static final int PATTERN_19 = 19;
	/** 获取分钟中的秒数 */
	public static final int PATTERN_20 = 20;
	/** 获取毫秒数 */
	public static final int PATTERN_21 = 21;

	/**
	 * 
	 * @param date
	 *            : 格式化的日期, 如果日期为null, 则代表当前日期
	 * @param pattern
	 *            : 格式化的日期模式
	 * @return: 返回字符串日期的格式.如果发生异常或格式不存在返回null
	 */
	public static String getStringDate(Date date, int pattern) {
		String strDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		String errMsg = null;

		if (date == null)
			date = new Date();

		try {
			switch (pattern) {
			case PATTERN_1:
				sdf.applyPattern("yyyy/MM/dd");
				break;
			case PATTERN_2:
				sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
				break;
			case PATTERN_3:
				sdf.applyPattern("yyyy/MM/dd HH:mm:ss:sss");
				break;
			case PATTERN_4:
				sdf.applyPattern("yyyy-MM-dd");
				break;
			case PATTERN_5:
				sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
				break;
			case PATTERN_6:
				sdf.applyPattern("yyyy-MM-dd HH:mm:ss:sss");
				break;
			case PATTERN_7:
				sdf.applyPattern("y");
				break;
			case PATTERN_8:
				sdf.applyPattern("M");
				break;
			case PATTERN_9:
				sdf.applyPattern("w");
				break;
			case PATTERN_10:
				sdf.applyPattern("W");
				break;
			case PATTERN_11:
				sdf.applyPattern("D");
				break;
			case PATTERN_12:
				sdf.applyPattern("d");
				strDate = sdf.format(date);
				break;
			case PATTERN_13:
				sdf.applyPattern("F");
				break;
			case PATTERN_14:
				sdf.applyPattern("E");
				break;
			case PATTERN_15:
				sdf.applyPattern("H");
				break;
			case PATTERN_16:
				sdf.applyPattern("k");
				break;
			case PATTERN_17:
				sdf.applyPattern("K");
				break;
			case PATTERN_18:
				sdf.applyPattern("h");
				break;
			case PATTERN_19:
				sdf.applyPattern("m");
				break;
			case PATTERN_20:
				sdf.applyPattern("s");
				break;
			case PATTERN_21:
				sdf.applyPattern("S");
				break;
			default:
				// errMsg = resource.getString("format_not_exist");
				// log.info(errMsg);
				break;
			}

			strDate = sdf.format(date);

			sdf = null;
		} catch (Exception e) {
			strDate = null;
			// errMsg = resource.getString("date_convert_string_error");
			// log.info(errMsg + "-->>" + e.getMessage());
		}

		return strDate;
	}

	/**
	 * 
	 * @param pattern
	 *            : 转换成当前日期的格式
	 * @return: 返回字符串当前日期格式
	 */
	public static String getStringNowDate(int pattern) {
		return getStringDate(null, pattern);
	}

	/**
	 * 
	 * @param date
	 *            : 格式化的日期, 如果日期为null, 则代表当前日期
	 * @param pattern
	 *            : 格式化日期的字符串模式, 例如:yyyy/MM/dd
	 * @return 返回字符串日期的格式.如果发生异常或格式不存在返回null
	 */
	public static String getStringDate(Date date, String pattern) {
		String errMsg = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(pattern);
			if (date == null)
				date = new Date();

			String strDate = sdf.format(date);

			sdf = null;
			return strDate;
		} catch (Exception e) {
			// errMsg = resource.getString("date_convert_string_error");
			// log.info(errMsg + "-->>" +e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param pattern
	 *            : 格式化日期的字符串模式, 例如:yyyy/MM/dd
	 * @return 返回字符串日期的格式.如果发生异常或格式不存在返回null
	 */
	public static String getStringNowDate(String pattern) {
		return getStringDate(null, pattern);
	}

	/**
	 * 
	 * @param strDate
	 *            : 字符串日期
	 * @param pattern
	 *            : 转换的格式, 字符串日期要与转换的格式相匹配
	 * @return 返回把字符串日期转换成日期, 如果发生异常返回null
	 */
	public static Date getDate(String strDate, String pattern) {
		String errMsg = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(pattern);
			Date date = sdf.parse(strDate);
			sdf = null;
			return date;
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * 
	 * @param
	 *            : 字符串日期
	 * @param pattern
	 *            : 字符串格式,字符串格式与日期匹配
	 * @return 返回日期加一天后的日期
	 */
	public static Date dateAddA(String strDate, String pattern) throws ParseException {
		Date date = getDate(strDate, pattern);
		if (date == null) {
			throw new ParseException("", 1);
		}

		return dateAddSub(date, '+');
	}

	/**
	 * 
	 * @param date
	 *            : 日期
	 * @return 返回日期加一天后的日期
	 * @throws Exception
	 */
	public static Date dateAddA(Date date) throws Exception {
		if (date == null) {
			throw new Exception("");
		}

		return dateAddSub(date, '+');
	}

	/**
	 * 
	 * @param
	 *            : 字符串日期
	 * @param pattern
	 *            : 字符串格式,字符串格式与日期匹配
	 * @return 返回日期减一天后的日期
	 */
	public static Date dateSubA(String strDate, String pattern) throws ParseException {
		Date date = getDate(strDate, pattern);
		if (date == null) {
			throw new ParseException("", 1);
		}

		return dateAddSub(date, '-');
	}

	/**
	 * 
	 * @param date
	 *            : 日期
	 * @return 返回日期减一天后的日期
	 * @throws Exception
	 */
	public static Date dateSubA(Date date) throws Exception {
		if (date == null) {
			throw new Exception("");
		}

		return dateAddSub(date, '-');
	}

	private static Date dateAddSub(Date date, char ch) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		if (ch == '+') {
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
		} else if (ch == '-') {
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
		}

		return calendar.getTime();
	}

	public static boolean isBetween(Date date, Date startDate, Date endDate) {
		return date.after(startDate) && date.before(endDate);
	}

	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int mapLastDay = 0;

		switch (month) {
		case 1: // fall through
		case 3: // fall through
		case 5: // fall through
		case 7: // fall through
		case 8: // fall through
		case 10: // fall through
		case 12:
			mapLastDay = 31;
			break;
		case 4: // fall through
		case 6: // fall through
		case 9: // fall through
		case 11:
			mapLastDay = 30;
			break;
		case 2:
			if (0 == year % 4 && 0 != year % 100 || 0 == year % 400) {
				mapLastDay = 29;
			} else {
				mapLastDay = 28;
			}
			break;
		}
		return new GregorianCalendar(year, month - 1, mapLastDay).getTime();
	}

	public static Date getDate(String dateStr) {
		Date date = new Date();
		// 注意format的格式要与日期String的格式相匹配
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String dateToString(Date date, String format){
		DateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

    public static Date stringToDate(String dateStr, String format){
        Date date = new Date();
        // 注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

	public static Date getAfterMonths(int afterMouth) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, afterMouth);
		Date afterDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getDate(dateFormat.format(afterDate));
	}
	
	public static Date getAfterDays(int afterDays) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, afterDays);
		Date afterDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getDate(dateFormat.format(afterDate));
	}

	public static Date getAfterDays(Date date, int afterDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, afterDays);
		Date afterDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return getDate(dateFormat.format(afterDate));
	}

    public static Date getBeforeHour(int before) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, -before);
        Date afterDate = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getDate(dateFormat.format(afterDate));
    }

    public static Date getBeforeMinute(int before) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -before);
        Date afterDate = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getDate(dateFormat.format(afterDate));
    }
	
	/**
     * 生成随机时间
     * @param beginDate
     * @param endDate
     * @return
     */
	public static Date randomDate(String beginDate, String endDate) {
		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = random(start.getTime(), end.getTime());
			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long random(long begin, long end) {
		long rtn = begin + (long) (Math.random() * (end - begin));
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) {
			return random(begin, end);
		}
		return rtn;
	}
	
	/**
	 * 当前日期加一天
	 * @param
	 * @return
	 */
	public static Date getCurrentNextDay(Date CurrentDate){
		
		Calendar c = Calendar.getInstance();
		c.setTime(CurrentDate);
		c.add(Calendar.DATE, 1);
		Date date = c.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");// 设置日期格式
		Date startTime = null;
		try {
			startTime = df.parse(df.format(date));
		} catch (Exception e) {
			
		}
		return startTime;
	}

    public static Date dateModified(Date date, int num , String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, num);
        Date resultDate = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
        Date startTime = null;
        try {
            startTime = df.parse(df.format(resultDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return startTime;
    }

	/**
	 * 获取当前时间
	 * @return String
	 */
	public static String getCurrentTime() {
		Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateString = formatter.format(currentTime);
	    return dateString;
	}

    public static Date[] getWeekStartAndEndDate (Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int dayInWeek = cal.get(Calendar.DAY_OF_WEEK);

        int offset;
        if (dayInWeek == 1) {
            offset = 6;
        } else {
            offset = dayInWeek - 2;
        }

        Date [] dates = new Date[2];
        cal.add(GregorianCalendar.DAY_OF_MONTH, -offset);
        dates[0] = cal.getTime();

        cal.add(GregorianCalendar.DAY_OF_MONTH, 6);
        dates[1] = cal.getTime();
        return dates;
    }

    public static Date[] getMonthStartAndEndDate(Date date) {

        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天

        Date[] dates = new Date[2];
        dates[0] = c.getTime();

        c.setTime(date);
        //获取当前月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        dates[1] = c.getTime();
        return dates;
    }

    public static Date getLastReplyDate() {

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, -365); // 当前日期减2天
        Date compareDate = c.getTime();
        return compareDate;
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess=true;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess=false;
        }
        return convertSuccess;

    }

	/**
	 * 时区计算
	 * @param srcDate
	 * @param srcTimeZone eg:"GMT+8:00"
	 * @param toTimeZone
     * @return
     */
	public static Date getTimeWithZone(Date srcDate, String srcTimeZone, String toTimeZone){
		int diffTime = TimeZone.getTimeZone(srcTimeZone).getRawOffset()
				- TimeZone.getTimeZone(toTimeZone).getRawOffset();
		long nowTime = srcDate.getTime();
		long newNowTime = nowTime - diffTime;
		srcDate = new Date(newNowTime);
		return srcDate;
	}
}
























