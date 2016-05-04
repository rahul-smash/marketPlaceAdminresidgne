package com.signity.shopkeeperapp.orders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.DataAdapter;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 29/10/15.
 */
public class DueOrderDetailFragmentWithoutUpdations extends Fragment implements View.OnClickListener {


    ListView listDueOrderItems;

    ImageButton mOrderDetailBtn;
    Button mApproveOrder, mDeclineOrder;
    DueOrderItemsAdapterWithoutUpdations adapter;
    String name;
    String phoneNumber;
    String type = "";
    List<ItemListModel> listItem;
    ArrayList<String> acceptItemList = new ArrayList<String>();
    ArrayList<String> rejectItemList = new ArrayList<String>();

    String userId = "";
    String orderId = "";
    String orderStatus = "";
    String itemAcceptIds = "";
    String itemRejectIds = "";

    String note = "";
    String address = "";
    Double total = 0.00;
    Double discount = 0.00;
    Double shipping_charges = 0.00;

    RelativeLayout mOrderDetailLayout, mDetailBtnBlock;

    Animation slideUpAnim;
    Animation slideDownAnim;

    TextView mTotalAmount;
    TextView mDeliveryAddress, mNote, mItemsPrice, mShippingCharges, mDiscountVal;
    RelativeLayout mNoteLayout, mAddressLayout;
    ImageButton btnOrderProceed, btnMoveToShipping, btnMoveToDeliver;
    Button buttonRejectOrder;
    private boolean isAlreadyShipped = false;
    private boolean isAlreadyProcess = false;
    private boolean isAlreadyDelivered = false;


    private LinearLayout footer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        phoneNumber = getArguments().getString("phone");
        orderId = getArguments().getString("orderID");
        userId = getArguments().getString("userID");
        type = getArguments().getString("type");
        listItem = DataAdapter.getInstance().getListItem();

        note = getArguments().getString("note");
        address = getArguments().getString("address");
        total = getArguments().getDouble("total");
        discount = getArguments().getDouble("discount");
        shipping_charges = getArguments().getDouble("shipping_charges");
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                DueOrderDetailFragmentWithoutUpdations.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_due_order_detail, container, false);
        listDueOrderItems = (ListView) rootView.findViewById(R.id.listDueOrderItems);

        btnOrderProceed = (ImageButton) rootView.findViewById(R.id.btnOrderProceed);
        btnMoveToShipping = (ImageButton) rootView.findViewById(R.id.btnMoveToShipping);
        btnMoveToDeliver = (ImageButton) rootView.findViewById(R.id.btnMoveToDeliver);
        buttonRejectOrder = (Button) rootView.findViewById(R.id.relDeclineOrder);
        btnMoveToShipping.setOnClickListener(this);
        btnMoveToDeliver.setOnClickListener(this);
        buttonRejectOrder.setOnClickListener(this);
        setUpButtonStatus();
        footer = (LinearLayout) rootView.findViewById(R.id.footer);
        footer.setVisibility(View.GONE);
        mApproveOrder = (Button) rootView.findViewById(R.id.btnApproveOrder);
        mApproveOrder.setVisibility(View.GONE);

        mDeclineOrder = (Button) rootView.findViewById(R.id.btnDeclineOrder);
        mDeclineOrder.setVisibility(View.GONE);

        mOrderDetailBtn = (ImageButton) rootView.findViewById(R.id.btnOrderDetail);


        mOrderDetailLayout = (RelativeLayout) rootView.findViewById(R.id.layout_order_detail);

        mDetailBtnBlock = (RelativeLayout) rootView.findViewById(R.id.detailBtnBlock);
