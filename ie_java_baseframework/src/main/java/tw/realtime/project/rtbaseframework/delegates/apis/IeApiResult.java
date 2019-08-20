package tw.realtime.project.rtbaseframework.delegates.apis;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.apis.IeRuntimeException;


public interface IeApiResult<T> {
    void onSuccess(@NonNull T data);
    void onError(@NonNull IeRuntimeException cause);
}
