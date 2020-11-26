package tw.com.goglobal.project.rxjava2;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import tw.realtime.project.rtbaseframework.LogWrapper;


public abstract class AppRxTask {

    public static abstract class WithExecutor<T> extends AbstractRxTask<T> implements Callable<T> {

        private final Executor executor;

        public WithExecutor(@NonNull Executor executor) { this.executor = executor; }

        @Override
        public final void runTask() {
            rxDisposeIfPossible();
            LogWrapper.showLog(Log.INFO, getLogTag(), "runTask on Thread: " + Thread.currentThread().getName());
            setDisposable(
                    Single.fromCallable(this)
                            .subscribeOn(Schedulers.from(executor))
                            //.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ApiDisposableSingleObserver())
            );
        }
    }

    public static abstract class WithRxIo<T> extends AbstractRxTask<T> implements Callable<T> {
        @Override
        public final void runTask() {
            rxDisposeIfPossible();
            LogWrapper.showLog(Log.INFO, getLogTag(), "runTask on Thread: " + Thread.currentThread().getName());
            setDisposable(
                    Single.fromCallable(this)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ApiDisposableSingleObserver())
            );
        }
    }

    public static abstract class WithRxComputation<T> extends AbstractRxTask<T> implements Callable<T> {
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
    }
}
