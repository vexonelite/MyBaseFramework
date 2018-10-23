package tw.realtime.project.rtbaseframework.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import tw.realtime.project.rtbaseframework.R;

/**
 * A Fragment simply showing a single text on it.
 */
public final class SingleTextFragment extends BaseFragment {

	public static final String SINGLE_TEXT_KEY = "_single_text";

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.base_fragment_single_text, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
		final Bundle args = getArguments();
		if ( (args != null) && (null != args.getString(SINGLE_TEXT_KEY)) ) {
			final TextView textView = rootView.findViewById(R.id.textView);
			if (null != textView) {
				textView.setText(args.getString(SINGLE_TEXT_KEY));
			}
		}
	}
}