//        mDetailBtnBlock.setOnClickListener(this);

        slideUpAnim = AnimationUtils.loadAnimation(getActivity()
                .getApplicationContext(), R.anim.slide_up_for_order_detail);
        slideDownAnim = AnimationUtils.loadAnimation(getActivity()
                .getApplicationContext(), R.anim.slide_down_for_oder_detail);

        mTotalAmount = (TextView) rootView.findViewById(R.id.txtTotalAmount);

        addHeaderToList();

        handleBackButton(rootView);
        handleCallButton(rootView);
        setHeader(rootView);


        adapter = new DueOrderItemsAdapterWithoutUpdations(getActivity());
        listDueOrderItems.setAdapter(adapter);

        setOrderDetails();

        return rootView;
    }

    private void setUpButtonStatus() {

        if (type.equalsIgnoreCase(Constant.TYPE_PROCESSING)) {
            isAlreadyProcess = true;
            btnOrderProceed.setSelected(true);
            btnMoveToShipping.setEnabled(true);
            btnMoveToDeliver.setEnabled(false);
            buttonRejectOrder.setVisibility(View.VISIBLE);
            buttonRejectOrder.setEnabled(true);
        } else if (type.equalsIgnoreCase(Constant.TYPE_SHIPPING)) {
            isAlreadyShipped = true;
            btnOrderProceed.setSelected(true);
            btnMoveToShipping.setSelected(true);
            btnMoveToDeliver.setEnabled(true);
            buttonRejectOrder.setVisibility(View.GONE);
        } else if (type.equalsIgnoreCase(Constant.TYPE_DELIVERED)) {
            isAlreadyDelivered = true;
            btnOrderProceed.setSelected(true);
            btnMoveToShipping.setSelected(true);
            btnMoveToDeliver.setSelected(true);
            buttonRejectOrder.setVisibility(View.GONE);
        }
    }


    private void addHeaderToList() {
        View headerView = (View) getActivity().getLayoutInflater().inflate(R.layout.layout_header_order_detail_address, null);
        mDeliveryAddress = (TextView) headerView.findViewById(R.id.txtDeliveryAddress);
        mNote = (TextView) headerView.findViewById(R.id.txtNote);
        mItemsPrice = (TextView) headerView.findViewById(R.id.items_price);
        mShippingCharges = (TextView) headerView.findViewById(R.id.shipping_charges);
        mDiscountVal = (TextView) headerView.findViewById(R.id.discountVal);

        mNoteLayout = (RelativeLayout) headerView.findViewById(R.id.noteLayout);
        mAddressLayout = (RelativeLayout) headerView.findViewById(R.id.addressLayout);
        listDueOrderItems.addHeaderView(headerView);

    }

    private void handleCallButton(View rootView) {

        ((Button) rootView.findViewById(R.id.btnCall)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phoneNumber.equalsIgnoreCase("")) {
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Sorry! phone number is not available.");
                } else {
                    callAlert();
                }
            }
        });
    }

    private void callAlert() {

        android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(getActivity());


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
        AnimUtil.slideFromLeftAnim(getActivity());
    }

    public void handleBackButton(View rootView) {
        ((Button) rootView.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    public void setHeader(View rootView) {
        ((TextView) rootView.findViewById(R.id.textTitle)).setText(name);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnApproveOrder:
                if (!Util.checkIntenetConnection(getActivity())) {
                    DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                    return;
                }
                orderStatus = "1";
                calculateIDS();
                break;
//            case R.id.btnDeclineOrder:
//                alertBox("Are you sure to Decline this order?");
//                break;
            case R.id.detailBtnBlock:
                actionForOrderDetailView();
                break;
            case R.id.relDeclineOrder:
                if (!Util.checkIntenetConnection(getActivity())) {
                    DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                    return;
                }
                if (buttonRejectOrder.isEnabled()) {
                    alertBox("Are you sure to Decline this order?");
                } else {
                    Toast.makeText(getActivity(), "Your order is shipped", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnMoveToShipping:
                if (!Util.checkIntenetConnection(getActivity())) {
                    DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                    return;
                }
                if (btnMoveToShipping.isEnabled()) {
                    if (!isAlreadyShipped) {
                        setOrderForShipping();
                    } else {
                        Toast.makeText(getActivity(), "Order already Shipped", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please process your order first", Toast.LENGTH_SHORT).show();
                }
//                getOrderIDS();
                break;
            case R.id.btnMoveToDeliver:
                if (!Util.checkIntenetConnection(getActivity())) {
                    DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                    return;
                }
                if (btnMoveToDeliver.isEnabled()) {
                    if (!isAlreadyDelivered) {
                        setOrderForDelivery();
                    } else {
                        Toast.makeText(getActivity(), "Order already Delivered", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please shipped your order first", Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }

    private void actionForOrderDetailView() {

        if (mOrderDetailBtn.isSelected()) {
            orderDetailViewGone();
        } else {

            orderDetailViewVisible();
        }

    }

    private void orderDetailViewGone() {

        mOrderDetailBtn.setSelected(false);
        mOrderDetailLayout.startAnimation(slideUpAnim);
        mOrderDetailLayout.setVisibility(View.GONE);

    }

    private void orderDetailViewVisible() {

        mOrderDetailBtn.setSelected(true);
        mOrderDetailLayout.startAnimation(slideDownAnim);
        mOrderDetailLayout.setVisibility(View.VISIBLE);

    }

    void alertBox(String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setCancelable(false);
        adb.setTitle(null);
        // adb.setMessage(R.string.select);
        TextView msg = new TextView(getActivity());
        msg.setText(message);
        msg.setPadding(10, 10, 10, 10);
        msg.setGravity(Gravity.CENTER);
        msg.setTextSize(20);
        try {
            if (android.os.Build.VERSION.SDK_INT > 14)
                msg.setTextColor(Color.BLACK);
            else
                msg.setTextColor(Color.WHITE);
        } catch (Exception e) {
            // TODO: handle exception
        }
        adb.setView(msg);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
                orderStatus = "2";
                calculateIDS();
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        AlertDialog alert = adb.create();
        alert.show();
    }

    private void calculateIDS() {

        acceptItemList.clear();
        rejectItemList.clear();

        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).isChecked) {
                acceptItemList.add(listItem.get(i).getCid());
            } else {
                rejectItemList.add(listItem.get(i).getCid());
            }
        }

        getItemIDS();
    }

    private void getItemIDS() {

        itemAcceptIds = "";
        itemRejectIds = "";

        for (int i = 0; i < acceptItemList.size(); i++) {
            if (i == 0) {
                itemAcceptIds = acceptItemList.get(i);
            } else {
                itemAcceptIds = itemAcceptIds + ","
                        + acceptItemList.get(i);
            }
        }

        for (int i = 0; i < rejectItemList.size(); i++) {
            if (i == 0) {
                itemRejectIds = rejectItemList.get(i);
            } else {
                itemRejectIds = itemRejectIds + ","
                        + rejectItemList.get(i);
            }
        }
        setOrderForReject();
    }

    private void setOrderStatus() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", userId);
        param.put("order_status", orderStatus);
        param.put("order_id", orderId);
        param.put("item_accept_ids", itemAcceptIds);
        param.put("item_reject_ids", itemRejectIds);

        NetworkAdaper.getInstance().getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }

    private void setOrderForShipping() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", userId);
//        param.put("api_key", "");
        param.put("order_status", "4");
        param.put("order_ids", orderId);


        NetworkAdaper.getInstance().getNetworkServices().setOrderStatusForAll(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    isAlreadyShipped = true;
                    btnOrderProceed.setSelected(true);
                    btnMoveToShipping.setSelected(true);
                    btnMoveToDeliver.setEnabled(true);
                    buttonRejectOrder.setEnabled(false);
                    ProgressDialogUtil.hideProgressDialog();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });

    }

    private void setOrderForDelivery() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", userId);
//        param.put("api_key", "");
        param.put("order_status", "5");
        param.put("order_ids", orderId);

        NetworkAdaper.getInstance().getNetworkServices().setOrderStatusForAll(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    isAlreadyDelivered = true;
                    btnOrderProceed.setSelected(true);
                    btnMoveToShipping.setSelected(true);
                    btnMoveToDeliver.setSelected(true);
                    buttonRejectOrder.setEnabled(false);
                    ProgressDialogUtil.hideProgressDialog();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });

    }

    private void setOrderForReject() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", userId);
        param.put("order_status", orderStatus);
        param.put("order_ids", orderId);
//        param.put("item_accept_ids", itemAcceptIds);
//        param.put("item_reject_ids", itemRejectIds);

        NetworkAdaper.getInstance().getNetworkServices().rejectOrder(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    final DialogHandler dialogHandler = new DialogHandler(getActivity());
                    dialogHandler.setDialog(Constant.APP_TITLE, getValues.getMessage());
                    dialogHandler.setPostiveButton("Ok", true)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogHandler.dismiss();
                                    getActivity().onBackPressed();
                                }
                            });
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }

    public void showAlertDialog(Context context, String title,
                                String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
//                        getActivity().onBackPressed();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /* Listadapter   */
    class DueOrderItemsAdapterWithoutUpdations extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ViewHolder holder = null;

        public DueOrderItemsAdapterWithoutUpdations(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rejectItemList.clear();
            acceptItemList.clear();

            if (type.equalsIgnoreCase("due")) {

                for (int i = 0; i < listItem.size(); i++) {
                    listItem.get(i).isChecked = true;
                }

            } else {
                for (int i = 0; i < listItem.size(); i++) {
                    if (listItem.get(i).getStatus().equalsIgnoreCase("1")) {
                        listItem.get(i).isChecked = true;
                    } else {
                        listItem.get(i).isChecked = false;
                    }
                }
            }
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
                        .inflate(R.layout.row_list_due_orders_items, null);
                holder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
                holder.itemName = (TextView) convertView.findViewById(R.id.txtItemName);
                holder.itemPrice = (TextView) convertView.findViewById(R.id.txtPrice);
                holder.txtWeight = (TextView) convertView.findViewById(R.id.txtWeight);
                holder.itemQuantiy = (TextView) convertView.findViewById(R.id.txtLblQuantity);
                holder.itemsTotal = (TextView) convertView.findViewById(R.id.txtLblTotal);
                holder.toggle = (ToggleButton) convertView.findViewById(R.id.toggle);
                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ItemListModel data = listItem.get(position);
            holder.itemName.setText(listItem.get(position).getName());
            holder.itemPrice.setText("Price: " +Util.getCurrency(context) + " " + listItem.get(position).getPrice());
            holder.itemQuantiy.setText("Qty: " + listItem.get(position).getQuantity());

            if ((data.getWeight() != null && !(data.getWeight().isEmpty())) && (data.getUnitType() != null && !(data.getUnitType()
                    .isEmpty()))) {
                holder.txtWeight.setText(data.getWeight() + " " + data.getUnitType());
                holder.txtWeight.setVisibility(View.VISIBLE);
            } else {
                holder.txtWeight.setVisibility(View.GONE);
            }


            if (listItem.get(position).getImageMedium() != null && !listItem.get(position).getImageMedium().isEmpty()) {
                Picasso.with(getActivity()).load(listItem.get(position).getImageMedium()).error(R.drawable.no_image).into(holder.itemImage);
            } else {
                holder.itemImage.setImageResource(R.drawable.no_image);
            }

            if (listItem.get(position).isChecked) {
                holder.toggle.setChecked(true);
            } else {
                holder.toggle.setChecked(false);
            }

            Double itemsTotal = 0.00;
            itemsTotal = listItem.get(position).getPrice() * Integer.parseInt(listItem.get(position).getQuantity());
            holder.itemsTotal.setText("Total: " + Util.getCurrency(context) + " " + itemsTotal);


            holder.toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listItem.get(position).isChecked = !listItem.get(position).isChecked;

                    notifyDataSetChanged();

                }
            });

            holder.toggle.setEnabled(false);

            return convertView;
        }

        class ViewHolder {
            ImageView itemImage;
            TextView itemName;
            TextView itemPrice;
            TextView txtWeight;
            TextView itemQuantiy;
            TextView itemsTotal;
            ToggleButton toggle;
            RelativeLayout parent;
        }


    }


    public void setOrderDetails() {

        mDeliveryAddress.setText(address);

        if (note.equalsIgnoreCase("") || note.equalsIgnoreCase(null)) {

            mNoteLayout.setVisibility(View.GONE);
        } else {
            mNote.setText(note);

        }

        Double itemsAmount = 0.00;

        for (int i = 0; i < listItem.size(); i++) {

            if (listItem.get(i).isChecked) {
                itemsAmount = itemsAmount + (listItem.get(i).getPrice() * Integer.parseInt(listItem.get(i).getQuantity()));
            }
        }

        Double totalAmount = 0.00;
        totalAmount = (itemsAmount + shipping_charges) - discount;

        mTotalAmount.setText(Util.getCurrency(getActivity()) + " " + totalAmount);
        mItemsPrice.setText(Util.getCurrency(getActivity()) + " " + itemsAmount);
        mShippingCharges.setText(Util.getCurrency(getActivity()) + " " + shipping_charges);
        mDiscountVal.setText(Util.getCurrency(getActivity()) + " " + discount);


    }
}
