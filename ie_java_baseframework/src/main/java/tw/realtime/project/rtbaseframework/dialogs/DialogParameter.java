package tw.realtime.project.rtbaseframework.dialogs;



public class DialogParameter {

    /** Banner Image Resource */
    private int mBannerResId;

    /** 對話框標題 */
    private String mTitleString;
    /** 是否設定對話框標題字型大小 */
    private boolean isEnableTitleFontSize;
    /** 對話框標題字型大小 */
    private float mTitleFontSize;
    /** 是否設定對話框標題文字顏色 */
    private boolean isEnableTitleTextColor;
    /** 對話框標題文字顏色 */
    private int mTitleTextColor;

    private boolean isEnableSubTitle;
    private String mSubTitleString;
    private boolean isEnableSubtitleFontSize;
    private float mSubtitleFontSize;
    private boolean isEnableSubtitleTextColor;
    private int mSubtitleTextColor;

    /** 是否設定對話框內容 */
    private boolean isEnableDescription;
    /** 對話框內容 */
    private String mDescriptionString;
    /** 是否設定對話框內容是置中顯示 */
    private boolean isEnableDescriptionCenterGravity;
    /** 是否設定對話框內容字型大小 */
    private boolean isEnableDescriptionFontSize;
    /** 對話框內容字型大小 */
    private float mDescriptionFontSize;
    /** 是否設定對話框內容文字色彩 */
    private boolean isEnableDescriptionTextColor = false;
    /** 對話框內容文字色彩 */
    private int mDescriptionTextColor;

    /** 對話框確定按鈕文字 */
    private String mDoneButtonTitleString;
    /** 是否設定對話框確定按鈕文字色彩 */
    private boolean isEnableDoneButtonTextColor;
    /** 對話框確定按鈕文字色彩 */
    private int mDoneButtonTextColor;
    /** 是否設定對話框確定按鈕背景色彩 */
    private boolean isEnableDoneButtonBackgroundColor;
    /** 對話框確定按鈕背景色彩 */
    private int mDoneButtonBackgroundColor;


    public String getTitle () {
        return mTitleString;
    }


    public int getBannerResourceId () {
        return mBannerResId;
    }

    public boolean isEnableTitleFontSize () {
        return isEnableTitleFontSize;
    }

    public float getTitleFontSize () {
        return mTitleFontSize;
    }

    public boolean isEnableTitleTextColor () {
        return isEnableTitleTextColor;
    }

    public int getTitleTextColor () {
        return mTitleTextColor;
    }


    public boolean isEnableSubTitle () {
        return isEnableSubTitle;
    }

    public String getSubTitle () {
        return mSubTitleString;
    }

    public boolean isEnableSubtitleFontSize () {
        return isEnableSubtitleFontSize;
    }

    public float getSubtitleFontSize () {
        return mSubtitleFontSize;
    }

    public boolean isEnableSubtitleTextColor () {
        return isEnableSubtitleTextColor;
    }

    public int getSubtitleTextColor () {
        return mSubtitleTextColor;
    }


    public boolean isEnableDescription () {
        return isEnableDescription;
    }

    public String getDescription () {
        return mDescriptionString;
    }

    public boolean isEnableDescriptionCenterGravity () {
        return isEnableDescriptionCenterGravity;
    }

    public boolean isEnableDescriptionFontSize () {
        return isEnableDescriptionFontSize;
    }

    public float getDescriptionFontSize () {
        return mDescriptionFontSize;
    }

    public boolean isEnableDescriptionTextColor () {
        return isEnableDescriptionTextColor;
    }

    public int getDescriptionTextColor () {
        return mDescriptionTextColor;
    }


    public String getDoneButtonTitle () {
        return mDoneButtonTitleString;
    }

    public boolean isEnableDoneButtonTextColor () {
        return isEnableDoneButtonTextColor;
    }

    public int getDoneButtonTextColor () {
        return mDoneButtonTextColor;
    }

    public int getDoneButtonBackgroundColor () {
        return mDoneButtonBackgroundColor;
    }


    public boolean isEnableDoneButtonBackgroundColor () {
        return isEnableDoneButtonBackgroundColor;
    }


    public static class Builder {

        /** Banner Image Resource */
        private int bBannerResId;

        /** 對話框標題 */
        private String bTitleString;
        /** 是否設定對話框標題字型大小 */
        private boolean isEnableTitleFontSize = false;
        /** 對話框標題字型大小 */
        private float bTitleFontSize;
        /** 是否設定對話框標題文字顏色 */
        private boolean isEnableTitleTextColor = false;
        /** 對話框標題文字顏色 */
        private int bTitleTextColor;

        private boolean isEnableSubTitle;
        private String bSubTitleString;
        private boolean isEnableSubtitleFontSize;
        private float bSubtitleFontSize;
        private boolean isEnableSubtitleTextColor;
        private int bSubtitleTextColor;

        /** 是否設定對話框內容 */
        private boolean isEnableDescription;
        /** 對話框內容 */
        private String bDescriptionString;
        /** 是否設定對話框內容是置中顯示 */
        private boolean isEnableDescriptionCenterGravity = false;
        /** 是否設定對話框內容字型大小 */
        private boolean isEnableDescriptionFontSize = false;
        /** 對話框內容字型大小 */
        private float bDescriptionFontSize;
        /** 是否設定對話框內容文字色彩 */
        private boolean isEnableDescriptionTextColor = false;
        /** 對話框內容文字色彩 */
        private int bDescriptionTextColor;


