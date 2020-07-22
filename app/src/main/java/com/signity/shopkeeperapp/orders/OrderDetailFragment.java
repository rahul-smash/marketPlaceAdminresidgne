package com.signity.shopkeeperapp.orders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.DashBoardModelStoreDetail;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.OrderItemResponseModel;
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
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.ACTION_DIAL;

/**
 * Created by Rajinder on 28/9/15.
 */
public class OrderDetailFragment extends Fragment implements View.OnClickListener {


    private Context context;
    ListView listDueOrderItems;
    ImageButton mOrderDetailBtn;
    Button mApproveOrder, mDeclineOrder;
    DueOrderItemsAdapter adapter;
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

    RelativeLayout mOrderDetailLayout, mDetailBtnBlock;

    Animation slideUpAnim;
    Animation slideDownAnim;

    TextView mTotalAmount;
    Button btnGuidMe;
    TextView mDeliveryAddress, mNote, mItemsPrice, mShippingCharges, mDiscountVal, taxVal, shipping_charges_text, discountLblText;
    RelativeLayout mNoteLayout, mAddressLayout;
    LinearLayout linearDynamicTaxBlock;
    ImageButton btnOrderProceed, btnMoveToShipping, btnMoveToDeliver;
    Button buttonRejectOrder;
    private OrdersListModel ordersListModel;
    private PrefManager prefManager;
    RelativeLayout shipping_layout, discount_layout;
    private LinearLayout footer1;
    String status;
    String getLat = "";
    String getLong = "";
    String destinationLat, destinationLang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        prefManager = new PrefManager(getActivity());
        ordersListModel = (OrdersListModel) getArguments().getSerializable("object");
        setUiData();
        setShareContent();
    }

    public DashBoardModelStoreDetail getStoreDataAsObject(String store) {
        DashBoardModelStoreDetail object;
        Gson gson = new Gson();
        Type type = new TypeToken<DashBoardModelStoreDetail>() {
        }.getType();
        object = gson.fromJson(store, type);
        return object;
    }

    private void setShareContent() {

        DashBoardModelStoreDetail jobj = getStoreDataAsObject(Util.loadPreferenceValue(getActivity(), Constant.STORE_DETAILS));

        try {
            getLat = jobj.getLat();
            getLong = jobj.getLng();
            Log.i("@@Latitude", "" + getLat + getLong);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUiData() {
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
        Log.i("@@OrderListFragment---", "" + address + status + "DestingLAtLng" + destinationLat + destinationLang);
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                OrderDetailFragment.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_due_order_detail, container, false);

        initView(rootView);
        setUpFooter();
        setUpClickListener();
        initListAdapter();
        setOrderDetails();
        return rootView;
    }

    private void setUpClickListener() {
        btnGuidMe.setOnClickListener(this);
        buttonRejectOrder.setOnClickListener(this);
        btnOrderProceed.setOnClickListener(this);
        btnMoveToShipping.setOnClickListener(this);
        btnMoveToDeliver.setOnClickListener(this);
    }

    private void setUpFooter() {

        if (status.equalsIgnoreCase("0")) {
            setFooterForDueOrder();
        } else if (status.equalsIgnoreCase("1")) {
            setFooterForProcessingOrder();
        } else if (status.equalsIgnoreCase("2")) {
            setFooterForRejectedOrder();
        } else if (status.equalsIgnoreCase("4")) {
            setFooterForShippedOrder();
        } else if (status.equalsIgnoreCase("5")) {
            setFooterForDeliveredOrder();
            btnGuidMe.setVisibility(View.GONE);
        } else if (status.equalsIgnoreCase("6")) {
            setFooterForCancelOrder();
        }
    }

    private void setFooterForDueOrder() {
        buttonRejectOrder.setEnabled(true);
        btnOrderProceed.setEnabled(true);
        btnMoveToShipping.setEnabled(false);
        btnMoveToDeliver.setEnabled(false);
    }

    private void setFooterForProcessingOrder() {
        buttonRejectOrder.setEnabled(true);
        btnOrderProceed.setEnabled(true);
        btnOrderProceed.setSelected(true);
        btnMoveToShipping.setEnabled(true);
        btnMoveToDeliver.setEnabled(false);
    }

    private void setFooterForShippedOrder() {
        buttonRejectOrder.setVisibility(View.GONE);
        btnOrderProceed.setEnabled(true);
        btnOrderProceed.setSelected(true);
        btnMoveToShipping.setEnabled(true);
        btnMoveToShipping.setSelected(true);
        btnMoveToDeliver.setEnabled(true);
    }

    private void setFooterForDeliveredOrder() {
        buttonRejectOrder.setVisibility(View.GONE);
        btnOrderProceed.setEnabled(true);
        btnOrderProceed.setSelected(true);
        btnMoveToShipping.setEnabled(true);
        btnMoveToShipping.setSelected(true);
        btnMoveToDeliver.setEnabled(true);
        btnMoveToDeliver.setSelected(true);
    }

    private void setFooterForCancelOrder() {
        footer1.setVisibility(View.GONE);
    }

    private void setFooterForRejectedOrder() {
        footer1.setVisibility(View.GONE);
    }

    private void initListAdapter() {
        adapter = new DueOrderItemsAdapter(getActivity(), listItem);
        listDueOrderItems.setAdapter(adapter);
    }

    private void initView(View rootView) {
        listDueOrderItems = (ListView) rootView.findViewById(R.id.listDueOrderItems);
        mTotalAmount = (TextView) rootView.findViewById(R.id.txtTotalAmount);
        footer1 = (LinearLayout) rootView.findViewById(R.id.footer1);

        /*footer view*/
        btnOrderProceed = (ImageButton) rootView.findViewById(R.id.btnOrderProceed);
        btnMoveToShipping = (ImageButton) rootView.findViewById(R.id.btnMoveToShipping);
        btnMoveToDeliver = (ImageButton) rootView.findViewById(R.id.btnMoveToDeliver);
        buttonRejectOrder = (Button) rootView.findViewById(R.id.relDeclineOrder);

        /*Add headear to listview*/
        addHeaderToList();
        /*Header parent*/
        handleBackButton(rootView);
        handleCallButton(rootView);
        setHeader(rootView);
    }

    private void addHeaderToList() {
        View headerView = (View) getActivity().getLayoutInflater().inflate(R.layout.layout_header_order_detail_address, null);
        Log.i("@@OrderDetailFragment", "OrderDetailFragment");
        btnGuidMe = (Button) headerView.findViewById(R.id.btnGuidMe);

        mDeliveryAddress = (TextView) headerView.findViewById(R.id.txtDeliveryAddress);
        mNote = (TextView) headerView.findViewById(R.id.txtNote);
        mItemsPrice = (TextView) headerView.findViewById(R.id.items_price);
        mShippingCharges = (TextView) headerView.findViewById(R.id.shipping_charges);
        mDiscountVal = (TextView) headerView.findViewById(R.id.discountVal);
        linearDynamicTaxBlock = (LinearLayout) headerView.findViewById(R.id.dynamicTaxBlock);
        mNoteLayout = (RelativeLayout) headerView.findViewById(R.id.noteLayout);
        mAddressLayout = (RelativeLayout) headerView.findViewById(R.id.addressLayout);
        shipping_charges_text = (TextView) headerView.findViewById(R.id.shipping_charges_text);
        discountLblText = (TextView) headerView.findViewById(R.id.discountLblText);
        shipping_layout = (RelativeLayout) headerView.findViewById(R.id.shipping_layout);
        discount_layout = (RelativeLayout) headerView.findViewById(R.id.discount_layout);
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
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void actionCall() {

        try {
            PackageManager pm = getActivity().getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
                AnimUtil.slideFromRightAnim(getActivity());
            } else {
                Toast.makeText(getActivity(), "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
        AnimUtil.slideFromLeftAnim(getActivity());*/
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

    public void openMap(String src_lat, String src_ltg, String des_lat, String des_ltg) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + src_lat + "," + src_ltg + "&daddr=" + des_lat + "," + des_ltg));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void openMap(Context context,String lat, String lng) {
        //String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + "Label which you want" + ")";
        String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + ")";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        context.startActivity(intent);
    }*/
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnGuidMe:
                //    Toast.makeText(getActivity(),address,Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.btnOrderProceed:
                if (!Util.checkIntenetConnection(getActivity())) {
                    showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                    return;
                }

                if (!btnOrderProceed.isEnabled()) {
                    Toast.makeText(getActivity(), "Unable to Proceed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (btnOrderProceed.isSelected()) {
                    Toast.makeText(getActivity(), "Order Already Processed", Toast.LENGTH_SHORT).show();
                    return;
                }
                orderStatus = "1";
                setOrderForProcessing();
                break;
            case R.id.relDeclineOrder:
                if (!Util.checkIntenetConnection(getActivity())) {
                    showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                    return;
                }

                if (!buttonRejectOrder.isEnabled()) {
                    return;
                }

                alertBoxNew("Are you sure to Decline this order?");
                break;
            case R.id.btnMoveToShipping:
                if (!Util.checkIntenetConnection(getActivity())) {
                    showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                    return;
                }

                if (!btnMoveToShipping.isEnabled()) {
                    return;
                }
                if (btnMoveToShipping.isSelected()) {
                    Toast.makeText(getActivity(), "Order already Shipped", Toast.LENGTH_SHORT).show();
                    return;
                }
                setOrderForShipping();
                break;
            case R.id.btnMoveToDeliver:
                if (!Util.checkIntenetConnection(getActivity())) {
                    showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                    return;
                }

                if (!btnMoveToDeliver.isEnabled()) {
                    return;
                }
                if (btnMoveToDeliver.isSelected()) {
                    Toast.makeText(getActivity(), "Order already Delivered", Toast.LENGTH_SHORT).show();
                    return;
                }
                setOrderForDelivery();
                break;

        }

    }


    void alertBoxNew(String message) {
        final DialogHandler dialogHandler = new DialogHandler(getActivity());

        dialogHandler.setDialog(Constant.APP_TITLE, message);
        dialogHandler.setPostiveButton("Yes", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogHandler.dismiss();
                        orderStatus = "2";
                        setOrderForReject();
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


    /*Api Calling for all order processing module working here below */

    private void setOrderForProcessing() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", userId);
        param.put("order_status", orderStatus);
        param.put("order_ids", orderId);

        NetworkAdaper.getInstance().getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "1");
                    setFooterForProcessingOrder();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                showAlertDialog(getActivity(), Constant.APP_TITLE, "Server Not Responding");
            }
        });
    }

    private void setOrderForShipping() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", userId);
        param.put("order_status", "4");
        param.put("order_ids", orderId);


        NetworkAdaper.getInstance().getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "1");
                    setFooterForShippedOrder();
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
        param.put("order_status", "5");
        param.put("order_ids", orderId);


        NetworkAdaper.getInstance().getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "1");
                    setFooterForDeliveredOrder();
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

        NetworkAdaper.getInstance().getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
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

    private void callOrderItemStatus(String itemID, String status) {

        ProgressDialogUtil.showProgressDialog(getActivity());
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
                        updateView(ordersListModel);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(),
                        Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });

    }


    /*Api works ends here*/

    public void showAlertDialog(Context context, String title,
                                String message) {

        final DialogHandler dialogHandler = new DialogHandler(getActivity());

        dialogHandler.setDialog(title, message);
        dialogHandler.setPostiveButton("Ok", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogHandler.dismiss();
                    }
                });
    }


    /* Listadapter   */
    class DueOrderItemsAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ViewHolder holder = null;
        List<ItemListModel> listItem;
        int totalItemSelected = 0;
        int totalItemRejected = 0;

        public DueOrderItemsAdapter(Context context, List<ItemListModel> listItem) {
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
                holder = (ViewHolder) convertView.getTag();
            }


            final ItemListModel item = listItem.get(position);
            Log.i("@@OrderDetailFrag", "-----" + item.getName());
            holder.itemName.setText(item.getName());
            holder.itemPrice.setText("Price: " + Util.getCurrency(context) + "" + item.getPrice());
            holder.itemQuantiy.setText("Qty: " + item.getQuantity());
            Log.i("@@OrderDetailag___000", "-----" + item.getImage());

            if (item.getImage() != null && !item.getImage().isEmpty()) {
                Log.i("@@ImageShowing", "-----" + item.getImage());

                Picasso.with(getActivity()).load(item.getImage()).resize(50, 50).error(R.mipmap.ic_launcher).into(holder.itemImage);
                holder.itemImage.setVisibility(View.GONE);

            } else {
                holder.itemImage.setVisibility(View.GONE);

                holder.itemImage.setImageResource(R.mipmap.ic_launcher);
            }
            /*if ((item.getWeight() != null && !(item.getWeight().isEmpty())) *//*&& (item.getUnitType() != null && !(item.getUnitType()
                    .isEmpty()))*//*) {*/
                if(item.getWeight()!=null){
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

                    if (btnOrderProceed.isSelected() || btnMoveToShipping.isSelected() || btnMoveToDeliver.isSelected()) {

                    } else {
                        if (item.getStatus().equalsIgnoreCase("2")) {
                            callOrderItemStatus(item.getItemId(), "1");
                        } else {
                            if (totalItemRejected == getCount() - 1) {
                                String message = "Kindly Accept atleast one item to proceed or else Reject the Complete Order.";

                                final DialogHandler dialogHandler = new DialogHandler(getActivity());

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


    private void updateView(OrdersListModel ordersListModel) {
        setUiData();
        adapter.updateListItem(listItem);
        linearDynamicTaxBlock.removeAllViews();
        setOrderDetails();
    }

    public void setOrderDetails() {
        mDeliveryAddress.setText(address);
        if (note.equalsIgnoreCase("") || note.equalsIgnoreCase(null)) {
            mNoteLayout.setVisibility(View.GONE);
        } else {
            mNote.setText(note);
        }
        mTotalAmount.setText(Util.getCurrency(getActivity()) + "" + Util.getDoubleValue(ordersListModel.getTotal()));
        mItemsPrice.setText(Util.getCurrency(getActivity()) + "" + Util.getDoubleValue(ordersListModel.getCheckout()));
        mShippingCharges.setText(Util.getCurrency(getActivity()) + "" + Util.getDoubleValue(shipping_charges));
        mDiscountVal.setText("-" + Util.getCurrency(getActivity()) + "" + Util.getDoubleValue(discount));

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

                Double tax = null;
                try {
                    tax = Double.parseDouble(storeTaxModel.getFixedTaxAmount());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (tax != null && tax != 0.0) {
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

                Double tax = null;
                try {
                    tax = Double.parseDouble(storeTaxModel.getFixedTaxAmount());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (tax != null && tax != 0.0) {
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
