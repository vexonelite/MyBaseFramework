package tw.realtime.project.rtbaseframework.models;

/**
 * Created by vexonelite on 2016/10/17.
 */
public class SideMenuData {

    private int mIconResId;
    private String mTitle;
    private String mBarTitle;
    private int mItemNumber;


    public int getIconResourceId () {
        return mIconResId;
    }

    public String getMenuTitle () {
        return mTitle;
    }

    public String getMenuBarTitle () {
        return mBarTitle;
    }

    public int getIconNumber () {
        return mItemNumber;
    }


    public static class Builder {
        private int bIconResId;
        private String bTitle;
        private String bBarTitle;
        private int bItemNumber;

        public Builder setIconResourceId (int resId) {
            bIconResId = resId;
            return this;
        }

        public Builder setMenuTitle (String title) {
            bTitle = title;
            return this;
        }

        public Builder setMenuBarTitle (String bartitle) {
            bBarTitle = bartitle;
            return this;
        }

        public Builder setItemNumber (int number) {
            bItemNumber = number;
            return this;
        }

        public SideMenuData build() {
            return new SideMenuData(this);
        }
    }

    private SideMenuData(Builder builder) {
        mIconResId = builder.bIconResId;
        mTitle = builder.bTitle;
        mBarTitle = builder.bBarTitle;
        mItemNumber = builder.bItemNumber;
    }
}
