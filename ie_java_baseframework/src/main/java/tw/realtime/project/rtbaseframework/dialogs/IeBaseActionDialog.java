package tw.realtime.project.rtbaseframework.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import tw.realtime.project.rtbaseframework.R;
import tw.realtime.project.rtbaseframework.enumerations.DialogAction;
import tw.realtime.project.rtbaseframework.delegates.ui.view.DialogDecisionDelegate;
import tw.realtime.project.rtbaseframework.widgets.CommonItemWrapper;


public abstract class IeBaseActionDialog<T> extends BaseDialogFragment {

	private DialogDecisionDelegate<T> dialogCallback;

	private String title = "";
	private String message = "";
	private String cancelButtonTitle = "";
	private String confirmButtonTitle = "";

	@ColorInt
	private int dividerColor;
	@ColorInt
	private int cancelButtonColor;
	@ColorInt
	private int confirmButtonColor;

	/**
	 * used to trace if the dialog has been shown.
	 * There is a short period of time that the {@link androidx.fragment.app.FragmentManager}
	 * has to do something such that the dialog ends up being shown on the screen.
	 * During the duration, I cannot find the specific dialog fragment
	 * via {@link androidx.fragment.app.FragmentManager#getFragments() }
	 * <p>
	 * added in 2020/03/05
	 */
	public boolean hasBeenShown = false;

	public final IeBaseActionDialog setData(@NonNull DialogDataBuilder builder) {
		this.title = builder.theTitle();
		this.message = builder.theMessage();
		this.cancelButtonTitle = builder.theCancalButtonTitle();
		this.cancelButtonColor = builder.theCancelButtonColor();
		this.confirmButtonTitle = builder.theConfirmButtonTitle();
		this.confirmButtonColor = builder.theConfirmButtonColor();
		this.dividerColor = builder.theDividerColor();
		return this;
	}

	public final IeBaseActionDialog setDialogDecisionCallback(@NonNull DialogDecisionDelegate<T> callback) {
		this.dialogCallback = callback;
		return this;
	}

	///

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
	}

//	@Override
//	public View onCreateView(
//			@NonNull LayoutInflater inflater,
//			@Nullable ViewGroup container,
//			@Nullable Bundle savedInstanceState) {
//		return inflater.inflate(R.layout.base_dialog_twin_actions, container);
//	}

//	@Override
//	public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
//		super.onViewCreated(rootView, savedInstanceState);
//		uiInitialization(rootView);
//	}

	///

	@Override
	protected final boolean doesLayoutNeedToBeMatchParent () { return true; }

	///

	protected final void uiInitializationBase(@NonNull View rootView) {

		final TextView titleView = rootView.findViewById(R.id.title);
		titleView.setText(title);

		final TextView messageView = rootView.findViewById(R.id.message);
		messageView.setText(message);

		final TextView confirmButton = rootView.findViewById(R.id.confirmButton);
		confirmButton.setText(confirmButtonTitle);
		confirmButton.setOnClickListener(new ConfirmButtonClickCallback());
		try {
			confirmButton.setTextColor(confirmButtonColor);
		} catch (Exception cause) {
		}

		final View dividerHorizontal = rootView.findViewById(R.id.dividerHorizontal);
		try {
			dividerHorizontal.setBackgroundColor(dividerColor);
		} catch (Exception cause) {
		}
	}

	protected final void uiInitializationExt(@NonNull View rootView) {
		final TextView cancelButton = rootView.findViewById(R.id.cancelButton);
		cancelButton.setText(cancelButtonTitle);
		cancelButton.setOnClickListener(new CancelButtonClickCallback());
		try {
			cancelButton.setTextColor(cancelButtonColor);
		} catch (Exception cause) {}

		final View dividerVertical = rootView.findViewById(R.id.dividerVertical);
		try {
			dividerVertical.setBackgroundColor(dividerColor);
		} catch (Exception cause) {}
	}

	private class ConfirmButtonClickCallback extends CommonItemWrapper<Boolean> implements View.OnClickListener {

		ConfirmButtonClickCallback() { super(false, "", 0); }

		@Override
		public void onClick(View view) {
			view.setEnabled(false);
			if (null != dialogCallback) {
				dialogCallback.onDecisionMade(
						IeBaseActionDialog.this, DialogAction.CONFIRM, null);
			}
			view.setEnabled(true);
		}
	}

	private class CancelButtonClickCallback extends CommonItemWrapper<Boolean> implements View.OnClickListener {

		CancelButtonClickCallback() { super(false, "", 0); }

		@Override
		public void onClick(View view) {
			view.setEnabled(false);
			if (null != dialogCallback) {
				dialogCallback.onDecisionMade(
						IeBaseActionDialog.this, DialogAction.CANCEL, null);
			}
			view.setEnabled(true);
		}
	}
}
