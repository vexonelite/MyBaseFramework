package tw.realtime.project.rtbaseframework.developments;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.realtime.project.rtbaseframework.R;
import tw.realtime.project.rtbaseframework.adapters.BaseRecyclerViewAdapter;
import tw.realtime.project.rtbaseframework.interfaces.HolderCellClickListener;
import tw.realtime.project.rtbaseframework.widgets.BaseItemClicker;

/**
 * Created by vexonelite on 2018/3/9.
 */

public class TestTextAdapter
        extends BaseRecyclerViewAdapter<String, TestTextViewHolder> {

    private HolderCellClickListener<String> mHolderCellClickCallback;

    public TestTextAdapter setHolderCellClickCallback(HolderCellClickListener<String> callback) {
        mHolderCellClickCallback = callback;
        return this;
    }

    @NonNull
    @Override
    public TestTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TestTextViewHolder( inflater.inflate(
                R.layout.test_text_view_item, parent, false) );
    }

    @Override
    public void onBindViewHolder(@NonNull TestTextViewHolder holder, int position) {
        String text = getObjectAtPosition(position);
        holder.onBind(position, text, new MyItemClicker(text, position));
    }

    private class MyItemClicker extends BaseItemClicker<String> {

        private MyItemClicker (String text, int position) {
            super(text, position);
        }

        @Override
        public void onClick(View view) {
            if (null != mHolderCellClickCallback) {
                mHolderCellClickCallback.onHolderCellClicked(getDataObject(), getPosition());
            }
        }
    }

    /**
     * Add the following code in your class;
     * This allow you build a simple list quickly
     *
    private void setupRecyclerView (RecyclerView recyclerView) {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String text = getLogTag() + i;
            dataList.add(text);
        }

        TestTextAdapter adapter = new TestTextAdapter()
                .setHolderCellClickCallback(new MyHolderCellClickCallback());
        adapter.appendNewDataSet(dataList, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.addItemDecoration(deco);
        //recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, this));
    }
    */
}