package tw.realtime.project.rtbaseframework.interfaces;

import android.support.annotation.NonNull;

/**
 * Created by vexonelite on 2017/8/23.
 */

public interface OptionDelegate<T> {
    @NonNull
    String getOptionTitle();

    @NonNull
    T getHeldObject();
}
