package tw.realtime.project.rtbaseframework.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.dialogs.ConfirmDialog;
import tw.realtime.project.rtbaseframework.dialogs.ProgressDialog;
import tw.realtime.project.rtbaseframework.delegates.fragment.FragmentManipulationDelegate;
import tw.realtime.project.rtbaseframework.R;


/**
 * Created by vexonelite on 2017/6/2.
 */
public abstract class BaseActivity extends AppCompatActivity implements FragmentManipulationDelegate {

    private Toolbar toolbar;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onCreate!");
        mLifeCycleState = LifeCycleState.ON_CREATE;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onStart!");
        mLifeCycleState = LifeCycleState.ON_START;
        super.onStart();
    }

    @Override
    protected void onResume() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onResume!");
        mLifeCycleState = LifeCycleState.ON_RESUME;
        super.onResume();
    }

    /*
     * Called when activity resume is complete
     * (after onResume() has been called).
     */
    @Override
    protected void onPostResume() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onPostResume!");
        mLifeCycleState = LifeCycleState.ON_POST_RESUME;
        super.onPostResume();
    }

    @Override
    protected void onResumeFragments() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onResumeFragments!");
        mLifeCycleState = LifeCycleState.ON_RESUME_FRAGMENT;
        super.onResumeFragments();
    }

    @Override
    protected void onRestart() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onRestart!");
        mLifeCycleState = LifeCycleState.ON_RESTART;
        super.onRestart();
    }

    @Override
    protected void onPause() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onPause!");
        mLifeCycleState = LifeCycleState.ON_PAUSE;
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "onSaveInstanceState!");
        mLifeCycleState = LifeCycleState.ON_SAVE_INSTANCE;
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
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

    public final boolean isAllowedToCommitFragmentTransaction() {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "isAllowedToCommitFragmentTransaction: " + mLifeCycleState);
        return ((mLifeCycleState == LifeCycleState.ON_CREATE) ||
                (mLifeCycleState == LifeCycleState.ON_RESUME_FRAGMENT) ||
                (mLifeCycleState == LifeCycleState.ON_POST_RESUME) );
    }

    protected final boolean testIfTaskRootIsNotLauncherAndMain() {
        return (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN));
    }


    /*
     * Fragment 的取代或蓋頁
     * @param targetFragment 要被顯示的 Fragment
     * @param doesReplace   是否要取代或蓋頁
     * @param containerResId 對應的容器 Id
     */
    @Override
    public final void replaceOrShroudFragment(
            @NonNull Fragment targetFragment, boolean doesReplace, @IdRes final int containerResId) {

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
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on FragmentTransaction.commit()", cause);
        }
    }

    public final void replaceFragment(@NonNull Fragment targetFragment, @IdRes final int containerResId) {

        popAllFragmentsIfNeeded();

        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .replace(containerResId, targetFragment)
                    .commit();
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on FragmentTransaction.commit()", cause);
        }
    }

    public final void conductNavigation(@NonNull Fragment targetFragment, @IdRes final int containerResId) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .add(containerResId, targetFragment)
                    .addToBackStack(null)
                    .commit();
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on FragmentTransaction.commit()", cause);
        }
    }

    public final void conductNavigationWithAnimation(@NonNull Fragment targetFragment, @IdRes final int containerResId) {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.base_animator_fragment_slide_left_in2,
                            R.animator.base_animator_fragment_slide_right_out2,
                            R.animator.base_animator_fragment_slide_left_in2,
                            R.animator.base_animator_fragment_slide_right_out2)
                    .add(containerResId, targetFragment)
                    .addToBackStack(null)
                    .commit();
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on FragmentTransaction.commit()", cause);
        }
    }

    @Override
    public final void popAllFragmentsIfNeeded() {
        try {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final int fragmentCount = fragmentManager.getBackStackEntryCount();
            if (fragmentCount > 0) {
                for(int i = 0; i < fragmentCount; ++i) {
                    fragmentManager.popBackStackImmediate();
                }
            }
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on FragmentManager.popBackStackImmediate()", cause);
        }
    }

    /*
     * Pop 目前畫面中的 Fragment
     */
    @Override
    public final void popFragmentIfNeeded() {
        final FragmentManager fragManager = getSupportFragmentManager();
        if (fragManager.getBackStackEntryCount() <= 0) {
            return;
        }
        try {
            //fragManager.popBackStack();
            fragManager.popBackStackImmediate();
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on FragmentManager.popBackStackImmediate()", cause);
        }
    }

    public final void showAlertDialog(
            boolean isSingleOption,
            @Nullable String title,
            @Nullable String message,
            @Nullable String positiveText,
            @Nullable String negativeText,
            @Nullable DialogInterface.OnClickListener positiveCallback,
            @Nullable DialogInterface.OnClickListener negativeCallback) {

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
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on showAlertDialog", cause);
        }
    }

    /**
     * 顯示對話框 Fragment
     * @param dialogFragment 要顯示的 DialogFragment 實體
     */
    public final void showDialogFragment(@NonNull DialogFragment dialogFragment) {
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
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on showDialogFragment", cause);
        }
    }

    /**
     * 顯示正在處理某事件之對話框
     */
    public void showProgressDialog(@NonNull String title) {
        mProgressDialog = new ProgressDialog(this, title);
        mProgressDialog.show();
    }

    /**
     * 隱藏正在處理某事件之對話框
     */
    public void dismissProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }

    /**
     * 將軟鍵盤隱藏
     */
    public final void hideSoftKeyboard() {
        if (null == getCurrentFocus()) {
            return;
        }
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (null == inputMethodManager) {
            LogWrapper.showLog(Log.WARN, getLogTag(), "hideSoftKeyboard - InputMethodManager is null!!");
            return;

        }
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        getCurrentFocus().clearFocus();
        //LogWrapper.showLog(Log.WARN, getLogTag(), "hideSoftKeyboard - done!!");
    }

    /**
     * 顯示軟鍵盤
     */
    public final void showSoftKeyboard(@NonNull View view) {
        final View currentFocusedView = getCurrentFocus();
        if (null != currentFocusedView) {
            currentFocusedView.clearFocus();
        }

        /*
         * Ref:
         * https://github.com/codepath/android_guides/wiki/Working-with-the-Soft-Keyboard
         */
        if (view.requestFocus()) {
            final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (null != inputMethodManager) {
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                //LogWrapper.showLog(Log.WARN, getLogTag(), "showSoftKeyboard - done!!");
            }
            else {
                LogWrapper.showLog(Log.WARN, getLogTag(), "showSoftKeyboard - InputMethodManager is null!!");
            }
        }
        else {
            LogWrapper.showLog(Log.WARN, getLogTag(), "showSoftKeyboard - Fail to view.requestFocus()!!");
        }
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

    protected void hideStatusBar() {
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

    public final void alterStatusBarColor(@ColorRes int colorResId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        final Window window = getWindow();
        if (null == window) {
            return;
        }
        final int color = ContextCompat.getColor(getApplicationContext(), colorResId);
        window.setStatusBarColor(color);
    }

    public final void alterStatusBarTextColor(boolean isLight) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        final Window window = getWindow();
        if (null == window) {
            return;
        }

        int flags = window.getDecorView().getSystemUiVisibility();
        if (isLight){
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        else{
            flags = 0; // white text color
        }
        window.getDecorView().setSystemUiVisibility(flags);
    }

    /**
     * Set the default Logo icon and Navigation icon.
     */
    protected final void setupToolbarAsActionBar(
            @NonNull final Toolbar toolbar,
            final boolean enableDefault,
            final int logoResourceId,
            final int navigationIconResourceId) {
        this.toolbar = toolbar;
        if (enableDefault) {
            try {
                toolbar.setLogo(logoResourceId);
                toolbar.setNavigationIcon(navigationIconResourceId);
            }
            catch (Exception cause) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on setupToolbarAsActionBar", cause);
            }
        }

        setSupportActionBar(toolbar);
    }

    public final void setToolbarTextColor(@ColorInt int textColor) {
        try {
            if (null != toolbar) {
                toolbar.setTitleTextColor(textColor);
            }
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on setToolbarTextColor", cause);
        }
    }

    public final void setToolbarTextColorResourceId(@ColorRes int textColorResId) {
        try {
            if (null != toolbar) {
                toolbar.setTitleTextColor(ContextCompat.getColor(this, textColorResId) );
            }
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on setToolbarTextColor", cause);
        }
    }

    public final void setToolbarBackgroundColor(@ColorInt int backgroundColor) {
        try {
            if (null != toolbar) {
                toolbar.setBackgroundColor(backgroundColor);
            }
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on setToolbarTextColor", cause);
        }
    }

    public final void setToolbarBackgroundDrawable(@DrawableRes int backgroundDrawable) {
        try {
            if (null != toolbar) {
                toolbar.setBackgroundResource(backgroundDrawable);
            }
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on setToolbarTextColor", cause);
        }
    }

    public final void setToolbarNavigationIcon(@DrawableRes int navigationIconResourceId) {
        try {
            if (null != toolbar) {
                toolbar.setNavigationIcon(navigationIconResourceId);
            }
        }
        catch (Exception cause) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on setNavigationIcon", cause);
        }
    }

    /**
     * 設定 ActionBar 的標題，並決定Icon 是 "<" 或 App Icon
     * @param title
     * @param homeButtonEnabledFlag
     * @param homeAsUpEnabledFlag
     */
    protected final void setUpActionBar(@NonNull String title, boolean homeButtonEnabledFlag, boolean homeAsUpEnabledFlag) {
        //LogWrapper.showLog(Log.INFO, getLogTag(), "setUpActionBar");
        final ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(title);
            actionBar.setHomeButtonEnabled(homeButtonEnabledFlag);
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabledFlag);
        }
    }

    public final void haveAudioManagerAlterStreamVolume(boolean hasRaised) {
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


    protected final String getLogTag() {
        return this.getClass().getSimpleName();
    }

    protected final void enableImmersiveMode () {
        getWindow().getDecorView().setSystemUiVisibility(getSystemUiVisibility());
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getWindow().getDecorView().setSystemUiVisibility(getSystemUiVisibility());
                }
            }
        });
    }

    // https://developer.android.com/training/system-ui/immersive.html#nonsticky
    // https://developer.android.com/training/system-ui/visibility.html
    //https://developer.android.com/samples/ImmersiveMode/src/com.example.android.immersivemode/ImmersiveModeFragment.html#l75
    protected final int getSystemUiVisibility() {
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
    public final void toggleHideyBar() {

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
