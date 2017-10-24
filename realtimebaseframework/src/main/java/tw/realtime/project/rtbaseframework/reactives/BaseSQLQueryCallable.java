package tw.realtime.project.rtbaseframework.reactives;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import tw.realtime.project.rtbaseframework.api.commons.ApiConstants;
import tw.realtime.project.rtbaseframework.api.commons.AsyncApiException;
import tw.realtime.project.rtbaseframework.api.commons.SQLQueryCallback;

/**
 * Created by vexonelite on 2017/10/16.
 */

abstract class BaseSQLQueryCallable<T> implements Callable<List<T>>, SQLQueryCallback {

    private List<T> mResultList;

    public BaseSQLQueryCallable() {
        mResultList = new ArrayList<>();
    }

    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }

    protected void appendObject(T object) throws AsyncApiException {
        try {
            mResultList.add(object);
        }
        catch (Exception e) {
            throw new AsyncApiException(ApiConstants.ExceptionCode.SQLITE_QUERY_FAILURE, e);
        }
    }

    protected List<T> getResultList () {
        return mResultList;
    }
}
