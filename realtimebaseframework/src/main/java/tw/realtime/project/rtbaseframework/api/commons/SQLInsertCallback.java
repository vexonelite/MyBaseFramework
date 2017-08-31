package tw.realtime.project.rtbaseframework.api.commons;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by vexonelite on 2017/6/23.
 */

public interface SQLInsertCallback {
    /**
     * gets called when SQLite instance is ready for you to insert your own data set.
     * @param sqLiteDatabase the SQLite instance. Ensure that you only involve the insert() method <p>
     *              for (ContentValues contentValues : contentValueList) {<p>
     *                       mSQLiteDb.insert(tableName, null, contentValues);<p>
     *              }<p>
     */
    void onSQLiteDbAvailable(SQLiteDatabase sqLiteDatabase) throws Exception;
}
