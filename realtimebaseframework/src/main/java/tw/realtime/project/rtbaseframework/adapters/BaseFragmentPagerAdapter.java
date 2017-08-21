package tw.realtime.project.rtbaseframework.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tw.com.kingshield.baseframework.LogWrapper;


/**
 * ViewPager 搭配使用之 Adapter，其內容是一或多個 Fragment
 * Created by vexonelite on 2017/6/12.
 */
public abstract class BaseFragmentPagerAdapter<T> extends FragmentPagerAdapter {

    private final byte[] mLock = new byte[0];
    private List<T> mItemSet;
    private SparseArrayCompat<Fragment> mFragmentHolder;


    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mItemSet = new ArrayList<>();
        mFragmentHolder = new SparseArrayCompat<>();
    }


    protected String getLogTag() {
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


    /**
     * 加入新的資料 List 到 內部資料 Holder
     * @param itemSet   新的資料 List
     */
    public void addItemSet(List<T> itemSet) {
        if(null != itemSet && !itemSet.isEmpty()) {
            synchronized (mLock) {
                mItemSet.addAll(itemSet);
            }
        }
    }

    /**
     * 設定內部資料 Holder 的 Reference 到指定的資料 List
     * @param itemSet 指定的資料 List
     */
    public void setItemSet(List<T> itemSet) {
        if(null != itemSet && !itemSet.isEmpty()) {
            synchronized (mLock) {
                mItemSet = itemSet;
            }
        }
    }

    /**
     * 取得 position 所對應的資料物件
     * @param position
     * @return position 所對應的資料物件；若 position不合法，會回傳 Null
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
     * 取得 position 所對應的 Fragment
     * @param position
     * @return position 所對應的Fragment；若 position不合法，會回傳 Null
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
