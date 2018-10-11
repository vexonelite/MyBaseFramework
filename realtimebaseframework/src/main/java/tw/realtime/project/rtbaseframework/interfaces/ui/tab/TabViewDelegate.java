package tw.realtime.project.rtbaseframework.interfaces.ui.tab;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public interface TabViewDelegate<T extends TabItemDelegate> {
    @NonNull
    View getTabView(@NonNull ViewGroup parent, int position, @NonNull T tabItem);
}
