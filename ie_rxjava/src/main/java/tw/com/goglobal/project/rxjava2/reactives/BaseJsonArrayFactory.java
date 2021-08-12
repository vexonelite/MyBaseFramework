package tw.com.goglobal.project.rxjava2.reactives;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import tw.com.goglobal.project.rxjava2.AbstractRxTask;
import tw.realtime.project.rtbaseframework.utils.CodeUtils;


public abstract class BaseJsonArrayFactory<T> extends AbstractRxTask<T> implements Callable<String> {

    private final Context context;

    @RawRes
    private final int resId;

    public BaseJsonArrayFactory(@NonNull Context context, @RawRes final int resId) {
        this.context = context;
        this.resId = resId;
    }

    @Override
    public final String call() throws Exception {
        return CodeUtils.readJsonStringFromResRawFile(context, resId);
    }

    @Override
    public void runTask() {
        rxDisposeIfPossible();
        setDisposable(
                getRxSingle()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new ApiDisposableSingleObserver())
        );
    }

    protected abstract Single<T> getRxSingle();
}
