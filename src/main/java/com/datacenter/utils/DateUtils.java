package com.datacenter.utils;

/**
 * @author pc
 * @date Create in  2023/2/23
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.google.common.collect.Maps;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final FastDateFormat DATE_FORMAT;
    public static final FastDateFormat TIME_FORMAT;
    public static final FastDateFormat DATE_TIME_FORMAT;
    public static Long DAY_MSEC_NUM;
    public static final Integer SHORT;

    public DateUtils() {
    }

    public static String getCurrentDatePath() {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        String monthStr = month > 9 ? String.valueOf(month) : "0" + month;
        int day = now.getDayOfMonth();
        String dayStr = day > 9 ? String.valueOf(day) : "0" + day;
        return StringUtils.joinWith("/", new Object[]{now.getYear(), monthStr, dayStr});
    }

    public static boolean validateDate(String date, String pattern) {
        if (StringUtils.isNotBlank(date)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            try {
                Date descDate = dateFormat.parse(date);
                return date.equals(dateFormat.format(descDate));
            } catch (Exception var4) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Map<String, Object> convertDateToObj(Date date) {
        LocalDateTime time = toLocalDateTime(date);
        Map<String, Object> obj = Maps.newHashMap();
        obj.put("date", time.getDayOfMonth());
        obj.put("hours", time.getHour());
        obj.put("seconds", time.getSecond());
        obj.put("month", time.getMonthValue());
        obj.put("year", time.getYear() - 1900);
        obj.put("minutes", time.getMinute());
        obj.put("time", date.getTime());
        obj.put("day", time.getDayOfWeek().getValue());
        return obj;
    }

    public static boolean before(Date date, Date compareDate) {
        return date != null && compareDate != null ? date.before(compareDate) : false;
    }

    public static boolean after(Date date, Date compareDate) {
        return date != null && compareDate != null ? date.after(compareDate) : false;
    }

    public static boolean between(Date date, Date startDate, Date endDate) {
        if (date != null && startDate != null && endDate != null) {
            return before(date, endDate) && after(date, startDate);
        } else {
            return false;
        }
    }

    public static int getCurrSecondTimestamp() {
        return (int)(System.currentTimeMillis() / 1000L);
    }

    public static FastDateFormat getDateFormat(String pattern) {
        return pattern == null ? null : FastDateFormat.getInstance(pattern);
    }

    public static String format() {
        return format("yyyy-MM-dd HH:mm:ss");
    }

    public static String formatInMillis() {
        return format("yyyy-MM-dd HH:mm:ss:SSS");
    }

    public static String format(String pattern) {
        return format(new Date(), pattern);
    }

    public static String formatWithNull(Date date) {
        return date == null ? null : format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Object date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date, String pattern) {
        return getDateFormat(pattern).format(date);
    }

    public static String format(String date, String srcPattern, String targetPattern) {
        FastDateFormat dateFormat = getDateFormat(srcPattern);

        try {
            Date formatDate = dateFormat.parse(date);
            return getDateFormat(targetPattern).format(formatDate);
        } catch (ParseException var5) {
            throw new RuntimeException(var5);
        }
    }

    public static String format(Object date, String pattern) {
        return getDateFormat(pattern).format(date);
    }

    public static String formatYmd(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String formatDate(Date date, String pattern) {
        return format(date, pattern);
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String formatCurrentDate(String pattern) {
        return format(pattern);
    }

    public static Date parse(String dateTime) {
        return parse(dateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(String date) {
        return parse(date, "yyyy-MM-dd");
    }

    public static Date parse(String date, String pattern) {
        if (StringUtils.isEmpty(date)) {
            return null;
        } else {
            try {
                return FastDateFormat.getInstance(pattern).parse(date);
            } catch (ParseException var3) {
                throw new RuntimeException("转换" + date + "错误:" + var3.getMessage());
            }
        }
    }

    public static String formatYesterday(String pattern) {
        return formatNumberDate(pattern, 1);
    }

    public static String formatNumberDate(String pattern, int number) {
        Date d = new Date();
        return formatNumberDate(pattern, d, number);
    }

    public static String formatNumberDate(String pattern, Date d, int number) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(5, -number);
        Date date = calendar.getTime();
        return FastDateFormat.getInstance(pattern).format(date);
    }

    public static Date getYesterday() {
        Date d = new Date();
        return getYesterday(d);
    }

    public static Date getYesterday(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(5, -1);
        return calendar.getTime();
    }

    public static Date getNextDay() {
        Date d = new Date();
        return getNextDay(d);
    }

    public static Date getNextDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(5, 1);
        return calendar.getTime();
    }

    public static Date getDateNoTime(Date d) {
        String s = format(d, "yyyy-MM-dd");
        return parse(s, "yyyy-MM-dd");
    }

    public static Date[] genDateArea(int dayNumber) {
        Date d = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(5, -dayNumber);
        Date date = calendar.getTime();
        return new Date[]{date, d};
    }

    public static String formatNextDate(String date, String pattern) {
        FastDateFormat fdf = FastDateFormat.getInstance(pattern);

        try {
            Date date1 = fdf.parse(date);
            Date date2 = new Date(date1.getTime() - DAY_MSEC_NUM);
            return fdf.format(date2);
        } catch (ParseException var5) {
            return formatYesterday(pattern);
        }
    }

    public static String formatNumberDate(String dateStr, String pattern, int number) {
        Date d = convertStringToDate(dateStr, pattern);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(5, -number);
        Date date = calendar.getTime();
        return FastDateFormat.getInstance(pattern).format(date);
    }

    public static Date formatTimeToCurrentDate(String time, String pattern) {
        FastDateFormat fdf = FastDateFormat.getInstance(pattern);

        try {
            return fdf.parse(DatePattern.yyyyMMdd_HYPHEN_FORMAT.format(new Date()) + " " + time);
        } catch (ParseException var4) {
            return new Date();
        }
    }

    public static String formatFirstDayOfCurrentMonth(String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(5, 1);
        return FastDateFormat.getInstance(pattern).format(calendar.getTime());
    }

    public static String formatLastDayOfCurrentMonth(String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, 1);
        calendar.set(5, 0);
        return FastDateFormat.getInstance(pattern).format(calendar.getTime());
    }

    public static Date convertStringToDate(String date, String pattern) {
        try {
            return FastDateFormat.getInstance(pattern).parse(date);
        } catch (ParseException var3) {
            return new Date();
        }
    }

    public static long computeByDaysWithTime(Date start, Date end) {
        return Math.abs((start.getTime() - end.getTime()) / DAY_MSEC_NUM);
    }

    public static long computeByDays(Date start, Date end) {
        try {
            start = DatePattern.yyyyMMdd_HYPHEN_FORMAT.parse(DatePattern.yyyyMMdd_HYPHEN_FORMAT.format(start));
            end = DatePattern.yyyyMMdd_HYPHEN_FORMAT.parse(DatePattern.yyyyMMdd_HYPHEN_FORMAT.format(end));
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            long time1 = cal.getTimeInMillis();
            cal.setTime(end);
            long time2 = cal.getTimeInMillis();
            return Math.abs(time2 - time1) / DAY_MSEC_NUM;
        } catch (Throwable var7) {
            throw new RuntimeException(var7);
        }
    }

    public static long computeTomorrowZeroClockByMinute(Date date) {
        String cDateStr = formatNumberDate("yyyy-MM-dd", -1);
        Date tomorrow = convertStringToDate(cDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        return (date.getTime() - tomorrow.getTime()) / 60000L;
    }

    public static long computeTodayZeroClockByMinute(Date date) {
        String cDateStr = formatCurrentDate("yyyy-MM-dd");
        Date today = convertStringToDate(cDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        return (date.getTime() - today.getTime()) / 60000L;
    }

    public static long computeByYesterdayMinute(Date date) {
        String cDateStr = formatYesterday("yyyy-MM-dd");
        Date yestaryDate = convertStringToDate(cDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        return (date.getTime() - yestaryDate.getTime()) / 60000L;
    }

    public static long compareToZeroClockByMinute(Date date, int number) {
        String cDateStr = formatNumberDate("yyyy-MM-dd", number);
        Date dayDate = convertStringToDate(cDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        return (date.getTime() - dayDate.getTime()) / 60000L;
    }

    public static long computeByMinute(Date start, Date end) {
        return (start.getTime() - end.getTime()) / 60000L;
    }

    public static long computeBySecond(Date start, Date end) {
        return (start.getTime() - end.getTime()) / 1000L;
    }

    public static long computeByMonths(Date start, Date end) {
        long result = 0L;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(start);
        c2.setTime(end);
        result = (long)((c2.get(1) - c1.get(1)) * 12);
        result += (long)(c2.get(2) - c1.get(2));
        return Math.abs(result);
    }

    public static Boolean isTwoYearsAgoDate(Date endTime) {
        Date endDate = null;
        Date twoYearsAgoDate = null;

        try {
            if (endTime == null) {
                return false;
            }

            endDate = DatePattern.yyyyMMdd_HYPHEN_FORMAT.parse(DatePattern.yyyyMMdd_HYPHEN_FORMAT.format(endTime));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(1, -2);
            twoYearsAgoDate = calendar.getTime();
        } catch (ParseException var4) {
        }

        return endDate.after(twoYearsAgoDate);
    }

    public static boolean isEmpty(Date date) {
        return date == null || date.getTime() == 0L;
    }

    public static Map<Integer, String> getMonthMap() {
        Map<Integer, String> map = new HashMap();
        map.put(1, "01/01:01/31");
        map.put(2, "02/01:02/28");
        map.put(3, "03/01:03/31");
        map.put(4, "04/01:04/30");
        map.put(5, "05/01:05/31");
        map.put(6, "06/01:06/30");
        map.put(7, "07/01:07/31");
        map.put(8, "08/01:08/31");
        map.put(9, "09/01:09/30");
        map.put(10, "10/01:10/31");
        map.put(11, "11/01:11/30");
        map.put(12, "12/01:12/31");
        return map;
    }

    public static Map<Integer, String> getMonthLabel() {
        Map<Integer, String> map = new HashMap();
        map.put(1, "一月");
        map.put(2, "二月");
        map.put(3, "三月");
        map.put(4, "四月");
        map.put(5, "五月");
        map.put(6, "六月");
        map.put(7, "七月");
        map.put(8, "八月");
        map.put(9, "九月");
        map.put(10, "十月");
        map.put(11, "十一月");
        map.put(12, "十二月");
        return map;
    }

    public static boolean checkValidity(long time, long durInHours) {
        if (durInHours <= 0L) {
            return false;
        } else {
            long hours = (System.currentTimeMillis() - time) / 3600000L;
            return durInHours >= hours;
        }
    }

    public static Date formatTimeStamp(String timestamp) {
        Long timestampL = Long.valueOf(timestamp);
        return new Date(timestampL * 1000L);
    }

    public static String formatTimeStamp(Date d) {
        return d != null ? String.valueOf(d.getTime()) : "";
    }

    public static Date genCurrentMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        int day_of_week = cal.get(7) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }

        cal.add(5, -day_of_week + 1);
        return cal.getTime();
    }

    public static String genCurrentMondayStr(Date date) {
        return formatDate(genCurrentMonday(date), "yyyy-MM-dd") + " 00:00:00";
    }

    public static String genNextMondayStr(Date date) {
        return formatDate(genNextMonday(date), "yyyy-MM-dd") + " 00:00:00";
    }

    public static Date genPreviousMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        int day_of_week = cal.get(7) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }

        cal.add(5, -(day_of_week + 7) + 1);
        return cal.getTime();
    }

    public static String genPreviousMondayStr(Date date) {
        return formatDate(genPreviousMonday(date), "yyyy-MM-dd") + " 00:00:00";
    }

    public static Date genNextMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        do {
            cal.add(5, 1);
        } while(cal.get(7) != 2);

        return cal.getTime();
    }

    public static long computeByWeeks(Date start, Date end) {
        Date nextM_start = genNextMonday(start);
        Date nextM_end = genNextMonday(end);
        long day = computeByDays(nextM_end, nextM_start);
        long week = day / 7L;
        return week;
    }

    public static String getSpecialFormat(Date date) {
        if (date == null) {
            return null;
        } else {
            long tmin = computeTodayZeroClockByMinute(date);
            long ymin = computeByYesterdayMinute(date);
            long qmin = compareToZeroClockByMinute(date, 2);
            long mmin = computeTomorrowZeroClockByMinute(date);
            String time = getHourAndMinute(date);
            if (mmin >= 0L) {
                return "明天 " + time;
            } else if (tmin >= 0L) {
                return "今天 " + time;
            } else if (ymin >= 0L) {
                return "昨天 " + time;
            } else {
                return qmin >= 0L ? "前天 " + time : DateFormat.getDateInstance(3).format(date) + " " + formatDate(date, "HH:mm");
            }
        }
    }

    public static String getHourAndMinute(Date date) {
        return DatePattern.HHmm_COLON_FORMAT.format(date);
    }

    public static Date genLastMonthStartTime(Date date) {
        String startTime = genLastMonthStartTimeStr(new Date());
        return convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date genMonthStartTime(Date date) {
        String startTime = genMonthStartTimeStr(date);
        return convertStringToDate(startTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static String genMonthStartTimeStr(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        int year = cal.get(1);
        int month = cal.get(2) + 1;
        String months = "";
        if (String.valueOf(month).length() <= 1) {
            months = "0" + month;
        } else {
            months = String.valueOf(month);
        }

        return year + "-" + months + "-01 00:00:00";
    }

    public static String genLastMonthStartTimeStr(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        int year = cal.get(1);
        int month = cal.get(2) + 1;
        cal.set(5, 1);
        cal.add(5, -1);
        String months = "";
        if (month > 1) {
            --month;
        } else {
            --year;
            month = 12;
        }

        if (String.valueOf(month).length() <= 1) {
            months = "0" + month;
        } else {
            months = String.valueOf(month);
        }

        return year + "-" + months + "-01 00:00:00";
    }

    public static Date genLastMonthEndTime(Date date) {
        String lastDay = genLastMonthEndTimeStr(date);
        return convertStringToDate(lastDay, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date genMonthEndTime(Date date) {
        String lastDay = genMonthEndTimeStr(date);
        return convertStringToDate(lastDay, "yyyy-MM-dd HH:mm:ss");
    }

    public static String genMonthEndTimeStr(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        int year = cal.get(1);
        int month = cal.get(2) + 1;
        cal.set(5, cal.getActualMaximum(5));
        String months = "";
        if (String.valueOf(month).length() <= 1) {
            months = "0" + month;
        } else {
            months = String.valueOf(month);
        }

        String days = "";
        int day = cal.get(5);
        if (String.valueOf(day).length() <= 1) {
            days = "0" + day;
        } else {
            days = String.valueOf(day);
        }

        return year + "-" + months + "-" + days + " 23:59:59";
    }

    public static String genLastMonthEndTimeStr(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        int year = cal.get(1);
        int month = cal.get(2) + 1;
        cal.set(5, 1);
        cal.add(5, -1);
        int day = cal.get(5);
        String months = "";
        String days = "";
        if (month > 1) {
            --month;
        } else {
            --year;
            month = 12;
        }

        if (String.valueOf(month).length() <= 1) {
            months = "0" + month;
        } else {
            months = String.valueOf(month);
        }

        if (String.valueOf(day).length() <= 1) {
            days = "0" + day;
        } else {
            days = String.valueOf(day);
        }

        return year + "-" + months + "-" + days + " 23:59:59";
    }

    public static int getYearByStrDate(String aggDate) {
        Date date = convertStringToDate(aggDate, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(1);
        return year;
    }

    public static int getMonthByStrDate(String aggDate) {
        Date date = convertStringToDate(aggDate, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(2);
        return month + 1;
    }

    public static int getMonthByStrDate(String aggDate, String formatPattern) {
        Date date = convertStringToDate(aggDate, formatPattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(2);
        return month + 1;
    }

    public static int getWeekOfYearByStrDate(String aggDate) {
        Date date = convertStringToDate(aggDate, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekOfYear = cal.get(3);
        return weekOfYear;
    }

    public static Date clearNoTime(Date sendDate) {
        Calendar calc = Calendar.getInstance();
        calc.setTime(sendDate);
        calc.set(11, 0);
        calc.set(12, 0);
        calc.set(13, 0);
        calc.set(14, 0);
        return calc.getTime();
    }

    public static String genDueTimeStr(Date dueDate, Date dueDateEnd) {
        String dueTime = getSpecialFormat(dueDate);
        String dueTimeEnd = getHourAndMinute(dueDateEnd);
        String dueTimeStr = dueTime + "-" + dueTimeEnd;
        return dueTimeStr;
    }

    public static String formatEndDateString(String cdate) {
        if (cdate != null && cdate.length() > 0 && cdate.length() <= 10) {
            cdate = cdate + " 23:59:59";
            return cdate;
        } else {
            return cdate;
        }
    }

    public static String formatStartDateString(String cdate) {
        if (cdate != null && cdate.length() > 0 && cdate.length() <= 10) {
            cdate = cdate + " 00:00:00";
            return cdate;
        } else {
            return cdate;
        }
    }

    public static Date convertStartDateString(String cdate) {
        cdate = formatStartDateString(cdate);
        return convertStringToDate(cdate, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date convertEndDateString(String cdate) {
        cdate = formatEndDateString(cdate);
        return convertStringToDate(cdate, "yyyy-MM-dd HH:mm:ss");
    }

    public static boolean isValidFormatter(String source, String formatter) {
        try {
            FastDateFormat.getInstance(formatter).parse(source);
            return true;
        } catch (ParseException var3) {
            return false;
        }
    }

    public static boolean isBetween(Date date, Date start, Date end) {
        if (date == null) {
            return false;
        } else {
            boolean result;
            if (start != null) {
                result = date.compareTo(start) >= 0;
            } else {
                result = true;
            }

            if (!result) {
                return false;
            } else {
                if (end != null) {
                    result = date.compareTo(end) <= 0;
                } else {
                    result = true;
                }

                return result;
            }
        }
    }

    public static String getYearStart() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);
        return year + "-01-01";
    }

    public static Date getEmptyDate() {
        Date date = new Date();
        date.setTime(0L);
        return date;
    }

    public static int getCurrYear() {
        Calendar date = Calendar.getInstance();
        return date.get(1);
    }

    public static int getCurrMonth() {
        Calendar date = Calendar.getInstance();
        return date.get(2);
    }

    public static Date addDate(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(6, days);
        cal.set(11, 0);
        cal.set(13, 0);
        cal.set(12, 0);
        return cal.getTime();
    }

    public static String minusMonth(String dateStr, String formatPattern, Integer minusMonth) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        Date date = null;

        try {
            date = format.parse(dateStr);
        } catch (ParseException var7) {
            throw new RuntimeException(var7);
        }

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(2, minusMonth);
        Date resultDate = rightNow.getTime();
        return format(resultDate, formatPattern);
    }

    static {
        DATE_FORMAT = DatePattern.yyyyMMdd_HYPHEN_FORMAT;
        TIME_FORMAT = DatePattern.HHmmss_COLON_FORMAT;
        DATE_TIME_FORMAT = DatePattern.yyyyMMdd_HYPHEN_HHmmss_COLON_FORMAT;
        DAY_MSEC_NUM = 86400000L;
        SHORT = 3;
    }
}
