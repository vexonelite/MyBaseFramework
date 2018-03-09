package tw.realtime.project.rtbaseframework.models;

/**
 * Created by vexonelite on 2016/10/17.
 */
public class SideMenuData {

    private int mIconResId;
    private String mTitle;
    private String mBarTitle;
    private int mItemNumber;
    private int mItemType;


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

    /**
     * return the type of menu item.
     * The type is used by the getItemViewType method of adapter class.
     */
    public int getItemType () {
        return mItemType;
    }

    public static class Builder {
        private int bIconResId;
        private String bTitle;
        private String bBarTitle;
        private int bItemNumber;
        private int bItemType = 0;

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

        /**
         * set type of menu item.
         * The type is used by the getItemViewType method of adapter class.
         */
        public Builder setItemType (int type) {
            bItemType = type;
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
        mItemType = builder.bItemType;
    }
}
