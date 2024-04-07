package com.example.the_tarlords.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DateHelper {
    public static long getTimestamp(String date){

        Calendar cal = Calendar.getInstance();
        int month = getMonth(date);
        int dayOfMonth = getDayOfMonth(date);
        int year = getYear(date);
        cal.set(year,month,dayOfMonth);
        return cal.getTimeInMillis();
    }
    public static int getYear(String date){
        return Integer.valueOf(date.substring(8,12));
    }
    public static int getMonth(String date){
        ArrayList<String> monthNames = new ArrayList<>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        return monthNames.indexOf(date.substring(0,3));
    }
    public static int getDayOfMonth(String date){
        return Integer.valueOf(date.substring(4,6));
    }

}
