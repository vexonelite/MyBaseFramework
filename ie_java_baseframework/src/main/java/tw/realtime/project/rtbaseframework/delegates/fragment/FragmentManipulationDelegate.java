package tw.realtime.project.rtbaseframework.delegates.fragment;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by vexonelite on 2018/09/13.
 */
public interface FragmentManipulationDelegate {
    /**
     * Fragment 的取代或蓋頁
     * @param targetFragment 要被顯示的 Fragment
     * @param doesReplace   是否要取代或蓋頁
     * @param containerResId 對應的容器 Id
     */
    void replaceOrShroudFragment (@NonNull Fragment targetFragment,
                                  boolean doesReplace,
                                  @IdRes final int containerResId);

    void popAllFragmentsIfNeeded();

    /**
     * Pop 目前畫面中的 Fragment
     */
    void popFragmentIfNeeded();
}
