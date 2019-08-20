package tw.realtime.project.rtbaseframework.delegates.ui.view;

import android.net.Uri;

public interface PhotoUrl {
    String onPhotoUrlIsRequired();
    Uri onPhotoUriIsRequired();
    int onPhotoResourceIdIsRequired();
}
