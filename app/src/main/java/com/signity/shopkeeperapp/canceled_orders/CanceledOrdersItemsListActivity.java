package com.signity.shopkeeperapp.canceled_orders;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rajesh on 11/12/15.
 */
public class CanceledOrdersItemsListActivity extends Activity implements View.OnClickListener {


    ListView listRejectedItems;
    TextView textTitle;
    Button backButton, btnCall;

    TextView mDeliveryAddress, mNote, mItemsPrice, mShippingCharges, mDiscountVal;
    RelativeLayout mNoteLayout, mAddressLayout;
    String note, discount, total, shippingCharge, address;
    TextView mTotalAmount;

    String name;
    String phoneNumber;
    List<ItemListModel> listItem;
    CanceledOrderItemsAdapter adapter;
    String userId = "";
    String orderId = "";
    private OrdersListModel ordersListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canceled_items_list_activity);

        ordersListModel = (OrdersListModel) getIntent().getSerializableExtra("object");

        name = ordersListModel.getCustomerName();
        phoneNumber = ordersListModel.getPhone();
        orderId = ordersListModel.getOrderId();
        userId = ordersListModel.getUserId();
        note = ordersListModel.getNote();
        total = String.format("%.2f", ordersListModel.getTotal());
        shippingCharge = String.format("%.2f", ordersListModel.getShippingCharges());
        discount = String.format("%.2f", ordersListModel.getDiscount());
        address = ordersListModel.getAddress();
        listItem = ordersListModel.getItems();
        initialize();
        addHeaderToList();

        backButton.setOnClickListener(this);
        btnCall.setOnClickListener(this);

        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).getStatus().equalsIgnoreCase("1")) {
                listItem.get(i).isChecked = true;
            } else {
                listItem.get(i).isChecked = false;
            }
        }

        adapter = new CanceledOrderItemsAdapter(CanceledOrdersItemsListActivity.this);
        listRejectedItems.setAdapter(adapter);
    }

    private void addHeaderToList() {
        View headerView = getLayoutInflater().inflate(R.layout.layout_header_order_detail_address, null);
        mDeliveryAddress = (TextView) headerView.findViewById(R.id.txtDeliveryAddress);
        mNote = (TextView) headerView.findViewById(R.id.txtNote);
        mShippingCharges = (TextView) headerView.findViewById(R.id.shipping_charges);
        mDiscountVal = (TextView) headerView.findViewById(R.id.discountVal);
        mNoteLayout = (RelativeLayout) headerView.findViewById(R.id.noteLayout);

        if (note != null && !note.isEmpty()) {
            mNote.setText(note);
        } else {
            mNoteLayout.setVisibility(View.GONE);
        }

        mAddressLayout = (RelativeLayout) headerView.findViewById(R.id.addressLayout);
        if (address != null && !address.isEmpty()) {
            mDeliveryAddress.setText(address);
        } else {
            mAddressLayout.setVisibility(View.GONE);
        }
        mItemsPrice = (TextView) headerView.findViewById(R.id.items_price);
        mItemsPrice.setText(Util.getCurrency(CanceledOrdersItemsListActivity.this) + total);
        mShippingCharges = (TextView) headerView.findViewById(R.id.shipping_charges);
        mShippingCharges.setText(Util.getCurrency(CanceledOrdersItemsListActivity.this) + shippingCharge);
        mDiscountVal = (TextView) headerView.findViewById(R.id.discountVal);
        mDiscountVal.setText(Util.getCurrency(CanceledOrdersItemsListActivity.this) + discount);

        listRejectedItems.addHeaderView(headerView);

    }

    private void initialize() {
        listRejectedItems = (ListView) findViewById(R.id.listCanceledItems);
        textTitle = (TextView) findViewById(R.id.textTitle);
        textTitle.setText(name);
        backButton = (Button) findViewById(R.id.backButton);
        btnCall = (Button) findViewById(R.id.btnCall);
        mTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        mTotalAmount.setText(Util.getCurrency(CanceledOrdersItemsListActivity.this) + (total != null && !total.isEmpty() ? total : ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;


            case R.id.btnCall:
                if (phoneNumber.equalsIgnoreCase("")) {
                    DialogUtils.showAlertDialog(CanceledOrdersItemsListActivity.this, Constant.APP_TITLE, "Sorry! phone number is not available.");
                } else {
                    callAlert();
                }

                break;
        }
    }

    private void callAlert() {

        android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(this);


        adb.setTitle("Call " + phoneNumber + " ?");


        adb.setIcon(R.drawable.ic_launcher);


        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                actionCall();


            }
        });


        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    private void actionCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
        AnimUtil.slideFromLeftAnim(this);
    }


    class CanceledOrderItemsAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ViewHolder holder = null;

        public CanceledOrderItemsAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return listItem.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {


            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater
                        .inflate(R.layout.row_canceled_order_item_list, null);
                holder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
                holder.itemName = (TextView) convertView.findViewById(R.id.txtItemName);
                holder.itemWeight = (TextView) convertView.findViewById(R.id.txtWeight);
                holder.itemPrice = (TextView) convertView.findViewById(R.id.txtPrice);
                holder.itemQuantiy = (TextView) convertView.findViewById(R.id.txtLblQuantity);
                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);
                holder.toggle = (ToggleButton) convertView.findViewById(R.id.toggle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.itemName.setText(listItem.get(position).getName());
            holder.itemWeight.setText("'");
            holder.itemPrice.setText("");
            holder.itemWeight.setVisibility(View.GONE);
            holder.itemPrice.setVisibility(View.GONE);
            holder.itemQuantiy.setText("Qty: " + listItem.get(position).getQuantity());

            if (listItem.get(position).getImageMedium() != null && !listItem.get(position).getImageMedium().isEmpty()) {
                Picasso.with(CanceledOrdersItemsListActivity.this).load(listItem.get(position).getImageMedium()).error(R.drawable.no_image).into(holder.itemImage);
            } else {
                holder.itemImage.setImageResource(R.drawable.no_image);
            }


            if (listItem.get(position).isChecked) {
                holder.toggle.setChecked(true);
            } else {
                holder.toggle.setChecked(false);
            }

            holder.toggle.setEnabled(false);

            return convertView;
        }

        class ViewHolder {
            ImageView itemImage;
            TextView itemName;
            TextView itemWeight;
            TextView itemPrice;
            TextView itemQuantiy;
            RelativeLayout parent;
            ToggleButton toggle;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(CanceledOrdersItemsListActivity.this);
    }
}
