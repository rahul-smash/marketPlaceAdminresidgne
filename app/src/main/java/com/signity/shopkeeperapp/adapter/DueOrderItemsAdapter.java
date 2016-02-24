package com.signity.shopkeeperapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;

/**
 * Created by Rajinder on 28/9/15.
 */
public class DueOrderItemsAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;


    public DueOrderItemsAdapter() {
        super();
    }

    public DueOrderItemsAdapter(Context context) {

        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater
                    .inflate(R.layout.row_list_due_orders_items, null);
            holder = new ViewHolder();
            holder.txtItems = (TextView) convertView.findViewById(R.id.txtItemName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    static class ViewHolder {
        TextView txtItems;

    }


}