package tw.realtime.project.rtbaseframework.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.adapters.BaseRecyclerViewAdapter;
import tw.realtime.project.rtbaseframework.app.BaseDialogFragment;
import tw.realtime.project.rtbaseframework.interfaces.OptionDelegate;
import tw.realtime.project.rtbaseframework.utils.CodeUtils;
import tw.realtime.project.rtbaseframework.widgets.BaseItemClicker;


public class OptionPickerDialog extends BaseDialogFragment {

	private OptionPickerAdapter mAdapter;

	private List<OptionDelegate> mDataSet = new ArrayList<>();
	private List<OptionDelegate> mSelectedList = new ArrayList<>();

	private boolean doesAllowMultipleSelection = false;

	private OptionPickerListener mCallback;

	private String mDialogTitle;
	private String mDoneButtonTitle;

	private int mSelectedColor;
	private int mNormalColor;


	private final int mDefaultLayoutResId = tw.realtime.project.rtbaseframework.R.layout.base_dialog_option_picker_fixed_height;
	private final int mItemLayoutResId = tw.realtime.project.rtbaseframework.R.layout.base_dialog_option_picker_item;


	public interface OptionPickerListener extends OnDecisionMadeListener {
		void onSubmitButtonPressed(List<OptionDelegate> resultList);
	}

	public OptionPickerDialog setDialogTitle (String title) {
		if ( (null != title) && (!title.isEmpty()) ) {
			mDialogTitle = title;
		}
		return this;
	}

	public OptionPickerDialog setDoneTitle (String title) {
		if ( (null != title) && (!title.isEmpty()) ) {
			mDoneButtonTitle = title;
		}
		return this;
	}

	public OptionPickerDialog setOptionPickerListener (OptionPickerListener listener) {
		this.mCallback = listener;
		return this;
	}

	public OptionPickerDialog setOptionPickerDataSet (List<OptionDelegate> dataSet) {
		if ( (null != dataSet) && (!dataSet.isEmpty()) ) {
			mDataSet.clear();
			mDataSet.addAll(dataSet);
		}
		return this;
	}

	public OptionPickerDialog setSelectedList (List<OptionDelegate> selectedList) {
		if ( (null != selectedList) && (!selectedList.isEmpty()) ) {
			mSelectedList.clear();
			mSelectedList.addAll(selectedList);
		}
		return this;
	}

	public OptionPickerDialog setAllowMultipleSelectionFlag (boolean flag) {
		doesAllowMultipleSelection = flag;
		return this;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Activity activity = getActivity();
		mNormalColor = CodeUtils.getColorFromResourceId(activity, android.R.color.white);
		mSelectedColor = CodeUtils.getColorFromResourceId(activity, android.R.color.darker_gray);
	}

	@Override
    public View onCreateView(LayoutInflater inflater,
							 ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(mDefaultLayoutResId, container);
    }

	@Override
	public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(rootView, savedInstanceState);

		TextView titleView = rootView.findViewById(tw.realtime.project.rtbaseframework.R.id.dialogTitle);
		if ( (null != titleView) && (null != mDialogTitle) ) {
			titleView.setText(mDialogTitle);
		}

