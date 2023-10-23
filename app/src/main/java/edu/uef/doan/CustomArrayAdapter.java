package edu.uef.doan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<RowItem>  {
    private final Context context;

    private class ViewHolder {
        TextView txtTitle,txtDesc,txtDate,txtType;
        ImageButton btnDelete,btnEdit;
        RelativeLayout bgColor;
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
            holder.bgColor = (RelativeLayout) convertView.findViewById(R.id.assignmentlayout);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.secondline);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.firstline);
            holder.txtDate = (TextView) convertView.findViewById(R.id.thirdline);
            holder.txtType = (TextView) convertView.findViewById(R.id.fourthline);
            holder.btnDelete = (ImageButton) convertView.findViewById(R.id.delete_btn);
            holder.btnEdit = (ImageButton) convertView.findViewById(R.id.edit_btn);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.btnDelete.setOnClickListener(view -> {
            
        });

        holder.txtDesc.setText("Task: "+rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        holder.txtDate.setText("Deadline: "+ rowItem.getDate());
        holder.txtType.setText(rowItem.getType());
        switch(rowItem.getType()){
            case "Essay":
                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor));
                holder.btnDelete.setVisibility(View.GONE);
                break;
            case "Examination":
                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor1));
                holder.btnDelete.setVisibility(View.GONE);
                break;
            case "None":
                holder.txtType.setText("");
                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor1));
                holder.btnDelete.setVisibility(View.VISIBLE);
                break;
            default:
                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor2));
                holder.btnDelete.setVisibility(View.GONE);
                break;
//            default:
//                holder.bgColor.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.assignmentblockbgcolor));

        }
        return convertView;
    }
}
