package tw.realtime.project.rtbaseframework.sqlites;

import java.util.List;

import tw.realtime.project.rtbaseframework.api.commons.ApiDataBuilder;
import tw.realtime.project.rtbaseframework.api.commons.BaseApiData;


/**
 *
 * <p>
 * Created by vexonelite on 2017/6/5.
 */
public class QueryParameter extends BaseApiData {

    public static final String TABLE_NAME = "tableName";
//    public static final String MATCHED_COLUMN_NAME = "matchedColumnName";
//    public static final String WHERE_OPERATOR = "whereOperator";
    public static final String SORTED_COLUMN_NAME = "sortedColumnName";
    public static final String ORDER_MANNER = "orderManner";
    public static final String ALL_ALIAS_COLUMNS = "allAliasColumns";
    public static final String LEFT_TABLE_NAME= "leftTableName";
    public static final String LEFT_TABLE_ALIAS = "leftTableAlias";
    public static final String RIGHT_TABLE_NAME = "rightTableName";
    public static final String RIGHT_TABLE_ALIAS = "rightTableAlias";
    public static final String LEFT_ALIAS_KEY = "leftAliasKey";
    public static final String RIGHT_ALIAS_KEY = "rightAliasKey";
    public static final String MATCHED_CONDITION = "matchedCondition";

//    private boolean mHasWhereCondition;
    private boolean mNeedToSort;
    private int mLimit;
    private int mOffset;
    private List<String> mWhereClauses;

//    public boolean hasWhereCondition() {
//        return mHasWhereCondition;
//    }

    public boolean doesNeedToSort() {
        return mNeedToSort;
    }

    public int getLimit() {
        return mLimit;
    }

    public int getOffset() {
        return mOffset;
    }

    public String getTableName() {
        return retrieveData(TABLE_NAME);
    }

//    public String getMatchedColumnName() {
//        return retrieveData(MATCHED_COLUMN_NAME);
//    }
//
//    public String getWhereOperator() {
//        return retrieveData(WHERE_OPERATOR);
//    }

    public List<String> getWhereClauses() {
        return mWhereClauses;
    }

    public String getSortedColumnName() {
        return retrieveData(SORTED_COLUMN_NAME);
    }

    public String getOrderManner() {
        return retrieveData(ORDER_MANNER);
    }


    public String getAllAliasColumns() {
        return retrieveData(ALL_ALIAS_COLUMNS);
    }

    public String getLeftTableName() {
        return retrieveData(LEFT_TABLE_NAME);
    }

    public String getRightTableName() {
        return retrieveData(RIGHT_TABLE_NAME);
    }

    public String getLeftTableAlias() {
        return retrieveData(LEFT_TABLE_ALIAS);
    }

    public String getRightTableAlias() {
        return retrieveData(RIGHT_TABLE_ALIAS);
    }

    public String getLeftAliasKey() {
        return retrieveData(LEFT_ALIAS_KEY);
    }

    public String getRightAliasKey() {
        return retrieveData(RIGHT_ALIAS_KEY);
    }

    public String getMatchedCondition() {
        return retrieveData(MATCHED_CONDITION);
    }


    public static class Builder extends ApiDataBuilder {

        //private boolean bHasWhereCondition = false;
        private boolean bNeedToSort = false;
        private int bLimit = 0;
        private int bOffset = 0;
        private List<String> bWhereClauses;

        public Builder () {
            super();
        }

//        public Builder setHasWhereConditionFlag(boolean flag) {
//            bHasWhereCondition = flag;
//            return this;
//        }

        public Builder setNeedToSortFlag (boolean flag) {
            bNeedToSort = flag;
            return this;
        }

        public Builder setLimit(int limit) {
            bLimit = limit;
            return this;
        }

        public Builder setOffset(int offset) {
            bOffset = offset;
            return this;
        }

        public Builder setTableName(String tableName) {
            setData(TABLE_NAME, tableName);
            return this;
        }

//        public Builder setMatchedColumnName(String columnName) {
//            setData(MATCHED_COLUMN_NAME, columnName);
//            return this;
//        }
//
//        public Builder setWhereOperator(String operator) {
//            setData(WHERE_OPERATOR, operator);
//            return this;
//        }

        public Builder setWhereClauses(List<String> whereClauses) {
            if ( (null != whereClauses) && (!whereClauses.isEmpty()) ) {
                bWhereClauses = whereClauses;
            }
            return this;
        }

        public Builder setSortedColumnName(String columnName) {
            setData(SORTED_COLUMN_NAME, columnName);
            return this;
        }

        public Builder setOrderManner(String manner) {
            setData(ORDER_MANNER, manner);
            return this;
        }



        public Builder setAllAliasColumns(String aliasColumns) {
            setData(ALL_ALIAS_COLUMNS, aliasColumns);
            return this;
        }

        public Builder setLeftTableName(String tableName) {
            setData(LEFT_TABLE_NAME, tableName);
            return this;
        }

        public Builder setRightTableName(String tableName) {
            setData(RIGHT_TABLE_NAME, tableName);
            return this;
        }

        public Builder setLeftTableAlias(String tableAlias) {
            setData(LEFT_TABLE_ALIAS, tableAlias);
            return this;
        }

        public Builder setRightTableAlias(String tableAlias) {
            setData(RIGHT_TABLE_ALIAS, tableAlias);
            return this;
        }

        public Builder setLeftAliasKey(String aliasKey) {
            setData(LEFT_ALIAS_KEY, aliasKey);
            return this;
        }

        public Builder setRightAliasKey(String aliasKey) {
            setData(RIGHT_ALIAS_KEY, aliasKey);
            return this;
        }

        public Builder setMatchedCondition(String condition) {
            setData(MATCHED_CONDITION, condition);
            return this;
        }

        public QueryParameter build() {
            return new QueryParameter(this);
        }
    }

    private QueryParameter(Builder builder) {
        super(builder.getDataMap());
        //mHasWhereCondition = builder.bHasWhereCondition;
        mWhereClauses = builder.bWhereClauses;
        mNeedToSort = builder.bNeedToSort;
        mLimit = builder.bLimit;
        mOffset = builder.bOffset;
    }

}
