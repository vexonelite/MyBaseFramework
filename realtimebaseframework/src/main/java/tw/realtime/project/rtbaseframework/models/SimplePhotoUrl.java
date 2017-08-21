package tw.realtime.project.rtbaseframework.models;

import android.net.Uri;

import tw.com.kingshield.baseframework.interfaces.PhotoUrl;

/**
 * Created by vexonelite on 2017/7/14.
 */

public class SimplePhotoUrl implements PhotoUrl {

    private String mUrl;
    public SimplePhotoUrl (String url) {
        mUrl = url;
    }

    @Override
    public String onPhotoUrlIsRequired() {
        return mUrl;
    }

    @Override
    public Uri onPhotoUriIsRequired() {
        return null;
    }

    @Override
    public int onPhotoResourceIdIsRequired() {
        return 0;
    }
}
