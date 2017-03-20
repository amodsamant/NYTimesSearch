package com.nytimessearch.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    static DateFormat mmDDYYYYDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");

   public static String getYYYYMMddFormatDate(String dateStr) {

        Date date = null;
        try {
            date = mmDDYYYYDateFormat.parse(dateStr);
            return yyyyMMddFormat.format(date);
        } catch (ParseException e) {
            // If this is unable to parse then no date will be shown
        }
        return null;
    }

    /**
     * Function generates the required string the the format MM/dd/yyyy
     * @param selectedYear
     * @param selectedDay
     * @param selectedMonth
     * @return
     */
    public static String createBeginDateString (
            int selectedYear, int selectedDay, int selectedMonth) {

        String year = String.valueOf(selectedYear);
        String month = String.valueOf(selectedMonth + 1);
        String day = String.valueOf(selectedDay);

        StringBuilder builder = new StringBuilder();
        builder.append(month).append("/")
                .append(day).append("/")
                .append(year);

        return builder.toString();
    }
}
