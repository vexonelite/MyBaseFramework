package tw.realtime.project.rtbaseframework.widgets;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import tw.com.kingshield.baseframework.LogWrapper;
import tw.com.kingshield.baseframework.api.commons.ApiConstants;
import tw.com.kingshield.baseframework.api.commons.AsyncApiCallback;
import tw.com.kingshield.baseframework.api.commons.AsyncApiException;
import tw.com.kingshield.baseframework.models.DateItem;
import tw.com.kingshield.baseframework.utils.CodeUtils;

/**
 * Created by vexonelite on 2017/3/27.
 */

public class DateItemGenerator {

    private static final int RANGE = 6;

    private Map<String, DateItem> mDateMap;
    private List<String> mDateItemIdList;
    private String mCurrentDateId;
    private int mRange;
    private boolean hasFuture;

    private ExecutorService mExecutorService;

    private AsyncApiCallback<List<String>> mCallback;
    private boolean doesNotNotifyCallback = false;

    private final ReadWriteLock mDataUpdateLock;

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }



    public DateItemGenerator() {
        mDataUpdateLock = new ReentrantReadWriteLock();
        mDateMap = new HashMap<>();
        mDateItemIdList = new ArrayList<>();
        mRange = RANGE;
        hasFuture = false;

        DateItem dummyDateItem = new DateItem.Builder()
                .setMode(DateItem.Mode.DUMMY)
                .setItemId(DateItem.Keys.DUMMY_ID)
                .build();
        addDateItem(dummyDateItem);

        DateItem dividerItem = new DateItem.Builder()
                .setMode(DateItem.Mode.DIVIDER)
                .setItemId(DateItem.Keys.DIVIDER_ID)
                .build();
        addDateItem(dividerItem);
    }

    public DateItemGenerator setRange (int range) {
        if (range > 0) {
            mRange = range;
        }
        return this;
    }

    public DateItemGenerator setFutureFlag (boolean flag) {
        hasFuture = flag;
        return this;
    }

    protected void addDateItem (DateItem dateItem) {
        if (null == dateItem) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "addDateItem - invalid dateItem!");
            return;
        }
        String dateItemId = dateItem.getItemId();
        if ((null == dateItemId) || (dateItemId.isEmpty()) ) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "addDateItem - invalid dateItemId!");
            return;
        }
        try {
            this.mDataUpdateLock.writeLock().lock();

            if (!mDateMap.containsKey(dateItem.getItemId())) {
                mDateMap.put(dateItem.getItemId(), dateItem);
            }
            else {
                LogWrapper.showLog(Log.WARN, getLogTag(), "addDateItem - mDateMap has contained key: " + dateItemId);
            }
        }
        finally {
            this.mDataUpdateLock.writeLock().unlock();
        }
    }

    public DateItem retrieveDateItem (String key) {
        if ( (null == key) || (key.isEmpty()) ) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "retrieveDateItem - invalid key!");
            return null;
        }
        //LogWrapper.showLog(Log.INFO, getLogTag(), "retrieveDateItem - key: " + key);

        DateItem dateItem = null;
        try {
            this.mDataUpdateLock.readLock().lock();

            if (mDateMap.containsKey(key)) {
                dateItem = mDateMap.get(key);
            }
            else {
                LogWrapper.showLog(Log.WARN, getLogTag(), "retrieveDateItem - mDateMap does not contain key: " + key);
            }
        }
        finally {
            this.mDataUpdateLock.readLock().unlock();
        }

        return dateItem;
    }

    public List<String> getDateItemIdList () {
        return mDateItemIdList;
    }

    protected void setCurrentDateItemId (String dateItemId) {
        if (null != dateItemId) {
            mCurrentDateId = dateItemId;
        }
    }

    public int getCurrentDateItemPosition () {
        return getPositionViaDateItemId(mCurrentDateId);
    }

    public int getPositionViaDateItemId (String dateItemId) {
        if ( (null != mDateItemIdList)  && (!mDateItemIdList.isEmpty())
                                        && (null != dateItemId)) {
            return mDateItemIdList.indexOf(dateItemId);
        }
        else {
            return -1;
        }
    }

    public static int getNumberOfDummyDays (int dayOfWeek, boolean isLeading) {

        switch (dayOfWeek) {
            case Calendar.SUNDAY: {
                return isLeading ? 0 : 6;
            }
            case Calendar.MONDAY: {
                return isLeading ? 1 : 5;
            }
            case Calendar.TUESDAY: {
                return isLeading ? 2 : 4;
            }
            case Calendar.WEDNESDAY: {
                return isLeading ? 3 : 3;
            }
            case Calendar.THURSDAY: {
                return isLeading ? 4 : 2;
            }
            case Calendar.FRIDAY: {
                return isLeading ? 5 : 1;
            }
            case Calendar.SATURDAY: {
                return isLeading ? 6 : 0;
            }
            default:{
                throw new IllegalArgumentException("Invalid day of week!!");
            }
        }
    }

    public void generate (AsyncApiCallback<List<String>> callback) {

        mCallback = callback;

        if (null == mExecutorService) {
            mExecutorService = Executors.newFixedThreadPool(1);
        }
        mExecutorService.submit(new GenerationTask());
    }

    public void cancelCurrentTask () {
        doesNotNotifyCallback = true;
        terminateExecutorService();
    }

    private void terminateExecutorService () {
        if (null == mExecutorService) {
            return;
        }

        try {
            LogWrapper.showLog(Log.INFO, getLogTag(), "terminateExecutorService: attempt to shutdown executor");
            mExecutorService.shutdown();
            mExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on terminateExecutorService", e);
            System.err.println("tasks interrupted");
        }
        finally {
            if (!mExecutorService.isTerminated()) {
                LogWrapper.showLog(Log.WARN, getLogTag(), "terminateExecutorService: cancel non-finished tasks");
            }
            mExecutorService.shutdownNow();
            mExecutorService = null;
            LogWrapper.showLog(Log.INFO, getLogTag(), "terminateExecutorService: shutdownNow finished");
        }
    }

    protected class DateItemListWrapper {
        private List<String> mDateItemIdList;
        private Date mDate;

        public DateItemListWrapper(List<String> dateItemIdList, Date date) {
            mDateItemIdList = dateItemIdList;
            mDate = date;
        }

        private List<String> getDateItemIdList () {
            return mDateItemIdList;
        }

        private Date getDate () {
            return mDate;
        }
    }

    private class GenerationTask implements Runnable {
        @Override
        public void run() {
            //new Handler(Looper.getMainLooper()).post(new StartNotificationTask());
            try {
                List<Calendar> calendarList = getFinalCalendarList();
                List<DateItemListWrapper> wrapperList = getWrapperList(calendarList);
                List<String> dateItemIdList = convertWrapperListToDateItemIdList(wrapperList);
                mDateItemIdList = dateItemIdList;
                new Handler(Looper.getMainLooper()).post(new SuccessNotificationTask(dateItemIdList));
            }
            catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(
                        new ErrorNotificationTask(
                                new AsyncApiException(ApiConstants.ExceptionCode.INTERNAL_CONVERSION_ERROR, e)
                        )
                );
            }
            new Handler(Looper.getMainLooper()).post(new TerminationTask());
        }
    }

    private List<Calendar> getFinalCalendarList () {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.addAll(getCalendarList(true));
        calendarList.add(Calendar.getInstance());
        if (hasFuture) {
            calendarList.addAll(getCalendarList(false));
        }
        return calendarList;
    }

    private List<Calendar> getCalendarList (boolean isReverse) {

        List<Calendar> calendarList = new ArrayList<>();
        for (int i = 0; i < mRange; i++) {
            Calendar calendar = Calendar.getInstance();
            if (isReverse) {
                calendar.add(Calendar.MONTH, (i - mRange) );
            }
            else {
                calendar.add(Calendar.MONTH, (i + 1) );
            }
            calendarList.add(calendar);
        }
        return calendarList;
    }

    private List<DateItemListWrapper> getWrapperList (List<Calendar> calendarList) {
        List<DateItemListWrapper> wrapperList = new ArrayList<>();
        for (Calendar calendar : calendarList) {
            DateItemListWrapper wrapper = generateForOneMonth(calendar);
            wrapperList.add(wrapper);
        }
        return wrapperList;
    }

    protected DateItemListWrapper generateForOneMonth (Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        LogWrapper.showLog(Log.INFO, getLogTag(), "generateForOneMonth - ThreadId: " +
                + Thread.currentThread().getId() + ", calendar: " + year + "/" + month);
        List<String> dateItemIdList = new ArrayList<>();
        DateItem dummyDateItem = new DateItem.Builder()
                .setMode(DateItem.Mode.DUMMY)
                .setItemId(DateItem.Keys.DUMMY_ID)
                .build();
        DateItem dividerItem = new DateItem.Builder()
                .setMode(DateItem.Mode.DIVIDER)
                .setItemId(DateItem.Keys.DIVIDER_ID)
                .build();

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, year);
//        calendar.set(Calendar.MONTH, month);

        String headerId = "" + year + "_" + month + "_" + System.currentTimeMillis();
        DateItem headerItem = new DateItem.Builder()
                .setMode(DateItem.Mode.SECTION)
                .setItemId(headerId)
                .setYear(calendar.get(Calendar.YEAR))
                .setMonth(calendar.get(Calendar.MONTH))
                .build();
        dateItemIdList.add(headerId);
        addDateItem(headerItem);

        calendar.set(Calendar.DATE, 1);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int leadingDummyNumber = getNumberOfDummyDays(weekday, true);
        LogWrapper.showLog(Log.INFO, getLogTag(), "generateForOneMonth - ThreadId: " +
                + Thread.currentThread().getId() + ", leadingDummyNumber: " + leadingDummyNumber);
        if (leadingDummyNumber > 0) {
            for (int i = 0; i < leadingDummyNumber; i++) {
                dateItemIdList.add(dummyDateItem.getItemId());
            }
        }

        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        LogWrapper.showLog(Log.INFO, getLogTag(), "generateForOneMonth - maxDayOfMonth: " + maxDayOfMonth);
        for (int i = 1; i <= maxDayOfMonth; i++) {
            calendar.set(Calendar.DATE, i);
            DateItem dateItem = new DateItem.Builder()
                    .setYear(calendar.get(Calendar.YEAR))
                    .setMonth(calendar.get(Calendar.MONTH))
                    .setDate(calendar.get(Calendar.DATE))
                    .setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
                    .setMode(DateItem.Mode.ITEM)
                    .setItemId(CodeUtils.convertDateToString(calendar.getTime(), "yyyy-MM-dd"))
                    .build();
            dateItemIdList.add(dateItem.getItemId());
            addDateItem(dateItem);
            if (dateItem.isToday()) {
                setCurrentDateItemId(dateItem.getItemId() );
                LogWrapper.showLog(Log.INFO, getLogTag(), "generateForOneMonth - isToday: " + dateItem.getItemId() );
            }
        }

        calendar.set(Calendar.DATE, maxDayOfMonth);
        weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int tailDummyNumber = getNumberOfDummyDays(weekday, false);
        LogWrapper.showLog(Log.INFO, getLogTag(), "generateForOneMonth - ThreadId: " +
                + Thread.currentThread().getId() + ", tailDummyNumber: " + tailDummyNumber);
        if (tailDummyNumber > 0) {
            for (int i = 0; i < tailDummyNumber; i++) {
                dateItemIdList.add(dummyDateItem.getItemId());
            }
        }

        dateItemIdList.add(dividerItem.getItemId());

