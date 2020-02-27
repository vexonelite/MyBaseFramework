package tw.realtime.project.rtbaseframework.widgets.ui;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;


/**
 * [Put constant text inside EditText which should be non-editable - Android](https://stackoverflow.com/questions/14195207/put-constant-text-inside-edittext-which-should-be-non-editable-android)
 * @see <a href="https://medium.com/@ali.muzaffar/adding-a-prefix-to-an-edittext-2a17a62c77e1">Adding a prefix to an EditText</a>
 */
public final class FixPrefixTextWatcher implements TextWatcher {

    private String thePrefix = "";
    private String beforeText = "";
    private WeakReference<EditText> editTextRef;

    public void setPrefix(@NonNull String prefix) { this.thePrefix = prefix; }

    public void setEditText(@Nullable EditText editText) {
        if (null != editText) { editTextRef = new WeakReference<>(editText); }
        else {
            if (null != editTextRef) { editTextRef.clear(); }
            editTextRef = null;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //LogWrapper.showLog(Log.INFO, "PrefixTextWatcher", "onTextChanged - s: " + s + ", start: " + start + ", before: " + before);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //LogWrapper.showLog(Log.INFO, "PrefixTextWatcher", "beforeTextChanged - s: " + s + ", start: " + start + ", after: " + after);
        // save the text before user is going to edit it
        // the beforeText supposes to have specific the prefix
        beforeText = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        //LogWrapper.showLog(Log.INFO, "PrefixTextWatcher", "afterTextChanged - s: " + s + ", beforeText: " + beforeText + ", thePrefix.length(): " + thePrefix.length());

        final EditText editText = (null != editTextRef) ? editTextRef.get() : null;
        if ( (null != editText) && (thePrefix.length() > 0) && (!s.toString().startsWith(thePrefix)) ) {
            // if the changed text whose prefix does not match the specific the prefix
            // than restore the beforeText to the edittext
            editText.setText(beforeText);
            Selection.setSelection(editText.getText(), editText.getText().length());
        }
    }
}
