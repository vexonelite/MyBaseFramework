package tw.com.goglobal.project.rxjava2;

import android.util.Log;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.apis.IeApiResponse;
import tw.realtime.project.rtbaseframework.models.IePair;


public abstract class AppRxTask {

    public static abstract class WithExecutor<T> extends AbstractRxTask<T> implements Callable<T> {

        private final Executor executor;

        public WithExecutor(@NonNull Executor executor) { this.executor = executor; }

        @Override
        public final void runTask() {
            rxDisposeIfPossible();
            LogWrapper.showLog(Log.INFO, "AppRxTask", "WithExecutor - runTask - on Thread: " + Thread.currentThread().getName());
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
            LogWrapper.showLog(Log.INFO, "AppRxTask", "WithRxIo - runTask - on Thread: " + Thread.currentThread().getName());
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
            LogWrapper.showLog(Log.INFO, "AppRxTask", "WithRxComputation - runTask - on Thread: " + Thread.currentThread().getName());
            setDisposable(
                    Single.fromCallable(this)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ApiDisposableSingleObserver())
            );
        }
    }


    // [start] added in 2020/12/23

    public static abstract class AbsParallelCommunicationTask<T, R>
            extends AbstractRxTask<List<IePair<T, IeApiResponse<R>>>> {

        private final List<T> productList;

        public AbsParallelCommunicationTask(@androidx.annotation.NonNull final List<T> productList) {
            this.productList = productList;
        }

        @Override
        public final void runTask() {
            LogWrapper.showLog(Log.INFO, "AppRxTask", "AbsParallelCommunicationTask - runTask - on Thread: " + Thread.currentThread().getName());
            rxDisposeIfPossible();
            setDisposable(
                    Flowable.just(productList)
                            .flatMap(getParallelFlatMapper())
                            .toList() // convert into Single
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ApiDisposableSingleObserver())
            );
        }

        @NonNull
        protected abstract Function<List<T>, Publisher<IePair<T, IeApiResponse<R>>>> getParallelFlatMapper();
    }

    public static abstract class AbsParallelCommunicationFlatMapper<T, R>
            implements Function<List<T>, Publisher<IePair<T, IeApiResponse<R>>>> {

        private final int threadNumberUpperBound;

        public AbsParallelCommunicationFlatMapper() { this.threadNumberUpperBound = 24; }

        public AbsParallelCommunicationFlatMapper(final int threadNumber) { this.threadNumberUpperBound = threadNumber; }

        @Override
        public final Publisher<IePair<T, IeApiResponse<R>>> apply(@androidx.annotation.NonNull final List<T> productList) throws Exception {
            LogWrapper.showLog(Log.INFO, "AppRxTask", "AbsParallelCommunicationFlatMapper - runTask - on Thread: " + Thread.currentThread().getName());
            final int evaluatedThreadCt = Runtime.getRuntime().availableProcessors() * 3;
            final int threadCt = Math.min(24, evaluatedThreadCt);
            return Flowable.fromIterable(productList)
                    .parallel(threadCt)
                    .runOn(Schedulers.io())
                    .map(getSingleCommunicationFlatMapper())
                    .sequential();
        }

        @NonNull
        protected abstract Function<T, IePair<T, IeApiResponse<R>>> getSingleCommunicationFlatMapper();
    }

    // [end] added in 2020/12/23
}
