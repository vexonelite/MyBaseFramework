package tw.realtime.project.rtbaseframework.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.R;


/**
 * 自定義顯示載入中的對話框
 */
public class ProgressDialog extends android.app.ProgressDialog {

	private String mTitle;
	private TextView mDescription;

	/**
	 *
	 * @param context
	 * @param title 要顯示給使用者看的字串
	 */
	public ProgressDialog(Context context, String title) {
		super(context);
		this.mTitle = title;
		this.setIndeterminate(true);
		this.setCancelable(false);
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
	}


	public void setTitle (String title) {
		this.mTitle = title;
		if (null != mDescription) {
			if (null != title) {
				mDescription.setText(title);
			}
			else {
				LogWrapper.showLog(Log.INFO, "ProgressDialog", "setTitle - title is null!");
			}
		}
		else {
			LogWrapper.showLog(Log.INFO, "ProgressDialog", "setTitle - mDescription is null!");
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_dialog_progress);

		mDescription = (TextView) findViewById(R.id.description);
		if (null != mTitle) {
			mDescription.setText(mTitle);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		setWindowStyle();
	}

	/**
	 * Set desired looks of the dialog, including background image/ color,
	 * window size, margin, etc.
	 *
	 * Refs:
	 * https://gist.github.com/ishitcno1/9408188
	 */
	private void setWindowStyle () {
		Window window = getWindow();
		window.setBackgroundDrawableResource(android.R.color.transparent);
		WindowManager.LayoutParams params = window.getAttributes();
		// Use dimAmount to control the amount of dim
		params.dimAmount = 0.6f;
		window.setAttributes(params);
	}
}
