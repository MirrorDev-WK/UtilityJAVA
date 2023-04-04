package th.co.ncr.connector.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import th.co.ncr.connector.config.Constants;


public class DateUtil {
	private static Integer offsetMs = 0;

	public static void setOffsetMs(Integer ms) {
		offsetMs = ms;
	}

	public static Date currDate() {
		Calendar cal = Calendar.getInstance(Constants.DEFAULT_LOCALE);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.add(Calendar.MILLISECOND, -offsetMs);
		return cal.getTime();
	}

	public static Date currTime() {
		Calendar cal = Calendar.getInstance(Constants.DEFAULT_LOCALE);
		cal.add(Calendar.MILLISECOND, -offsetMs);
		return cal.getTime();
	}
	
	public static Calendar currTimeC(){
        return Calendar.getInstance(Constants.DEFAULT_LOCALE);
    }
	
	public static Date timePlusSec(Date d, int sec) {
		Calendar cal = Calendar.getInstance(Constants.DEFAULT_LOCALE);
		cal.setTime(d);
		cal.add(Calendar.SECOND, sec);
		return cal.getTime();
	}

	public static Date currDatePlusSec(int sec) {
		Calendar cal = Calendar.getInstance(Constants.DEFAULT_LOCALE);
		cal.add(Calendar.SECOND, sec);
		cal.add(Calendar.MILLISECOND, -offsetMs);
		return cal.getTime();
	}

	public static Date currDatePlusDay(int day) {
		final Calendar cal = Calendar.getInstance(Constants.DEFAULT_LOCALE);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	public static Date datePlusSec(final Date date, int sec) {
		Calendar cal = Calendar.getInstance(Constants.DEFAULT_LOCALE);
		cal.setTime(date);
		cal.add(Calendar.SECOND, sec);
		return cal.getTime();
	}
	
	public static Date datePlusDay(final Date date, int day) {
		Calendar cal = Calendar.getInstance(th.co.ncr.connector.config.Constants.DEFAULT_LOCALE);
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	public static String dateToDateStr(final Date date, String format) {
		DateFormat df = new SimpleDateFormat(format, Constants.DEFAULT_LOCALE);
		return df.format(date);
	}

	public static Date dateStrToDate(final String dateStr, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format, Constants.DEFAULT_LOCALE);
		return df.parse(dateStr);
	}

	public static Date BEtoAD(final Date be) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(be);
		
		if (calendar.get(Calendar.YEAR) - 2000 >= 543) {
			return convertYear(be, -543);
		} else {
			return be;
		}
	}

	public static Date ADtoBE(final Date ad) {
		return convertYear(ad, 543);
	}

	public static Date convertYear(Date date, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		return new Date(cal.getTimeInMillis());
	}
	
	public static Timestamp currentSQLDatetime() {
		Calendar cal = Calendar.getInstance(); 
		Timestamp dateSql = new Timestamp(cal.getTimeInMillis());
		return dateSql;
	}
	
	public static Timestamp currentSQLDate() {
		Calendar cal = Calendar.getInstance(Constants.DEFAULT_LOCALE);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.add(Calendar.MILLISECOND, 0);
		
		Timestamp dateSql = new Timestamp(cal.getTimeInMillis());
		return dateSql;
	}
}
