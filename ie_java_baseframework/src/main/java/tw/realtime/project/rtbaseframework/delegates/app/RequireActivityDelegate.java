package tw.realtime.project.rtbaseframework.delegates.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public interface RequireActivityDelegate {
    @NonNull
    FragmentActivity requireActivity();
}
