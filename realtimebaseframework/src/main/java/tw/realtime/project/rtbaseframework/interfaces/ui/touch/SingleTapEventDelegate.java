package tw.realtime.project.rtbaseframework.interfaces.ui.touch;

import androidx.annotation.NonNull;
import android.view.MotionEvent;

/**
 * Created by vexonelite on 2018/2/8.
 */

public interface SingleTapEventDelegate {
    boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent);
}
