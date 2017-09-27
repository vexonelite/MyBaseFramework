package tw.realtime.project.rtbaseframework.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tw.realtime.project.rtbaseframework.R;

/**
 * A Fragment simply showing a single text on it.
 */
public class SingleTextFragment extends BaseFragment {

	public static final String SINGLE_TEXT_KEY = "_single_text";

	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.base_fragment_single_text, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Bundle args = getArguments();
		if ( (args != null) && (null != args.getString(SINGLE_TEXT_KEY)) ) {
			TextView textView = (TextView) view.findViewById(R.id.textView);
			if (null != textView) {
				textView.setText(args.getString(SINGLE_TEXT_KEY));
			}
		}
	}
}













