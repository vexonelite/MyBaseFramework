package tw.realtime.project.rtbaseframework.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.R;
import tw.realtime.project.rtbaseframework.adapters.BaseRecyclerViewAdapter;
import tw.realtime.project.rtbaseframework.app.BaseDialogFragment;
import tw.realtime.project.rtbaseframework.interfaces.OptionDelegate;
import tw.realtime.project.rtbaseframework.utils.CodeUtils;
import tw.realtime.project.rtbaseframework.widgets.BaseItemClicker;


public class OptionPickerDialog extends BaseDialogFragment {

	private OptionDelegate mSelectedOption;
	private OptionPickerAdapter mAdapter;

	private OptionPickerListener mCallback;
	private List<OptionDelegate> mDataSet = new ArrayList<>();

	private int mSelectedColor;
	private int mNormalColor;

	private int mLayoutResId = R.layout.base_dialog_option_picker;
	private int mItemLayoutResId = R.layout.base_dialog_option_picker_item;

	private LayoutInflater mInflater;


	public interface OptionPickerListener extends OnDecisionMadeListener {
		void onSubmitButtonPressed(OptionDelegate result);
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

	public OptionPickerDialog setSelectedOption (OptionDelegate selectedOption) {
		if (null != selectedOption) {
			mSelectedOption = selectedOption;
		}
		return this;
	}

	public OptionPickerDialog setLayoutResourceId (int resourceId) {
		mLayoutResId = resourceId;
		return this;
	}

	public OptionPickerDialog setItemLayoutResourceId (int resourceId) {
		mItemLayoutResId = resourceId;
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
		mInflater = inflater;
		return inflater.inflate(mLayoutResId, container);
    }

	@Override
	public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(rootView, savedInstanceState);

		setupRecyclerView((RecyclerView) rootView.findViewById(R.id.recyclerView));

		View view = rootView.findViewById(R.id.doneButton);
		if (null != view) {
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					view.setEnabled(false);
					notifyCallbackIfNeeded();
					view.setEnabled(true);
				}
			});
		}
	}

	private void setupRecyclerView (RecyclerView recyclerView) {
		if (null == recyclerView) {
			return;
		}

		mAdapter = new OptionPickerAdapter();
		mAdapter.appendNewDataSet(mDataSet, false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setItemAnimator(null);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
	}

	private class OptionPickerAdapter extends BaseRecyclerViewAdapter<OptionDelegate, RecyclerView.ViewHolder> {

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//			LayoutInflater inflater = LayoutInflater.from(
//					MainApplication.getInstance().getApplicationContext());
			return new OptionPickerItem( mInflater.inflate(mItemLayoutResId, parent, false) );
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
			if (viewHolder instanceof OptionPickerItem) {
				((OptionPickerItem) viewHolder).onBind(getObjectAtPosition(position), position);
			}
		}
	}
	
	private class OptionPickerItem extends RecyclerView.ViewHolder {

		private TextView mTitleView;
		private View mContainer;

		private OptionPickerItem(View itemView) {
			super(itemView);
			mContainer = itemView;
			mTitleView = (TextView) itemView.findViewById(R.id.itemDescription);
		}

		private void onBind (final OptionDelegate item, int position) {

			if (null == item) {
				resetItemViewLooks();
				return;
			}

			final int color = ((null != mSelectedOption) && (item.isSelected(mSelectedOption.getTitle())) )
					? mSelectedColor : mNormalColor;
			String text = "";
			if (null != item.getTitle()) {
				text = text + item.getTitle();
			}
			mTitleView.setText(text);
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

	private void handleStateSwitch (int position) {

		if ( (position < 0) || (position >= mAdapter.getItemCount()) ) {
			return;
		}

		int prePosition = -1;
		if (null != mSelectedOption) {
			prePosition = mAdapter.getIndexOfObject(mSelectedOption);
		}

		mSelectedOption = mAdapter.getObjectAtPosition(position);
		new Handler().post(new NotifyRunnable(prePosition, position));
	}

	private class NotifyRunnable implements Runnable {
		private int nPrePosition;
		private int nPostPosition;

		private NotifyRunnable (int prePosition, int postPosition) {
			nPrePosition = prePosition;
			nPostPosition = postPosition;
		}

		@Override
		public void run() {
			if ( (nPrePosition >= 0) && (nPrePosition < mAdapter.getItemCount()) ) {
				mAdapter.notifyItemChanged(nPrePosition);
			}
			if ( (nPostPosition >= 0) && (nPostPosition < mAdapter.getItemCount()) ) {
				mAdapter.notifyItemChanged(nPostPosition);
			}
		}
	}

	private void notifyCallbackIfNeeded () {
		if (null != mCallback) {
			mCallback.onSubmitButtonPressed(mSelectedOption);
			mCallback.onNotification(OptionPickerDialog.this, DialogAction.CONFIRM);
		}
	}
}
