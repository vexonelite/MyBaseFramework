package tw.realtime.project.rtbaseframework.widgets.viewholders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.delegates.ui.tab.TabItemDelegate;
import tw.realtime.project.rtbaseframework.delegates.ui.tab.TabViewDelegate;

public abstract class BasePagerTabLayoutViewHolder<T extends TabItemDelegate> {

    private final TabLayout tabLayout;

    private final ViewPager viewPager;

    private final TabViewDelegate<T> tabViewDelegate;

    public UiInitializationDelegate uiInitializationDelegate;

    public TabUpdateDelegate tabUpdateDelegate;

    private boolean isInitializing;

    private final TabLayout.OnTabSelectedListener tabSelectedCallback = new TabSelectedCallback();
    private final ViewPager.OnPageChangeListener pagerPageChangeCallback = new PagerPageChangeCallback();


    protected final String getLogTag () {
        return this.getClass().getSimpleName();
    }

    public BasePagerTabLayoutViewHolder(@NonNull TabLayout tabLayout,
                                        @NonNull ViewPager viewPager,
                                        @NonNull PagerAdapter pagerAdapter,
                                        @NonNull TabViewDelegate<T> tabViewDelegate) {
        isInitializing = true;

        this.tabViewDelegate = tabViewDelegate;

        this.viewPager = viewPager;
        viewPager.setAdapter(pagerAdapter);
        //addDefaultOnPageChangeListener();
        //viewPager.setOffscreenPageLimit();

        this.tabLayout = tabLayout;
        tabLayout.setTabMode(getTabModeForTabLayout());
        tabLayout.setTabGravity(getTabGravityForTabLayout());
        //addDefaultOnTabSelectedListener();
        //tabLayout.setupWithViewPager(viewPager, true);
    }

    public final void haveViewPagerSetCurrentItem(final int index) {
        //LogWrapper.showLog(Log.WARN, getLogTag(), "haveViewPagerSetCurrentItem: " + index);
        viewPager.setCurrentItem(index, true);
    }

    public final int getCurrentItemFromViewPager () {
        return viewPager.getCurrentItem();
    }

    public final void addDefaultOnPageChangeListener () {
        viewPager.addOnPageChangeListener(pagerPageChangeCallback);
    }

    public final void addCustomOnPageChangeListener (@NonNull ViewPager.OnPageChangeListener callback) {
        viewPager.addOnPageChangeListener(callback);
    }

    public final void removeDefaultOnPageChangeListener () {
        viewPager.removeOnPageChangeListener(pagerPageChangeCallback);
    }

    public final void removeCustomOnPageChangeListener (@NonNull ViewPager.OnPageChangeListener callback) {
        viewPager.removeOnPageChangeListener(callback);
    }

    @Nullable
    public abstract Fragment getFragmentFromPagerAdapter (final int index);

    protected abstract void pagerAdapterSetItemSet(@NonNull List<T> resultList);

    @Nullable
    protected abstract T getItemElementAtPositionFromPagerAdapter (final int index);

    @Nullable
    public final TabLayout.Tab getTabFromTabLayout(final int index) {
        return (null != tabLayout) ? tabLayout.getTabAt(index) : null;
    }

    // use default tab
    private void addTabsToTabLayout (@NonNull List<T> tabItemSet) {
        if (tabLayout.getTabCount() > 0) {
            tabLayout.removeAllTabs();
        }

        for (T delegate : tabItemSet) {
            tabLayout.addTab(tabLayout.newTab().setText(delegate.theDescription()));
        }
    }

    private void addCustomTabsToTabLayout (@NonNull List<T> tabItemSet) {
        if (tabLayout.getTabCount() > 0) {
            tabLayout.removeAllTabs();
        }

        for (int index = 0; index < tabItemSet.size(); index++) {
            final T delegate = tabItemSet.get(index);
            final View tabView = getCustomTabView(index, delegate);
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabView));
        }
    }

    /**
     * Keep in mind, make sure you have involved ViewPager#removeOnPageChangeListener()
     * and TabLayout#removeOnTabSelectedListener() before you involve the method.
     * Also, you might need to involve ViewPager#addOnPageChangeListener() and
     * TabLayout#addOnTabSelectedListener() after the operation has been done.
     */
    public final void replaceTabWithNewOne (int index) {
//        removeDefaultOnPageChangeListener();
//        removeDefaultOnTabSelectedListener();
        tabLayout.removeTabAt(index);
        final T delegate = getItemElementAtPositionFromPagerAdapter(index);
        final View tabView = getCustomTabView(index, delegate);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabView), index);
