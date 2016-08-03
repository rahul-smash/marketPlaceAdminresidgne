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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.OrderTaxModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.StoreTaxModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

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
    Double tax = 0.00;
    Double shipping_charges = 0.00;

    RelativeLayout mOrderDetailLayout, mDetailBtnBlock;

    Animation slideUpAnim;
    Animation slideDownAnim;

    TextView mTotalAmount;
    TextView mDeliveryAddress, mNote, mItemsPrice, mShippingCharges, mDiscountVal, mTaxVal,shipping_charges_text,discountLblText;
    RelativeLayout mNoteLayout, mAddressLayout;
    ImageButton btnOrderProceed, btnMoveToShipping, btnMoveToDeliver;
    Button buttonRejectOrder;
    private boolean isAlreadyShipped = false;
    private boolean isAlreadyProcess = false;
    private boolean isAlreadyDelivered = false;


    private OrdersListModel ordersListModel;
    private PrefManager prefManager;
    private LinearLayout linearDynamicTaxBlock;
    RelativeLayout shipping_layout,discount_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getActivity());
        ordersListModel = (OrdersListModel) getArguments().getSerializable("object");
        setUiData();
    }

    public void setUiData() {
        name = ordersListModel.getCustomerName();
        phoneNumber = ordersListModel.getPhone();
        orderId = ordersListModel.getOrderId();
        userId = ordersListModel.getUserId();
        note = ordersListModel.getNote();
        total = ordersListModel.getTotal();
        shipping_charges = ordersListModel.getShippingCharges();
        discount = ordersListModel.getDiscount();
        address = ordersListModel.getAddress();
        listItem = ordersListModel.getItems();
        tax = ordersListModel.getTax();
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
        mOrderDetailBtn = (ImageButton) rootView.findViewById(R.id.btnOrderDetail);
        mOrderDetailLayout = (RelativeLayout) rootView.findViewById(R.id.layout_order_detail);


        slideUpAnim = AnimationUtils.loadAnimation(getActivity()
                .getApplicationContext(), R.anim.slide_up_for_order_detail);
        slideDownAnim = AnimationUtils.loadAnimation(getActivity()
                .getApplicationContext(), R.anim.slide_down_for_oder_detail);

        mTotalAmount = (TextView) rootView.findViewById(R.id.txtTotalAmount);

        addHeaderToList();

        handleBackButton(rootView);
        handleCallButton(rootView);
        setHeader(rootView);

        adapter = new DueOrderItemsAdapterWithoutUpdations(getActivity(), listItem);
        listDueOrderItems.setAdapter(adapter);

        setOrderDetails();

        return rootView;
    }

    private void setUpButtonStatus() {

        if (ordersListModel.getStatus().equalsIgnoreCase("1")) {
            isAlreadyProcess = true;
            btnOrderProceed.setSelected(true);
            btnMoveToShipping.setEnabled(true);
            btnMoveToDeliver.setEnabled(false);
            buttonRejectOrder.setVisibility(View.VISIBLE);
            buttonRejectOrder.setEnabled(true);
        } else if (ordersListModel.getStatus().equalsIgnoreCase("4")) {
            isAlreadyShipped = true;
            btnOrderProceed.setSelected(true);
            btnMoveToShipping.setSelected(true);
            btnMoveToDeliver.setEnabled(true);
            buttonRejectOrder.setVisibility(View.GONE);
        } else if (ordersListModel.getStatus().equalsIgnoreCase("5")) {
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
        linearDynamicTaxBlock = (LinearLayout) headerView.findViewById(R.id.dynamicTaxBlock);
        mNoteLayout = (RelativeLayout) headerView.findViewById(R.id.noteLayout);
        mAddressLayout = (RelativeLayout) headerView.findViewById(R.id.addressLayout);
        shipping_charges_text=(TextView)headerView.findViewById(R.id.shipping_charges_text);
        discountLblText=(TextView)headerView.findViewById(R.id.discountLblText);
        shipping_layout=(RelativeLayout)headerView.findViewById(R.id.shipping_layout);
        discount_layout=(RelativeLayout)headerView.findViewById(R.id.discount_layout);
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
                setOrderForReject();
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
                    prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "1");
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
                    prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "1");
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
                    prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "1");
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

        List<ItemListModel> listItem;

        public DueOrderItemsAdapterWithoutUpdations(Context context, List<ItemListModel> listItem) {
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.listItem = listItem;
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
                holder.itemName = (TextView) convertView.findViewById(R.id.txtItemName);
                holder.itemPrice = (TextView) convertView.findViewById(R.id.txtPrice);
                holder.txtWeight = (TextView) convertView.findViewById(R.id.txtWeight);
                holder.itemQuantiy = (TextView) convertView.findViewById(R.id.txtLblQuantity);
                holder.itemsTotal = (TextView) convertView.findViewById(R.id.txtLblTotal);
                holder.toggle = (ImageButton) convertView.findViewById(R.id.toggle);
                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ItemListModel itemListModel = listItem.get(position);
            holder.itemName.setText(itemListModel.getName());
            holder.itemPrice.setText("Price: " + Util.getCurrency(context) + "" + itemListModel.getPrice());
            holder.itemQuantiy.setText("Qty: " + listItem.get(position).getQuantity());

            if ((itemListModel.getWeight() != null && !(itemListModel.getWeight().isEmpty())) && (itemListModel.getUnitType() != null && !(itemListModel.getUnitType()
                    .isEmpty()))) {
                holder.txtWeight.setText(itemListModel.getWeight() + " " + itemListModel.getUnitType());
                holder.txtWeight.setVisibility(View.VISIBLE);
            } else {
                holder.txtWeight.setVisibility(View.GONE);
            }


            if (itemListModel.getStatus().equalsIgnoreCase("2")) {
                holder.toggle.setSelected(false);
            } else {
                holder.toggle.setSelected(true);
            }

            Double itemsTotal = 0.00;
            itemsTotal = listItem.get(position).getPrice() * Integer.parseInt(listItem.get(position).getQuantity());
            holder.itemsTotal.setText("Total: " + Util.getCurrency(context) + "" + itemsTotal);

            holder.toggle.setEnabled(false);

            return convertView;
        }

        class ViewHolder {
            TextView itemName;
            TextView itemPrice;
            TextView txtWeight;
            TextView itemQuantiy;
            TextView itemsTotal;
            ImageButton toggle;
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
        totalAmount = (itemsAmount + shipping_charges) - discount + tax;

        mTotalAmount.setText(Util.getCurrency(getActivity()) + "" + Util.getDoubleValue(totalAmount));
        mItemsPrice.setText(Util.getCurrency(getActivity()) + "" + Util.getDoubleValue(itemsAmount));
        mShippingCharges.setText(Util.getCurrency(getActivity()) + "" + Util.getDoubleValue(shipping_charges));
        mDiscountVal.setText("-"+Util.getCurrency(getActivity()) + "" + Util.getDoubleValue(discount));

        if (shipping_charges == 0.0) {
            shipping_charges_text.setVisibility(View.GONE);
            shipping_layout.setVisibility(View.GONE);
        } else {
            shipping_charges_text.setVisibility(View.VISIBLE);
            shipping_layout.setVisibility(View.VISIBLE);
        }

        if (discount == 0.0) {
            discountLblText.setVisibility(View.GONE);
            discount_layout.setVisibility(View.GONE);
        } else {
            discountLblText.setVisibility(View.VISIBLE);
            discount_layout.setVisibility(View.VISIBLE);
        }

        setupTaxModule();
    }

    private void setupTaxModule() {
        List<StoreTaxModel> fixedStoreTaxes = ordersListModel.getStoreTaxes();
        List<OrderTaxModel> taxes = ordersListModel.getTaxes();
        if (fixedStoreTaxes != null && fixedStoreTaxes.size() > 0) {
            for (StoreTaxModel storeTaxModel : fixedStoreTaxes) {
                View child = getActivity().getLayoutInflater().inflate(R.layout.tax_row_layout, null);
                TextView tax_label = (TextView) child.findViewById(R.id.tax_label);
                TextView tax_value = (TextView) child.findViewById(R.id.tax_value);
                TextView rs5 = (TextView) child.findViewById(R.id.rs5);

                String currency = Util.getCurrency(getActivity());
                if (currency.contains("\\")) {
                    rs5.setText(unescapeJavaString(currency));
                } else {
                    rs5.setText(currency);
                }
                tax_label.setText("" + storeTaxModel.getFixedTaxLabel());
                tax_value.setText("" + Util.getDoubleValue(storeTaxModel.getFixedTaxAmount()));

                Double tax=null;
                try {
                    tax= Double.parseDouble(storeTaxModel.getFixedTaxAmount());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if(tax!=null && tax!=0.0){
                    if (storeTaxModel.getIsTaxEnable() != null && storeTaxModel.getIsTaxEnable().equalsIgnoreCase("1")) {
                        linearDynamicTaxBlock.addView(child);
                    }
                }

            }
        }
        if (taxes != null && taxes.size() > 0) {
            for (OrderTaxModel taxModel : taxes) {
                View child = getActivity().getLayoutInflater().inflate(R.layout.tax_row_layout, null);
                TextView tax_label = (TextView) child.findViewById(R.id.tax_label);
                TextView tax_value = (TextView) child.findViewById(R.id.tax_value);
                TextView rs5 = (TextView) child.findViewById(R.id.rs5);

                String currency = Util.getCurrency(getActivity());
                if (currency.contains("\\")) {
                    rs5.setText(unescapeJavaString(currency));
                } else {
                    rs5.setText(currency);
                }
                tax_label.setText("" + taxModel.getLabel() + "(" + taxModel.getRate() + "%)");
                tax_value.setText("" + Util.getDoubleValue(taxModel.getTax()));
                Double taxValue = null;
                try {
                    taxValue = Double.parseDouble(taxModel.getTax());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (taxValue != null && taxValue != 0.0) {
                    linearDynamicTaxBlock.addView(child);
                }

            }
        }
        if (fixedStoreTaxes != null && fixedStoreTaxes.size() > 0) {
            for (StoreTaxModel storeTaxModel : fixedStoreTaxes) {
                View child = getActivity().getLayoutInflater().inflate(R.layout.tax_row_layout, null);
                TextView tax_label = (TextView) child.findViewById(R.id.tax_label);
                TextView tax_value = (TextView) child.findViewById(R.id.tax_value);
                TextView rs5 = (TextView) child.findViewById(R.id.rs5);

                String currency = Util.getCurrency(getActivity());
                if (currency.contains("\\")) {
                    rs5.setText(unescapeJavaString(currency));
                } else {
                    rs5.setText(currency);
                }
                tax_label.setText("" + storeTaxModel.getFixedTaxLabel());
                tax_value.setText("" + Util.getDoubleValue(storeTaxModel.getFixedTaxAmount()));

                Double tax=null;
                try {
                    tax= Double.parseDouble(storeTaxModel.getFixedTaxAmount());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if(tax!=null && tax!=0.0){
                    if (storeTaxModel.getIsTaxEnable() != null && storeTaxModel.getIsTaxEnable().equalsIgnoreCase("0")) {
                        linearDynamicTaxBlock.addView(child);
                    }
                }


            }
        }
    }

    public String unescapeJavaString(String st) {

        StringBuilder sb = new StringBuilder(st.length());

        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == st.length() - 1) ? '\\' : st
                        .charAt(i + 1);
// Octal escape?
                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                            && st.charAt(i + 1) <= '7') {
                        code += st.charAt(i + 1);
                        i++;
                        if ((i < st.length() - 1) && st.charAt(i + 1) >= '0'
                                && st.charAt(i + 1) <= '7') {
                            code += st.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append((char) Integer.parseInt(code, 8));
                    continue;
                }
                switch (nextChar) {
                    case '\\':
                        ch = '\\';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
// Hex Unicode: u????
                    case 'u':
                        if (i >= st.length() - 5) {
                            ch = 'u';
                            break;
                        }
                        int code = Integer.parseInt(
                                "" + st.charAt(i + 2) + st.charAt(i + 3)
                                        + st.charAt(i + 4) + st.charAt(i + 5), 16);
                        sb.append(Character.toChars(code));
                        i += 5;
                        continue;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

}
