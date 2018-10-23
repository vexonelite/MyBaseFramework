package tw.realtime.project.rtbaseframework.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.dialogs.ConfirmDialog;
import tw.realtime.project.rtbaseframework.dialogs.ProgressDialog;
import tw.realtime.project.rtbaseframework.interfaces.fragment.FragmentManipulationDelegate;


/**
 * Created by vexonelite on 2017/6/2.
 */
public abstract class BaseActivity extends AppCompatActivity implements FragmentManipulationDelegate {

    private Toolbar mToolbar;

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

    /*
     * Fragment 的取代或蓋頁
     * @param targetFragment 要被顯示的 Fragment
     * @param doesReplace   是否要取代或蓋頁
     * @param containerResId 對應的容器 Id
     */
    @Override
    public final void replaceOrShroudFragment (@NonNull Fragment targetFragment,
                                               boolean doesReplace,
                                               @IdRes final int containerResId) {

        if (doesReplace) {
            popAllFragmentsIfNeeded();
        }

        final FragmentTransaction fragTransaction = getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE);
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

    @Override
    public final void popAllFragmentsIfNeeded () {
        try {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final int fragmentCount = fragmentManager.getBackStackEntryCount();
            if (fragmentCount > 0) {
                for(int i = 0; i < fragmentCount; ++i) {
                    fragmentManager.popBackStackImmediate();
                }
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(),
                    "Exception on FragmentManager.popBackStackImmediate()", e);
        }
    }

    /*
     * Pop 目前畫面中的 Fragment
     */
    @Override
    public final void popFragmentIfNeeded () {
        final FragmentManager fragManager = getSupportFragmentManager();
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

    public final void showAlertDialog (boolean isSingleOption,
                                       String title,
                                       String message,
                                       String positiveText,
                                       String negativeText,
                                       DialogInterface.OnClickListener positiveCallback,
                                       DialogInterface.OnClickListener negativeCallback) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

        try {
            if (isAllowedToCommitFragmentTransaction() ) {
                final ConfirmDialog dialog = new ConfirmDialog();
                dialog.setAlertDialogBuilder(builder);
                dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on showAlertDialog", e);
        }
    }

    /**
     * 顯示對話框 Fragment
     * @param dialogFragment 要顯示的 DialogFragment 實體
     */
    public final void showDialogFragment (DialogFragment dialogFragment) {
        if (null == dialogFragment) {
            return;
        }

        // DialogFragment.show() will take care of adding the fragment in a transaction.
        // We also want to remove any currently showing dialog,
        // so make our own transaction and take care of that here.
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();
        final Fragment prev = fragmentManager.findFragmentByTag(dialogFragment.getClass().getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        try {
            dialogFragment.show(ft, dialogFragment.getClass().getSimpleName());
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on showDialogFragment", e);
        }
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
    public final void hideSoftKeyboard() {
        if (null != getCurrentFocus()) {
            final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            getCurrentFocus().clearFocus();
        }
    }

    /**
     * 顯示軟鍵盤
     */
    public final void showSoftKeyboard(@NonNull View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


    public void showActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.show();
        }
    }

    public void hideActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }
    }

    protected void hideStatusBar () {
        // If the Android version is lower than Jellybean, use this call to hide the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            final View decorView = getWindow().getDecorView();
            // Hide the status bar.
            final int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            hideActionBar();
        }
    }

    /**
     * Set the default Logo icon and Navigation icon.
     */
    protected final void setupToolbarAsActionBar (@NonNull final Toolbar toolbar,
                                                  final boolean enableDefault,
                                                  final int logoResourceId,
                                                  final int navigationIconResourceId) {
        mToolbar = toolbar;
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

    public final void setToolbarTextColor (@ColorInt final int textColor) {
        try {
            if (null != mToolbar) {
                mToolbar.setTitleTextColor(textColor);
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(), "Exception on setToolbarTextColor", e);
        }
    }

    public final void setToolbarTextColorResourceId (@ColorRes final int textColorResId) {
        try {
            if (null != mToolbar) {
                mToolbar.setTitleTextColor(ContextCompat.getColor(this, textColorResId) );
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(), "Exception on setToolbarTextColor", e);
        }
    }

    public final void setToolbarBackgroundColor (@ColorInt final int backgroundColor) {
        try {
            if (null != mToolbar) {
                mToolbar.setBackgroundColor(backgroundColor);
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(), "Exception on setToolbarTextColor", e);
        }
    }

    public final void setToolbarBackgroundDrawable (@DrawableRes final int backgroundDrawable) {
        try {
            if (null != mToolbar) {
                mToolbar.setBackgroundResource(backgroundDrawable);
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(), "Exception on setToolbarTextColor", e);
        }
    }

    public final void setToolbarNavigationIcon (@DrawableRes final int navigationIconResourceId) {
        try {
            if (null != mToolbar) {
                mToolbar.setNavigationIcon(navigationIconResourceId);
            }
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, BaseActivity.class.getSimpleName(), "Exception on setNavigationIcon", e);
        }
    }

    /**
     * 設定 ActionBar 的標題，並決定Icon 是 "<" 或 App Icon
     * @param title
     * @param homeButtonEnabledFlag
     * @param homeAsUpEnabledFlag
     */
    protected final void setUpActionBar (@NonNull String title, boolean homeButtonEnabledFlag, boolean homeAsUpEnabledFlag) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "setUpActionBar");
        final ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(title);
            actionBar.setHomeButtonEnabled(homeButtonEnabledFlag);
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabledFlag);
        }
    }

    public void haveAudioManagerAlterStreamVolume (boolean hasRaised) {
        final AudioManager myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final int maxLevelVol = myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int currentLevelVol = myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int nextLevelVol = hasRaised ? (currentLevelVol + 1) : (currentLevelVol - 1);
        if (nextLevelVol < 0) {
            nextLevelVol = 0;
        }
        else if (nextLevelVol > maxLevelVol) {
            nextLevelVol = maxLevelVol;
        }
        LogWrapper.showLog(Log.INFO, getLogTag(), "haveAudioManagerAlterStreamVolume - maxLevelVol: " + maxLevelVol);
        LogWrapper.showLog(Log.INFO, getLogTag(), "haveAudioManagerAlterStreamVolume - currentLevelVol: " + currentLevelVol);
        LogWrapper.showLog(Log.INFO, getLogTag(), "haveAudioManagerAlterStreamVolume - nextLevelVol: " + nextLevelVol);
        myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, nextLevelVol, 0);
    }


    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }

    protected void enableImmersiveMode () {
        getWindow().getDecorView().setSystemUiVisibility(setSystemUiVisibility());
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getWindow().getDecorView().setSystemUiVisibility(setSystemUiVisibility());
                }
            }
        });
    }

    // https://developer.android.com/training/system-ui/immersive.html#nonsticky
    // https://developer.android.com/training/system-ui/visibility.html
    //https://developer.android.com/samples/ImmersiveMode/src/com.example.android.immersivemode/ImmersiveModeFragment.html#l75
    protected int setSystemUiVisibility() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    public void toggleHideyBar() {

        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        final int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "Turning immersive mode mode off. ");
        } else {
            LogWrapper.showLog(Log.INFO, getLogTag(), "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    /**
     * If want to enable Immersive sticky Mode, involve enableImmersiveMode() and override this method
     *
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //toggleHideyBar();
        if (hasFocus) {
            enableImmersiveMode();
        }
    }
    */

}
