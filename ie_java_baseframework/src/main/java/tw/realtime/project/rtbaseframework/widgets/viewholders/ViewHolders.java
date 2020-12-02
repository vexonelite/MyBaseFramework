package tw.realtime.project.rtbaseframework.widgets.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tw.realtime.project.rtbaseframework.delegates.ui.view.DescriptionDelegate;

/**
 * Default ViewHolder for the default ViewType
 * @author elite_lin
 * @version 1.0
 */
public final class ViewHolders {

    public static final class CommonDivider extends RecyclerView.ViewHolder {
        public CommonDivider(View itemView) { super(itemView); }
    }

    public static final class WrapContentLoader extends RecyclerView.ViewHolder {
        public WrapContentLoader(View itemView) { super(itemView); }
    }

    public static final class MatchParentLoader extends RecyclerView.ViewHolder {
        public MatchParentLoader(View itemView) { super(itemView); }
    }

    public static final class MatchParentUnavailable extends RecyclerView.ViewHolder {
        public MatchParentUnavailable(View itemView) { super(itemView); }
    }

    // [start] added in 2020/12/02
    public static final class IeSingleText extends RecyclerView.ViewHolder {
        public IeSingleText(View itemView) { super(itemView); }

        public void onBind(@NonNull final DescriptionDelegate delegate, final int position) {
            if (itemView instanceof TextView) {
                ((TextView) itemView).setText(delegate.theDescription());
            }
        }
    }
    // [end] added in 2020/12/02
}

