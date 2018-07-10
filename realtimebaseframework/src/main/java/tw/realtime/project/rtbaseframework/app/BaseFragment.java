package tw.realtime.project.rtbaseframework.app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.dialogs.ProgressDialog;


public abstract class BaseFragment extends Fragment {

    private boolean hasBeenShroudedByChild;

    private ProgressDialog mProgressDialog;

    private onViewDestroyListener mCallback;


    public interface onViewDestroyListener {
        /**
         * 通知目前 Fragment 的 onDestroyView 已被執行
         */
        void onDestroyViewGetCalled();
    }


    /**
     * 設定目前 Fragment 是否被接下來的 Fragment 給蓋頁
     * @param flag
     */
    protected void setHasBeenShroudedByChildFlag (boolean flag) {
        hasBeenShroudedByChild = flag;
    }

    /**
     * 取得目前 Fragment 是否被其它的 Fragment 蓋頁
     */
    protected boolean getHasBeenShroudedByChildFlag () {
        return hasBeenShroudedByChild;
    }

    public void setonViewDestroyListener (onViewDestroyListener callback) {
        mCallback = callback;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (null != mCallback) {
                mCallback.onDestroyViewGetCalled();
            }
        } catch (Exception e) {
            // 防止閃退；當 mCallback 中做了某些會閃退的事情
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onDestroyView", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }
    

    public String getLogTag() {
        return this.getClass().getSimpleName();
    }


    @Deprecated
    protected void setProgressDialog(Context context, String title) {
        ProgressDialog.show(context, null, title, true, false);
    }

    /**
     * 顯示正在處理某事件之對話框
     */
    protected void showProgressDialog (String title) {
        if (isAdded() && (null != getActivity()) ) {
            if (null == mProgressDialog) {
                mProgressDialog = new ProgressDialog(getActivity(), title);
            }
            else {
                mProgressDialog.cancel();
                //mProgressDialog.dismiss();
                mProgressDialog.setTitle(title);
            }
            mProgressDialog.show();
        }
    }

    /**
     * 隱藏正在處理某事件之對話框
     */
    protected void dismissProgressDialog () {
        if (null != mProgressDialog) {
            mProgressDialog.cancel();
            //mProgressDialog = null;
        }
    }

    protected void showAlertDialog (boolean isSingleOption,
                                    String title,
                                    String message,
                                    String positiveText,
                                    String negativeText,
                                    DialogInterface.OnClickListener positiveCallback,
                                    DialogInterface.OnClickListener negativeCallback) {
        if (!isAdded()) {
            return;
        }
        BaseActivity activity = (BaseActivity) getActivity();
        if (null == activity) {
            return;
        }
        activity.showAlertDialog(
                isSingleOption,
                title,
                message,
                positiveText,
                negativeText,
                positiveCallback,
                negativeCallback);
    }

    /**
     * 設定 ActionBar 的標題，並決定Icon 是 "<" 或 App Icon
     * @param title
     * @param homeButtonEnabledFlag
     * @param homeAsUpEnabledFlag
     */
    protected void setUpActionBar (String title, boolean homeButtonEnabledFlag, boolean homeAsUpEnabledFlag) {
        if ( (null == title) || (title.isEmpty()) || (!isAdded()) || (hasBeenShroudedByChild) ) {
            return;
        }
        //LogWrapper.showLog(Log.INFO, getLogTag(), "setUpActionBar");

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        ActionBar actionBar = activity.getSupportActionBar();
        boolean isTargetActivity = isTargetActivity();
        if (null != actionBar) {
            actionBar.setTitle(title);
            if (!isTargetActivity) {
                actionBar.setHomeButtonEnabled(homeButtonEnabledFlag);
                actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabledFlag);
            }
            else {
                actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabledFlag);
            }
        }
    }

    /**
     * 讓 Subclass的 Fragment 有機會決定這件事
     * @return 是不是 Fragment attached 的 Activity
     */
    protected boolean isTargetActivity() {
        return false;
    }

    /**
     * 將軟鍵盤隱藏
     */
    protected void hideSoftKeyboard () {
        if (!isAdded()) {
            return;
        }
        BaseActivity activity = (BaseActivity) getActivity();
        if (null == activity) {
            return;
        }
        activity.hideSoftKeyboard();
    }

    protected boolean isAllowedToCommitFragmentTransaction () {
        if (!isAdded()) {
            return false;
        }
        if (null == getActivity()) {
            return false;
        }
        BaseActivity activity = (BaseActivity) getActivity();
        return activity.isAllowedToCommitFragmentTransaction();
    }

    protected void showDialogFragment (DialogFragment dialogFragment) {
        if (null == dialogFragment) {
            return;
        }
        if (!isAllowedToCommitFragmentTransaction()) {
            return;
        }
        // DialogFragment.show() will take care of adding the fragment in a transaction.
        // We also want to remove any currently showing dialog,
        // so make our own transaction and take care of that here.
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentByTag(dialogFragment.getClass().getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, dialogFragment.getClass().getSimpleName());
    }


    /**
     * 預設實作好，給確定或取消選項之 Callback，點下會令目前 Fragment 被 Pop
     */
    public class BackPressedCallback implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    backToPrevious();
                }
            });
        }
    }

    /**
     * 預設實作好，給確定或取消選項之 Callback，點下會令目前 Fragment 被 Pop
     */
    public class BackPressedCallback2 implements BaseDialogFragment.OnDecisionMadeListener {
        @Override
        public void onNotification(DialogFragment dialogFrag, BaseDialogFragment.DialogAction dialogAction) {
            dialogFrag.dismiss();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    backToPrevious();
                }
            });
        }
    }

    /**
     * 預設實作好，給確定或取消選項之 Callback，點下會令目前 Fragment 被 Pop
     */
    public class DefaultDismissCallback implements BaseDialogFragment.OnDecisionMadeListener {
        @Override
        public void onNotification(DialogFragment dialogFrag, BaseDialogFragment.DialogAction dialogAction) {
            dialogFrag.dismiss();
        }
    }


    /**
     * 呼叫 Attached Activity 的 onBackPressed 方法
     */
    protected void backToPrevious () {
        if (isAdded()) {
            Activity activity = getActivity();
            if (null != activity) {
                activity.onBackPressed();
            }
        }
    }


    protected void conductNavigation (@NonNull BaseFragment fragment,
                                      @IdRes int fragmentContainerId) {
        conductNavigation(fragment, new DefaultOnViewDestroyCallback(), fragmentContainerId);
    }


    protected void conductNavigation (@NonNull BaseFragment fragment,
                                      @Nullable onViewDestroyListener callback,
                                      @IdRes int fragmentContainerId) {

        if (!isAllowedToCommitFragmentTransaction() || (getHasBeenShroudedByChildFlag()) ) {
            return;
        }

        fragment.setonViewDestroyListener(callback);
        setMenuVisibility(false);
        setHasBeenShroudedByChildFlag(true);

        BaseActivity activity = (BaseActivity) getActivity();
        onConductNavigationExecuted(activity);
        activity.replaceOrShroudFragment(fragment, false, fragmentContainerId);
    }

    private class DefaultOnViewDestroyCallback implements BaseFragment.onViewDestroyListener {
        @Override
        public void onDestroyViewGetCalled() {
            setMenuVisibility(true);
            setHasBeenShroudedByChildFlag(false);

            if ( (isAdded()) && (null != getActivity()) ) {
                onDestroyViewExecuted((BaseActivity) getActivity());
            }
        }
    }

    protected void onConductNavigationExecuted(@NonNull BaseActivity activity) {

    }

    protected void onDestroyViewExecuted(@NonNull BaseActivity activity) {

    }
}
