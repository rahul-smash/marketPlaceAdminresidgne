package com.signity.shopkeeperapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.customer.CustomerActivity;
import com.signity.shopkeeperapp.model.UserModel;
import com.signity.shopkeeperapp.util.AnimUtil;
import java.util.List;

/**
 * Created by Rajinder on 28/9/15.
 */
public class CustomerAdapter extends BaseAdapter implements Filterable {


    Context context;
    LayoutInflater inflater;
    List<UserModel> list;


    public CustomerAdapter() {
        super();
    }

    public CustomerAdapter(Context context, List<UserModel> list) {
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
        return this.list.size();
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
                    .inflate(R.layout.row_list_customer, null);
            holder = new ViewHolder();
            holder.txtCustName = (TextView) convertView.findViewById(R.id.txtCustName);
            holder.txtCustNumber = (TextView) convertView.findViewById(R.id.txtCustNumber);
            holder.parent = (LinearLayout) convertView.findViewById(R.id.parent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).getFullName().equalsIgnoreCase("") || list.get(position).getFullName().equals(null)) {
            holder.txtCustName.setText("Guest User");
        } else {
            holder.txtCustName.setText(list.get(position).getFullName());
        }
        holder.txtCustNumber.setText(list.get(position).getPhone());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent custDetailIntent = new Intent(context, CustomerActivity.class);
                custDetailIntent.putExtra("name", list.get(position).getFullName());
                custDetailIntent.putExtra("id", list.get(position).getId());
                custDetailIntent.putExtra("email", list.get(position).getEmail());
                custDetailIntent.putExtra("phone", list.get(position).getPhone());
                context.startActivity(custDetailIntent);
                AnimUtil.slideFromRightAnim((Activity)context);

            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    static class ViewHolder {
        TextView txtCustName, txtCustNumber;
        LinearLayout parent;

    }


}