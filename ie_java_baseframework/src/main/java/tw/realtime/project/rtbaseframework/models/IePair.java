package tw.realtime.project.rtbaseframework.models;

import androidx.annotation.NonNull;


public final class IePair<T, R> {

    public final T first;
    public final R second;

    public IePair(@NonNull final T first, @NonNull final R second) {
        this.first = first;
        this.second = second;
    }

    @NonNull
    @Override
    public String toString() { return "IePair {first: " + first + ", second: " + second + "}"; }
}
