package tw.realtime.project.rtbaseframework.reactives;

import android.util.Log;

import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;


public class DefaultJsonSingleObserver extends DisposableSingleObserver<JSONObject> {

    private String mTag;

    public DefaultJsonSingleObserver(@NonNull String tag) {
        mTag = tag;
    }

    @Override
    public void onSuccess(@NonNull JSONObject jsonObject) {
        LogWrapper.showLog(Log.INFO, mTag, "onSuccess - jsonObject: ");
        int maxLogSize = 1000;
        for(int i = 0; i <= jsonObject.toString().length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > jsonObject.toString().length() ? jsonObject.toString().length() : end;
            LogWrapper.showLog(Log.INFO, mTag, jsonObject.toString().substring(start, end));
        }
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
}
