package tw.realtime.project.rtbaseframework.widgets;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResponse;


public abstract class FifoQueueTask<T, R> {

    protected final List<T> queueTaskList;
    protected final Map<T, IeApiResponse<R>> resultMap = new HashMap<>();

    public FifoQueueTask(@NonNull final List<T> taskList) {
        this.queueTaskList = new ArrayList<>(taskList);
    }

    protected final void onNext() {
        LogWrapper.showLog(Log.INFO, "FifoQueueTask", "onNext - queueTaskList.size: " + queueTaskList.size() + ", resultMap.size: " + resultMap.size());
        if (queueTaskList.size() > 0) {
            final T nextTask = queueTaskList.remove(0);
            if (null != nextTask) {
                LogWrapper.showLog(Log.INFO, "FifoQueueTask", "onNext --> executeIndividualTask(nextTask)");
                executeIndividualTask(nextTask);
            }
            else {
                LogWrapper.showLog(Log.ERROR, "FifoQueueTask", "onNext - cannot get nextTask!! --> onNext()");
                onNext();
            }
        }
        else { onComplete(); }
    }

    protected abstract void executeIndividualTask(@NonNull final T nextTask);

    protected abstract void onComplete();
}
