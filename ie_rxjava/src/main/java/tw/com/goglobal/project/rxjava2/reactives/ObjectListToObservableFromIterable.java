package tw.com.goglobal.project.rxjava2.reactives;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public final class ObjectListToObservableFromIterable<T> implements Function<List<T>, ObservableSource<T>> {
    @Override
    public ObservableSource<T> apply(@io.reactivex.annotations.NonNull List<T> givenList) throws Exception {
        return Observable.fromIterable(givenList);
    }
}
