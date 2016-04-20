package uk.co.thedistance.components.spinners;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HintSpinnerAdapter<T> extends ArrayAdapter<T> {

    private final LayoutInflater inflator;
    private int layoutId;
    private int dropdownLayoutId;
    private final String hint;
    private int selection = -1;

    public HintSpinnerAdapter(@NonNull Context context, @LayoutRes int layoutId, @NonNull List<T> objects, @NonNull String hint) {
        super(context, layoutId, objects);

        this.hint = hint;
        this.dropdownLayoutId = this.layoutId = layoutId;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public T getRealItem(int position) {
        if (position == 0) {
            return null;
        }
        return getItem(position - 1);
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        this.layoutId = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        SpinnerItemViewHolder holder;
        if (convertView == null) {
            holder = new SpinnerItemViewHolder(inflator.inflate(layoutId, parent, false));
            convertView = holder.itemView;
        } else {
            holder = (SpinnerItemViewHolder) convertView.getTag();
        }

        Object item;
        boolean selected;
        if (position == 0) {
            item = hint;
            selected = (selection == -1);
        } else {
            position--;
            item = getItem(position);
            selected = (selection == position);
        }

        holder.itemView.setSelected(selected);
        holder.text.setText(item.toString());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerItemViewHolder holder;
        if (convertView == null) {
            holder = new SpinnerItemViewHolder(inflator.inflate(dropdownLayoutId, parent, false));
            convertView = holder.itemView;
        } else {
            holder = (SpinnerItemViewHolder) convertView.getTag();
        }

        Object item;
        if (position == 0) {
            item = hint;
        } else {
            position--;
            item = getItem(position);
        }

        holder.text.setText(item.toString());

        return convertView;
    }

    /**
     * Set the selected row.
     *
     * @param selection row to select, -1 for hint / no selection
     */
    public void setSelection(int selection) {
        if (selection != this.selection) {
            this.selection = selection;
            notifyDataSetChanged();
        }
    }

    class SpinnerItemViewHolder {
        View itemView;
        TextView text;

        public SpinnerItemViewHolder(View itemView) {
            this.itemView = itemView;
            itemView.setTag(this);
            text = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}