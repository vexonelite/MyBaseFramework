package tw.com.goglobal.project.rxjava2.reactives;

import android.util.Log;

import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.apis.errors.IeHttpException;


public final class DefaultJsonObserver extends DisposableObserver<JSONObject> {

    private String mTag;

    public DefaultJsonObserver(@NonNull String tag) {
        mTag = tag;
    }

    @Override
    public void onError(Throwable cause) {
        LogWrapper.showLog(Log.ERROR, mTag, "onError");

        if (cause instanceof IeHttpException) {
            final String code = ((IeHttpException) cause).getStatusCode();
            final String message = cause.getLocalizedMessage();
            LogWrapper.showLog(Log.ERROR, mTag, "code: " + code + ", message: " + message);
        }
    }

    @Override
    public void onComplete() {
        LogWrapper.showLog(Log.INFO, mTag, "onComplete");
    }

    @Override
    public void onNext(@NonNull JSONObject jsonObject) {
        LogWrapper.showLog(Log.INFO, mTag, "onNext - jsonObject: ");
        LogWrapper.showLongLog(Log.INFO, mTag, jsonObject.toString());
    }
}