		View doneButton = rootView.findViewById(tw.realtime.project.rtbaseframework.R.id.doneButton);
		if (null != doneButton) {
			if ( (doneButton instanceof TextView) && (null != mDoneButtonTitle) ) {
				((TextView) doneButton).setText(mDoneButtonTitle);
			}
			doneButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					view.setEnabled(false);
					notifyCallbackIfNeeded();
					view.setEnabled(true);
				}
			});
		}

		setupRecyclerView((RecyclerView) rootView.findViewById(tw.realtime.project.rtbaseframework.R.id.recyclerView));
	}

	@Override
	public void onResume () {
		super.onResume();
		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}
	}

	private void setupRecyclerView (RecyclerView recyclerView) {
		if (null == recyclerView) {
			return;
		}

		mAdapter = getOptionPickerAdapter();
		mAdapter.appendNewDataSet(mDataSet, false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setItemAnimator(null);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
	}

	/**
	 * You can override the method and return a subclass of OptionPickerAdapter
	 */
	protected OptionPickerAdapter getOptionPickerAdapter () {
		return new OptionPickerAdapter();
	}

	protected class OptionPickerAdapter extends BaseRecyclerViewAdapter<OptionDelegate, RecyclerView.ViewHolder> {

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return getOptionPickerItem(parent);
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
			if (viewHolder instanceof OptionPickerItem) {
				((OptionPickerItem) viewHolder).onBind(getObjectAtPosition(position), position);
			}
		}
	}

	/**
	 * You can override the method and return a subclass of OptionPickerItem
	 */
	protected OptionPickerItem getOptionPickerItem (ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		return new DefaultOptionPickerItem( inflater.inflate(mItemLayoutResId, parent, false) );
	}


	protected class OptionPickerItem extends RecyclerView.ViewHolder {

		protected OptionPickerItem(View itemView) {
			super(itemView);
		}

		public void onBind (final OptionDelegate item, int position) {

		}
	}

	private class DefaultOptionPickerItem extends OptionPickerItem {

		private TextView mTitleView;
		private View mContainer;

		private DefaultOptionPickerItem(View itemView) {
			super(itemView);
			mContainer = itemView;
			mTitleView = itemView.findViewById(tw.realtime.project.rtbaseframework.R.id.itemDescription);
		}

		@Override
		public void onBind (final OptionDelegate item, int position) {
			LogWrapper.showLog(Log.INFO, getLogTag(), "DefaultOptionPickerItem - onBind: " + position);

			if (null == item) {
				resetItemViewLooks();
				return;
			}

			String text = "";
			if (null != item.getOptionTitle()) {
				text = text + item.getOptionTitle();
			}
			mTitleView.setText(text);

			final int color = (isInSelectedList(item)) ? mSelectedColor : mNormalColor;
			mTitleView.setBackgroundColor(color);
			mContainer.setOnClickListener(
					new BaseItemClicker<OptionDelegate>(item, position) {
						@Override
						public void onClick(View view) {
							view.setEnabled(false);
							handleStateSwitch(getPosition());
							view.setEnabled(true);
						}
					});
		}

		private void resetItemViewLooks () {
			mTitleView.setText("");
			mContainer.setOnClickListener(null);
		}
	}

	protected boolean isInSelectedList (OptionDelegate delegate) {
		if (null == delegate) {
			return false;
		}
		else {
			return mSelectedList.contains(delegate);
		}
	}

	private void handleStateSwitch (int position) {

		if ( (position < 0) || (position >= mAdapter.getItemCount()) ) {
			return;
		}

		OptionDelegate selectedDelegate = mAdapter.getObjectAtPosition(position);

		if (doesAllowMultipleSelection) {
			if (!mSelectedList.contains(selectedDelegate)) {
				mSelectedList.add(selectedDelegate);
			}
			else {
				mSelectedList.remove(selectedDelegate);
			}
			new Handler(Looper.getMainLooper()).post(new NotifyRunnable(position));
		}
		else {
			if (!mSelectedList.isEmpty()) {
				OptionDelegate preSelectedDelegate = mSelectedList.get(0);
				int prePosition = mAdapter.getIndexOfObject(preSelectedDelegate);
				new Handler(Looper.getMainLooper()).post(new NotifyRunnable(prePosition));
			}
			mSelectedList.clear();
			mSelectedList.add(selectedDelegate);
			new Handler(Looper.getMainLooper()).post(new NotifyRunnable(position));
		}
	}

	private class NotifyRunnable implements Runnable {
		private int nPosition;

		private NotifyRunnable (int position) {
			nPosition = position;
		}

		@Override
		public void run() {
			if ( (nPosition >= 0) && (nPosition < mAdapter.getItemCount()) ) {
				mAdapter.notifyItemChanged(nPosition);
			}
		}
	}

	private void notifyCallbackIfNeeded () {
		if (null != mCallback) {
			mCallback.onSubmitButtonPressed(mSelectedList);
			mCallback.onNotification(OptionPickerDialog.this, DialogAction.CONFIRM);
		}
	}
}
