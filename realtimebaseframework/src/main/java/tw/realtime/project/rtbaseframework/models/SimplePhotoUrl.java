package tw.realtime.project.rtbaseframework.models;

import android.net.Uri;
import android.support.annotation.NonNull;

import tw.realtime.project.rtbaseframework.interfaces.ui.view.PhotoUrl;

/**
 * Created by vexonelite on 2017/7/14.
 */

public final class SimplePhotoUrl implements PhotoUrl {

    private final String mUrl;

    public SimplePhotoUrl (@NonNull String url) {
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
