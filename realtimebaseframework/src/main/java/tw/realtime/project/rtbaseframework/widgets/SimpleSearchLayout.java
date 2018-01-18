package tw.realtime.project.rtbaseframework.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import tw.realtime.project.rtbaseframework.R;


public class SimpleSearchLayout extends RelativeLayout {

    private SearchLayoutCallback mCallback;

    private EditText mInput;
    private ImageView mSearchIconView;


    public interface SearchLayoutCallback {
        void onQueryActionSubmitted(String queryText);
    }


    public SimpleSearchLayout(Context context) {
        super(context);
    }

    public SimpleSearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleSearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public SimpleSearchLayout init () {
        mInput = (EditText) findViewById(R.id.keywordInput);

        mSearchIconView = (ImageView) findViewById(R.id.searchIcon);

        View view = findViewById(R.id.actionSearch);
        if (null != view) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setEnabled(false);
                    searchClickHandler();
                    view.setEnabled(true);
                }
            });
        }

        return this;
    }

    public SimpleSearchLayout setSearchLayoutCallback (SearchLayoutCallback callback) {
        mCallback = callback;
        return this;
    }

    public SimpleSearchLayout setHint (CharSequence hint) {
        if ( (null != hint) && (null != mInput) ) {
            mInput.setHint(hint);
        }
        return this;
    }

    public SimpleSearchLayout setHint (int resourceId) {
        if (null != mInput) {
            mInput.setHint(resourceId);
        }
        return this;
    }

    public SimpleSearchLayout setSearchIconResourceId (int resourceId) {
        if (null != mSearchIconView) {
            mSearchIconView.setImageResource(resourceId);
        }
        return this;
    }

    public SimpleSearchLayout setSearchIconBitmap (Bitmap icon) {
        if ( (null != icon) && (null != mSearchIconView) ) {
            mSearchIconView.setImageBitmap(icon);
        }
        return this;
    }

    public SimpleSearchLayout setSearchIconDrawable (Drawable icon) {
        if ( (null != icon) && (null != mSearchIconView) ) {
            mSearchIconView.setImageDrawable(icon);
        }
        return this;
    }

    private void searchClickHandler () {
        if ( (null == mCallback) || (null == mInput) ) {
            return;
        }
        String queryText = mInput.getText().toString().trim();
        mCallback.onQueryActionSubmitted(queryText);
    }
}
