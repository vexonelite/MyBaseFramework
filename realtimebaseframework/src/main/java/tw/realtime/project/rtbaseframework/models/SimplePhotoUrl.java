package tw.realtime.project.rtbaseframework.models;

import android.support.annotation.NonNull;

import tw.realtime.project.rtbaseframework.interfaces.ui.view.PhotoUrlDelegate;

/**
 * Created by vexonelite on 2017/7/14.
 */

public final class SimplePhotoUrl implements PhotoUrlDelegate {

    private final String photoUrl;

    public SimplePhotoUrl (@NonNull String url) {
        photoUrl = url;
    }

    @NonNull
    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }
}
