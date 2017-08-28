package tw.realtime.project.rtbaseframework.app;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.dialogs.ConfirmDialog;
import tw.realtime.project.rtbaseframework.dialogs.ProgressDialog;


/**
 * Created by vexonelite on 2017/6/2.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private LifeCycleState mLifeCycleState = LifeCycleState.DEFAULT;


    private enum LifeCycleState {
        DEFAULT,
        ON_CREATE,
        ON_START,
        ON_RESTART,
        ON_RESUME,
        ON_RESUME_FRAGMENT,
        ON_POST_RESUME,
        ON_PAUSE,
        ON_SAVE_INSTANCE,
        ON_STOP
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onCreate!");
        mLifeCycleState = LifeCycleState.ON_CREATE;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onStart!");
        mLifeCycleState = LifeCycleState.ON_START;
        super.onStart();
    }

    @Override
    public void onResume() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onResume!");
        mLifeCycleState = LifeCycleState.ON_RESUME;
        super.onResume();
    }

    /*
     * Called when activity resume is complete
     * (after onResume() has been called).
     */
    @Override
    public void onPostResume() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onPostResume!");
        mLifeCycleState = LifeCycleState.ON_POST_RESUME;
        super.onPostResume();
    }

    @Override
    public void onResumeFragments() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onResumeFragments!");
        mLifeCycleState = LifeCycleState.ON_RESUME_FRAGMENT;
        super.onResumeFragments();
    }

    @Override
    public void onRestart() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onRestart!");
        mLifeCycleState = LifeCycleState.ON_RESTART;
        super.onRestart();
    }

    @Override
    public void onPause() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onPause!");
        mLifeCycleState = LifeCycleState.ON_PAUSE;
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onSaveInstanceState!");
        mLifeCycleState = LifeCycleState.ON_SAVE_INSTANCE;
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onStop!");
        mLifeCycleState = LifeCycleState.ON_STOP;
        super.onStop();
    }

    /*
     * 實作這個 method，專門處理 Home/Back button 事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //LogWrapper.showLog(Log.INFO, getLogTag(), "onOptionsItemSelected - home button!");
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isAllowedToCommitFragmentTransaction () {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "isAllowedToCommitFragmentTransaction: " + mLifeCycleState);
        return ((mLifeCycleState == LifeCycleState.ON_CREATE) ||
                (mLifeCycleState == LifeCycleState.ON_RESUME_FRAGMENT) ||
                (mLifeCycleState == LifeCycleState.ON_POST_RESUME) );
    }

    /**
     * Fragment 的取代或蓋頁
     * @param targetFragment 要被顯示的 Fragment
     * @param doesReplace   是否要取代或蓋頁
     * @param containerResId 對應的容器 Id
     */
    public void replaceOrShroudFragment (final Fragment targetFragment,
                                         boolean doesReplace,
                                         final int containerResId) {

        if (null == targetFragment) {
            return;
        }

        if (doesReplace) {
            popAllFragmentsIfNeeded();
        }

        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);

        if (doesReplace) {
            fragTransaction.replace(containerResId, targetFragment);
        }
        else {
            fragTransaction.add(containerResId, targetFragment);
            fragTransaction.addToBackStack(null);
        }

        try {
            fragTransaction.commit();
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(),
                    "Exception on FragmentTransaction.commit()", e);
        }
    }

    public void popAllFragmentsIfNeeded () {
        FragmentManager fragManager = getFragmentManager();
        try {
            int len = fragManager.getBackStackEntryCount();
            if (len > 0) {
                for(int i = 0; i < len; ++i) {
                    fragManager.popBackStackImmediate();
                }
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(),
                    "Exception on FragmentManager.popBackStackImmediate()", e);
        }
    }

    /**
     * Pop 目前畫面中的 Fragment
     */
    public void popFragment () {
        FragmentManager fragManager = getFragmentManager();
        if (fragManager.getBackStackEntryCount() <= 0) {
            return;
        }
        try {
            //fragManager.popBackStack();
            fragManager.popBackStackImmediate();
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(),
                    "Exception on FragmentManager.popBackStackImmediate()", e);
        }
    }

    public void showAlertDialog (boolean isSingleOption,
                                 String title,
                                 String message,
                                 String positiveText,
                                 String negativeText,
                                 DialogInterface.OnClickListener positiveCallback,
                                 DialogInterface.OnClickListener negativeCallback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (null != message) {
            builder.setMessage(message);
        }

        if ( (null != title) && (!title.isEmpty()) ) {
            builder.setTitle(title);
        }

        if ( (null != positiveText) && (!positiveText.isEmpty()) ) {
            builder.setPositiveButton(positiveText, positiveCallback);
        }
        else {
            builder.setPositiveButton(android.R.string.ok, positiveCallback);
        }

        if (!isSingleOption) {
            if ( (null != negativeText) && (!negativeText.isEmpty()) ) {
                builder.setNegativeButton(negativeText, negativeCallback);
            }
            else {
                builder.setNegativeButton(android.R.string.no, negativeCallback);
            }
        }

        if (isAllowedToCommitFragmentTransaction() ) {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setAlertDialogBuilder(builder);
            dialog.show(getFragmentManager(), "error_dialog");
        }
    }

    /**
     * 顯示對話框 Fragment
     * @param dialogFragment 要顯示的 DialogFragment 實體
     */
    public void showDialogFragment (DialogFragment dialogFragment) {
        if (null == dialogFragment) {
            return;
        }

        // DialogFragment.show() will take care of adding the fragment in a transaction.
        // We also want to remove any currently showing dialog,
        // so make our own transaction and take care of that here.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag(dialogFragment.getClass().getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, dialogFragment.getClass().getSimpleName());
    }

    /**
     * 顯示正在處理某事件之對話框
     */
    public void showProgressDialog (String title) {
        mProgressDialog = new ProgressDialog(this, title);
        mProgressDialog.show();
    }

    /**
     * 隱藏正在處理某事件之對話框
     */
    public void dismissProgressDialog () {
        if (null != mProgressDialog) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }

    /**
     * 將軟鍵盤隱藏
     */
    public void hideSoftKeyboard() {
        if (null != getCurrentFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            getCurrentFocus().clearFocus();
        }
    }

    /**
     * 顯示軟鍵盤
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.show();
        }
    }

    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }
    }

    /**
     * Set the default Logo icon and Navigation icon.
     */
    protected void setupToolbarAsActionBar (final Toolbar toolbar,
                                            final boolean enableDefault,
                                            final int logoResourceId,
                                            final int navigationIconResourceId) {
        if (null == toolbar) {
            return;
        }

        if (enableDefault) {
            try {
                toolbar.setLogo(logoResourceId);
                toolbar.setNavigationIcon(navigationIconResourceId);
            }
            catch (Exception e) {
                LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(),
                        "Exception on setupToolbarAsActionBar", e);
            }
        }

        setSupportActionBar(toolbar);
    }

    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }

}
