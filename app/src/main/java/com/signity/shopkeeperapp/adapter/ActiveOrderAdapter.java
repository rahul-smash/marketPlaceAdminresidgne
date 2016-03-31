package com.signity.shopkeeperapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.orders.DueOrderActivity;

import java.util.List;

/**
 * Created by Rajinder on 28/9/15.
 */
public class ActiveOrderAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    List<String> list;
    FragmentManager fragmentManager;


    public ActiveOrderAdapter() {
        super();
    }

    public ActiveOrderAdapter(Context context, List<String> list, FragmentManager fragmentManager) {

        this.fragmentManager = fragmentManager;
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater
                    .inflate(R.layout.row_list_active_orders, null);
            holder = new ViewHolder();
            holder.txtCustName = (TextView) convertView.findViewById(R.id.valCustName);
            holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtCustName.setText(list.get(position));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(view, list.get(position));
            }
        });

        return convertView;
    }

    public void onButtonClick(View view, String name) {
        Intent dueOrderIntent = new Intent(context, DueOrderActivity.class);
        dueOrderIntent.putExtra("name", name);
        context.startActivity(dueOrderIntent);
    }

    static class ViewHolder {
        TextView txtCustName;
        RelativeLayout parent;
    }


}