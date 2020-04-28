package tw.realtime.project.rtbaseframework.models;

import androidx.annotation.NonNull;


public final class IeTuple<T, R, S> {

    public final T first;
    public final R second;
    public final S third;

    public IeTuple(@NonNull final T first, @NonNull final R second, @NonNull final S third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @NonNull
    @Override
    public String toString() { return "IeTuple {first: " + first + ", second: " + second + ", third: " + third + "}"; }
}
