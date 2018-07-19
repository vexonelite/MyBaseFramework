package tw.realtime.project.rtbaseframework.interfaces;

/**
 * Created by vexonelite on 2017/7/21.
 */

public interface CalendarEvent<T> {
    T getEvent();
    String getSummary();
    String getUUID();
}
