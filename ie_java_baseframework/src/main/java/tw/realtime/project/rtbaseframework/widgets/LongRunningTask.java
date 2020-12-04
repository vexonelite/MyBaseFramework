package tw.realtime.project.rtbaseframework.widgets;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * Created in 2020/12/04
 */
public final class LongRunningTask implements Runnable {

    private final byte[] lock = new byte[0];
    private boolean isStillLongRunning = false;
    private ExecutorService executorService;
    private Runnable task;

    private String getLogTag() { return this.getClass().getSimpleName(); }

    public void setTask(@NonNull final Runnable task) { this.task = task; }

    public void startTask() {
        if (null != executorService) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "startTask - executorService existed !!");
            return;
        }

        executorService = Executors.newSingleThreadExecutor();
        LogWrapper.showLog(Log.INFO, getLogTag(), "startTask - Executors.newSingleThreadExecutor() !!");
        modifyIsStillLongRunningFlag(true);
        executorService.submit(this);
        LogWrapper.showLog(Log.INFO, getLogTag(), "startTask - executorService.submit!!");
    }

    public void stopTask() {
        modifyIsStillLongRunningFlag(false);

        if (null != executorService) {
            if (! executorService.isShutdown()) {
                try {
                    executorService.shutdownNow();
                    executorService = null;
                    LogWrapper.showLog(Log.INFO, getLogTag(), "stopTask - executorService.shutdownNow!!");
                }
                catch (Exception cause) {
                    LogWrapper.showLog(Log.ERROR, getLogTag(), "stopTask - error on ExecutorService.shutdown(): " + cause.getLocalizedMessage());
                }
            }
            else {
                executorService = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "stopTask - executorService has been shutdown!!");
            }
        }
        else {
            LogWrapper.showLog(Log.INFO, getLogTag(), "stopTask - executorService is null!!");
        }
    }

    public boolean isStillLongRunning() { return this.isStillLongRunning; }

    public void modifyIsStillLongRunningFlag(final boolean flag) {
        synchronized(lock) {
            isStillLongRunning = flag;
        }
        LogWrapper.showLog(Log.ERROR, getLogTag(), "modifyIsStillLongRunningFlag - result: " + isStillLongRunning);
    }

    @Override
    public void run() {
        while (isStillLongRunning) {
            if (null != this.task) { this.task.run(); }
            else { LogWrapper.showLog(Log.ERROR, getLogTag(), "run - this.task is null!!"); }
        }
    }
}
