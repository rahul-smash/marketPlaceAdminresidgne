package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.OrderItemResponseModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.ACTION_DIAL;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTotal, txtTotalPrice, txtCartSavings, txtAddress, txtDate, txtStausVal, txtnoteValue, txtItems;
    ListView recyclerView;
    ImageView imgGuideMe;
    Button btnCall;
    String status;
    String getLat = "";
    String getLong = "";
    String destinationLat, destinationLang;
    String name;
    String phoneNumber;
    String type = "";
    List<ItemListModel> listItem;
    String userId = "";
    String orderId = "";
    String orderStatus = "";
    String note = "";
    String address = "";
    Double total = 0.00;
    Double tax = 0.00;
    Double discount = 0.00;
    Double shipping_charges = 0.00;
    String time;
    ListView listDueOrderItems;
    OrderDetailAdapter adapter;
    Button backButton;
    private OrdersListModel ordersListModel;
    private PrefManager prefManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity);
        setUpToolbar();
        prefManager = new PrefManager(getApplicationContext());
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        ordersListModel = (OrdersListModel) bundle.getSerializable("object");
        initview();
        setUiData();

        setOrderDetails();
        initListAdapter();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initListAdapter() {
        adapter = new OrderDetailAdapter(getApplicationContext(), listItem);
        listDueOrderItems.setAdapter(adapter);
    }

    public void setUiData() {
        time = ordersListModel.getTime();
        status = ordersListModel.getStatus();
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
        destinationLat = ordersListModel.getDestinationuser_lat();
        destinationLang = ordersListModel.getDestinationuser_lng();
        ordersListModel.getItems().size();
        Log.i("@@OrderListFragment---", "" + address + status + "DestingLAtLng" + destinationLat + destinationLang + "----" + ordersListModel.getItems().size());
    }

    public void setOrderDetails() {
        txtItems.setText(String.valueOf(ordersListModel.getItems().size()) + " Items ");
        txtDate.setText(time);
        txtAddress.setText(address);
        if (note.equalsIgnoreCase("") || note.equalsIgnoreCase(null)) {
            txtnoteValue.setVisibility(View.VISIBLE);
            txtnoteValue.setText(note);
        } else {
            txtnoteValue.setText(note);
        }
        txtTotalPrice.setText(Util.getCurrency(getApplicationContext()) + "" + Util.getDoubleValue(ordersListModel.getTotal()));
        txtStausVal.setText(ordersListModel.getStatus());
        //  txtItems.setText(listItem.size());
        if (ordersListModel.getStatus().equalsIgnoreCase("1")) {
            txtStausVal.setText("Processing");
            txtStausVal.setBackgroundResource(R.drawable.bg_transaparent);
        } else if (ordersListModel.getStatus().equalsIgnoreCase("4")) {
            txtStausVal.setText("Shipping");
            txtStausVal.setBackgroundResource(R.drawable.bg_transaparent);
        } else if (ordersListModel.getStatus().equalsIgnoreCase("5")) {
            Log.i("@@DeleiverdOrder__", "order.getStatus()");
            txtStausVal.setText("Delivered");
            txtStausVal.setBackgroundResource(R.drawable.bg_transaparent);
        } else if (ordersListModel.getStatus().equalsIgnoreCase("0")) {
            txtStausVal.setText("Due Orders");
            txtStausVal.setBackgroundResource(R.drawable.bg_transaparent);
        } else if (ordersListModel.getStatus().equalsIgnoreCase("2")) {
            txtStausVal.setText("Rejected");
            txtStausVal.setBackgroundResource(R.drawable.bg_transaparent);
        } else if (ordersListModel.getStatus().equalsIgnoreCase("6")) {
            txtStausVal.setText("Cancelled");
            txtStausVal.setBackgroundResource(R.drawable.bg_transaparent);
        }

    }

    private void initview() {
        toolbar = findViewById(R.id.toolbar);

        listDueOrderItems = (ListView) findViewById(R.id.recyclerView);
        // backButton = (Button) findViewById(R.id.backButton);
        btnCall = (Button) findViewById(R.id.btnCall);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        txtCartSavings = (TextView) findViewById(R.id.txtCartSavings);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtDate = (TextView) findViewById(R.id.tv_order_date_time);
        txtStausVal = (TextView) findViewById(R.id.txtStausVal);
        txtnoteValue = (TextView) findViewById(R.id.txtnoteValue);
        txtItems = (TextView) findViewById(R.id.mtxtItems);
        imgGuideMe = (ImageView) findViewById(R.id.imgGuideMe);
        // recyclerView = (ListView) findViewById(R.id.recyclerView);
        imgGuideMe.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        //   backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
     /*   if (view == backButton) {
            onBackPressed();
        }*/
        if (view == imgGuideMe) {
            if (getLat.equalsIgnoreCase("") || getLong.equalsIgnoreCase("") || destinationLat.equalsIgnoreCase("") || destinationLang.equalsIgnoreCase("")) {
                Log.i("@@---1", "" + getLat);
                Log.i("@@---2", "" + getLong);
                Log.i("@@---3", "" + destinationLat);
                Log.i("@@---4", "" + destinationLang);
                Log.i("@@---FirstCondition", "" + "FirstCondition");
                String map = "http://maps.google.co.in/maps?q=" + address;
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            } else {
                Log.i("@@---else", "" + "else");
                //   Toast.makeText(getActivity(),"blankAddrees"+address,Toast.LENGTH_SHORT).show();
                openMap(getLat, getLong, destinationLat, destinationLang);


            }
        }
        if (view == btnCall) {
            if (phoneNumber.equalsIgnoreCase("")) {
                DialogUtils.showAlertDialog(getApplicationContext(), Constant.APP_TITLE, "Sorry! phone number is not available.");
            } else {
                callAlert();
            }
        }

    }

    private void callAlert() {
        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(OrderDetailActivity.this);
        adb.setTitle("Call " + phoneNumber + " ?");
        adb.setIcon(R.drawable.ic_launcher);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                actionCall();
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void actionCall() {

        try {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
                AnimUtil.slideFromRightAnim(OrderDetailActivity.this);
            } else {
                Toast.makeText(getApplicationContext(), "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openMap(String src_lat, String src_ltg, String des_lat, String des_ltg) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + src_lat + "," + src_ltg + "&daddr=" + des_lat + "," + des_ltg));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callOrderItemStatus(String itemID, String status) {

        ProgressDialogUtil.showProgressDialog(OrderDetailActivity.this);
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", ordersListModel.getUserId());
        param.put("order_id", ordersListModel.getOrderId());
        param.put("item_ids", itemID);
        param.put("item_status", status);

        NetworkAdaper.getInstance().getNetworkServices().setOrderItemStatus(param, new Callback<OrderItemResponseModel>() {
            @Override
            public void success(OrderItemResponseModel orderItemResponseModel, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (orderItemResponseModel.getSuccess() != null ? orderItemResponseModel.getSuccess() : false) {
                    prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "1");
                    OrdersListModel ordersListModelTemp = orderItemResponseModel.getOrdersListModel();
                    if (ordersListModelTemp != null) {
                        ordersListModel = ordersListModelTemp;
                        //  updateView(ordersListModel);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getApplicationContext(),
                        Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_orders_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_call) {
            Toast.makeText(getApplicationContext(), "Call", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /* Listadapter   */
    class OrderDetailAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        OrderDetailAdapter.ViewHolder holder = null;
        List<ItemListModel> listItem;
        int totalItemSelected = 0;
        int totalItemRejected = 0;

        public OrderDetailAdapter(Context context, List<ItemListModel> listItem) {
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
                holder = new OrderDetailAdapter.ViewHolder();
                convertView = inflater
                        .inflate(R.layout.row_list_due_orders_items, null);
                holder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
                holder.itemName = (TextView) convertView.findViewById(R.id.txtItemName);
                holder.itemPrice = (TextView) convertView.findViewById(R.id.txtPrice);
                holder.itemQuantiy = (TextView) convertView.findViewById(R.id.txtLblQuantity);
                holder.txtWeight = (TextView) convertView.findViewById(R.id.txtWeight);
                holder.itemsTotal = (TextView) convertView.findViewById(R.id.txtLblTotal);
                holder.toggle = (ImageButton) convertView.findViewById(R.id.toggle);
                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);
                convertView.setTag(holder);
            } else {
                holder = (OrderDetailAdapter.ViewHolder) convertView.getTag();
            }


            final ItemListModel item = listItem.get(position);
            Log.i("@@OrderDetailFrag", "-----" + item.getName());
            holder.itemName.setText(item.getName());
            holder.itemPrice.setText("Price: " + Util.getCurrency(context) + "" + item.getPrice());
            holder.itemQuantiy.setText("Qty: " + item.getQuantity());
            Log.i("@@OrderDetailag___000", "-----" + item.getImage());

            if (item.getImage() != null && !item.getImage().isEmpty()) {
                Log.i("@@ImageShowing", "-----" + item.getImage());

                Picasso.with(getApplicationContext()).load(item.getImage()).resize(50, 50).error(R.mipmap.ic_launcher).into(holder.itemImage);
                holder.itemImage.setVisibility(View.VISIBLE);

            } else {
                holder.itemImage.setVisibility(View.VISIBLE);

                holder.itemImage.setImageResource(R.mipmap.ic_launcher);
            }
            /*if ((item.getWeight() != null && !(item.getWeight().isEmpty())) *//*&& (item.getUnitType() != null && !(item.getUnitType()
                    .isEmpty()))*//*) {*/
            if (item.getWeight() != null) {
                holder.txtWeight.setText("Weight: " + item.getWeight() /*+ " " + item.getUnitType()*/);
                holder.txtWeight.setVisibility(View.VISIBLE);
            } else {
                holder.txtWeight.setVisibility(View.GONE);
            }
            if (item.getStatus().equalsIgnoreCase("2")) {
                totalItemRejected = totalItemRejected + 1;
                holder.toggle.setSelected(false);
            } else {
                totalItemSelected = totalItemSelected + 1;
                holder.toggle.setSelected(true);
            }

            Double itemsTotal = 0.00;
            itemsTotal = listItem.get(position).getPrice() * Integer.parseInt(listItem.get(position).getQuantity());
            holder.itemsTotal.setText("Total: " + Util.getCurrency(context) + "" + itemsTotal);
            holder.toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (item.getStatus().equalsIgnoreCase("2")) {
                        callOrderItemStatus(item.getItemId(), "1");
                    } else {
                        if (totalItemRejected == getCount() - 1) {
                            String message = "Kindly Accept atleast one item to proceed or else Reject the Complete Order.";

                            final DialogHandler dialogHandler = new DialogHandler(OrderDetailActivity.this);

                            dialogHandler.setDialog(Constant.APP_TITLE, message);
                            dialogHandler.setPostiveButton("OK", true)
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogHandler.dismiss();
                                        }
                                    });

                        } else {
                            callOrderItemStatus(item.getItemId(), "2");
                        }
                    }


                }
            });


            return convertView;
        }

        public void updateListItem(List<ItemListModel> listItem) {
            this.listItem = listItem;
            totalItemSelected = 0;
            totalItemRejected = 0;
            notifyDataSetChanged();
        }

        class ViewHolder {
            ImageView itemImage;
            TextView itemName;
            TextView itemPrice;
            TextView txtWeight;
            TextView itemQuantiy;
            TextView itemsTotal;
            ImageButton toggle;

            RelativeLayout parent;
        }
    }
}