//        for (DateItem dateItem : dateItemIdList) {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "dateItem: " + dateItem.getLogString());
//        }

        calendar.set(Calendar.DATE, 1);
        Date date = calendar.getTime();
        return new DateItemListWrapper(dateItemIdList, date);
    }

    private List<String> convertWrapperListToDateItemIdList (List<DateItemListWrapper> wrapperList) {
        List<String> dateItemIdList = new ArrayList<>();
        for (DateItemListWrapper wrapper : wrapperList) {
            if ( (null != wrapper.getDateItemIdList()) && (!wrapper.getDateItemIdList().isEmpty()) ) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "DateItemListWrapperListToDateItemIdList - " +
                        "wrapper.getDateItemIdList().size: " + wrapper.getDateItemIdList().size());
                dateItemIdList.addAll(wrapper.getDateItemIdList());
                LogWrapper.showLog(Log.INFO, getLogTag(), "DateItemListWrapperListToDateItemIdList - " +
                        "dateItemIdList.size: " + dateItemIdList.size());
            }
            else {
                LogWrapper.showLog(Log.WARN, getLogTag(), "DateItemListWrapperListToDateItemIdList - " +
                        "wrapper.getDateItemIdList() is either null or empty!");
            }
        }
        return dateItemIdList;
    }

//    private class StartNotificationTask implements Runnable {
//        @Override
//        public void run() {
//            if ( (null != mCallback) && (!doesNotNotifyCallback) ) {
//                mCallback.onStart();
//            }
//        }
//    }

    private class ErrorNotificationTask implements Runnable {

        private AsyncApiException mException;

        private ErrorNotificationTask (AsyncApiException e) {
            mException = e;
        }

        @Override
        public void run() {
            if ( (null != mCallback) && (!doesNotNotifyCallback) ) {
                mCallback.onError(mException);
            }
        }
    }

    private class SuccessNotificationTask implements Runnable {

        private List<String> mResult;

        private SuccessNotificationTask (List<String> result) {
            mResult = result;
        }

        @Override
        public void run() {
            if ( (null != mCallback) && (!doesNotNotifyCallback) ) {
                mCallback.onSuccess(mResult);
            }
        }
    }

    private class TerminationTask implements Runnable {
        @Override
        public void run() {
            terminateExecutorService();
        }
    }

    private class DateItemComparator implements Comparator<DateItemListWrapper> {
        private boolean isAscending;

        private DateItemComparator (boolean isAscending) {
            this.isAscending = isAscending;
        }

        @Override
        public int compare(DateItemListWrapper wrapper1, DateItemListWrapper wrapper2) {

            Date date1 = wrapper1.getDate();
            Date date2 = wrapper2.getDate();

            if ( (null == date1) || (null == date2) ) {
                return 0;
            }
            int result = date1.compareTo(date2);
            if (result > 0) { // date1 is after date2
                return isAscending ? 1 : -1;
                //for order by ascending
                // return 1;
                // for order by descending
                //return -1;
            } else if (result < 0) { // date1 is before date2
                return isAscending ? -1 : 1;
                //for order by ascending
                //return -1;
                // for order by descending
                //return 1;
            } else { // date1 is equal date2
                return 0;
            }
        }
    }
}
