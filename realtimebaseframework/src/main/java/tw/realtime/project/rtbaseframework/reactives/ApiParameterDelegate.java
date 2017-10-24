package tw.realtime.project.rtbaseframework.reactives;

/**
 * Created by vexonelite on 2017/10/3.
 */

public interface ApiParameterDelegate {
    boolean doesEnableAesEncoding();
    boolean doesEnableHttpLog();
    String getApiUrl();
    String getAccessToken();
    String getDeviceToken();
    ApiDataDelegate getApiDataDelegate();
}
