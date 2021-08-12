package tw.com.goglobal.project.rxjava2.reactives;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import tw.com.goglobal.project.rxjava2.RxDisposeDelegate;
import tw.realtime.project.rtbaseframework.LogWrapper;


public final class TimerTest implements RxDisposeDelegate {

    @Override
    public void rxDisposeIfPossible() {
        cancelTimer1();
        cancelTimer2();
    }

    private Disposable timer1Disposable;

    private void cancelTimer1() {
        if (null != timer1Disposable) {
            timer1Disposable.dispose();
            LogWrapper.showLog(Log.INFO, "TimerTest", "cancelTimer1");
        }
    }

    //https://blog.mindorks.com/understanding-rxjava-timer-delay-and-interval-operators
    private void testTimer() {
        cancelTimer1();
        timer1Disposable = Observable.interval(2, TimeUnit.SECONDS)
        //timer1Disposable = Observable.interval(0, 2, TimeUnit.SECONDS)
                //.take(5)
                .flatMap(new TimeStampFlatMapper())
                .observeOn(AndroidSchedulers.mainThread())
                //.subscribeWith(new TimeStampDisposableObserver());
                .subscribeWith(new TimeStampDisposableObserver());
    }

    static final class TimeStampFlatMapper implements Function<Long, ObservableSource<String>> {
        @Override
        public ObservableSource<String> apply(Long timeStamp) throws Exception {
            LogWrapper.showLog(Log.INFO, "TimerTest", "TimeStampFlatMapper - apply - timeStamp: " + timeStamp + ", " + Thread.currentThread().getName());
            //return Observable.create(new TimeStampObservableCreator(timeStamp));
            return Observable.just("" + timeStamp);
        }
    }

    static final class TimeStampObservableCreator implements ObservableOnSubscribe<String> {

        final Long timeStamp;

        TimeStampObservableCreator(@NonNull final Long timeStamp) { this.timeStamp = timeStamp; }

        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            LogWrapper.showLog(Log.INFO, "TimerTest", "TimeStampObservableCreator - subscribe"  + ", " + Thread.currentThread().getName());
            emitter.onNext("timeStamp: " + timeStamp);
            emitter.onComplete();
        }
    }

    static final class TimeStampDisposableObserver extends DisposableObserver<String> {

        private String cachedData;

        @Override
        public void onNext(@NonNull String result) {
            LogWrapper.showLog(Log.INFO, "TimerTest", "TimeStampDisposableObserver- onNext - result: " + result + ", " + Thread.currentThread().getName());
            cachedData = result;
        }

        @Override
        public void onError(@NonNull Throwable cause) {
            LogWrapper.showLog(Log.ERROR, "TimerTest", "TimeStampDisposableObserver - onError " + Thread.currentThread().getName(), cause);
        }

        @Override
        public void onComplete() {
            LogWrapper.showLog(Log.INFO, "TimerTest", "TimeStampDisposableObserver - onComplete - result: " + cachedData  + ", " + Thread.currentThread().getName());
        }
    }

    private Disposable timer2Disposable;

    private void cancelTimer2() {
        if (null != timer2Disposable) {
            timer2Disposable.dispose();
            LogWrapper.showLog(Log.INFO, "TimerTest", "cancelTimer2");
        }
    }

    private void testTimer2() {
        cancelTimer2();
        timer2Disposable = Flowable.interval(2L, TimeUnit.SECONDS)
                .take(8)
                //timer2Disposable = Flowable.interval(0, 2L, TimeUnit.SECONDS)
                .map(timeStamp -> {
                    LogWrapper.showLog(Log.INFO, "TimerTest", "testTimer2 - map - timeStamp: " + timeStamp + ", " + Thread.currentThread().getName());
                    return "timeStamp: " + timeStamp;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TimeStampConsumer());
    }

    static final class TimeStampConsumer implements Consumer<String> {
        @Override
        public void accept(@NonNull String result) throws Exception {
            LogWrapper.showLog(Log.INFO, "TimeStampConsumer", "accept - Tid: " + Thread.currentThread().getId() + ", result: " + result);
        }
    }

    private class TimeStampConsumer2 implements Consumer<Long> {
        @Override
        public void accept(@NonNull Long value) throws Exception {
            LogWrapper.showLog(Log.INFO, "TimeStampConsumer2", "accept - value: " + value + ", " + Thread.currentThread().getName());
            if (value == 7) { cancelTimer2(); }
        }
    }
}
