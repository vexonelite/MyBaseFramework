package tw.com.goglobal.project.rxjava2.reactives;

import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import tw.com.goglobal.project.rxjava2.AbstractRxTask;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.ErrorCodes;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.widgets.recyclerviews.IeDiffListCallback;


public final class CalculateDiffResultTask<T>
        extends AbstractRxTask<DiffUtil.DiffResult> implements Callable<DiffUtil.DiffResult> {

    public final IeDiffListCallback<T> diffListCallback;

    public CalculateDiffResultTask(@NonNull IeDiffListCallback<T> diffListCallback) {
        this.diffListCallback = diffListCallback;
    }

    @Override
    public final void runTask() {
        rxDisposeIfPossible();
        LogWrapper.showLog(Log.INFO, getLogTag(), "runTask on Thread: " + Thread.currentThread().getName());
        setDisposable(
                Single.fromCallable(this)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new ApiDisposableSingleObserver())
        );
    }

    @Override
    public DiffUtil.DiffResult call() throws IeRuntimeException {
        try { return DiffUtil.calculateDiff(diffListCallback); }
        catch (Exception cause) { throw new IeRuntimeException(cause, ErrorCodes.Base.CALCULATE_DIFF_RESULT_ERROR); }
    }
}