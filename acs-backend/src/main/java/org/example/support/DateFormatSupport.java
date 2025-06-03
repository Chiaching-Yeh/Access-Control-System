package org.example.support;

import java.util.Calendar;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.ParseException;

public final class DateFormatSupport {

	public static String getNowYearByNoSignal() {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(Calendar.getInstance().getTime());
	}
	
    public static String getNowYear() {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		return fmt.format(Calendar.getInstance().getTime());
	}
    
    public static String getNowTime() {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fmt.format(Calendar.getInstance().getTime());
	}
    
    public static String get7Time() {
        final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Calendar c = Calendar.getInstance();
        c.add(5, -7);
        return fmt.format(c.getTime());
    }
    
    /**
     * 日期格式轉為字串
     * fmt 格式範例yyyyMMdd  (20150303) same as SimpleDateFormat format
    */   
    public static String convertDateToStr(Date inputDate, String fmt) {
        return new SimpleDateFormat(fmt).format(inputDate);
    }

     /**
         * 宇串格式轉為日期
         * fmt 格式範例yyyyMMdd  (20150303) same as SimpleDateFormat format
        */   
     
    public static Date convertStrToDate(String inputDateStr, String fmt) throws Exception {
        return new SimpleDateFormat(fmt).parse(inputDateStr);
    }
    
    /**
     * 某年月最後一天YYYYMMDD
     * @param yyyyMm 年月(YYYYMM)
     * @return 該年月最後一天(YYYYMMDD)
    */   
    
    public static String lastDateOfMonth(String yyyyMm) throws Exception {

        Calendar calendar = Calendar.getInstance();
        Date firstDate = convertStrToDate(yyyyMm + "01", "yyyyMMdd");
        calendar.setTime(firstDate);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date lastDayOfMonth = calendar.getTime();

        return convertDateToStr(lastDayOfMonth, "yyyyMMdd");

    }
    
      /*
       *  是否為有效日期
       *  @param text yyyy-MM-dd
       *  @return true - 有效日期 false-非有效日期    *
       */
    public static boolean isValid(String text, String pattern) {
        if (text == null)
            return false;
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        try {
            df.parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }
    
    private DateFormatSupport() {
        super();
    }

}
