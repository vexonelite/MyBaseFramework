package tw.realtime.project.rtbaseframework.widgets.ui;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;


/**
 * prevent user from tapping seekbar to alter value in Android
 * https://stackoverflow.com/questions/19679187/prevent-seekbar-click-in-android
 */
@SuppressLint("ClickableViewAccessibility")
public final class NoActionTouchHandler implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent event) { return true; }
}
