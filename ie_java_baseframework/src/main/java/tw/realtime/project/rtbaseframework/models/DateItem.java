package tw.realtime.project.rtbaseframework.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.BaseApiData;
import tw.realtime.project.rtbaseframework.delegates.CalendarEvent;
import tw.realtime.project.rtbaseframework.parameters.ApiMapDataBuilder;


public class DateItem extends BaseApiData {

    public static final int TYPE_DEFAULT = -1;
    public static final int TYPE_SECTION = 1001;
    public static final int TYPE_ITEM = 1002;
    public static final int TYPE_DIVIDER = 1003;

    @Deprecated
    /** (calendar_uuid, CalendarEvent) */
    private Map<String, CalendarEvent> mEventMap;
    @Deprecated
    private List<CalendarEvent> mEventList;

    private Mode mMode;
    private int mDayOfWeek;
    private int mYear;
    private int mMonth;
    private int mDate;

    public enum Mode {
        SECTION,
        ITEM,
        DIVIDER,
        DUMMY
    }

    public interface Keys {
        String ITEM_ID = "_item_id";
        String DUMMY_ID = "_dummy_id";
        String DIVIDER_ID = "_divider_id";
    }

    private static String getLogTag () {
        return DateItem.class.getSimpleName();
    }

    @Deprecated
    public List<CalendarEvent> getCalendarEventListFromMap () {
        List<CalendarEvent> calendarEventList = new ArrayList<>();
        Set<String> keySet = mEventMap.keySet();
        for (String key : keySet) {
            calendarEventList.add(mEventMap.get(key));
        }
        return calendarEventList;
    }

