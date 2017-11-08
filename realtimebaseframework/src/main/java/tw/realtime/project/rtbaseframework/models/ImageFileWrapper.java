package tw.realtime.project.rtbaseframework.models;


public class ImageFileWrapper {

    private String mExtension;
    private String mFileName;
    private byte[] mImgByteArray;
    private int mOrientation;


    public String getFileExtension () {
        return mExtension;
    }

    public String getFileName () {
        return mFileName;
    }

    public byte[] getImageByteArray () {
        //return mImgByteArray;
        return mImgByteArray.clone();
    }

    public int getOrientation () {
        return mOrientation;
    }

    public boolean isValid () {
        return ( (null != mImgByteArray) && (null != mExtension) && (null != mFileName) );
    }


    public static class Builder {
        private String bExtension;
        private String bFileName;
        private byte[] bImgByteArray;
        private int bOrientation = 0;

        public Builder setFileExtension (String extension) {
            bExtension = extension;
            return this;
        }

        public Builder setFileName (String fileName) {
            bFileName = fileName;
            return this;
        }

        public Builder setImageByteArray (byte[] imgByteArray) {
            if (null != imgByteArray) {
                bImgByteArray = imgByteArray.clone();
            }
            //bImgByteArray = imgByteArray;
            return this;
        }

        public Builder setOrientation (int orientation) {
            if ( (orientation == 90) || (orientation == 180) || (orientation == 270) ) {
                bOrientation = orientation;
            }
            return this;
        }

        public boolean isValid () {
            return ( (null != bImgByteArray) && (null != bExtension) && (null != bFileName) );
        }

        public ImageFileWrapper build () {
            return new ImageFileWrapper(this);
        }
    }

    private ImageFileWrapper(Builder builder) {
        mExtension = builder.bExtension;
        mFileName = builder.bFileName;
        mImgByteArray = builder.bImgByteArray;
        mOrientation = builder.bOrientation;
    }

}
