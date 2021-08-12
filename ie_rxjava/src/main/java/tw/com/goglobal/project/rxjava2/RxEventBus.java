package tw.com.goglobal.project.rxjava2;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * @see <a href="https://blog.mindorks.com/implementing-eventbus-with-rxjava-rxbus-e6c940a94bd8">Reference</a>
 * <p>
 *  To subscribe:
 *  .getEventBus()
 *  .toObservable()
 *  .observeOn(AndroidSchedulers.mainThread()) // should add this
 *  .subscribe(callback);
 */
public final class RxEventBus {

    private final PublishSubject<Object> publisher = PublishSubject.create();

    public void post(@NonNull Object event) { publisher.onNext(event); }

    @NonNull
    public Observable<Object> toObservable() { return this.publisher; }

    @NonNull
    public Flowable<Object> toFlowable() {
        return this.toFlowable(BackpressureStrategy.BUFFER);
    }

    @NonNull
    public Flowable<Object> toFlowable(@NonNull final BackpressureStrategy strategy) {
        return this.publisher.toFlowable(strategy);
    }
}

/*
// Usage:
private void subscribeNetworkEvent() {
    final Activity activity = requireActivity();
    if (activity instanceof ConnectDetailActivity) {
        rxDisposeIfPossible();
        disposable = ((ConnectDetailActivity) activity).getEventBus()
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread()) // should add this
                .subscribe(new NetworkEventCallback());
        LogWrapper.showLog(Log.INFO, getLogTag(), "subscribeNetworkEvent");
    }
}

// add in 2020/01/17
private class NetworkEventCallback implements Consumer<Object> {
    @Override
    public void accept(@NonNull Object given) throws Exception {
        if (given instanceof WiFiNetworkEvent) {
            final WiFiNetworkEvent event = (WiFiNetworkEvent) given;
            switch (event.wiFiState) {
                case AVAILABLE: {
                    LogWrapper.showLog(Log.INFO, getLogTag(), "NetworkEventCallback - accept - AVAILABLE");
                    break;
                }
                case LOST: {
                    LogWrapper.showLog(Log.INFO, getLogTag(), "NetworkEventCallback - accept - LOST");
                    rxDisposeIfPossible();
                    startConnect(netWorkData);
                    break;
                }
                default: {
                    LogWrapper.showLog(Log.INFO, getLogTag(), "NetworkEventCallback - accept - " + event.wiFiState);
                    break;
                }
            }
        }
        else { LogWrapper.showLog(Log.ERROR, getLogTag(), "NetworkEventCallback - accept - unknown event type!"); }
    }
}
 */