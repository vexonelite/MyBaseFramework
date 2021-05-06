package tw.realtime.project.rtbaseframework.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;


public interface IeFragmentManagerDelegate {
    @NonNull
    FragmentManager getSupportFragmentManager();
}
