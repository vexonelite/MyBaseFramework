package tw.realtime.project.rtbaseframework.developments;

import android.support.v7.widget.RecyclerView;
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

    public void onBind (int position, String title, View.OnClickListener clicker) {
        String text = "";
        if (null != title) {
            text = text + title;
            mTextLabel.setOnClickListener(clicker);
        }
        else {
            mTextLabel.setOnClickListener(null);
        }
        mTextLabel.setText(text);
    }
}