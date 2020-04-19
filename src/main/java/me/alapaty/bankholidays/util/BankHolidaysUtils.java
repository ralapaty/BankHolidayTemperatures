package me.alapaty.bankholidays.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BankHolidaysUtils {
    private static Logger log = LoggerFactory.getLogger(BankHolidaysUtils.class);
    private static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static Date parseDate(String strDate) {
        try {
            Date date = new SimpleDateFormat(YYYY_MM_DD).parse(strDate);
            return date;
        } catch (ParseException ex) {
            log.error("Error parsing Date {} {}", strDate, ex);
        }
        return null;
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(YYYY_MM_DD).format(date);
    }
}
