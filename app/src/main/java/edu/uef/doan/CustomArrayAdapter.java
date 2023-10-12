package edu.uef.doan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<RowItem>  {
    private final Context context;
//    private final String[] titles;
//    private final String[] details;

    private class ViewHolder {
        TextView txtTitle;
        TextView txtDesc;
    }
    public CustomArrayAdapter(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }


//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = inflater.inflate(R.layout.list_assignment, parent, false);
//        TextView firstline = (TextView) rowView.findViewById(R.id.firstLine);
//        TextView secondline = (TextView) rowView.findViewById(R.id.secondLine);
//        firstline.setText(titles[position]);
//        secondline.setText(details[position]);
//
//        return rowView;
//    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_assignment, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.secondline);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.firstline);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());

        return convertView;
    }
}
