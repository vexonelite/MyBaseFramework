package tw.realtime.project.rtbaseframework.sqlites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.api.commons.ApiConstants;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;
import tw.realtime.project.rtbaseframework.api.commons.SQLInsertCallback;
import tw.realtime.project.rtbaseframework.api.commons.SQLQueryCallback;


public abstract class BaseDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_ENABLE_FOREIGN_KEY = "PRAGMA foreign_keys = ON;";

    public static final String SQL_UNIQUE = " UNIQUE";
    public static final String SQL_FOREIGN_KEY = " FOREIGN KEY ";
    public static final String SQL_REFERENCES = " REFERENCES ";

    public interface Operation {
        String CREATE_TABLE = "CREATE TABLE ";
        String CREATE_TABLE_IF_NEEDED = "CREATE TABLE IF NOT EXISTS ";
        String DROP_TABLE = "DROP TABLE IF EXISTS ";
        String INSERT_OR_REPLACE = "INSERT OR REPLACE INTO ";
        String INSERT_INTO = "INSERT INTO ";
        String DELETE_FROM = "DELETE FROM ";
    }

    public interface DataType {
        // String PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT";
        String PRIMARY_KEY = " PRIMARY KEY";
        String AUTOINCREMENT = " AUTOINCREMENT";
        String INTEGER = " INTEGER";
        String TEXT = " TEXT";
        String VARCHAR = " VARCHAR";
        String OPTION_NOT_NULL = " NOT NULL";
        String DECIMAL_9_6 = " DECIMAL(9,6)";
    }

    public interface QueryKey {
        String SELECT = "SELECT ";
        String ALL_ATTRIBUTES = "* ";
        String FROM = "FROM ";
        String WHERE = " WHERE ";
        String EQUAL_WHAT = " = ?";
        String LARGE_THAN_WHAT = " > ?";
        String LESS_THAN_WHAT = " < ?";
        String EQUAL_TO = " = ";
        String ORDER_BY = " ORDER BY ";
        String ORDER_ASCENDING = " ASC";
        String ORDER_DESCENDING = " DESC";
        String LIMIT = " LIMIT ";
        String OFFSET = " OFFSET ";
        String VALUES = " VALUES";
        String INNER_JOIN = "INNER JOIN ";
        String LEFT_JOIN = "LEFT JOIN ";
        String AS = "AS ";
        String ON = "ON ";
        String AND = " AND ";
        String OR = " OR ";
        String BETWEEN = " BETWEEN ? AND ?";
    }

    public interface ColumnName {
        String FOREIGN_KEY = "foreignKey";
        String CREATED_DATE = "createdAt";
        String UPDATED_DATE = "updatedAt";
        String PRIMARY_KEY = "_id";
        String OBJECT_ID = "objectid";
        String NAME = "name";
        String TITLE = "title";
        String DESCRIPTION = "description";
        String JSON = "json";
        String QR_CODE = "qrcode";
    }

    private SQLiteDatabase mSQLiteDb;

    private final ReadWriteLock mDataUpdateLock;

    protected BaseDbHelper(Context context,
                         String databaseName,
                         SQLiteDatabase.CursorFactory factory,
                         int databaseVersion) {
        super(  context,
                databaseName,
                factory,
                databaseVersion);
        mDataUpdateLock = new ReentrantReadWriteLock();
    }

    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }

    /**
     * add a new entry into the specified table
     */
    public void insertSingleItem (String tableName, ContentValues contentValues) throws Exception {
        long tmp = -1;
        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "insertSingleItem - Open Database, TID: " + Thread.currentThread().getId());
            }
            tmp = mSQLiteDb.insert(tableName, null, contentValues);
            LogWrapper.showLog(Log.INFO, getLogTag(), "insertSingleItem - insert, TID: " + Thread.currentThread().getId());
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "insertSingleItem - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }

        if (tmp < 0) {
            throw new Exception("insertSingleItem is fail!!");
        }
        LogWrapper.showLog(Log.INFO, getLogTag(), "insertSingleItem - index: " + tmp
                + ", tableName: " + tableName + ", contentValues: " + contentValues);
    }

    /**
     * add a new entry into the specified table
     */
    public void deletedAndInsertSingleItem (String tableName,
                                            String whereClause,
                                            String[] whereArgs,
                                            ContentValues contentValues) throws Exception {
        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deletedAndInsertSingleItem - Open Database, TID: " + Thread.currentThread().getId());
            }

            mSQLiteDb.beginTransaction();
            try {
                int deleteIndex = mSQLiteDb.delete(tableName, whereClause, whereArgs);
                LogWrapper.showLog(Log.INFO, getLogTag(), "deletedAndInsertSingleItem - TID: "
                        + Thread.currentThread().getId() + ", deleted index: " + deleteIndex);

                long insertedIndex = mSQLiteDb.insert(tableName, null, contentValues);
                LogWrapper.showLog(Log.INFO, getLogTag(), "deletedAndInsertSingleItem - TID: "
                        + Thread.currentThread().getId() + ", inserted index: " + insertedIndex);

                mSQLiteDb.setTransactionSuccessful();
            }
            finally {
                mSQLiteDb.endTransaction();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deletedAndInsertSingleItem - endTransaction, TID: " + Thread.currentThread().getId());
            }
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "deletedAndInsertSingleItem - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    @Deprecated
    /**
     * call updateSingleItemWithCondition instead
     */
    public void updateSingleItem (String tableName, ContentValues contentValues, long itemId) throws Exception {
        long tmp = -1;
        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "updateSingleItem - Open Database, TID: " + Thread.currentThread().getId());
            }

            tmp = mSQLiteDb.update(tableName, contentValues, ("_id=" + itemId), null);
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "updateSingleItem - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }

        if (tmp < 0) {
            throw new Exception("updateSingleItem is fail!!");
        }
        LogWrapper.showLog(Log.INFO, getLogTag(), "updateSingleItem: " + tableName + ", " + contentValues);
    }

    public void updateSingleItemWithCondition (String tableName,
                                               ContentValues contentValues,
                                               String whereClause,
                                               String[] whereArgs) throws Exception {
        long tmp = -1;
        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "updateSingleItemWithCondition - Open Database, TID: " + Thread.currentThread().getId());
            }

            tmp = mSQLiteDb.update(tableName, contentValues, whereClause, whereArgs);
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "updateSingleItemWithCondition - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }

        if (tmp < 0) {
            throw new Exception("updateSingleItemWithCondition is fail!!");
        }
        LogWrapper.showLog(Log.INFO, getLogTag(), "updateSingleItemWithCondition - updated index: " + tmp
                + ", tableName: " + tableName + ", whereClause: " + whereClause);
    }



    /**
     * 批次新增多筆多料到指定的 Table 中
     * @param tableName 指定的 Table
     * @param contentValueList  要新增的資料陣列
     * @throws Exception
     */
    public void bulkInsertion (String tableName, List<ContentValues> contentValueList) throws Exception {

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "bulkInsertion - Open Database, TID: " + Thread.currentThread().getId());
            }

            mSQLiteDb.beginTransaction();
            try {
                for (ContentValues contentValues : contentValueList) {
                    long tmp = mSQLiteDb.insert(tableName, null, contentValues);
                    LogWrapper.showLog(Log.INFO, getLogTag(), "bulkInsertion - TID: "
                            + Thread.currentThread().getId() + ", inserted index: " + tmp);
                }
                mSQLiteDb.setTransactionSuccessful();
            }
            finally {
                mSQLiteDb.endTransaction();
                LogWrapper.showLog(Log.INFO, getLogTag(), "bulkInsertion - endTransaction, TID: " + Thread.currentThread().getId());
            }
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "bulkInsertion - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    /**
     * 批次新增多筆多料到指定的 Table 中
     * @param tableName 指定的 Table
     * @param contentValueList  要新增的資料陣列
     * @throws Exception
     */
    public void deleteAndBulkInsertion (String tableName, List<ContentValues> contentValueList) throws Exception {

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteAndBulkInsertion - Open Database, TID: " + Thread.currentThread().getId());
            }

            mSQLiteDb.beginTransaction();
            try {
                String sql = getDeleteAllFromTableSQL(tableName);
                mSQLiteDb.execSQL(sql);
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteAndBulkInsertion - SQL: " + sql);

                for (ContentValues contentValues : contentValueList) {
                    long tmp = mSQLiteDb.insert(tableName, null, contentValues);
                    LogWrapper.showLog(Log.INFO, getLogTag(), "deleteAndBulkInsertion - TID: "
                            + Thread.currentThread().getId() + ", inserted index: " + tmp);
                }
                mSQLiteDb.setTransactionSuccessful();
            }
            finally {
                mSQLiteDb.endTransaction();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteAndBulkInsertion - endTransaction, TID: " + Thread.currentThread().getId());
            }
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteAndBulkInsertion - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    @Deprecated
    /**
     * 為了對多張Table 的多筆資料進行刪除，再新增資料而開的介面
     * @param callback
     * @throws Exception
     */
    public void bulkUpdateMultipleTables (SQLInsertCallback callback) throws Exception {

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "bulkInsertionMultipleTables - Open Database, TID: " + Thread.currentThread().getId());
            }

            if (null != callback) {
                callback.onSQLiteDbAvailable(mSQLiteDb);
            }
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "bulkInsertionMultipleTables - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    public static class ContentValuesIdPair {
        private ContentValues mContentValues;
        private String mItemId;

        public ContentValuesIdPair (String itemId, ContentValues contentValues) {
            mItemId = itemId;
            mContentValues = contentValues;
        }
    }

    public void bulkUpdate (String tableName,
                            List<ContentValuesIdPair> contentValueList) throws Exception {

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "bulkUpdate - Open Database, TID: " + Thread.currentThread().getId());
            }

            mSQLiteDb.beginTransaction();
            try {
                for (ContentValuesIdPair pair : contentValueList) {
                    long tmp = mSQLiteDb.update(tableName, pair.mContentValues, ("_id=" + pair.mItemId), null);
                    LogWrapper.showLog(Log.INFO, getLogTag(), "bulkUpdate - TID: "
                            + Thread.currentThread().getId() + ", updated index: " + tmp);
                }
                mSQLiteDb.setTransactionSuccessful();
            }
            finally {
                mSQLiteDb.endTransaction();
                LogWrapper.showLog(Log.INFO, getLogTag(), "bulkUpdate - endTransaction, TID: " + Thread.currentThread().getId());
            }
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "bulkUpdate - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    public void deleteAllFromTable (List<String> tableNameList) throws Exception {

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteAllFromTable - Open Database, TID: " + Thread.currentThread().getId());
            }

            for (String tableName : tableNameList) {
                String sql = getDeleteAllFromTableSQL(tableName);
                mSQLiteDb.execSQL(sql);
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteAllFromTable - SQL: " + sql);
            }
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteAllFromTable - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    @Deprecated
    public void deleteSingleItem (String tableName, String condition) throws Exception {
        long tmp = -1;

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteSingleItem - Open Database, TID: " + Thread.currentThread().getId());
            }

            tmp = mSQLiteDb.delete(tableName, condition, null);
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteSingleItem - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }

        if (tmp < 0) {
            throw new Exception("deleteSingleItem is fail!!");
        }
        LogWrapper.showLog(Log.INFO, getLogTag(), "deleteSingleItem: index: " + tmp
                + ", tableName: " + tableName + ", condition: " + condition);
    }

    @Deprecated
    public void deleteWithCondition (String tableName,
                                     String columnName,
                                     String matchedValue) throws Exception {
        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteWithCondition - Open Database, TID: " + Thread.currentThread().getId());
            }

            String[] selectionArgs = (null != matchedValue) ? new String[] {matchedValue} : null;
            mSQLiteDb.delete(tableName, columnName + " = ?", selectionArgs);
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteWithCondition - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    public void deleteWithCondition (String tableName,
                                     String whereClause,
                                     String[] whereArgs) throws Exception {

        int tmp = -1;

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteWithCondition - Open Database, TID: " + Thread.currentThread().getId());
            }

            tmp = mSQLiteDb.delete(tableName, whereClause, whereArgs);
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteWithCondition - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }

        if (tmp < 0) {
            throw new Exception("deleteWithCondition is fail!!");
        }
        LogWrapper.showLog(Log.INFO, getLogTag(), "deleteWithCondition - index : " + tmp
                + ", tableName: " + tableName + ", whereClause: " + whereClause + " TID: " + Thread.currentThread().getId());
    }

    public void deleteMultipleTables (List<String> tableNameList) throws Exception {

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteMultipleTables - Open Database, TID: " + Thread.currentThread().getId());
            }

            mSQLiteDb.beginTransaction();
            try {
                for (String tableName : tableNameList) {
                    String sql = getDeleteAllFromTableSQL(tableName);
                    mSQLiteDb.execSQL(sql);
                    LogWrapper.showLog(Log.INFO, getLogTag(), "deleteMultipleTables - SQL: " + sql);
                }

                mSQLiteDb.setTransactionSuccessful();
            }
            finally {
                mSQLiteDb.endTransaction();
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteMultipleTables - endTransaction, TID: " + Thread.currentThread().getId());
            }
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "deleteMultipleTables - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    public void performCompoundOperations (List<SqlOperationSpec> operationList) throws Exception {

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "performCompoundOperations - Open Database, TID: " + Thread.currentThread().getId());
            }

            mSQLiteDb.beginTransaction();
            try {
                for (SqlOperationSpec xtOperation : operationList) {
                    performSingleOperation(xtOperation);
                }
                mSQLiteDb.setTransactionSuccessful();
            }
            finally {
                mSQLiteDb.endTransaction();
                LogWrapper.showLog(Log.INFO, getLogTag(), "performCompoundOperations - endTransaction, TID: " + Thread.currentThread().getId());
            }
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "performCompoundOperations - Close Database, TID: " + Thread.currentThread().getId());
            }
            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    private void performSingleOperation (SqlOperationSpec xtOperation) throws Exception {

        SqlOperation operation = xtOperation.getSqlOperation();
        if (null == operation) {
            throw new IllegalArgumentException("performSingleOperation - SqlOperation is null");
        }
        String tableName = xtOperation.setTableName();
        if ( (null == tableName) || (tableName.isEmpty()) ) {
            throw new IllegalArgumentException("performSingleOperation - TableName is null or empty");
        }

        switch (operation) {
            case DELETE: {
                String sql = getDeleteAllFromTableSQL(tableName);
                mSQLiteDb.execSQL(sql);
                LogWrapper.showLog(Log.INFO, getLogTag(), "performSingleOperation - SQL: " + sql);
                break;
            }
            case DELETE_WITH_CONDITION: {
                String whereClause = xtOperation.setWhereClauseSql();
                String[] whereArgs = xtOperation.getWhereArgs();
                if ( (null == whereClause) || (whereClause.isEmpty()) || (null == whereArgs) ) {
                    throw new IllegalArgumentException("performSingleOperation - whereClause or whereArgs is valid");
                }
                int tmp = mSQLiteDb.delete(tableName, whereClause, whereArgs);
                LogWrapper.showLog(Log.INFO, getLogTag(), "performSingleOperation - deleteWithCondition - tmp : " + tmp
                        + ", tableName: " + tableName + ", whereClause: " + whereClause);
                break;
            }
            case INSERT: {
                ContentValues contentValues = xtOperation.getContentValues();
                if (null == contentValues) {
                    throw new IllegalArgumentException("performSingleOperation - contentValues is valid");
                }
                long tmp = mSQLiteDb.insert(tableName, null, contentValues);
                if (tmp < 0) {
                    throw new AsyncApiException(ApiConstants.ExceptionCode.SQLITE_INSERT_FAILURE, "cannot insert item for table: " + tableName);
                }
                LogWrapper.showLog(Log.INFO, getLogTag(), "performSingleOperation - insert into table: " +
                        tableName + " at row: " + tmp + ", TID: " + Thread.currentThread().getId());
                break;
            }
        }
    }

    public void findObjectFromTableOrderByWithCondition (String querySQL,
                                                         boolean hasWhereCondition,
                                                         String[] selectionArgs,
                                                         SQLQueryCallback callback) throws Exception {

        Cursor cursor = null;

        try {
            this.mDataUpdateLock.readLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getReadableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "findObjectFromTableOrderByWithEqualTo - Open Database, TID: " + Thread.currentThread().getId());
            }

            LogWrapper.showLog(Log.INFO, getLogTag(), "findObjectFromTableOrderByWithEqualTo - " +
                    "hasWhereCondition: " + hasWhereCondition + ", sql: " + querySQL);

            if ( (hasWhereCondition) && (null != selectionArgs) ) {
                String text = "findObjectFromTableOrderByWithEqualTo - selectionArg: ";
                for (String arg : selectionArgs) {
                    text = text + arg + " ";
                }
                LogWrapper.showLog(Log.INFO, getLogTag(), text);

                cursor = mSQLiteDb.rawQuery(querySQL, selectionArgs);
            }
            else {
                cursor = mSQLiteDb.rawQuery(querySQL, null);
            }

            if (null != callback) {
                callback.onCursorAvailable(cursor);
            }
        }
        finally {
            if (null != cursor) {
//            if ( (cursor.getCount() > 0) && (cursor.moveToFirst()) ) {
//            }
                cursor.close();
                LogWrapper.showLog(Log.INFO, getLogTag(), "findObjectFromTableOrderByWithEqualTo - close cursor");
            }

            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "findObjectFromTableOrderByWithEqualTo - Close Database, TID: " + Thread.currentThread().getId());
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.readLock().unlock();
            }
        }
    }


    @Deprecated
    /** Use only for development */
    public void reCreateTableIfNeeded (String dropSQL, String creationSQL) {
        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "reCreateTableIfNeeded - Open DataBase");
            }

            LogWrapper.showLog(Log.INFO, getLogTag(), "reCreateTableIfNeeded - dropExistingTable: " + dropSQL);
            mSQLiteDb.execSQL(dropSQL);
            LogWrapper.showLog(Log.INFO, getLogTag(), "reCreateTableIfNeeded - createNewTable: " + creationSQL);
            mSQLiteDb.execSQL(creationSQL);
        }
        finally {
            if (null != mSQLiteDb) {
                mSQLiteDb.close();
                mSQLiteDb = null;
                LogWrapper.showLog(Log.INFO, getLogTag(), "reCreateTableIfNeeded - Close DataBase");
            }

            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }

    @Deprecated
    /**
     * 開發過程中使用
     */
    public void dropExistingTablesAndReCreated() throws Exception {

        try {
            this.mDataUpdateLock.writeLock().lock();

            if (null == mSQLiteDb) {
                mSQLiteDb = getWritableDatabase();
                LogWrapper.showLog(Log.INFO, getLogTag(), "dropExistingTables - Open Database, TID: " + Thread.currentThread().getId());
            }
            dropExistingTables(mSQLiteDb);
            onCreate(mSQLiteDb);
            LogWrapper.showLog(Log.INFO, getLogTag(), "dropExistingTables - done, TID: " + Thread.currentThread().getId());

            mSQLiteDb.close();
            LogWrapper.showLog(Log.INFO, getLogTag(), "dropExistingTables - Close Database, TID: " + Thread.currentThread().getId());
            mSQLiteDb = null;
        }
        finally {
            if (null != this.mDataUpdateLock) {
                this.mDataUpdateLock.writeLock().unlock();
            }
        }
    }


    protected void dropExistingTables (SQLiteDatabase db) {

        String[] tableList = getDroppedTableList();
        if ( (null == tableList) || (tableList.length == 0) ) {
            return;
        }
        for (String tableName : tableList) {
            String sql = getDropTableSQL(tableName);
            LogWrapper.showLog(Log.INFO, getLogTag(), "dropExistingTables: " + sql);
            db.execSQL(sql);
        }
    }

    protected String[] getDroppedTableList () {
        return null;
    }

    /** "DROP TABLE IF EXISTS tableName" */
    public static String getDropTableSQL (String tableName) {
        return BaseDbHelper.Operation.DROP_TABLE
                + tableName;
    }

    /** "DELETE FROM tableName" */
    public static String getDeleteAllFromTableSQL (String tableName) {
        return BaseDbHelper.Operation.DELETE_FROM
                + tableName;
    }

    /**
     * "SELECT * FROM tableName WHERE matchedColumn=? ORDER BY sortedColumn ASC LIMIT limit OFFSET offset;"
     * @param parameter the query relevant parameter
     * @return SQL query string
     */
    public static String getObjectFromTableOrderByWithConditionSQL (QueryParameter parameter) {
        if (null == parameter) {
            return null;
        }

        String sql = QueryKey.SELECT
                + QueryKey.ALL_ATTRIBUTES
                + QueryKey.FROM
                + parameter.getTableName();

//        if (parameter.hasWhereCondition()) {
//            sql = sql + QueryKey.WHERE
//                    + parameter.getMatchedColumnName()
//                    + parameter.getWhereOperator();
//        }
        String whereClause = getWhereClauseSQL(parameter);
        if (!whereClause.isEmpty()) {
            sql = sql + QueryKey.WHERE + whereClause;
        }
        sql = sql + getSortingLimitOffsetSQL(parameter);
        return (sql + ";");
    }

    /**
     * SELECT AllAliasColumns
     * FROM LeftTableName AS LeftTableAlias
     * LEFT JOIN RightTableName AS RightTableAlias
     * ON LeftAliasKey = RightAliasKey
     * WHERE LeftAliasKey = MatchedCondition
     */
    public static String getLeftJoinSQL (QueryParameter parameter) {
        if (null == parameter) {
            return null;
        }

        String sql = QueryKey.SELECT + parameter.getAllAliasColumns()
                + QueryKey.FROM + parameter.getLeftTableName() + " " + QueryKey.AS + parameter.getLeftTableAlias() + " "
                + QueryKey.LEFT_JOIN + parameter.getRightTableName() + " " + QueryKey.AS + parameter.getRightTableAlias() + " "
                + QueryKey.ON + parameter.getLeftAliasKey() + QueryKey.EQUAL_TO + parameter.getRightAliasKey();
//        if (parameter.hasWhereCondition()) {
//            sql = sql + QueryKey.WHERE
//                    + parameter.getMatchedColumnName()
//                    + parameter.getWhereOperator();
//        }

        String whereClause = getWhereClauseSQL(parameter);
        if (!whereClause.isEmpty()) {
            sql = sql + QueryKey.WHERE + whereClause;
        }
        sql = sql + getSortingLimitOffsetSQL(parameter);
        return (sql + ";");
    }

    public static String getWhereClauseSQL (QueryParameter parameter) {
        String sql = "";
        List<String> whereClauses = parameter.getWhereClauses();
        if ( (null != whereClauses) && (!whereClauses.isEmpty()) ) {
            for (String clause : whereClauses) {
                sql = sql + clause;
            }
        }
        return sql;
    }

    private static String getSortingLimitOffsetSQL (QueryParameter parameter) {
        String sql = "";
        if (parameter.doesNeedToSort()) {
            sql = sql + QueryKey.ORDER_BY
                    + parameter.getSortedColumnName()
                    + parameter.getOrderManner();
        }
        int limit = parameter.getLimit();
        if (limit > 0) {
            // " LIMIT limit"
            sql = sql + QueryKey.LIMIT + limit;
        }
        int offset = parameter.getOffset();
        if (offset > 0) {
            // " OFFSET offset"
            sql = sql + QueryKey.OFFSET + offset;
        }
        return sql;
    }

//    private static String getAlienTableCreationSQL () {
//
//        /*
//        return MyDbHelper.Operation.CREATE_TABLE_IF_NEEDED + TableName.ALIEN
//                + " (_id" + MyDbHelper.DataType.PRIMARY_KEY + ", "
//                + ColumnName.TITLE + DataType.VARCHAR + MyDbHelper.DataType.OPTION_NOT_NULL + ", "
//                + ColumnName.DESCRIPTION + MyDbHelper.DataType.TEXT + MyDbHelper.DataType.OPTION_NOT_NULL + ", "
//                + ColumnName.UPDATED_DATE + MyDbHelper.DataType.INTEGER + MyDbHelper.DataType.OPTION_NOT_NULL + ");";
//        */
//        //FOREIGN KEY(checklist_id) REFERENCES checklist(_id)
//        String text = SQL_FOREIGN_KEY + "(" + "columnName" + ")" + SQL_REFERENCES +
//                "refTableName" + "(" + "refColumnName" + ")";
//        return text;
//
//    }
}
