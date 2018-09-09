package tw.realtime.project.rtbaseframework.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import tw.realtime.project.rtbaseframework.R;
import tw.realtime.project.rtbaseframework.developments.TestTextAdapter;

/**
 * A Fragment simply showing a single text on it.
 */
public final class TestTextRecyclerViewFragment extends BaseFragment {

	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.base_recyclerview_only, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
		setupRecyclerView((RecyclerView) rootView);
	}


	/**
	 * Add the following code in your class;
	 * This allow you build a simple list quickly
	 */
	private void setupRecyclerView (@NonNull RecyclerView recyclerView) {

		final int minValue1 = 1;
		final int maxValue1 = 10;
		final int minValue2 = 5;
		final int maxValue2 = 30;
		final int inclusiveStart = maxValue1 - minValue1 + 1;
		final int inclusiveRange = maxValue2 - minValue2 + 1;
		final int start = secureRandom.nextInt(inclusiveStart) + minValue1;
		final int range = secureRandom.nextInt(inclusiveRange) + minValue2;

		final List<String> dataList = new ArrayList<>();
		for (int i = start; i < (start + 20); i++) {
			final String text;
			if (i == start) {
				text = "Start - " + i;
			}
			else if (i == (start + range - 1)){
				text = "End - " + i;
			}
			else {
				text = "Index - " + i;
			}
			dataList.add(text);
		}

		final TestTextAdapter adapter = new TestTextAdapter();
		adapter.appendNewDataSet(dataList, false);

		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		//recyclerView.addItemDecoration(deco);
		//recyclerView.setItemAnimator(null);
		recyclerView.setAdapter(adapter);
		recyclerView.setHasFixedSize(true);
	}

}













