package tw.realtime.project.rtbaseframework.delegates.fragment;

/**
 * @see <a href="https://tausiq.wordpress.com/2014/06/06/android-multiple-fragments-stack-in-each-viewpager-tab/">Android: Multiple Fragments stack in each ViewPager Tab</a>
 * <p>
 * Created by shahabuddin on 2014/06/06.
 */
public interface OnBackPressedDelegate {
    boolean onBackPressed();

    /*
    @Override
    public boolean onBackPressed() {
        final int childCount = getChildFragmentManager().getBackStackEntryCount();
        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false;

        } else {
            getChildFragmentManager().popBackStackImmediate();
            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true;
        }
    }
    */

    /*
    @Override
    public boolean onBackPressed() {
        if (parentFragment == null) return false;

        int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();

        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false;

        } else {
            // get the child Fragment
            FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
            OnBackPressListener childFragment = (OnBackPressListener) childFragmentManager.getFragments().get(0);

            // propagate onBackPressed method call to the child Fragment
            if (!childFragment.onBackPressed()) {
                // child Fragment was unable to handle the task
                // It could happen when the child Fragment is last last leaf of a chain
                // removing the child Fragment from stack
                childFragmentManager.popBackStackImmediate();

            }

            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true;
        }
    }
    */
}
