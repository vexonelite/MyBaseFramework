package tw.realtime.project.rtbaseframework.models;

import android.net.Uri;

import tw.realtime.project.rtbaseframework.interfaces.PhotoUrl;


/**
 * Created by vexonelite on 2016/10/23.
 */

public class DefaultPhotoUrl implements PhotoUrl {

    private String mUrl;
    private Uri mUri;
    private int mResId = 0;
    private int mPosition;


    @Override
    public String onPhotoUrlIsRequired() {
        return mUrl;
    }

    @Override
    public Uri onPhotoUriIsRequired() {
        return mUri;
    }

    @Override
    public int onPhotoResourceIdIsRequired() {
        return mResId;
    }

    public int getPosition() {
        return mPosition;
    }


    public static class Builder {
        private String bUrl;
        private Uri bUri;
        private int bResId = 0;
        private int bPosition;

        public Builder setPhotoResourceId (int resId) {
            bResId = resId;
            return this;
        }

        public Builder setPhotoUrl (String url) {
            bUrl = url;
            return this;
        }

        public Builder setPhotoUri (Uri uri) {
            bUri = uri;
            return this;
        }

        public Builder setPosition (int position) {
            if (position >= 0) {
                bPosition = position;
            }
            return this;
        }

        public DefaultPhotoUrl build() {
            return new DefaultPhotoUrl(this);
        }
    }

    private DefaultPhotoUrl(Builder builder) {
        mResId = builder.bResId;
        mUrl = builder.bUrl;
        mUri = builder.bUri;
        mPosition = builder.bPosition;
    }
}
