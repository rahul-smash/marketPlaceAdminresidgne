package com.signity.shopkeeperapp.orders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.DataAdapter;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.Order;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajinder on 28/9/15.
 */
public class ActiveOrderFragmentItem extends Fragment implements View.OnClickListener {


    private ListView listActiveOrdersItems;
    private Button buttonOrderProced, buttonOrderDecline, buttonMoveToShipping, buttonMoveToDelivered;
    private LinearLayout footer, footer2, footer3;
    ActiveOrderAdapter adapter;
    List<OrdersListModel> listOrderParent;
    List<OrdersListModel> listOrder;
    List<OrdersListModel> listOrderSelected;
    String type;
    Animation slideUpAnim;
    Animation slideDownAnim;
    PrefManager prefManager;
    private TextView noDataFound;

    String orderIDS = "";
    String orderStatus = "";
    String userId = "3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getActivity());
        type = getArguments().getString("type");
        listOrder = new ArrayList<>();
        listOrderSelected = new ArrayList<>();
    }


    // Setup List Element on the basis of mode Approve,Proccesing, shipping
    public void setUpListData() {

        listOrder.clear();


        if (type.equals(Constant.TYPE_APPROVE)) {

            for (int i = 0; i < listOrderParent.size(); i++) {
                if (listOrderParent.get(i).getStatus().equalsIgnoreCase("1")) {
                    listOrder.add(listOrderParent.get(i));
                }
            }
        } else if (type.equals(Constant.TYPE_PROCESSING)) {
            for (int i = 0; i < listOrderParent.size(); i++) {
//                if (listOrderParent.get(i).getStatus().equalsIgnoreCase("3")) {
//                    listOrder.add(listOrderParent.get(i));
//                }
                if (listOrderParent.get(i).getStatus().equalsIgnoreCase("1")) {
                    listOrder.add(listOrderParent.get(i));
                }
            }
        } else if (type.equals(Constant.TYPE_SHIPPING)) {
            for (int i = 0; i < listOrderParent.size(); i++) {
                if (listOrderParent.get(i).getStatus().equalsIgnoreCase("4")) {
                    listOrder.add(listOrderParent.get(i));
                }
            }
        } else if (type.equals(Constant.TYPE_DELIVERED)) {
            for (int i = 0; i < listOrderParent.size(); i++) {
                if (listOrderParent.get(i).getStatus().equalsIgnoreCase("5")) {
                    listOrder.add(listOrderParent.get(i));
                }
            }
        }


        adapter = new ActiveOrderAdapter(getActivity(), listOrder);
        listActiveOrdersItems.setAdapter(adapter);

        if (listOrder.size() == 0) {
            noDataFound.setVisibility(View.VISIBLE);
        } else {
            noDataFound.setVisibility(View.GONE);
        }
    }


    public void updateList() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Order>>() {
        }.getType();
        String json = prefManager.getSharedValue(PrefManager.PREF_PROCESSING);
        if (!TextUtils.isEmpty(json)) {
            listOrder = gson.fromJson(json, type);
        } else {
            listOrder = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                ActiveOrderFragmentItem.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_active_order_item, container, false);
        listActiveOrdersItems = (ListView) rootView.findViewById(R.id.listActiveOrdersItems);
        footer = (LinearLayout) rootView.findViewById(R.id.footer);
        footer2 = (LinearLayout) rootView.findViewById(R.id.footer2);
        footer3 = (LinearLayout) rootView.findViewById(R.id.footer3);
        noDataFound = (TextView) rootView.findViewById(R.id.noDataFound);
        buttonOrderProced = (Button) rootView.findViewById(R.id.btnOrderProceed);
        buttonOrderDecline = (Button) rootView.findViewById(R.id.btnDeclineOrder);
        buttonMoveToShipping = (Button) rootView.findViewById(R.id.btnMoveToShipping);
        buttonMoveToDelivered = (Button) rootView.findViewById(R.id.btnMoveToDeliver);


        slideUpAnim = AnimationUtils.loadAnimation(getActivity()
                .getApplicationContext(), R.anim.slide_up);
        slideDownAnim = AnimationUtils.loadAnimation(getActivity()
                .getApplicationContext(), R.anim.slide_down);

        buttonOrderProced.setOnClickListener(this);
        buttonOrderDecline.setOnClickListener(this);
        buttonMoveToShipping.setOnClickListener(this);
        buttonMoveToDelivered.setOnClickListener(this);

        //  getOrdersMethod();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        listOrderSelected.clear();
        footer.startAnimation(slideDownAnim);
        footer.setVisibility(View.GONE);
        getOrdersMethod();

    }

    class ActiveOrderAdapter extends BaseAdapter {

        Context context;

        List<OrdersListModel> listOrder;


        public ActiveOrderAdapter() {
            super();
        }

        public ActiveOrderAdapter(Context context, List<OrdersListModel> listOrder) {

            this.context = context;
            this.listOrder = listOrder;

        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getCount() {
            return listOrder.size();
        }

        @Override
        public Object getItem(int position) {
            return listOrder.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.row_list_active_orders, null);
                holder = new ViewHolder();
                holder.txtOrderId = (TextView) convertView.findViewById(R.id.valOdrId);
                holder.txtCustName = (TextView) convertView.findViewById(R.id.valCustName);
                holder.txtTotalAmount = (TextView) convertView.findViewById(R.id.valTotalAmt);
                holder.txtTime = (TextView) convertView.findViewById(R.id.valTime);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final OrdersListModel order = listOrder.get(position);

            holder.txtOrderId.setText(order.getOrderId());
            if (order.getCustomerName().equalsIgnoreCase("") || order.getCustomerName().equals(null)) {
                holder.txtCustName.setText("Guest User");
            } else {
                holder.txtCustName.setText(order.getCustomerName());
            }

            Double totalPrice = 0.00;
            Double itemsPrice = 0.00;

            for (int i = 0; i < order.getItems().size(); i++) {

                if (order.getItems().get(i).getStatus().equalsIgnoreCase("1")) {
                    itemsPrice = itemsPrice + (order.getItems().get(i).getPrice() * Integer.parseInt(order.getItems().get(i).getQuantity()));
                }
            }

            totalPrice = ((itemsPrice + order.getShippingCharges()) - order.getDiscount());

            holder.txtTotalAmount.setText(((Activity) context).getString(R.string.text_rs) + " " + totalPrice);
            holder.txtTime.setText(order.getTime());
            holder.checkbox.setChecked(false);

            if (listOrderSelected.contains(order)) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }

            if (type.equals(Constant.TYPE_DELIVERED)) {
                holder.checkbox.setVisibility(View.GONE);
            }
            holder.checkbox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (!listOrderSelected.contains(order)) {
                        listOrderSelected.add(order);

                        if (listOrderSelected.size() == 1) {
                            if (type.equals(Constant.TYPE_APPROVE)) {
                                footer.startAnimation(slideUpAnim);
                                footer.setVisibility(View.VISIBLE);
                            } else if (type.equals(Constant.TYPE_PROCESSING)) {
                                footer2.startAnimation(slideUpAnim);
                                footer2.setVisibility(View.VISIBLE);
                            } else if (type.equals(Constant.TYPE_SHIPPING)) {
                                footer3.startAnimation(slideUpAnim);
                                footer3.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        listOrderSelected.remove(order);
                        if (listOrderSelected.size() == 0) {
                            if (type.equals(Constant.TYPE_APPROVE)) {
                                footer.startAnimation(slideDownAnim);
                                footer.setVisibility(View.GONE);
                            } else if (type.equals(Constant.TYPE_PROCESSING)) {
                                footer2.startAnimation(slideDownAnim);
                                footer2.setVisibility(View.GONE);
                            } else if (type.equals(Constant.TYPE_SHIPPING)) {
                                footer3.startAnimation(slideDownAnim);
                                footer3.setVisibility(View.GONE);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type.equals(Constant.TYPE_APPROVE)) {

                        onButtonClick(view, listOrder.get(position).getItems(), listOrder.get(position).getCustomerName(),
                                listOrder.get(position).getPhone(), listOrder.get(position).getOrderId(),
                                listOrder.get(position).getUserId(), listOrder.get(position).getNote(), listOrder.get(position).getDiscount()
                                , listOrder.get(position).getTotal(), listOrder.get(position).getShippingCharges(), listOrder.get(position).getAddress());

                    } else if (type.equals(Constant.TYPE_PROCESSING)) {

                        DataAdapter.getInstance().setListItem(listOrder.get(position).getItems());
                        Intent dueOrderIntent = new Intent(context, DueOrderActivityWithoutUpdations.class);
                        dueOrderIntent.putExtra("name", listOrder.get(position).getCustomerName());
                        dueOrderIntent.putExtra("phone", listOrder.get(position).getPhone());
                        dueOrderIntent.putExtra("orderID", listOrder.get(position).getOrderId());
                        dueOrderIntent.putExtra("userID", listOrder.get(position).getUserId());
                        dueOrderIntent.putExtra("type", type);

                        dueOrderIntent.putExtra("note", listOrder.get(position).getNote());
                        dueOrderIntent.putExtra("discount", listOrder.get(position).getDiscount());
                        dueOrderIntent.putExtra("total", listOrder.get(position).getTotal());
                        dueOrderIntent.putExtra("shipping_charges", listOrder.get(position).getShippingCharges());
                        dueOrderIntent.putExtra("address", listOrder.get(position).getAddress());

                        context.startActivity(dueOrderIntent);
                        AnimUtil.slideFromRightAnim((Activity) context);

                    } else if (type.equals(Constant.TYPE_SHIPPING)) {

                        DataAdapter.getInstance().setListItem(listOrder.get(position).getItems());
                        Intent dueOrderIntent = new Intent(context, DueOrderActivityWithoutUpdations.class);
                        dueOrderIntent.putExtra("name", listOrder.get(position).getCustomerName());
                        dueOrderIntent.putExtra("phone", listOrder.get(position).getPhone());
                        dueOrderIntent.putExtra("orderID", listOrder.get(position).getOrderId());
                        dueOrderIntent.putExtra("userID", listOrder.get(position).getUserId());
                        dueOrderIntent.putExtra("type", type);

                        dueOrderIntent.putExtra("note", listOrder.get(position).getNote());
                        dueOrderIntent.putExtra("discount", listOrder.get(position).getDiscount());
                        dueOrderIntent.putExtra("total", listOrder.get(position).getTotal());
                        dueOrderIntent.putExtra("shipping_charges", listOrder.get(position).getShippingCharges());
                        dueOrderIntent.putExtra("address", listOrder.get(position).getAddress());

                        context.startActivity(dueOrderIntent);
                        AnimUtil.slideFromRightAnim((Activity) context);

                    } else if (type.equals(Constant.TYPE_DELIVERED)) {

                        DataAdapter.getInstance().setListItem(listOrder.get(position).getItems());
                        Intent dueOrderIntent = new Intent(context, DueOrderActivityWithoutUpdations.class);
                        dueOrderIntent.putExtra("name", listOrder.get(position).getCustomerName());
                        dueOrderIntent.putExtra("phone", listOrder.get(position).getPhone());
                        dueOrderIntent.putExtra("orderID", listOrder.get(position).getOrderId());
                        dueOrderIntent.putExtra("userID", listOrder.get(position).getUserId());
                        dueOrderIntent.putExtra("type", type);

                        dueOrderIntent.putExtra("note", listOrder.get(position).getNote());
                        dueOrderIntent.putExtra("discount", listOrder.get(position).getDiscount());
                        dueOrderIntent.putExtra("total", listOrder.get(position).getTotal());
                        dueOrderIntent.putExtra("shipping_charges", listOrder.get(position).getShippingCharges());
                        dueOrderIntent.putExtra("address", listOrder.get(position).getAddress());

                        context.startActivity(dueOrderIntent);
                        AnimUtil.slideFromRightAnim((Activity) context);

                    }
                }
            });

            return convertView;
        }

    }

    public void onButtonClick(View view, List<ItemListModel> list, String name, String phone, String orderID,
                              String userID, String note, Double discount,
                              Double total, Double shipping_charges, String address) {


        DataAdapter.getInstance().setListItem(list);
        Intent dueOrderIntent = new Intent(getActivity(), DueOrderActivity.class);
        dueOrderIntent.putExtra("name", name);
        dueOrderIntent.putExtra("phone", phone);
        dueOrderIntent.putExtra("orderID", orderID);
        dueOrderIntent.putExtra("userID", userID);
        dueOrderIntent.putExtra("type", "active");

        dueOrderIntent.putExtra("note", note);
        dueOrderIntent.putExtra("discount", discount);
        dueOrderIntent.putExtra("total", total);
        dueOrderIntent.putExtra("shipping_charges", shipping_charges);
        dueOrderIntent.putExtra("address", address);

        getActivity().startActivity(dueOrderIntent);
        AnimUtil.slideFromRightAnim((Activity) getActivity());
    }

    static class ViewHolder {
        TextView txtOrderId;
        TextView txtCustName;
        TextView txtTotalAmount;
        TextView txtTime;
        CheckBox checkbox;
        RelativeLayout parent;
    }

    public void getOrdersMethod() {

        if (Util.checkIntenetConnection(getActivity())) {
            getOrders();
        } else {
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
        }
    }

    public void getOrders() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", "all");
        param.put("api_key", "");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getValues.getData().getOrders().size() > 0) {
                        listOrderParent = getValues.getData().getOrders();
                        setUpListData();
                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                }
            }
            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnOrderProceed:
                orderStatus = "3";
                getOrderIDS();
                //   UiDialog.showAlertDialog(getActivity(), "Success", "Successfully Place order for processing", this);
                break;
            case R.id.btnMoveToShipping:
                orderStatus = "4";
                getOrderIDS();
                //    UiDialog.showAlertDialog(getActivity(), "Success", "Successfully place order for Shipping", this);
                break;
            case R.id.btnMoveToDeliver:

                orderStatus = "5";
                getOrderIDS();
                //   UiDialog.showAlertDialog(getActivity(), "Success", "Successfully place order for Delivery", this);
                break;
            case R.id.btnDeclineOrder:

                orderStatus = "2";
                alertBoxNew("Are you sure to Decline this order?");

                //   UiDialog.showAlertDialog(getActivity(), "Decline Order", "Are you sure to Decline Order?", this);
                break;

        }

    }

    void alertBoxNew(String message) {
        final DialogHandler dialogHandler = new DialogHandler(getActivity());

        dialogHandler.setDialog(Constant.APP_TITLE, message);
        dialogHandler.setPostiveButton("Ok", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogHandler.dismiss();
                        getOrderIDS();
                    }
                });

        dialogHandler.setNegativeButton("No", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogHandler.dismiss();
                    }
                });
    }

    private void getOrderIDS() {

        orderIDS = "";
        userId = "";

        for (int i = 0; i < listOrderSelected.size(); i++) {
            if (i == 0) {
                orderIDS = listOrderSelected.get(i).getOrderId();
                userId = listOrderSelected.get(i).getUserId();
            } else {
                orderIDS = orderIDS + ","
                        + listOrderSelected.get(i).getOrderId();
                userId = userId + ","
                        + listOrderSelected.get(i).getUserId();
            }
        }


        Log.v("orderStatus : ", "" + orderStatus);
        Log.v("orderIDS : ", "" + orderIDS);
        Log.v("userId : ", "" + userId);

        setOrderStatus();
    }

    private void setOrderStatus() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", userId);