    @Deprecated
    public void putCalendarEventIntoMap (CalendarEvent calendarEvent) {
        if (null == calendarEvent) {
            return;
        }
        String calendarEventUUID = calendarEvent.getUUID();
        if (null == calendarEventUUID) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "putCalendarEventIntoMap - calendarEventUUID is null!");
            return;
        }
        if (!mEventMap.containsKey(calendarEventUUID)) {
            mEventMap.put(calendarEventUUID, calendarEvent);
            LogWrapper.showLog(Log.INFO, getLogTag(), "putCalendarEventIntoMap - add event for calendarEventUUID: " + calendarEventUUID);
        }
        else {
            LogWrapper.showLog(Log.WARN, getLogTag(), "putCalendarEventIntoMap - calendarEventUUID has existed!");
            mEventMap.remove(calendarEventUUID);
            LogWrapper.showLog(Log.INFO, getLogTag(), "putCalendarEventIntoMap - remove calendarEventUUID!");
            mEventMap.put(calendarEventUUID, calendarEvent);
            LogWrapper.showLog(Log.INFO, getLogTag(), "putCalendarEventIntoMap - put event for calendarEventUUID: " + calendarEventUUID);
        }
    }

    @Deprecated
    public List<CalendarEvent> getCalendarEventListFromList () {
        return mEventList;
    }

    @Deprecated
    public void addCalendarEventIntoList (CalendarEvent calendarEvent) {
        if (null == calendarEvent) {
            return;
        }
        String calendarEventUUID = calendarEvent.getUUID();
        if (null == calendarEventUUID) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "addCalendarEventIntoList - calendarEventUUID is null!");
            return;
        }
        if (mEventList.contains(calendarEvent)) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "addCalendarEventIntoList - calendarEvent has existed!");
        }
        else {
            mEventList.add(calendarEvent);
            LogWrapper.showLog(Log.INFO, getLogTag(), "addCalendarEventIntoList - add event for calendarEventUUID: " + calendarEventUUID);
        }
    }

    @Deprecated
    public void addCalendarEventListIntoList (List<CalendarEvent> calendarEventList) {
        if ( (null != calendarEventList) && (!calendarEventList.isEmpty()) ) {
            mEventList.addAll(calendarEventList);
        }
    }

    @Deprecated
    public void clearCalendarEventList () {
        mEventList.clear();
    }

    public String getItemId () {
        return retrieveData(Keys.ITEM_ID);
    }

    public int getDayOfWeek () {
        return mDayOfWeek;
    }

    private String getDayOfWeekString () {
        String weekdayString = "";
        switch (mDayOfWeek) {
            case Calendar.SUNDAY:
                weekdayString = weekdayString + "Sunday";
                break;
            case Calendar.MONDAY:
                weekdayString = weekdayString + "Monday";
                break;
            case Calendar.TUESDAY:
                weekdayString = weekdayString + "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                weekdayString = weekdayString + "Wednesday";
                break;
            case Calendar.THURSDAY:
                weekdayString = weekdayString + "Thursday";
                break;
            case Calendar.FRIDAY:
                weekdayString = weekdayString + "Friday";
                break;
            case Calendar.SATURDAY:
                weekdayString = weekdayString + "Saturday";
                break;
        }

        return  weekdayString;
    }

    public int getYear () {
        return mYear;
    }

    public int getMonth () {
        return mMonth;
    }

    public int getDate () {
        return mDate;
    }

    public String getLogString () {
        switch (mMode) {
            case SECTION:
            case DIVIDER:
                return getItemId();
            case ITEM:
                return "year: " + mYear + ", month: " + (mMonth + 1) + ", date: " + mDate  + ", weekday: " + getDayOfWeekString();
            case DUMMY:
                return "dummy";
            default:
                return "Invalid";
        }
    }

    public Mode getMode () {
        return mMode;
    }

    public int getItemType () {
        switch (mMode) {
            case SECTION:
                return TYPE_SECTION;
            case DIVIDER:
                return TYPE_DIVIDER;
            case DUMMY:
            case ITEM:
                return TYPE_ITEM;
            default:
                return TYPE_DEFAULT;
        }
    }

    public boolean isToday () {
        Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.get(Calendar.DATE);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        return (    (currentDate == getDate()) &&
                    (currentMonth == getMonth()) &&
                    (currentYear == getYear()) );
    }

    public static class Builder extends ApiMapDataBuilder {

        /** (calendar_uuid, CalendarEvent) */
        private Map<String, CalendarEvent> bEventMap;
        private List<CalendarEvent> bEventList;
        private int bDayOfWeek = Integer.MAX_VALUE;
        private int bYear;
        private int bMonth;
        private int bDate;
        private Mode bMode = Mode.ITEM;

        public Builder () {
            super();
            bEventMap = new HashMap<>();
            bEventList = new ArrayList<>();
        }

        public Builder setItemId (String itemId) {
            if ( (null != itemId) && (!itemId.isEmpty()) ) {
                setData(Keys.ITEM_ID, itemId);
            }
            return  this;
        }

        public Builder setDayOfWeek (int dayOfWeek) {
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                case Calendar.MONDAY:
                case Calendar.TUESDAY:
                case Calendar.WEDNESDAY:
                case Calendar.THURSDAY:
                case Calendar.FRIDAY:
                case Calendar.SATURDAY:
                    bDayOfWeek = dayOfWeek;
                    break;
                default:{
                    throw new IllegalArgumentException("setDayOfWeek: invalid dayOfWeek!!");
                }
            }

            return this;
        }

        public Builder setYear (int year) {
            bYear = year;
            return  this;
        }

        public Builder setMonth (int month) {
            bMonth = month;
            return  this;
        }

        public Builder setDate (int date) {
            bDate = date;
            return  this;
        }

        public Builder setMode (Mode mode) {
            if (null == mode) {
                throw new IllegalArgumentException("setMode: mode cannot be null!!");
            }
            else {
                bMode = mode;
                return  this;
            }
        }

        public DateItem build() {
            return new DateItem(this);
        }
    }

    private DateItem(Builder builder) {
        super(builder.getDataMap());
        mEventMap = builder.bEventMap;
        mEventList = builder.bEventList;
        mDayOfWeek = builder.bDayOfWeek;
        mYear = builder.bYear;
        mMonth = builder.bMonth;
        mDate = builder.bDate;
        mMode = builder.bMode;
    }



}
