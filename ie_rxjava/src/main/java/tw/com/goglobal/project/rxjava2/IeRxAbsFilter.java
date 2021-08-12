package tw.com.goglobal.project.rxjava2;

import io.reactivex.rxjava3.functions.Predicate;
import tw.realtime.project.rtbaseframework.apis.errors.IeRuntimeException;
import tw.realtime.project.rtbaseframework.apis.filters.IeBaseFilter;


public abstract class IeRxAbsFilter<T> extends IeBaseFilter<T> implements Predicate<T> {
    @Override
    public final boolean test(@io.reactivex.rxjava3.annotations.NonNull T input) throws IeRuntimeException {
        return predicate(input);
    }
}
