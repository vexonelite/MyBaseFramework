package tw.realtime.project.rtbaseframework.app;

import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.RandomAccessFile;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;


public abstract class IeActivityFragmentHelper {

    private final IeFragmentManagerDelegate fragmentManagerDelegate;

    public IeActivityFragmentHelper(@NonNull final IeFragmentManagerDelegate fragmentManagerDelegate) {
        this.fragmentManagerDelegate = fragmentManagerDelegate;
    }

    private String getLogTag() { return this.fragmentManagerDelegate.getClass().getSimpleName(); }

    @Nullable
    protected abstract Fragment getFragmentByTag(@Nullable String fragmentTag);
//    {
//        if (null == fragmentTag) { return null; }
//        switch (fragmentTag) {
//            case DASH_BOARD_TAG: { return dashboardFragment; }
//            case GROUP_TAG: { return groupFragment; }
//            case MODE_TAG: { return modeFragment; }
//            case SETTING_TAG: { return settingFragment; }
//            default: {
//                LogWrapper.showLog(Log.ERROR, getLogTag(), "getFragmentByTag - invalid tag [" + fragmentTag + "]");
//                return null;
//            }
//        }
//    }

    @Nullable
    protected final Fragment findFragmentByTag(@Nullable final String fragmentTag) {
        if ( (null == fragmentTag) || (fragmentTag.isEmpty()) ) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "findFragmentByTag - fragmentTag is either null or empty!!");
            return null;
        }
        return fragmentManagerDelegate.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    public final void hideExistingFragmentsIfNeeded(
            @Nullable final String fragmentTag, @Nullable final List<String> tagList) {

        if ( (null == fragmentTag) || (fragmentTag.isEmpty()) ) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "hideExistingFragmentsIfNeeded - fragmentTag is either null or empty!!");
            return;
        }
        if ( (null == tagList) || (tagList.isEmpty()) ) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "hideExistingFragmentsIfNeeded - tagList is either null or empty!!");
            return;
        }

        for(final String fragTag : tagList) {
            if (!fragmentTag.equals(fragTag)) {
                final Fragment fragment = findFragmentByTag(fragTag);
                if (null != fragment) {
                    LogWrapper.showLog(Log.INFO, getLogTag(), "hideExistingFragmentsIfNeeded - tag [" + fragTag + "]");
                    try { this.fragmentManagerDelegate.getSupportFragmentManager()
                            .beginTransaction()
                            .hide(fragment)
                            .commit();
                    }
                    catch (Exception cause) {
                        LogWrapper.showLog(Log.ERROR, getLogTag(), "hideExistingFragmentsIfNeeded - error on hide().commit() for tag [" + fragTag + "]");
                    }
                }
                else { LogWrapper.showLog(Log.ERROR, getLogTag(), "hideExistingFragmentsIfNeeded - no Fragment found for tag [" + fragTag + "]"); }
            }
        }
    }

    public final int addOrShowFragmentBy(@Nullable final String fragmentTag, @IdRes final int fragmentViewId) {
        final Fragment fragment = findFragmentByTag(fragmentTag);
        if (null != fragment) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "addOrShowFragmentBy - show for tag [" + fragmentTag + "]");
            try {
                this.fragmentManagerDelegate.getSupportFragmentManager()
                        .beginTransaction()
                        .show(fragment)
                        .commit();
                return 2;
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "addOrShowFragmentBy - error on show().commit() for tag [" + fragmentTag + "]");
                return 0;
            }
        }
        else {
            LogWrapper.showLog(Log.INFO, getLogTag(), "addOrShowFragmentBy - add for tag [" + fragmentTag + "]");
            final Fragment fragment2 = getFragmentByTag(fragmentTag);
            if (null != fragment2) {
                try {
                    this.fragmentManagerDelegate.getSupportFragmentManager()
                            .beginTransaction()
                            .add(fragmentViewId, fragment2, fragmentTag)
                            .commit();
                    return 1;
                }
                catch (Exception cause) {
                    LogWrapper.showLog(Log.ERROR, getLogTag(), "addOrShowFragmentBy - error on add().commit() for tag [" + fragmentTag + "]");
                    return 0;
                }
            }
            else { return 0; }
        }
    }
}
