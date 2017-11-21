package tw.realtime.project.rtbaseframework.sqlites;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by vexonelite on 2017/11/21.
 */

public class CrossTableOperation {
    private SqlOperation mOperation;
    private ContentValues mContentValues;
    private String mTableName;
    private String mWhereClauseSql;
    private List<String> mWhereArgs;

    public SqlOperation getSqlOperation () {
        return mOperation;
    }

    public ContentValues getContentValues () {
        return mContentValues;
    }

    public String setTableName () {
        return mTableName;
    }

    public String setWhereClauseSql () {
        return mWhereClauseSql;
    }

    public String[] getWhereArgs () {
        if ( (null != mWhereArgs) && (!mWhereArgs.isEmpty()) ) {
            return mWhereArgs.toArray(new String[0]);
        }
        else {
            return null;
        }
    }

    public static class Builder {
        private SqlOperation bOperation;
        private ContentValues bContentValues;
        private String bTableName;
        private String bWhereClauseSql;
        private List<String> bWhereArgs;

        public Builder setSqlOperation (SqlOperation operation) {
            bOperation = operation;
            return this;
        }

        public Builder setContentValues (ContentValues contentValues) {
            bContentValues = contentValues;
            return this;
        }

        public Builder setTableName (String tableName) {
            bTableName = tableName;
            return this;
        }

        public Builder setWhereClauseSql (String whereClauseSql) {
            bWhereClauseSql = whereClauseSql;
            return this;
        }

        public Builder setWhereArgs (List<String> whereArgs) {
            bWhereArgs = whereArgs;
            return this;
        }

        public CrossTableOperation build() {
            return new CrossTableOperation(this);
        }
    }

    private CrossTableOperation (Builder builder) {
        mOperation = builder.bOperation;
        mContentValues = builder.bContentValues;
        mTableName = builder.bTableName;
        mWhereClauseSql = builder.bWhereClauseSql;
        mWhereArgs = builder.bWhereArgs;
    }
}
