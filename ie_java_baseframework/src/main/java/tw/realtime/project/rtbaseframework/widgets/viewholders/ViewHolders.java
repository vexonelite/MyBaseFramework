package tw.realtime.project.rtbaseframework.widgets.viewholders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

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
}

