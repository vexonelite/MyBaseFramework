package tw.realtime.project.rtbaseframework.widgets;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResponse;


public abstract class MultipleQueueTask<T, R> implements Callable<Map<T, IeApiResponse<R>>> {

    protected final List<T> queueTaskList;
    protected final Map<T, IeApiResponse<R>> resultMap = new HashMap<>();

    public MultipleQueueTask(@NonNull final List<T> taskList) {
        this.queueTaskList = new ArrayList<>(taskList);
    }

    public final Map<T, IeApiResponse<R>> call() throws Exception {
        if (queueTaskList.isEmpty()) {
            LogWrapper.showLog(Log.ERROR, "MultipleQueueTask", "call - queueTaskList.isEmpty!!");
            return resultMap;
        }

        setup();

        while (queueTaskList.size() > 0) {
            LogWrapper.showLog(Log.INFO, "MultipleQueueTask", "call - queueTaskList.size: " + queueTaskList.size());
            final T nextTask = queueTaskList.remove(0);
            if (null != nextTask) {
                LogWrapper.showLog(Log.INFO, "MultipleQueueTask", "call --> executeIndividualTask(nextTask)");
                executeIndividualTask(nextTask);
            }
            else {
                LogWrapper.showLog(Log.ERROR, "MultipleQueueTask", "call - cannot get nextTask!! --> onNext()");
            }

            Thread.sleep(getRestTime());
        }

        tearDown();

        return resultMap;
    }

    protected abstract void setup();

    protected abstract void tearDown();

    protected abstract void executeIndividualTask(@NonNull final T nextTask);

    protected long getRestTime() { return 200L; }
}
