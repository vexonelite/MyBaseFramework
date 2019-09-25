package tw.realtime.project.rtbaseframework.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import tw.realtime.project.rtbaseframework.LogWrapper;


public final class TimeUtils {

    /**
     * 更新剩餘時間字串 hh:mm:ss
     * @param timeStamp
     */
    @NonNull
    public static String getTimeString(long timeStamp) {
        final Locale locale = Locale.getDefault();
        final int day = (int) TimeUnit.SECONDS.toDays(timeStamp);
        final int hours = (int)(TimeUnit.SECONDS.toHours(timeStamp) - (day * 24));
        final int minutes = (int)(TimeUnit.SECONDS.toMinutes(timeStamp) - (TimeUnit.SECONDS.toHours(timeStamp) * 60));
        final int seconds = (int)(TimeUnit.SECONDS.toSeconds(timeStamp) - (TimeUnit.SECONDS.toMinutes(timeStamp) * 60));
        //LogWrapper.showLog(Log.WARN, "CodeUtil", "getTimeString - hours: " + hours + ", minutes: " + minutes + ", seconds: " + seconds);
        return  String.format(locale, "%02d", hours) + ":" +
                String.format(locale, "%02d", minutes) + ":" +
                String.format(locale, "%02d", seconds);
    }

    /**
     * The timeStamp typically is a time stamp in the near future.
     * If the time difference between timeStamp and currentTime
     * is less than acceptableDuration, the timeStamp is treated as expired.
     *  @param timeStamp            the time stamp in the near future!
     * @param acceptableDuration    the expected duration between timeStamp and current time
     * @return
     */
    public static boolean isTimeStampExpired(long timeStamp, long acceptableDuration) {
        final long currentTime = System.currentTimeMillis() / 1000L;
        final long timeDifference = timeStamp - currentTime;
        LogWrapper.showLog(Log.INFO, "CodeUtils", "isTimeStampExpired" +
                "\ntimeStamp:          " + timeStamp +
                "\ncurrentTime:        " + currentTime +
                "\nacceptableDuration: " + acceptableDuration +
                "\ntimeDifference:     " + timeDifference);
        return (timeDifference < acceptableDuration);
    }


    /**
     * return a default Calendar where the properties hour, minute, and second are set to '0'
     * @param date The specific date
     * @return a default calendar
     */
    @NonNull
    public static Calendar getTripleZeroCalendar(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    @NonNull
    public static Date getCurrentDateWithZeroSecond() {
        final Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.SECOND, 0);
        return currentCalendar.getTime();
    }

    public static int getTimeDifferenceInMinutesForCalendar(@NonNull Calendar futureTime) {
        return getTimeDifferenceInMinutesForDate(futureTime.getTime());
    }

    //

    public static int getTimeDifferenceInMinutesForDate(@NonNull Date futureTime) {
        final Date currentTime = getCurrentDateWithZeroSecond();
        final boolean isLater = futureTime.after(currentTime);
        LogWrapper.showLog(Log.INFO, "TimeUtils", "futureTime: " + convertDateToString(futureTime, "yyyy/MM/dd - HH:mm:ss"));
        LogWrapper.showLog(Log.INFO, "TimeUtils", "currentTime: " + convertDateToString(currentTime, "yyyy/MM/dd - HH:mm:ss"));
        LogWrapper.showLog(Log.INFO, "TimeUtils", "isLater: " + isLater);
        if (isLater) {
            final long futureTimeStamp = futureTime.getTime();
            final long currentTimeStamp = currentTime.getTime();
            final long timeDifference = futureTimeStamp - currentTimeStamp;
            final int timeDifferenceToInMinutes = timeDifferenceToInMinutes(timeDifference);
            LogWrapper.showLog(Log.INFO, "TimeUtils", "timeDifferenceToInMinutes: " + timeDifferenceToInMinutes + ", timeDifference: " + timeDifference);
            return timeDifferenceToInMinutes;
        } else {
            return -1;
        }
    }

    @Nullable
    public static Date convertStringToDate(@NonNull String dateString, @NonNull String dateFormat) {
        try {
            final String internalDateFormat = (dateFormat.isEmpty()) ? "yyyy-MM-dd hh:mm:ss" : dateFormat;
            final Locale locale = Locale.getDefault();
            //LogWrapper.showLog(Log.INFO, "CodeUtil", "convertStringToDate: " + locale);
            final SimpleDateFormat fmt = new SimpleDateFormat(internalDateFormat, locale);
            return fmt.parse(dateString);

        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "CodeUtil", "Exception on convertStringToDate", e);
            return null;
        }
    }

    @NonNull
    public static String convertDateToString(@NonNull Date date, @NonNull String dateFormat) {
        try {
            final String internalDateFormat = (dateFormat.isEmpty()) ? "yyyyMMdd_HHmmss" : dateFormat;
            final Locale locale = Locale.getDefault();
            final SimpleDateFormat fmt = new SimpleDateFormat(internalDateFormat, locale);
            //Logger.d(fmt.format(date));
            return fmt.format(date);
        } catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, "TimeUtils", "Exception on convertDateToString", e);
            return "";
        }
    }

///

    public static int timeDifferenceToInMinutes(long timeDifference) {
        return ((int) TimeUnit.SECONDS.toMinutes(timeDifference)) / 1000;
    }

    @NonNull
    public static Long getTimestampFromString(@NonNull String given) {
        return Long.parseLong(given) * 1000L;
    }

    @NonNull
    public static Date timestampToDate(@NonNull Long timestamp) {
        return new Date(timestamp);
    }

    @NonNull
    public static String timestampToDateFormattedString(
            @NonNull Date date, @NonNull String formatPattern) {
        return formatPattern.isEmpty()
                ? new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date)
                : new SimpleDateFormat(formatPattern, Locale.getDefault()).format(date);
    }

    @NonNull
    public static String parseTimestampString(@Nullable String timestampString) {
        try {
            final Long timestamp = getTimestampFromString(Objects.requireNonNull(timestampString));
            final Date date = timestampToDate(timestamp);
            return timestampToDateFormattedString(date, "yyyy/MM/dd HH:mm:ss");
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "TimeUtils", "Exception on parseTimestampString", cause);
            return "";
        }
    }

    @NonNull
    public static String parseTimestampStringSimple(@Nullable String timestampString) {
        try {
            final Long timestamp = getTimestampFromString(Objects.requireNonNull(timestampString));
            final Date date = timestampToDate(timestamp);
            return timestampToDateFormattedString(date, "yyyy/MM/dd");
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "TimeUtils", "Exception on parseTimestampString", cause);
            return "";
        }
    }

    @Nullable
    public static Date timestampStringToDate(@Nullable String timestampString) {
        try {
            final Long timestamp = getTimestampFromString(Objects.requireNonNull(timestampString));
            return timestampToDate(timestamp);
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, "TimeUtils", "Exception on timestampStringToDate", cause);
            return null;
        }
    }
}
