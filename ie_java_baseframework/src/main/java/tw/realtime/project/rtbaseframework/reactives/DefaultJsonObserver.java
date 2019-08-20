package tw.realtime.project.rtbaseframework.reactives;

import android.util.Log;

import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import tw.realtime.project.rtbaseframework.LogWrapper;


public class DefaultJsonObserver extends DisposableObserver<JSONObject> {

    private String mTag;

    public DefaultJsonObserver(@NonNull String tag) {
        mTag = tag;
    }

    @Override
    public void onError(Throwable e) {
        LogWrapper.showLog(Log.ERROR, mTag, "onError");

        if (e instanceof AsyncApiException) {
            String code = ((AsyncApiException) e).getStatusCode();
            String message = e.getLocalizedMessage();
            LogWrapper.showLog(Log.ERROR, mTag, "code: " + code + ", message: " + message);
        }
    }

    @Override
    public void onComplete() {
        LogWrapper.showLog(Log.INFO, mTag, "onComplete");
    }

    @Override
    public void onNext(JSONObject jsonObject) {
        LogWrapper.showLog(Log.INFO, mTag, "onNext - jsonObject: ");
        int maxLogSize = 1000;
        for(int i = 0; i <= jsonObject.toString().length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > jsonObject.toString().length() ? jsonObject.toString().length() : end;
            LogWrapper.showLog(Log.INFO, mTag, jsonObject.toString().substring(start, end));
        }
    }
}
