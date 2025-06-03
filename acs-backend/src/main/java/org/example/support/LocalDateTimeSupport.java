package org.example.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.MinguoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.util.Calendar;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple3;

@Slf4j
public final class LocalDateTimeSupport {
	
	public static String formatTime(String timestamp, int index) {

		String splitToken = " ";
		if(timestamp.indexOf(" ") != -1) {
			splitToken = " ";
		}else if(timestamp.indexOf("T") != -1) {
			splitToken = "T";
		}
		String[] timestampArray = timestamp.split(splitToken);
		String date = timestampArray[0];
		
		String time = "";
		if(timestampArray.length == 2) {
			time = timestampArray[1];
		}
		
		String splitDateToken = "-";
		if(date.indexOf("-") != -1) {
			splitDateToken = "-";
		}else if(date.indexOf("/") != -1) {
			splitDateToken = "/";
		}
		String[] dateArray = date.split(splitDateToken);
		String year = "";
		String month = "";
		String day = "";
		if(dateArray.length == 1) {
			year = dateArray[0];
		}else if(dateArray.length == 2) {
			year = dateArray[0];
			month = dateArray[1];
		}else if(dateArray.length == 3) {
			year = dateArray[0];
			month = dateArray[1];
			day = dateArray[2];
		}
		
		if(month == null || month.isBlank()) {
			month = "01";
		}else {
			if(month.length() == 1) {
				month = "0" + month;
			}
		}
		
		if(day == null || day.isBlank()) {
			day = "01";
		}else {
			if(day.length() == 1) {
				day = "0" + day;
			}
		}
		
		String[] timeArray = time.split(":");
		String hour = "00";
		String minute = "00";
		String second = "00";
		if(timeArray.length == 1) {
			hour = timeArray[0];
		}else if(timeArray.length == 2) {
			hour = timeArray[0];
			minute = timeArray[1];
		}else if(timeArray.length == 3) {
			hour = timeArray[0];
			minute = timeArray[1];
			second = timeArray[2];
		}
		
		if(hour == null || hour.isBlank()) {
			hour = "00";
		}else {
			hour = hour.substring(0, 2);
		}
		
		if(minute == null || minute.isBlank()) {
			minute = "00";
		}else {
			minute = minute.substring(0, 2);
		}
		
		if(second == null || second.isBlank()) {
			second = "00";
		}else {
			second = second.substring(0, 2);
		}
		
		date = year  + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
		
		return date;
	}
	
    public static DateTimeFormatter formatter(String timestamp) {
    	DateTimeFormatter formatter;
    	if(timestamp.indexOf("T") != -1) {
    		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    	}else {
    		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	}
    	return formatter;
    }
    
    public static DateTimeFormatter formatterByTW(String timestamp) {
    	DateTimeFormatter formatter;
    	if(timestamp.indexOf("T") != -1) {
    		formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss", Locale.CHINESE);
    	}else {
    		formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
    	}
    	return formatter;
    }


    public static String transfer(String date, String hour, String minute) {
    	
    	date = formatTime(date, 10);
    	date = date.substring(0, 10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDate localDate = LocalDate.parse(date, formatter);
        if(hour == null) {
        	hour = "0";
        }
        if(minute == null) {
        	minute = "0";
        }
        LocalDateTime localDateTime = localDate.atTime(Integer.valueOf(hour), Integer.valueOf(minute));
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public static String parse(String timestamp) {
		try {
			if (timestamp != null && !timestamp.isBlank()) {
				timestamp = formatTime(timestamp, 19);
				DateTimeFormatter formatter = formatter(timestamp);

				LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);
				return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
			}
		}catch (Exception ex) {
//			Log.error(ex);
		}
		return "";
    }

    public static Tuple3<String, String, String> decompose(String timestamp) {
    	
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH");
        DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("mm");
        
        timestamp = formatTime(timestamp, 19);
        DateTimeFormatter formatter = formatter(timestamp);
        
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);

        return Tuple.tuple(localDateTime.format(dateFormatter),
                localDateTime.format(hourFormatter),
                localDateTime.format(minuteFormatter));
    }

    public static String getTW(String timestamp) {
    	
    	timestamp = formatTime(timestamp, 19);
    	DateTimeFormatter formatter = formatterByTW(timestamp);
    	
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);

        Chronology chrono = MinguoChronology.INSTANCE;
        DateTimeFormatter df = new DateTimeFormatterBuilder().parseLenient()
                .appendPattern("yyy-MM-dd").toFormatter().withChronology(chrono)
                .withDecimalStyle(DecimalStyle.of(Locale.getDefault()));

