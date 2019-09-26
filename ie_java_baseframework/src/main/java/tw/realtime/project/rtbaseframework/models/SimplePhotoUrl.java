package tw.realtime.project.rtbaseframework.models;

import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.delegates.ui.view.PhotoUrlDelegate;

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
    public String thePhotoUrl() {
        return photoUrl;
    }
}
