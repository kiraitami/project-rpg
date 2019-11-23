package com.example.apprpg.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MyCustomDate implements Serializable {
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    public MyCustomDate() {
    }

    public MyCustomDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public MyCustomDate(Calendar calendar){
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public Calendar myDateToCalendar(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(this.year, this.month, this.day, this.hour, this.minute);
        return calendar;
    }

    public String formatMyDateToString(String pattern){
        Calendar calendar = myDateToCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        simpleDateFormat.setCalendar( calendar );
        return simpleDateFormat.format( calendar.getTime() );
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