        /** 對話框確定按鈕文字 */
        private String bDoneButtonTitleString;
        /** 是否設定對話框確定按鈕文字色彩 */
        private boolean isEnableDoneButtonTextColor = false;
        /** 對話框確定按鈕文字色彩 */
        private int bDoneButtonTextColor;
        /** 是否設定對話框確定按鈕背景色彩 */
        private boolean isEnableDoneButtonBackgroundColor = false;
        /** 對話框確定按鈕背景色彩 */
        private int bDoneButtonBackgroundColor;


        public Builder setBannerResourceId (int resourceId) {
            bBannerResId = resourceId;
            return this;
        }

        public Builder setTitle (String title) {
            bTitleString = title;
            return this;
        }

        public Builder setEnableTitleFontSize (boolean flag) {
            isEnableTitleFontSize = flag;
            return this;
        }

        public Builder setTitleFontSize (float fontSize) {
            bTitleFontSize = fontSize;
            return this;
        }

        public Builder setEnableSubTitleFlag (boolean flag) {
            isEnableSubTitle = flag;
            return this;
        }

        public Builder setEnableTitleTextColor (boolean flag) {
            isEnableTitleTextColor = flag;
            return this;
        }

        public Builder setTitleTextColor (int color) {
            bTitleTextColor = color;
            return this;
        }

        public Builder setSubTitle (String subtitle) {
            bSubTitleString = subtitle;
            return this;
        }

        public Builder setEnableSubtitleFontSize (boolean flag) {
            isEnableSubtitleFontSize = flag;
            return this;
        }

        public Builder setSubtitleFontSize (float fontSize) {
            bSubtitleFontSize = fontSize;
            return this;
        }

        public Builder setEnableSubtitleTextColor (boolean flag) {
            isEnableSubtitleTextColor = flag;
            return this;
        }

        public Builder setSubtitleTextColor (int color) {
            bSubtitleTextColor = color;
            return this;
        }

        public Builder setEnableDescriptionFlag (boolean flag) {
            isEnableDescription = flag;
            return this;
        }

        public Builder setDescription (String description) {
            bDescriptionString = description;
            return this;
        }

        public Builder setEnableDescriptionCenterGravity (boolean flag) {
            isEnableDescriptionCenterGravity = flag;
            return this;
        }

        public Builder setEnableDescriptionFontSize (boolean flag) {
            isEnableDescriptionFontSize = flag;
            return this;
        }

        public Builder setDescriptionFontSize (float fontSize) {
            bDescriptionFontSize = fontSize;
            return this;
        }

        public Builder setEnableDescriptionTextColor (boolean flag) {
            isEnableDescriptionTextColor = flag;
            return this;
        }

        public Builder setDescriptionTextColor (int color) {
            bDescriptionTextColor = color;
            return this;
        }


        public Builder setDoneButtonTitle (String title) {
            bDoneButtonTitleString = title;
            return this;
        }

        public Builder setEnableDoneButtonTextColor (boolean flag) {
            isEnableDoneButtonTextColor = flag;
            return this;
        }

        public Builder setDoneButtonTextColor (int color) {
            bDoneButtonTextColor = color;
            return this;
        }

        public Builder setEnableDoneButtonBackgroundColor (boolean flag) {
            isEnableDoneButtonBackgroundColor = flag;
            return this;
        }

        public Builder setDoneButtonBackgroundColor (int color) {
            bDoneButtonBackgroundColor = color;
            return this;
        }

        public DialogParameter build() {
            return new DialogParameter(this);
        }
    }

    private DialogParameter(Builder builder) {
        mBannerResId = builder.bBannerResId;

        mTitleString = builder.bTitleString;
        isEnableTitleFontSize = builder.isEnableTitleFontSize;
        mTitleFontSize = builder.bTitleFontSize;
        isEnableTitleTextColor = builder.isEnableTitleTextColor;
        mTitleTextColor = builder.bTitleTextColor;

        isEnableSubTitle = builder.isEnableSubTitle;
        mSubTitleString = builder.bSubTitleString;
        isEnableSubtitleFontSize = builder.isEnableSubtitleFontSize;
        mSubtitleFontSize = builder.bSubtitleFontSize;
        isEnableSubtitleTextColor = builder.isEnableSubtitleTextColor;
        mSubtitleTextColor = builder.bSubtitleTextColor;

        isEnableDescription = builder.isEnableDescription;
        mDescriptionString = builder.bDescriptionString;
        isEnableDescriptionCenterGravity = builder.isEnableDescriptionCenterGravity;
        isEnableDescriptionFontSize = builder.isEnableDescriptionFontSize;
        mDescriptionFontSize = builder.bDescriptionFontSize;
        isEnableDescriptionTextColor = builder.isEnableDescriptionTextColor;
        mDescriptionTextColor = builder.bDescriptionTextColor;

        mDoneButtonTitleString = builder.bDoneButtonTitleString;
        mDoneButtonTextColor = builder.bDoneButtonTextColor;
        isEnableDoneButtonTextColor = builder.isEnableDoneButtonTextColor;
        mDoneButtonBackgroundColor = builder.bDoneButtonBackgroundColor;
        isEnableDoneButtonBackgroundColor = builder.isEnableDoneButtonBackgroundColor;
    }
}
