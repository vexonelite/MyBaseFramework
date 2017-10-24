package tw.realtime.project.rtbaseframework.reactives;

import android.util.Log;

import java.io.IOException;
import java.net.SocketException;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import tw.realtime.project.rtbaseframework.LogWrapper;

public abstract class RxJavaErrorHandler implements Consumer<Throwable> {

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        if (throwable instanceof UndeliverableException) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "AppRxJavaErrorHandler - UndeliverableException", throwable);
            //throwable = throwable.getCause();
        }
        if ((throwable instanceof IOException) || (throwable instanceof SocketException)) {
            // fine, irrelevant network problem or API that throws on cancellation
            LogWrapper.showLog(Log.ERROR, getLogTag(), "AppRxJavaErrorHandler - IOException or SocketException", throwable);
            return;
        }
        if (throwable instanceof InterruptedException) {
            // fine, some blocking code was interrupted by a dispose call
            LogWrapper.showLog(Log.ERROR, getLogTag(), "AppRxJavaErrorHandler - InterruptedException", throwable);
            return;
        }
        if ((throwable instanceof NullPointerException) || (throwable instanceof IllegalArgumentException)) {
            // that's likely a bug in the application
            LogWrapper.showLog(Log.ERROR, getLogTag(), "AppRxJavaErrorHandler - NullPointerException or IllegalArgumentException", throwable);
            return;
        }
        if (throwable instanceof IllegalStateException) {
            // that's a bug in RxJava or in a custom operator
            LogWrapper.showLog(Log.ERROR, getLogTag(), "AppRxJavaErrorHandler - IllegalStateException", throwable);
            //return;
        }
    }
}
