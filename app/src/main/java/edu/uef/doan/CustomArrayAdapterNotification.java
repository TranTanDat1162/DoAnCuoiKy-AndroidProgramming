package edu.uef.doan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapterNotification extends ArrayAdapter<RowItem>  {
    private final Context context;

    private class ViewHolder {
        TextView txtTitle;
        TextView txtDay;
//        TextView txtTime;
        RelativeLayout bgColor;
    }
    public CustomArrayAdapterNotification(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }


//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = inflater.inflate(R.layout.list_assignment, parent, false);
//        TextView firstlineTitle = (TextView) rowView.findViewById(R.id.firstLine);
//        TextView Day = (TextView) rowView.findViewById(R.id.Day);
//        TextView Time = (TextView) rowView.findViewById(R.id.Time);
//        firstlineTitle.setText(titles[position]);
//        Day.setText(Date[position]);
//          Time.setText(Time[position]);
//
//        return rowView;
//    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_notification, null);
            holder = new ViewHolder();
            holder.txtDay = (TextView) convertView.findViewById(R.id.Date);
//            holder.txtTime = (TextView) convertView.findViewById(R.id.Time);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.Description);
            holder.bgColor = (RelativeLayout) convertView.findViewById(R.id.notificationlayout);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem.getTitle());
        holder.txtDay.setText(rowItem.getDayNotification().toLocaleString());
//        holder.txtTime.setText(rowItem.getTimeNotification().toString());
        switch(rowItem.getBgcolors()){
            case 0:
                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor));
                break;
            case 1:
                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor1));
                break;
            case 2:
                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor2));
                break;
            default:
                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor));
        }
        return convertView;
    }
}
