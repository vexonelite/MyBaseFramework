package tw.realtime.project.rtbaseframework.developments;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.realtime.project.rtbaseframework.R;
import tw.realtime.project.rtbaseframework.adapters.recyclerview.BaseRecyclerViewAdapter;
import tw.realtime.project.rtbaseframework.interfaces.HolderCellClickDelegate;
import tw.realtime.project.rtbaseframework.widgets.CommonItemWrapper;

/**
 * Created by vexonelite on 2018/3/9.
 */

public class TestTextAdapter
        extends BaseRecyclerViewAdapter<String, TestTextViewHolder> {

    private HolderCellClickDelegate<String> mHolderCellClickCallback;

    public TestTextAdapter setHolderCellClickCallback(HolderCellClickDelegate<String> callback) {
        mHolderCellClickCallback = callback;
        return this;
    }

    @NonNull
    @Override
    public TestTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TestTextViewHolder( inflater.inflate(
                R.layout.base_dev_text_view_item, parent, false) );
    }

    @Override
    public void onBindViewHolder(@NonNull TestTextViewHolder holder, int position) {
        final String text = (null != getObjectAtPosition(position)) ? getObjectAtPosition(position) : "";
        holder.onBind(position, text, new MyItemClicker(text, position));
    }

    private class MyItemClicker extends CommonItemWrapper<String> implements View.OnClickListener{

        private MyItemClicker(@NonNull String object, int position) {
            super(object, "", position);
        }

        @Override
        public void onClick(View view) {
            if (null != mHolderCellClickCallback) {
                mHolderCellClickCallback.onHolderCellClicked(getDataObject(), getAction(), getPosition());
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