package tw.realtime.project.rtbaseframework.widgets;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import tw.com.kingshield.baseframework.LogWrapper;

/**
 * Created by vexonelite on 2017/7/7.
 */

public class TimerWrapper {

    private TimerUpdateCallback mCallback;
    private long mDelay;
    private long mPeriod;
    private long mDuration;

    private MsTimerTask mTimerTask;
    private Timer mTimer;


    private TimerWrapper (Builder builder) {
        mCallback = builder.bCallback;
        mDelay = builder.bDelay;
        mPeriod = builder.bPeriod;
        mDuration = builder.bDuration;
    }

    private String getLogTag() {
        return this.getClass().getSimpleName();
    }

    private class MsTimerTask extends TimerTask {
        private long mTimeCounter;
        private long mDeadline;

        private MsTimerTask () {
            mTimeCounter = 0L;
            mDeadline = (mDuration > 0L) ? mDuration : 0L;
        }

        @Override
        public void run() {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "TimerTask - run - TID: " + Thread.currentThread().getId());

            mTimeCounter = mTimeCounter + mPeriod;
            //LogWrapper.showLog(Log.INFO, getLogTag(), "TimerTask - run - mTimeCounter: " + mTimeCounter);

            new Handler(Looper.getMainLooper()).post(new NotifyTask(mTimeCounter));

            if ( (mDeadline > 0L) && (mTimeCounter >= mDeadline) ) {
                cancelTimerIfNeeded();
            }
        }
    }

    private class NotifyTask implements Runnable {
        private long mTimeStamp;

        private NotifyTask (long timestamp) {
            mTimeStamp = timestamp;
        }

        @Override
        public void run() {
//            LogWrapper.showLog(Log.INFO, getLogTag(), "NotifyTask - run - TID: " + Thread.currentThread().getId());
//            LogWrapper.showLog(Log.INFO, getLogTag(), "NotifyTask - run - mTimeStamp: " + mTimeStamp);

//            long ms = (mTimeStamp % 1000L) / 10;
//            long sec = mTimeStamp / 1000L;
//            LogWrapper.showLog(Log.INFO, getLogTag(), "TimerTask - run - ms: " + ms + ", sec: " + sec);

            if (null != mCallback) {
                mCallback.onTimeUpdated(mTimeStamp);
            }
        }
    }

    public void scheduleTimer () throws IllegalArgumentException {
        if ( (mDelay <= 0L) || (mPeriod <= 0L) ) {
            throw new IllegalArgumentException("Either Delay or Period is invalid!");
        }

        if (null == mTimer) {
            mTimer = new Timer(true);
            mTimerTask = new MsTimerTask();

            // Schedules the specified task for repeated fixed-delay execution,
            // beginning after the specified delay.
            // Subsequent executions take place at approximately regular intervals separated by the specified period.
            //
            // In fixed-delay execution, each execution is scheduled relative to
            // the actual execution time of the previous execution.
            // If an execution is delayed for any reason
            // (such as garbage collection or other background activity),
            // subsequent executions will be delayed as well.
            // In the long run, the frequency of execution will generally be slightly lower than
            // the reciprocal of the specified period
            // (assuming the system clock underlying Object.wait(long) is accurate).
            //
            // Fixed-delay execution is appropriate for recurring activities that require "smoothness."
            // In other words, it is appropriate for activities where
            // it is more important to keep the frequency accurate in the short run than in the long run.
            // This includes most animation tasks, such as blinking a cursor at regular intervals.
            // It also includes tasks wherein regular activity is performed in response to human input,
            // such as automatically repeating a character as long as a key is held down.
            mTimer.schedule(mTimerTask, mDelay, mPeriod);
            LogWrapper.showLog(Log.INFO, getLogTag(), "scheduleTimer");
        }
    }

    public void cancelTimerIfNeeded () {
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask = null;
            LogWrapper.showLog(Log.INFO, getLogTag(), "cancelTimer");
            new Handler(Looper.getMainLooper()).post(new NotifyTask(0));
        }
    }


    public interface TimerUpdateCallback {
        void onTimeUpdated(long timeStamp);
    }

    public static class Builder {
        private TimerUpdateCallback bCallback;
        private long bDelay = 0L;
        private long bPeriod = 0L;
        private long bDuration = 0L;

        public Builder setTimerUpdateCallback (TimerUpdateCallback callback) {
            bCallback = callback;
            return this;
        }

        /**
         *
         * @param delay delay in milliseconds before task is to be executed.
         * @return Builder itself
         */
        public Builder setTimerDelay (long delay) {
            if (delay > 0L) {
                bDelay = delay;
            }
            return this;
        }

        /**
         *
         * @param period time in milliseconds between successive task executions.
         * @return Builder itself
         */
        public Builder setTimerPeriod (long period) {
            if (period > 0L) {
                bPeriod = period;
            }
            return this;
        }

        /**
         *
         * @param duration time in milliseconds that the timer has to count.
         * @return Builder itself
         */
        public Builder setDuration (long duration) {
            if (duration > 0L) {
                bDuration = duration;
            }
            return this;
        }

        public TimerWrapper build () {
            return new TimerWrapper(this);
        }
    }
}
