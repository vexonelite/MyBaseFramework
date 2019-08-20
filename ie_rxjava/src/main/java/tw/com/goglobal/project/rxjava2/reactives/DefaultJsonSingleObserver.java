package tw.com.goglobal.project.rxjava2.reactives;

import android.util.Log;

import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.IeRuntimeException;


public class DefaultJsonSingleObserver extends DisposableSingleObserver<JSONObject> {

    private String mTag;

    public DefaultJsonSingleObserver(@NonNull String tag) {
        mTag = tag;
    }

    @Override
    public void onSuccess(@NonNull JSONObject jsonObject) {
        LogWrapper.showLog(Log.INFO, mTag, "onSuccess - jsonObject: ");
        LogWrapper.showLongLog(Log.INFO, mTag, jsonObject.toString());

        if (!isDisposed()) {
            dispose();
        }
    }

    @Override
    public void onError(Throwable cause) {
        LogWrapper.showLog(Log.ERROR, mTag, "onError");

        if (cause instanceof IeRuntimeException) {
            final String code = ((IeRuntimeException) cause).getStatusCode();
            final String message = cause.getLocalizedMessage();
            LogWrapper.showLog(Log.ERROR, mTag, "code: " + code + ", message: " + message);
        }

        if (!isDisposed()) {
            dispose();
        }
    }
}
