package tw.realtime.project.rtbaseframework.interfaces;


import tw.realtime.project.rtbaseframework.enumerations.ObjectEdit;

/**
 * Created by vexonelite on 2017/10/30.
 */

public interface ObjectEditDelegate<T> {
    void onObjectEdited(ObjectEdit what, T object);
}
