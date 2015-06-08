package com.syw.weiyu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * author: songyouwei
 * date: 2015-06-07
 * desc:
 */
public class TimeUtil {

    /**
     * 拿时间差，xx{时间单位}前
     * @param largerTime
     * @param smallerTime
     * @return
     */
    public static String timeDiff(long largerTime, long smallerTime) {
        long l = largerTime-smallerTime;
        long day=l/(24*60*60*1000);
        long hour=(l/(60*60*1000)-day*24);
        long min=((l/(60*1000))-day*24*60-hour*60);
        long s=(l/1000-day*24*60*60-hour*60*60-min*60);
//        System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
        if (day!=0) return day+"天前";
        else if (hour!=0) return hour+"小时前";
        else if (min!=0) return min+"分钟前";
        else return "刚刚";
    }

    public static long parse2Timestamp(String formattedTimeStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            return dateFormat.parse(formattedTimeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
