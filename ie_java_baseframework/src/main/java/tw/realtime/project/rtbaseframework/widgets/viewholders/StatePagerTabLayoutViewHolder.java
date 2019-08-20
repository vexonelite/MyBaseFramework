package tw.realtime.project.rtbaseframework.widgets.viewholders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import tw.realtime.project.rtbaseframework.adapters.viewpager.BaseFragmentStatePagerAdapter;
import tw.realtime.project.rtbaseframework.delegates.ui.tab.TabItemDelegate;
import tw.realtime.project.rtbaseframework.delegates.ui.tab.TabViewDelegate;


public abstract class StatePagerTabLayoutViewHolder<T extends TabItemDelegate>
        extends BasePagerTabLayoutViewHolder<T> {

    // this is view pager version of adapter
    private final BaseFragmentStatePagerAdapter<T> pagerAdapter;

    public StatePagerTabLayoutViewHolder(@NonNull TabLayout tabLayout,
                                         @NonNull ViewPager viewPager,
                                         @NonNull BaseFragmentStatePagerAdapter<T> pagerAdapter,
                                         @NonNull TabViewDelegate<T> tabViewDelegate) {
        super(tabLayout, viewPager, pagerAdapter, tabViewDelegate);
        this.pagerAdapter = pagerAdapter;
    }

    @Nullable
    public final Fragment getFragmentFromPagerAdapter (final int index) {
        return (null != pagerAdapter)
                ? pagerAdapter.dataWrapper.getArrayItemAtPosition(index) : null;
    }

    @Override
    protected final void pagerAdapterSetItemSet(@NonNull List<T> resultList) {
        pagerAdapter.dataWrapper.setItemSet(resultList);
        pagerAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    protected final T getItemElementAtPositionFromPagerAdapter (final int index) {
        return pagerAdapter.dataWrapper.getItemElementAtPosition(index);
    }

}
