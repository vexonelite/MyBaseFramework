package tw.com.goglobal.project.rxjava2;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import io.reactivex.rxjava3.functions.Action;

/**
 * Implementation of Rx Action interface.
 * It's mainly to have an ExecutorService shutdown.
 */
public final class ExecutorTermination implements Action {

    private ExecutorService mExecutorService;

    public ExecutorTermination(@NonNull ExecutorService executorService) { mExecutorService = executorService; }

    @Override
    public void run() throws Exception {
        if (null != mExecutorService) { mExecutorService.shutdown(); }
    }
}