//        param.put("api_key", "");
        param.put("order_status", orderStatus);
        param.put("order_ids", orderIDS);


        NetworkAdaper.getInstance().getNetworkServices().setOrderStatusForAll(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    footerViewGone();
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

    private void footerViewGone() {

        if (type.equals(Constant.TYPE_APPROVE)) {
            footer.startAnimation(slideDownAnim);
            footer.setVisibility(View.GONE);
        } else if (type.equals(Constant.TYPE_PROCESSING)) {
            footer2.startAnimation(slideDownAnim);
            footer2.setVisibility(View.GONE);
        } else if (type.equals(Constant.TYPE_SHIPPING)) {
            footer3.startAnimation(slideDownAnim);
            footer3.setVisibility(View.GONE);
        }
    }

    public void showAlertDialog(Context context, String title,
                                String message) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                context);
//
//        // set title
//        alertDialogBuilder.setTitle(title);
//
//        // set dialog message
//        alertDialogBuilder.setMessage(message).setCancelable(false)
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // if this button is clicked, close
//                        // current activity
//                        dialog.cancel();
//                        getOrdersMethod();
//                        ActiveOrderFragment.api_refreshed = true;
//
//
//                        if (type.equals(Constant.TYPE_APPROVE)) {
//                            ((MainActivity) getActivity()).printNUm(1);
//                        } else if (type.equals(Constant.TYPE_PROCESSING)) {
//                            ((MainActivity) getActivity()).printNUm(2);
//                        } else if (type.equals(Constant.TYPE_SHIPPING)) {
//                            ((MainActivity) getActivity()).printNUm(3);
//                        }
//
//                    }
//                });
//
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//
//        // show it
//        alertDialog.show();

        final DialogHandler dialogHandler = new DialogHandler(getActivity());

        dialogHandler.setDialog(Constant.APP_TITLE, message);
        dialogHandler.setPostiveButton("Ok", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogHandler.dismiss();
                        getOrdersMethod();
                        ActiveOrderFragment.api_refreshed = true;
//                        if (type.equals(Constant.TYPE_APPROVE)) {
//                            ((MainActivity) getActivity()).printNUm(1);
//                        } else
                        if (type.equals(Constant.TYPE_PROCESSING)) {
                            ((MainActivity) getActivity()).printNUm(1);
                        } else if (type.equals(Constant.TYPE_SHIPPING)) {
                            ((MainActivity) getActivity()).printNUm(2);
                        }
                    }
                });
    }


}
