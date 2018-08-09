package tw.realtime.project.rtbaseframework.adapters.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import tw.realtime.project.rtbaseframework.adapters.PagerDataWrapper;
import tw.realtime.project.rtbaseframework.interfaces.ui.tab.TabItemDelegate;


public abstract class BaseFragmentPagerAdapter<T extends TabItemDelegate> extends FragmentPagerAdapter {

    public final PagerDataWrapper<T, Fragment> dataWrapper = new PagerDataWrapper<>();

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    final protected String getLogTag() {
        return this.getClass().getSimpleName();
    }


    /**
     * Return the number of views available.
     */
    @Override public final int getCount() {
        return dataWrapper.getRealDataCount();
    }

    @Override public final int getItemPosition(@NonNull Object objectX) {
        return PagerAdapter.POSITION_NONE;
    }

    @NonNull
    @Override public final Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object holder = super.instantiateItem(container, position);
        if (holder instanceof Fragment) {
            Fragment fragment = (Fragment) holder;
            dataWrapper.putArrayItemAtPosition(fragment, position);
//            LogWrapper.showLog(Log.INFO, getLogTag(), "instantiateItem - position: " + position
//                    + ", tag: " + holder.getClass().getSimpleName());
        }
        return holder;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object objectX) {
        super.destroyItem(container, position, objectX);
        dataWrapper.removeArrayItemAtPosition(position);
        //LogWrapper.showLog(Log.INFO, getLogTag(), "destroyItem - position: $position");
    }

//    @Override
//    public Fragment getItem(int position) {
//        TabItemDelegate tabItem = dataWrapper.getItemElementAtPosition(position);
//        return CategoryFragment()
//    }
}
