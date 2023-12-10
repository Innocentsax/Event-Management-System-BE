package dev.Innocent.EventManagementSystem.util;

import java.util.Calendar;

public class TimestampUtil {
    public static int EXPIRATION_MINUTES;
    public static Calendar calculateExpirationTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, EXPIRATION_MINUTES);
        return calendar;
    }

    public static boolean isExpired(Calendar expirationTime){
        Calendar calendar = Calendar.getInstance();
        return calendar.before(expirationTime);
    }
}
