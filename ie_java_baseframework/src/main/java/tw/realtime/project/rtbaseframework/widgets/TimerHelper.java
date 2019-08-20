package tw.realtime.project.rtbaseframework.widgets;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * Created by vexonelite on 2017/7/7.
 */
public final class TimerHelper {

    private TimerUpdateCallback callback;

    private Timer timer;


    private TimerHelper(@Nullable TimerUpdateCallback callback) {
        this.callback = callback;
    }

    private String getLogTag() {
        return this.getClass().getSimpleName();
    }

    private class IeTimerTask extends TimerTask {
        /**
         * The sum of time since from a certain start time
         */
        private long timeCounter;
        /**
         * A period of time that will be added to [timeCounter]
         * when [Timer] starts working
         */
        private final long period;
        /**
         * A specific time in the (near) future that the [Timer] will stop
         */
        private final long deadline;

        private IeTimerTask(long period, long deadline) {
            timeCounter = 0L;
            this.period = period;
            this.deadline = deadline;
        }

        @Override
        public void run() {
            LogWrapper.showLog(Log.INFO, getLogTag(), "TimerTask - run - on thread: " + Thread.currentThread().getName());
            timeCounter = timeCounter + period;
            final long timeStamp = timeCounter;
            final long ms = (timeStamp % 1000L) / 10L;
            final long sec = timeStamp / 1000L;
            LogWrapper.showLog(Log.INFO, getLogTag(), "TimerTask - run - timeStamp: " + timeStamp + ", ms: " + ms + ", sec: " + sec);
            new Handler(Looper.getMainLooper()).post(new NotifyTask(timeStamp));

            if (timeCounter >= deadline) {
                cancelTimerIfPossible();
            }
        }
    }

    private class NotifyTask implements Runnable {
        private final long timeStamp;

        private NotifyTask(long timestamp) {
            this.timeStamp = timestamp;
        }

        @Override
        public void run() {
            if (null != callback) {
                callback.onTimeUpdated(timeStamp);
            }
        }
    }

    /**
     *
     * @param initialDelay  delay in milliseconds before task is to be executed.
     * @param period        time in milliseconds between successive task executions.
     * @param deadline      time in milliseconds that the timer has to count.
     * @throws IllegalArgumentException
     */
    public void scheduleTimer(long initialDelay, long period, long deadline) throws IllegalArgumentException {
        if (period <= 0L) {
            throw new IllegalArgumentException("period must exceed 0L!");
        }
        if (initialDelay <= 0L) {
            throw new IllegalArgumentException("initialDelay must exceed 0L!");
        }
        if (deadline <= 0L) {
            throw new IllegalArgumentException("deadline must exceed 0L!");
        }

        if (null == timer) {
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
            timer = new Timer(true);
            final IeTimerTask timerTask = new IeTimerTask(period, deadline);
            timer.schedule(timerTask, initialDelay, period);
            LogWrapper.showLog(Log.INFO, getLogTag(), "scheduleTimer");
        }
    }

    public void cancelTimerIfPossible() {
        if (null != timer) {
            timer.cancel();
            timer = null;
            LogWrapper.showLog(Log.INFO, getLogTag(), "cancelTimer");
            new Handler(Looper.getMainLooper()).post(new NotifyTask(0));
        }
    }

    public interface TimerUpdateCallback {
        void onTimeUpdated(long timeStamp);
    }
}
