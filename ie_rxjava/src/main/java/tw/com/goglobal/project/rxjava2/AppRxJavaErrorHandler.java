package tw.com.goglobal.project.rxjava2;

import android.util.Log;

import java.io.IOException;
import java.net.SocketException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.functions.Consumer;
import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
 */
public final class AppRxJavaErrorHandler implements Consumer<Throwable> {

    private final String logTag;

    public AppRxJavaErrorHandler(@NonNull String logTag) {
        this.logTag = logTag;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        if (throwable instanceof UndeliverableException) {
            LogWrapper.showLog(Log.ERROR, logTag, "AppRxJavaErrorHandler - UndeliverableException", throwable);
            throwable = throwable.getCause();
        }
        if ((throwable instanceof IOException) || (throwable instanceof SocketException)) {
            // fine, irrelevant network problem or API that throws on cancellation
            LogWrapper.showLog(Log.ERROR, logTag, "AppRxJavaErrorHandler - IOException or SocketException", throwable);
            return;
        }
        if (throwable instanceof InterruptedException) {
            // fine, some blocking code was interrupted by a dispose call
            LogWrapper.showLog(Log.ERROR, logTag, "AppRxJavaErrorHandler - InterruptedException", throwable);
            return;
        }
        if ((throwable instanceof NullPointerException) || (throwable instanceof IllegalArgumentException)) {
            // that's likely a bug in the application
            LogWrapper.showLog(Log.ERROR, logTag, "AppRxJavaErrorHandler - NullPointerException or IllegalArgumentException [that's likely a bug in the application]: " + throwable.getLocalizedMessage());
            handleUncaughtException(throwable);
            return;
        }
        if (throwable instanceof IllegalStateException) {
            // that's a bug in RxJava or in a custom operator
            LogWrapper.showLog(Log.ERROR, logTag, "AppRxJavaErrorHandler - IllegalStateException [that's a bug in RxJava or in a custom operator]: " + throwable.getLocalizedMessage());
            handleUncaughtException(throwable);
            return;
        }
        LogWrapper.showLog(Log.ERROR, logTag, "Undeliverable exception received, not sure what to do", throwable);
    }

    private void handleUncaughtException(@NonNull final Throwable throwable) {
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.currentThread().getUncaughtExceptionHandler();
        if (null == uncaughtExceptionHandler) { return; }
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), throwable);
    }
}
