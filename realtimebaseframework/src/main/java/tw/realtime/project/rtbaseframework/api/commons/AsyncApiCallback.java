package tw.realtime.project.rtbaseframework.api.commons;

import androidx.annotation.NonNull;

/**
 * 定義 執行 Api 過程中的介面
 * <p>
 * Created by vexonelite on 2017/5/25.
 */
public interface AsyncApiCallback<T> {
    /** 通知 Api 開啟執行 */
    void onStart();
    /** 通知 Api 已執行結束; 之後 onSuccess(), onError() 或 onTokenError() 其中一個會被呼叫 */
     void onEnd();
    /** Status code 為 200 時，傳為對應的資料 */
    void onSuccess(@NonNull T t);
    /** Status code 非 200 或 004 以外的例外情況 */
    void onError(AsyncApiException e);
    /** Status code 為 004 (Access Token 錯誤) 時會呼叫 */
    void onTokenError();
}
