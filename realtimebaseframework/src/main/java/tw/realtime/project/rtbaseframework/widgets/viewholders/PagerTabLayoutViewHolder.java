package tw.realtime.project.rtbaseframework.widgets.viewholders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;

import tw.realtime.project.rtbaseframework.adapters.viewpager.BaseFragmentPagerAdapter;
import tw.realtime.project.rtbaseframework.interfaces.ui.tab.TabItemDelegate;
import tw.realtime.project.rtbaseframework.interfaces.ui.tab.TabViewDelegate;

public abstract class PagerTabLayoutViewHolder<T extends TabItemDelegate> {

    private final TabLayout tabLayout;
    private final ViewPager viewPager;
    // this is view pager version of adapter
    private final BaseFragmentPagerAdapter<T> pagerAdapter;
    private final TabViewDelegate<T> tabViewDelegate;

    public UiInitializationDelegate uiInitializationDelegate;
    public TabUpdateDelegate tabUpdateDelegate;

    //private int currentPagePosition = 0;
    private boolean isInitializing = false;

    protected final String getLogTag () {
        return this.getClass().getSimpleName();
    }

    public PagerTabLayoutViewHolder(@NonNull TabLayout tabLayout,
                                    @NonNull ViewPager viewPager,
                                    @NonNull BaseFragmentPagerAdapter<T> pagerAdapter,
                                    @NonNull TabViewDelegate<T> tabViewDelegate) {
        isInitializing = true;

        this.tabViewDelegate = tabViewDelegate;

        this.pagerAdapter = pagerAdapter;

        this.viewPager = viewPager;
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new PagerPageChangeCallback());
        //viewPager.setOffscreenPageLimit();

        this.tabLayout = tabLayout;
        tabLayout.setTabMode(getTabModeForTabLayout());
        tabLayout.setTabGravity(getTabGravityForTabLayout());
        tabLayout.addOnTabSelectedListener(new TabSelectedCallback());
        //tabLayout.removeOnTabSelectedListener(onTabSelectedCallback);
        //tabLayout.setupWithViewPager(viewPager, true);

        //addTabsToTabLayout(tabItemSet)
    }

    public final void haveViewPagerSetCurrentItem(final int index) {
        if (null != viewPager) {
            viewPager.setCurrentItem(index, true);
        }
    }

    public final int getCurrentItemFromViewPager () {
        return (null != viewPager) ? viewPager.getCurrentItem() : -1;
    }

    @Nullable
    public final Fragment getFragmentFromPagerAdapter (final int index) {
        return (null != pagerAdapter)
                ? pagerAdapter.dataWrapper.getArrayItemAtPosition(index) : null;
    }

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
            tabLayout.addTab(tabLayout.newTab().setText(delegate.getDescription()));
        }
    }

    private void addCustomTabsToTabLayout (@NonNull List<T> tabItemSet) {
        if (tabLayout.getTabCount() > 0) {
            tabLayout.removeAllTabs();
        }

        for (int index = 0; index < tabItemSet.size(); index++) {
            final T delegate = tabItemSet.get(index);
            final View tabView = tabViewDelegate.getTabView(tabLayout, index, delegate);
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabView));
        }
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

            //            if (position < 0 || position >= pagerAdapter.getCount()) {
            //                LogWrapper.showLog(Log.WARN, getLogTag(), "onPageSelected - position is invalid!")
            //                return
            //            }

            //            int previousPosition = currentPagePosition;
            //            currentPagePosition = position;

            viewPager.setCurrentItem(position, true);

            // TabLayout
            final TabLayout.Tab tab = tabLayout.getTabAt(position);
            if (null != tab) {
                tab.select();
            } else {
                LogWrapper.showLog(Log.WARN, getLogTag(), "onPageSelected - tabView in " + position + " is null!");
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

    /**
     * When you has this method, ensure that you will involve disableIsInitializingFlagIfNeeded() later!
     */
    public final void onTabDataSetAvailable (@NonNull List<T> resultList) {
        pagerAdapter.dataWrapper.setItemSet(resultList);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setOffscreenPageLimit(resultList.size());
        tabLayout.setupWithViewPager(viewPager, true);
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
