package tw.realtime.project.rtbaseframework.interfaces;

public interface HolderCellClickListener<T extends Object> {
    void onHolderCellClicked(T item, int position);
}