//        addDefaultOnTabSelectedListener();
//        addDefaultOnPageChangeListener();
//        haveViewPagerSetCurrentItem(index);
    }

    private View getCustomTabView (int index, T delegate) {
        return tabViewDelegate.getTabView(tabLayout, index, delegate);
    }

    public final void addDefaultOnTabSelectedListener () {
        tabLayout.addOnTabSelectedListener(tabSelectedCallback);
    }

    public final void addCustomOnTabSelectedListener (@NonNull TabLayout.OnTabSelectedListener callback) {
        tabLayout.addOnTabSelectedListener(callback);
    }

    public final void removeDefaultOnTabSelectedListener () {
        tabLayout.removeOnTabSelectedListener(tabSelectedCallback);
    }

    public final void removeCustomOnTabSelectedListener (@NonNull TabLayout.OnTabSelectedListener callback) {
        tabLayout.removeOnTabSelectedListener(callback);
    }

    /**
     * 處理 ViewPager 頁面變動事件
     */
    private class PagerPageChangeCallback implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onPageSelected - position: " + position);

            viewPager.setCurrentItem(position, true);
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onPageSelected - viewPager.setCurrentItem: " + position);

            // TabLayout
            final TabLayout.Tab tab = tabLayout.getTabAt(position);
            if (null != tab) {
                tab.select();
                //LogWrapper.showLog(Log.INFO, getLogTag(), "onPageSelected - tab in " + position + " is selected!");
            } else {
                LogWrapper.showLog(Log.WARN, getLogTag(), "onPageSelected - tab in " + position + " is null!");
            }
        }

        // 目前頁面改變時的狀態
        @Override
        public void onPageScrollStateChanged(int newState) {
            switch (newState) {
                case ViewPager.SCROLL_STATE_IDLE:
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_IDLE");
                    disableIsInitializingFlagIfNeeded();
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_DRAGGING");
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    //LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_SETTLING");
                    break;
            }
        }
    }

    private class TabSelectedCallback implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            final int position = tab.getPosition();
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onTabSelected - position: " + position);
            if (viewPager.getCurrentItem() != position) {
                viewPager.setCurrentItem(position, true);
            } else {
                onTabReselected(tab);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            final int position = tab.getPosition();
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onTabUnselected - position: " + position);
            if (null != tabUpdateDelegate) {
                tabUpdateDelegate.updateTabLooks(tab, position, false);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            final int position = tab.getPosition();
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onTabReselected - position: " + position);
            if (null != tabUpdateDelegate) {
                tabUpdateDelegate.updateTabLooks(tab, position, true);
            }
        }
    }

    public final void onTabDataSetAvailable (@NonNull List<T> resultList) {
        onTabDataSetAvailable(resultList, resultList.size(), true);
    }

    /**
     * When you has this method, ensure that you will involve disableIsInitializingFlagIfNeeded() later!
     */
    public final void onTabDataSetAvailable (@NonNull List<T> resultList,
                                             int offscreenPageLimit,
                                             boolean doesSetupTabLayoutWithViewPager) {
        pagerAdapterSetItemSet(resultList);
        if ( (offscreenPageLimit > 1) && (offscreenPageLimit <= resultList.size()) ) {
            viewPager.setOffscreenPageLimit(offscreenPageLimit);
        }
        if (doesSetupTabLayoutWithViewPager) {
            tabLayout.setupWithViewPager(viewPager, true);
        }
        addCustomTabsToTabLayout(resultList);
    }


    /**
     * This method must be involved after onTabDataSetAvailable() has been called!
     */
    public final void disableIsInitializingFlagIfNeeded () {
        if ( (isInitializing) && (null != uiInitializationDelegate) ) {
            isInitializing = false;
            uiInitializationDelegate.onUiHasBeenInitialized();
        }
    }


    public int getTabModeForTabLayout () {
        return TabLayout.MODE_FIXED;
    }

    public int getTabGravityForTabLayout () {
        return TabLayout.GRAVITY_FILL;
    }

    public interface UiInitializationDelegate {
        void onUiHasBeenInitialized();
    }

    public interface TabUpdateDelegate {
        void updateTabLooks(TabLayout.Tab tab, int position, boolean isSelected);
    }
}
