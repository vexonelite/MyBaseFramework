package tw.realtime.project.rtbaseframework.widgets.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by vexonelite on 2016/10/26.
 */

public class AnimatedTextView extends AppCompatTextView {

    private int mPlace;
    private int mCurrentPlace;

    public AnimatedTextView(Context context) {
        super(context);
    }

    public AnimatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void updatePlace (int place) {
        if ( (place >= 1) && (place <=3) ) {
            mPlace = place;
        }
    }

    public int getPlace () {
        return mPlace;
    }

    public void updateCurrentPlace (int place) {
        if ( (place >= 0) && (place <=3) ) {
            mCurrentPlace = place;
        }
    }

    public int getCurrentPlace () {
        return mCurrentPlace;
    }


}
