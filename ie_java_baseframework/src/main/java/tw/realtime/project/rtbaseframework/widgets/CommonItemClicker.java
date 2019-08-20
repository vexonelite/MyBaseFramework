package tw.realtime.project.rtbaseframework.widgets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;


public abstract class CommonItemClicker<T> extends CommonItemWrapper<T> implements View.OnClickListener {
    public CommonItemClicker(@NonNull T object, @NonNull String action, int position) {
        super(object, action, position);
    }
}
