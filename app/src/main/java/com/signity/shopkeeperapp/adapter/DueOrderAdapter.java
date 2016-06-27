package com.signity.shopkeeperapp.adapter;

import android.app.Activity;
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
import com.signity.shopkeeperapp.app.DataAdapter;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.orders.DueOrderActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.Util;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Rajinder on 28/9/15.
 */
public class DueOrderAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    List<OrdersListModel> list;
    FragmentManager fragmentManager;


    public DueOrderAdapter() {
        super();
    }

    public DueOrderAdapter(Context context, List<OrdersListModel> list, FragmentManager fragmentManager) {

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
                    .inflate(R.layout.row_list_due_orders, null);
            holder = new ViewHolder();
            holder.txtCustName = (TextView) convertView.findViewById(R.id.valCustName);
            holder.txtCustName.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));
            holder.txtCustOrderId = (TextView) convertView.findViewById(R.id.valOdrId);
            holder.txtCustOrderId.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));
            holder.txtCustTotalAmt = (TextView) convertView.findViewById(R.id.valTotalAmt);
            holder.txtCustTotalAmt.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));
            holder.txtCustTime = (TextView) convertView.findViewById(R.id.valTime);
            holder.txtCustTime.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));
            holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);

            holder.txtLblOdrId = (TextView) convertView.findViewById(R.id.txtLblOdrId);
            holder.txtLblOdrId.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));
            holder.txtLblCustName = (TextView) convertView.findViewById(R.id.txtLblCustName);
            holder.txtLblCustName.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));
            holder.txtLblTotalAmt = (TextView) convertView.findViewById(R.id.txtLblTotalAmt);
            holder.txtLblTotalAmt.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));
            holder.txtLblTime = (TextView) convertView.findViewById(R.id.txtLblTime);
            holder.txtLblTime.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).getCustomerName().equalsIgnoreCase("") || list.get(position).getCustomerName().equals(null)) {
            holder.txtCustName.setText("Guest User");
        } else {
            holder.txtCustName.setText(list.get(position).getCustomerName());
        }

        holder.txtCustOrderId.setText("" + list.get(position).getOrderId());
        holder.txtCustTotalAmt.setText(Util.getCurrency(context) + " " + Double.parseDouble(new DecimalFormat(
                "#####.##").format(list.get(position).getTotal())));
        holder.txtCustTime.setText("" + list.get(position).getTime());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick(view, list.get(position).getItems(), list.get(position).getCustomerName(),
                        list.get(position).getPhone(), list.get(position).getOrderId(),
                        list.get(position).getUserId(), list.get(position).getNote(), list.get(position).getDiscount()
                        , list.get(position).getTotal(), list.get(position).getShippingCharges(), list.get(position).getAddress(),
                        list.get(position).getTax());
            }
        });

        return convertView;
    }

    public void onButtonClick(View view, List<ItemListModel> list, String name, String phone,
                              String orderID, String userID, String note, Double discount,
                              Double total, Double shipping_charges, String address, Double tax) {


        DataAdapter.getInstance().setListItem(list);
        Intent dueOrderIntent = new Intent(context, DueOrderActivity.class);
        dueOrderIntent.putExtra("name", name);
        dueOrderIntent.putExtra("phone", phone);
        dueOrderIntent.putExtra("orderID", orderID);
        dueOrderIntent.putExtra("userID", userID);
        dueOrderIntent.putExtra("type", "due");

        dueOrderIntent.putExtra("note", note);
        dueOrderIntent.putExtra("discount", discount);
        dueOrderIntent.putExtra("total", total);
        dueOrderIntent.putExtra("tax", tax);
        dueOrderIntent.putExtra("shipping_charges", shipping_charges);
        dueOrderIntent.putExtra("address", address);


        context.startActivity(dueOrderIntent);
        AnimUtil.slideFromRightAnim((Activity) context);
    }

    static class ViewHolder {
        TextView txtCustName, txtCustOrderId, txtCustTotalAmt, txtCustTime;
        TextView txtLblOdrId, txtLblCustName, txtLblTotalAmt, txtLblTime;
        RelativeLayout parent;
    }


}