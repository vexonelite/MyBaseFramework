package tw.realtime.project.rtbaseframework.api.commons;

import android.database.Cursor;

/**
 * Created by vexonelite on 2017/6/23.
 */

public interface SQLQueryCallback {
    /**
     * gets called when a SQL query has been done.
     * @param cursor the query result. It could be null. Ensure that you has done the null checking <p>
     *              if (null != cursor) { <p>
     *                  if ( (cursor.getCount() > 0) && (cursor.moveToFirst()) ) {<p>
     *                      ....<p>
     *                  }<p>
     *                  cursor.close();
     *              }<p>
     */
    void onCursorAvailable (Cursor cursor);
}
