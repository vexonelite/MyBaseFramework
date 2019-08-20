package tw.realtime.project.rtbaseframework.developments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vexonelite on 2018/3/9.
 */

public class TestTextViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextLabel;

    public TestTextViewHolder(View view) {
        super(view);
        mTextLabel = (TextView) view;
    }

    public void onBind (int position, @NonNull String title, @Nullable View.OnClickListener clicker) {
        if (title.isEmpty()) {
            mTextLabel.setOnClickListener(null);
        }
        else {
            mTextLabel.setOnClickListener(clicker);
        }
        mTextLabel.setText(title);
    }
}