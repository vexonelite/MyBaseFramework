package tw.realtime.project.rtbaseframework.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tw.realtime.project.rtbaseframework.R;


public class IeAbstractTwinActionsDialog<T> extends IeBaseActionDialog<T> {

	@Override
	public final View onCreateView(
			@NonNull LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.base_dialog_twin_actions, container);
	}

	@Override
	public final void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
		uiInitializationBase(rootView);
		uiInitializationExt(rootView);
	}
}
