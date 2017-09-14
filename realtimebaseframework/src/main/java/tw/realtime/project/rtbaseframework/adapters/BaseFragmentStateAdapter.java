package tw.realtime.project.rtbaseframework.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * Base adapter that has some common features and is set to the ViewPager
 */
public abstract class BaseFragmentStateAdapter<T> extends FragmentStatePagerAdapter {

    private final byte[] mLock = new byte[0];
    private List<T> mItemSet;
    private SparseArrayCompat<Fragment> mFragmentHolder;


    public BaseFragmentStateAdapter(FragmentManager fm) {
        super(fm);
        mItemSet = new ArrayList<>();
        mFragmentHolder = new SparseArrayCompat<>();
    }

    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }


    @Override
    public int getCount() {
        return mItemSet.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Object holder = super.instantiateItem(container, position);
        if (holder instanceof Fragment) {
            Fragment fragment = (Fragment)holder;
            synchronized(mLock) {
                mFragmentHolder.put(position, fragment);
            }
            LogWrapper.showLog(Log.INFO, getLogTag(), "instantiateItem - position: " + position
            		+ ", tag: " + fragment.getClass().getSimpleName() );
        }
        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        synchronized(mLock) {
            mFragmentHolder.remove(position);
            LogWrapper.showLog(Log.INFO, getLogTag(), "destroyItem - position: " + position);
        }
    }


    /** append the given list of data to the tail of data holder in the adapter. */
    public void addItemSet (List<T> itemSet) {
        if ( (null != itemSet) && (!itemSet.isEmpty()) ) {
            synchronized (mLock) {
                mItemSet.addAll(itemSet);
            }
        }
    }

    /** replace the given list of data with the current data holder in the adapter. */
    public void setItemSet (List<T> itemSet) {
        if ( (null != itemSet) && (!itemSet.isEmpty()) ) {
            synchronized (mLock) {
                mItemSet = itemSet;
            }
        }
    }

    /**
     * get the corresponding element in the data holder by the given position.
     * If it has not existed, 'null' will be return.
     */
    public T getItemSetElement (int position) {
        if ( (position >= 0) && (position < mItemSet.size()) ) {
            return mItemSet.get(position);
        }
        else {
            return null;
        }
    }

    /**
     * get the corresponding Fragment in the Fragment holder by the given position.
     * If it has not existed, 'null' will be return.
     */
    public Fragment getRegisteredFragment(int position) {
        LogWrapper.showLog(Log.INFO, getLogTag(), "getRegisteredFragment");
        try {
            return mFragmentHolder.get(position);
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on getRegisteredFragment!", e);
            return null;
        }
    }

}

