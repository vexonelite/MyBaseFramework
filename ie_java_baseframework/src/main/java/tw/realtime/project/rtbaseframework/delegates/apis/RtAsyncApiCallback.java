package tw.realtime.project.rtbaseframework.delegates.apis;

/**
 * 定義 執行 Api 過程中的介面
 * <p>
 * Created by vexonelite on 2017/5/25.
 * <p>
 * revision on 2018/11/19, 2019/01/30
 */
public interface RtAsyncApiCallback<T> extends AsyncApiCallback<T> {
    /** 通知 Api 開啟執行 */
    void onStart();
    /** 通知 Api 已執行結束; 之後 onSuccess(), onError() 或 onTokenError() 其中一個會被呼叫 */
     void onEnd();
    /** Status code 為 004 (Access Token 錯誤) 時會呼叫 */
    void onTokenError();
}
