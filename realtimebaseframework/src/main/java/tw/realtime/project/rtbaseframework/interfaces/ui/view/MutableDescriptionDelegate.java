package tw.realtime.project.rtbaseframework.interfaces.ui.view;

import android.support.annotation.NonNull;

/**
 * Created by vexonelite on 2018/03/14.
 */

public interface MutableDescriptionDelegate extends DescriptionDelegate {
    void setDescription(@NonNull String description);
}