        return localDateTime.format(df);
    }

    public static String getDate(String timestamp) {
    	
    	timestamp = formatTime(timestamp, 10);
    	DateTimeFormatter formatter = formatter(timestamp);
    	
    	LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);
    	
        if (localDateTime != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return localDateTime.format(dateFormatter);
        } else {
            return "";
        }
    }
    
    /**
     * 比較第一個日期是否大於第二個日期
     *
     * @param firstDateTime  第一個日期
     * @param secondDateTime 第二個日期
     * @return true-大於;false-不大於
     */
    public static boolean localDateTimeIsBefore(String firstDateTime, String secondDateTime) {
    	try {
			firstDateTime = formatTime(firstDateTime, 19);
			DateTimeFormatter formatterFirst = formatter(firstDateTime);
			secondDateTime = formatTime(secondDateTime, 19);
			DateTimeFormatter formatterSencode = formatter(secondDateTime);

			LocalDateTime localDateTimeBefore = LocalDateTime.parse(firstDateTime, formatterFirst);
			LocalDateTime localDateTimeAfter = LocalDateTime.parse(secondDateTime, formatterSencode);

			return localDateTimeBefore.isBefore(localDateTimeAfter);
		}catch (Exception ex){
//			Log.error(ex);
		}
		return false;
	}

    /**
     * 比較第一個日期是否小於第二個日期
     *
     * @param firstDateTime  第一個日期
     * @param secondDateTime 第二個日期
     * @return true-小於;false-大於
     */
    public static boolean localDateTimeIsAfter(String firstDateTime, String secondDateTime) {
    	
    	firstDateTime = formatTime(firstDateTime, 19);
    	DateTimeFormatter formatterFirst = formatter(firstDateTime);
    	secondDateTime = formatTime(secondDateTime, 19);
    	DateTimeFormatter formatterSencode = formatter(secondDateTime);
    	
        LocalDateTime localDateTimeBefore = LocalDateTime.parse(firstDateTime, formatterFirst);
        LocalDateTime localDateTimeAfter = LocalDateTime.parse(secondDateTime, formatterSencode);
    	
        return localDateTimeBefore.isAfter(localDateTimeAfter);
    }

    /**
     * 比較兩個日期是否相等
     *
     * @param firstDateTime  第一個日期
     * @param secondDateTime 第二個日期
     * @return true-相等;false-不相等
     */
    public static boolean localDateTimeIsEqual(String firstDateTime, String secondDateTime) {
    	
    	firstDateTime = formatTime(firstDateTime, 19);
    	DateTimeFormatter formatterFirst = formatter(firstDateTime);
    	secondDateTime = formatTime(secondDateTime, 19);
    	DateTimeFormatter formatterSencode = formatter(secondDateTime);
    	
        LocalDateTime localDateTimeBefore = LocalDateTime.parse(firstDateTime, formatterFirst);
        LocalDateTime localDateTimeAfter = LocalDateTime.parse(secondDateTime, formatterSencode);
    	
        return localDateTimeBefore.isEqual(localDateTimeAfter);
    }

    public static String convertTWDate(String AD, String beforeFormat, String afterFormat) throws ParseException {
        if (AD == null) return "";
        SimpleDateFormat df4 = new SimpleDateFormat(beforeFormat);
        SimpleDateFormat df2 = new SimpleDateFormat(afterFormat);
        Calendar cal = Calendar.getInstance();
       
        //ParseException
        cal.setTime(df4.parse(AD));
        if (cal.get(Calendar.YEAR) > 1492) cal.add(Calendar.YEAR, -1911);
        else cal.add(Calendar.YEAR, +1911);
        return df2.format(cal.getTime());

    }
    
    /**
	 * Transfer minguo date to AD date.
	 * 民國年 yyyMMdd 轉 西元年 yyyyMMdd
	 *
	 * @param dateString the String dateString
	 * @return the string
	 */
	public static String transferMinguoDateToADDate(String dateString) {
		Chronology chrono = MinguoChronology.INSTANCE;
		DateTimeFormatter df = new DateTimeFormatterBuilder().parseLenient()
		        .appendPattern("yyy-MM-dd")
		        .toFormatter()
		        .withChronology(chrono)
		        .withDecimalStyle(DecimalStyle.of(Locale.getDefault()));

		ChronoLocalDate chDate = chrono.date(df.parse(dateString));
		return LocalDate.from(chDate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

    private LocalDateTimeSupport() {
        super();
    }
    

}
