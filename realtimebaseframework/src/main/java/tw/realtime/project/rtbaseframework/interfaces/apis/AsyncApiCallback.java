package tw.realtime.project.rtbaseframework.interfaces.apis;

import androidx.annotation.NonNull;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;

/**
 * 定義 執行 Api 過程中的介面
 * <p>Created by vexonelite on 2019/01/30.
 */
public interface AsyncApiCallback<T> {
    /** Status code 為 200 時，傳為對應的資料 */
    void onSuccess(@NonNull T t);
    /** Status code 非 200 或 004 以外的例外情況 */
    void onError(@NonNull AsyncApiException e);
}
