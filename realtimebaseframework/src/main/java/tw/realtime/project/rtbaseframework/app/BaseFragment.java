package tw.realtime.project.rtbaseframework.app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.dialogs.ProgressDialog;
import tw.realtime.project.rtbaseframework.interfaces.ActionBarDelegate;


public abstract class BaseFragment extends Fragment implements ActionBarDelegate {

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
        try {
            if (null != mCallback) {
                mCallback.onDestroyViewGetCalled();
            }
        } catch (Exception e) {
            // 防止閃退；當 mCallback 中做了某些會閃退的事情
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onDestroyView", e);
        }
        super.onDestroyView();
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


    private boolean forbidSetupActionBar () {
        return (!isAdded()) || hasBeenShroudedByChild;
    }

    protected final void setUpActionBarHomeAsUpIndicator (@DrawableRes final int iconResId) {
        if (forbidSetupActionBar()) {
            return;
        }
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setHomeAsUpIndicator(iconResId);
        }
    }
    protected final void setUpActionBarTitle (String title) {
        if ( (null == title) || (forbidSetupActionBar()) ) {
            return;
        }
        //LogWrapper.showLog(Log.INFO, getLogTag(), "setUpActionBar");

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(title);
        }
    }

    protected final void setUpActionBarTitleAndDisplayHomeAsUp (String title, boolean homeAsUpEnabledFlag) {
        if ( (null == title) || (forbidSetupActionBar()) ) {
            return;
        }
        //LogWrapper.showLog(Log.INFO, getLogTag(), "setUpActionBar");

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabledFlag);
        }
    }

    /**
     * 設定 ActionBar 的標題，並決定Icon 是 "<" 或 App Icon
     * @param title
     * @param homeButtonEnabledFlag
     * @param homeAsUpEnabledFlag
     */
    protected final void setUpActionBar (@NonNull String title, boolean homeButtonEnabledFlag, boolean homeAsUpEnabledFlag) {
        if (forbidSetupActionBar()) {
            return;
        }
        //LogWrapper.showLog(Log.INFO, getLogTag(), "setUpActionBar");

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(title);
            actionBar.setHomeButtonEnabled(homeButtonEnabledFlag);
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabledFlag);
        }
    }

    protected final void hideActionBar () {
        if (forbidSetupActionBar()) {
            return;
        }
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }
    }

    protected void showActionBar () {
        if (forbidSetupActionBar()) {
            return;
        }
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.show();
        }
    }


    /**
     * 將軟鍵盤隱藏
     */
    protected final void hideSoftKeyboard () {
        if (!isAdded()) {
            return;
        }
        final BaseActivity activity = (BaseActivity) getActivity();
        if (null == activity) {
            return;
        }
        activity.hideSoftKeyboard();
    }

    /**
     * 顯示軟鍵盤
     */
    protected final void showSoftKeyboard(@NonNull View view) {
        if (!isAdded()) {
            return;
        }
        final BaseActivity activity = (BaseActivity) getActivity();
        if (null == activity) {
            return;
        }
        activity.showSoftKeyboard(view);
    }

    protected final boolean isAllowedToCommitFragmentTransaction () {
        if (!isAdded()) {
            return false;
        }
        if (null == getActivity()) {
            return false;
        }
        final BaseActivity activity = (BaseActivity) getActivity();
        return activity.isAllowedToCommitFragmentTransaction();
    }

    /**
     * make sure you have added
     * <pre>
     * {@code   if (!isAllowedToCommitFragmentTransaction() ) {
     *      return;
     *     }
     * }
     * </pre>
     * before involve the method!
     * @param dialogFragment
     */
    protected final void showDialogFragment (@NonNull DialogFragment dialogFragment) {
        if (!isAllowedToCommitFragmentTransaction()) {
            return;
        }
        // DialogFragment.show() will take care of adding the fragment in a transaction.
        // We also want to remove any currently showing dialog,
        // so make our own transaction and take care of that here.
        final FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        final Fragment previousFragment = getChildFragmentManager().findFragmentByTag(dialogFragment.getClass().getSimpleName());
        if (null != previousFragment) {
            fragmentTransaction.remove(previousFragment);
        }
        fragmentTransaction.addToBackStack(null);
        dialogFragment.show(fragmentTransaction, dialogFragment.getClass().getSimpleName());
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


    protected final void conductNavigation (@NonNull BaseFragment fragment, @IdRes int fragmentContainerId) {
        conductNavigation(fragment, new DefaultOnViewDestroyCallback(), fragmentContainerId);
    }


    protected final void conductNavigation (@NonNull BaseFragment fragment,
                                            @Nullable onViewDestroyListener callback,
                                            @IdRes int fragmentContainerId) {

        if (!isAllowedToCommitFragmentTransaction() || (getHasBeenShroudedByChildFlag()) ) {
            return;
        }

        fragment.setonViewDestroyListener(callback);
        setMenuVisibility(false);
        setHasBeenShroudedByChildFlag(true);

        final BaseActivity activity = (BaseActivity) getActivity();
        onConductNavigationExecuted(activity);
        activity.replaceOrShroudFragment(fragment, false, fragmentContainerId);
    }

    public class DefaultOnViewDestroyCallback implements BaseFragment.onViewDestroyListener {
        @Override
        public void onDestroyViewGetCalled() {
            setMenuVisibility(true);
            setHasBeenShroudedByChildFlag(false);

            if ( (isAdded()) && (null != getActivity()) ) {
                onDestroyViewExecuted((BaseActivity) getActivity());
            }
        }
    }

    protected void onConductNavigationExecuted(@Nullable BaseActivity activity) {

    }

    protected void onDestroyViewExecuted(@Nullable BaseActivity activity) {

    }

    @Override
    public void onActionBarSetup() {

    }
}
