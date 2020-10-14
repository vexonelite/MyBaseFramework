package tw.com.goglobal.project.httpstuff;

public final class OkHttpConfigure {

    public final long connectionTimeOut;
    public final long readTimeOut;
    public final long writeTimeOut;
    public final boolean redirectsFlag;
    public final boolean sslRedirectsFlag;

    public OkHttpConfigure() {
        this.connectionTimeOut = OkHttpSetting.CONNECTION_TIME;
        this.readTimeOut = OkHttpSetting.READ_TIMEOUT;
        this.writeTimeOut = OkHttpSetting.WRITE_TIMEOUT;
        this.redirectsFlag = true;
        this.sslRedirectsFlag = true;
    }

    public OkHttpConfigure(final long connectionTimeOut, final long readTimeOut, final long writeTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
        this.readTimeOut = readTimeOut;
        this.writeTimeOut = writeTimeOut;
        this.redirectsFlag = true;
        this.sslRedirectsFlag = true;
    }

    public OkHttpConfigure(final long connectionTimeOut,
                           final long readTimeOut,
                           final long writeTimeOut,
                           final boolean redirectsFlag,
                           final boolean sslRedirectsFlag) {
        this.connectionTimeOut = connectionTimeOut;
        this.readTimeOut = readTimeOut;
        this.writeTimeOut = writeTimeOut;
        this.redirectsFlag = redirectsFlag;
        this.sslRedirectsFlag = sslRedirectsFlag;
    }
}
