package tw.com.goglobal.project.httpstuff;

/**
 * OkHttp 的設定
 */
public interface OkHttpSetting {
    long CONNECTION_TIME = 60000;
    long READ_TIMEOUT = 120000;
    long WRITE_TIMEOUT = 120000;
}

