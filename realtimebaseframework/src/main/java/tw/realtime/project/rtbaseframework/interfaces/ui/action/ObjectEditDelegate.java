package tw.realtime.project.rtbaseframework.interfaces.ui.action;


import android.support.annotation.NonNull;

import tw.realtime.project.rtbaseframework.enumerations.ObjectEdit;

/**
 * Created by vexonelite on 2017/10/30.
 */

public interface ObjectEditDelegate<T> {
    void onObjectEdited(@NonNull ObjectEdit what, T object);
}
