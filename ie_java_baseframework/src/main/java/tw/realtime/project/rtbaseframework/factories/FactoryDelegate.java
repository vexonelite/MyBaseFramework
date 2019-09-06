package tw.realtime.project.rtbaseframework.factories;

import androidx.annotation.NonNull;

public interface FactoryDelegate<T> {
    @NonNull
    T create();
}
