package tw.realtime.project.rtbaseframework.reactives;

/**
 * Created by vexonelite on 2017/10/3.
 */

public interface ApiParameterSetDelegate extends ApiParameterDelegate {
    ApiParameterSetDelegate setApiUrl(String apiUrl);
    ApiParameterSetDelegate setAccessToken(String accessToken);
    ApiParameterSetDelegate setDeviceToken(String deviceToken);
    ApiParameterSetDelegate setApiDataDelegate(ApiDataDelegate delegate);
}
