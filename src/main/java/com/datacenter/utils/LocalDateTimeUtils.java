package com.datacenter.utils;

/**
 * @author pc
 * @date Create in  2023/2/24
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalDateTimeUtils {
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_PATTERN_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_PATTERN_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_PATTERN_MONTH = "yyyy-MM";
    public static final String DATE_FORMAT_PATTERN_YEAR = "yyyy";
    public static final String DATE_FORMAT_PATTERN_HOUR_MINUTE = "HH:mm";
    public static final String DATE_FORMAT_PATTERN_NUMBER = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_PATTERN_NUMBER_NOS = "yyyyMMddHHmm";
    public static final String DATE_FORMAT_PATTERN_DAY_NOS = "yyyyMMdd";
    private static final int PATTERN_CACHE_SIZE = 500;
    private static final ConcurrentMap<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap();

    public LocalDateTimeUtils() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String getTimeStrNow() {
        return format(LocalDateTime.now(), (String)null);
    }

    public static LocalDateTime plusDayForToday(long day) {
        return LocalDateTime.now().plusDays(day);
    }

    public static LocalDateTime getCustomizeLastTime(LocalDateTime localDateTime) {
        return localDateTime.with(LocalTime.MAX);
    }

    public static LocalDateTime getCustomizeBeginTime(LocalDateTime localDateTime) {
        return localDateTime.with(LocalTime.MIN);
    }

    public static long getTodayLastMilli() {
        return LocalDateTime.now().with(LocalTime.MAX).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static LocalDateTime parse(String text) {
        return parse(text, (String)null);
    }

    public static LocalDateTime parse(String text, String pattern) {
        return parse(text, pattern, true);
    }

    public static LocalDateTime parse(String text, String pattern, boolean defaultMinFlag) {
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }

        DateTimeFormatter formatter = createCacheFormatter(pattern);
        if (formatter == null) {
            throw new NullPointerException("DateTimeFormatter is NULL");
        } else {
            TemporalAccessor accessor = formatter.parse(text);
            return accessor instanceof LocalDate ? ((LocalDate)accessor).atStartOfDay() : LocalDateTime.of(getFieldDefault(accessor, ChronoField.YEAR, defaultMinFlag), getFieldDefault(accessor, ChronoField.MONTH_OF_YEAR, defaultMinFlag), getFieldDefault(accessor, ChronoField.DAY_OF_MONTH, defaultMinFlag), getFieldDefault(accessor, ChronoField.HOUR_OF_DAY, defaultMinFlag), getFieldDefault(accessor, ChronoField.MINUTE_OF_HOUR, defaultMinFlag), getFieldDefault(accessor, ChronoField.SECOND_OF_MINUTE, defaultMinFlag), getFieldDefault(accessor, ChronoField.NANO_OF_SECOND, defaultMinFlag));
        }
    }

    public static LocalDate parseDate(String text) {
        return parseDate(text, (String)null);
    }

    public static LocalDate parseDate(String text, String pattern) {
        return parseDate(text, pattern, true);
    }

    public static LocalDate parseDate(String text, String pattern, boolean defaultMinFlag) {
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd";
        }

        DateTimeFormatter formatter = createCacheFormatter(pattern);
        if (formatter == null) {
            throw new NullPointerException("DateTimeFormatter is NULL");
        } else {
            TemporalAccessor accessor = formatter.parse(text);
            return accessor instanceof LocalDateTime ? ((LocalDateTime)accessor).toLocalDate() : LocalDate.of(getFieldDefault(accessor, ChronoField.YEAR, defaultMinFlag), getFieldDefault(accessor, ChronoField.MONTH_OF_YEAR, defaultMinFlag), getFieldDefault(accessor, ChronoField.DAY_OF_MONTH, defaultMinFlag));
        }
    }

    public static int getFieldDefault(TemporalAccessor temporalAccessor, TemporalField field, boolean defaultMinFlag) {
        if (temporalAccessor.isSupported(field)) {
            return temporalAccessor.get(field);
        } else if (defaultMinFlag) {
            return (int)field.range().getMinimum();
        } else {
            return field == ChronoField.DAY_OF_MONTH && temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) ? LocalDate.of(temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR), 1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth() : (int)field.range().getMaximum();
        }
    }

    public static String format(LocalDateTime time) {
        return format(time, (String)null);
    }

    public static String formatDate(LocalDate time) {
        return formatDate(time, (String)null);
    }

    public static String formatDate(LocalDate time, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            pattern = "yyyy-MM-dd";
        }

        return format(time, pattern);
    }

    public static String format(TemporalAccessor time, String pattern) {
        if (null == time) {
            return null;
        } else {
            if (pattern == null || pattern.isEmpty()) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            DateTimeFormatter formatter = createCacheFormatter(pattern);
            if (formatter == null) {
                throw new NullPointerException("DateTimeFormatter is NULL");
            } else {
                try {
                    return formatter.format(time);
                } catch (UnsupportedTemporalTypeException var4) {
                    if (time instanceof LocalDate && var4.getMessage().contains("HourOfDay")) {
                        return formatter.format(((LocalDate)time).atStartOfDay());
                    } else if (time instanceof LocalTime && var4.getMessage().contains("YearOfEra")) {
                        return formatter.format(((LocalTime)time).atDate(LocalDate.now()));
                    } else if (time instanceof Instant) {
                        return formatter.format(((Instant)time).atZone(ZoneId.systemDefault()));
                    } else {
                        throw var4;
                    }
                }
            }
        }
    }

    public static String parseString(String text, String pattern1, String pattern2) {
        return format(parse(text, pattern1), pattern2);
    }

    public static String parseStringMax(String text, String pattern1, String pattern2) {
        return format(parse(text, pattern1, false), pattern2);
    }

    public static long parseLong(TemporalAccessor time) {
        if (time == null) {
            return 0L;
        } else {
            return time instanceof LocalDate ? ((LocalDate)time).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() : ((LocalDateTime)time).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }

    public static LocalDateTime formatLong(long timestamp) {
        return timestamp <= 0L ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }

    public static LocalDate formatLongDate(long timestamp) {
        return timestamp <= 0L ? null : Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static long diffDays(LocalDateTime first, LocalDateTime second) {
        return duration(first, second).toDays();
    }

    public static long diffHours(LocalDateTime first, LocalDateTime second) {
        return duration(first, second).toHours();
    }

    public static long diffMinute(LocalDateTime first, LocalDateTime second) {
        return duration(first, second).toMinutes();
    }

    public static long diffMillis(LocalDateTime first, LocalDateTime second) {
        return duration(first, second).toMillis();
    }

    private static Duration duration(LocalDateTime first, LocalDateTime second) {
        return Duration.between(first, second);
    }

    public static long compareToDateTime(String t1, String t2, String pattern) {
        return (long)parse(t1, pattern).compareTo(parse(t2, pattern));
    }

    public static String friendlyTime(long ms) {
        if (ms == 0L) {
            return "0秒";
        } else if (ms < 1000L) {
            return "1秒";
        } else {
            long seconds = ms % 60000L / 1000L;
            if (ms < 60000L) {
                return seconds + "秒";
            } else {
                long minutes = ms % 3600000L / 60000L;
                if (ms < 3600000L) {
                    return minutes + "分" + seconds + "秒";
                } else {
                    long hours = ms % 86400000L / 3600000L;
                    if (ms < 86400000L) {
                        return hours + "小时" + minutes + "分" + seconds + "秒";
                    } else {
                        long days = ms / 86400000L;
                        return days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
                    }
                }
            }
        }
    }

    private static DateTimeFormatter createCacheFormatter(String pattern) {
        if (pattern != null && pattern.length() != 0) {
            DateTimeFormatter formatter = (DateTimeFormatter)FORMATTER_CACHE.get(pattern);
            if (formatter == null && FORMATTER_CACHE.size() < 500) {
                formatter = DateTimeFormatter.ofPattern(pattern);
                DateTimeFormatter oldFormatter = (DateTimeFormatter)FORMATTER_CACHE.putIfAbsent(pattern, formatter);
                if (oldFormatter != null) {
                    formatter = oldFormatter;
                }
            }

            return formatter;
        } else {
            throw new IllegalArgumentException("Invalid pattern specification");
        }
    }
}
