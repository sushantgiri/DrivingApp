package com.driving_app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_FORMAT = "HH:mm";

    private DateUtils(){

    }

    public static long getCurrentDate(){
//        return new Date().getTime();
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime();
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public static String getCurrentTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        return sdf.format(cal.getTime());
    }

    public static Calendar getCalendar(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);
//        return
    }

    public static String getCalendarDate(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Calendar getCalendarFromSelectedDay(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar;
    }

    public static String getDate(Calendar calendar, Calendar selectedCalendarTime){
        if(selectedCalendarTime != null){
            calendar.set(Calendar.HOUR_OF_DAY, selectedCalendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, selectedCalendarTime.get(Calendar.MINUTE));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_NOW);
        return simpleDateFormat.format(calendar.getTime());
    }

}
