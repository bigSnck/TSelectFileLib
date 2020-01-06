package com.yt.tselectlibrary.ui.util;


import java.text.SimpleDateFormat;

public class DateUtils {
    private static SimpleDateFormat msFormat = new SimpleDateFormat("mm:ss");

    public DateUtils() {
    }

    public static String timeParse(long duration) {
        String time = "";
        if (duration > 1000L) {
            time = timeParseMinute(duration);
        } else {
            long minute = duration / 60000L;
            long seconds = duration % 60000L;
            long second = (long) Math.round((float) seconds / 1000.0F);
            if (minute < 10L) {
                time = time + "0";
            }

            time = time + minute + ":";
            if (second < 10L) {
                time = time + "0";
            }

            time = time + second;
        }

        return time;
    }

    public static String timeParseMinute(long duration) {
        try {
            return msFormat.format(duration);
        } catch (Exception var3) {
            var3.printStackTrace();
            return "0:00";
        }
    }

    public static int dateDiffer(long d) {
        try {
            long l1 = Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10));
            long interval = l1 - d;
            return (int) Math.abs(interval);
        } catch (Exception var6) {
            var6.printStackTrace();
            return -1;
        }
    }

    public static String cdTime(long sTime, long eTime) {
        long diff = eTime - sTime;
        return diff > 1000L ? diff / 1000L + "秒" : diff + "毫秒";
    }
}