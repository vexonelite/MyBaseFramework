package tw.realtime.project.rtbaseframework.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tw.realtime.project.rtbaseframework.R;


/**
 * @see <a href="http://mrbool.com/how-to-customize-spinner-in-android/28286#ixzz42I38QpCy">The reference 1</a>
 * @see <a href="http://www.edureka.co/blog/custom-spinner-in-android/">The reference 2</a>
 */
public abstract class CustomSpinnerAdapter<T> extends ArrayAdapter {

    protected class ItemViewHolder {
        TextView optionDescription;
        View divider;
    }
    //public SpinnerAdapter(Context context, int txtViewResourceId, String[] objects) {
    //    super(context, txtViewResourceId, objects);
    //}

    public CustomSpinnerAdapter(Context context, int txtViewResourceId, List<T> objects) {
        super(context, txtViewResourceId, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, true);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, boolean isSelected) {

        final ItemViewHolder ivHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.base_text_spinner_item, parent, false);

            ivHolder = new ItemViewHolder();
            ivHolder.optionDescription = (TextView) convertView.findViewById(R.id.description);
            ivHolder.divider = convertView.findViewById(R.id.divider);
            convertView.setTag(ivHolder);
        }
        else {
            ivHolder = (ItemViewHolder) convertView.getTag();
        }

        if (null != ivHolder) {
            if (isSelected) {
                ivHolder.divider.setVisibility(View.INVISIBLE);
            }
            else if (position == (getCount() - 1)) {
                ivHolder.divider.setVisibility(View.INVISIBLE);
            }
            else {
                ivHolder.divider.setVisibility(View.VISIBLE);
            }

            onBind(position, ivHolder.optionDescription);
        }

        return convertView;
    }

    public abstract void onBind (int position, final TextView textView);
}
