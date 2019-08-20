package tw.realtime.project.rtbaseframework.apis;

import androidx.annotation.NonNull;


public interface IeApiResult<T> {
    void onSuccess(@NonNull T data);
    void onError(@NonNull IeRuntimeException cause);
}
