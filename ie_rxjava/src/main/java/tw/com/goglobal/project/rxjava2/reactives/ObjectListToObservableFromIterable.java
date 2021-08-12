package tw.com.goglobal.project.rxjava2.reactives;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;

public final class ObjectListToObservableFromIterable<T> implements Function<List<T>, ObservableSource<T>> {
    @Override
    public ObservableSource<T> apply(@io.reactivex.rxjava3.annotations.NonNull List<T> givenList) throws Exception {
        return Observable.fromIterable(givenList);
    }
}
