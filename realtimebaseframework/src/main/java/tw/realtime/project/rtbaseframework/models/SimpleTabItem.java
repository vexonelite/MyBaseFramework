package tw.realtime.project.rtbaseframework.models;

import android.support.annotation.NonNull;

import tw.realtime.project.rtbaseframework.interfaces.ui.tab.TabItemDelegate;
import tw.realtime.project.rtbaseframework.utils.CryptUtils;


/**
 * Created by vexonelite on 2018/09/08.
 */

public final class SimpleTabItem implements TabItemDelegate {

    private final String identifier;
    private final String description;

    public SimpleTabItem(String identifier, String description) {
        this.identifier = (null != identifier) ? identifier : CryptUtils.generateRandomStringViaUuid();
        this.description = (null != description) ? description : "";
    }

    @NonNull
    @Override
    public String getIdentifier() {
        return identifier;
    }

    @NonNull
    @Override
    public String getDescription() {
        return description;
    }
}
