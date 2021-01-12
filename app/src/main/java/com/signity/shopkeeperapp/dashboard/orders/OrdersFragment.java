package com.signity.shopkeeperapp.dashboard.orders;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.dashboard.StoreDashboardResponse;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.model.runner.CommonResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.notifications.NotificationDialog;
import com.signity.shopkeeperapp.runner.ChooseRunnerDialog;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.ACTION_DIAL;

public class OrdersFragment extends Fragment implements HomeOrdersAdapter.OrdersListener {
    public static final String TAG = "OrdersFragment";
    private boolean needRefesh;
    private PopupWindow rightMenuPopUpWindow;
    private View hiddenView;
    private RecyclerView recyclerViewOrders;
    private HomeOrdersAdapter ordersAdapter;
    private HomeOrdersAdapter.OrderType orderTypeFilter = HomeOrdersAdapter.OrderType.ALL;
    private LinearLayoutManager layoutManager;
    @IdRes
    private int checkedId;
    private int pageSize = 10, currentPageNumber = 1, start, totalOrders;
    private boolean isLoading;
    private Chip chipAll, chipPending, chipAccepted, chipReady, chipDelivered, chipRejected, chipCancelled, chipOnTheWay;
    private int pageNumberToRefresh;
    private ChipGroup chipGroup;
    private HorizontalScrollView horizontal;
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    if (start < totalOrders) {
                        getAllOrdersMethod();
                    }
                }
            }
        }
    };

    public static OrdersFragment getInstance(Bundle bundle) {
        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void storeDashboard() {
        Map<String, Integer> param = new HashMap<>();
        param.put("days_filder", 1);

        NetworkAdaper.getNetworkServices().storeDashboard(param, new Callback<StoreDashboardResponse>() {
            @Override
            public void success(StoreDashboardResponse storeDashboardResponse, Response response) {

                if (!isAdded()) {
                    return;
                }

                if (storeDashboardResponse.isSuccess()) {

                    if (storeDashboardResponse.getData() == null) {
                        return;
                    }

                    if (storeDashboardResponse.getData().getDashboardOrdersData() != null) {
                        chipAll.setText(String.format("%d | All", storeDashboardResponse.getData().getDashboardOrdersData().getTotalOrders()));
                        chipPending.setText(String.format("%d | Pending", storeDashboardResponse.getData().getDashboardOrdersData().getDueOrders()));
                        chipAccepted.setText(String.format("%d | Accepted", storeDashboardResponse.getData().getDashboardOrdersData().getActiveOrders()));
                        chipReady.setText(String.format("%d | Ready to be Picked", storeDashboardResponse.getData().getDashboardOrdersData().getReadyToBePickedOrders()));
                        chipOnTheWay.setText(String.format("%d | On the Way", storeDashboardResponse.getData().getDashboardOrdersData().getOnTheWayOrders()));
                        chipDelivered.setText(String.format("%d | Delivered", storeDashboardResponse.getData().getDashboardOrdersData().getDeliveredOrders()));
                        chipRejected.setText(String.format("%d | Rejected", storeDashboardResponse.getData().getDashboardOrdersData().getRejectedOrders()));
                        chipCancelled.setText(String.format("%d | Cancelled", storeDashboardResponse.getData().getDashboardOrdersData().getCancelOrders()));
                    }
                } else {
                    Toast.makeText(getContext(), storeDashboardResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewOrders.setLayoutManager(layoutManager);
        ordersAdapter = new HomeOrdersAdapter(getContext());
        ordersAdapter.setListener(this);
        recyclerViewOrders.setAdapter(ordersAdapter);
        recyclerViewOrders.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerViewOrders.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(getContext(), 16)));
    }

    private void initView(View rootView) {
        recyclerViewOrders = rootView.findViewById(R.id.rv_orders);
        hiddenView = rootView.findViewById(R.id.view);
        chipGroup = rootView.findViewById(R.id.chip_group);
        horizontal = rootView.findViewById(R.id.horizontal_scroll_chip);

        chipAll = rootView.findViewById(R.id.chip_all);
        chipAll.setChecked(true);

        chipPending = rootView.findViewById(R.id.chip_pending);
        chipAccepted = rootView.findViewById(R.id.chip_accepted);
        chipReady = rootView.findViewById(R.id.chip_ready);
        chipOnTheWay = rootView.findViewById(R.id.chip_on_the_way);
        chipDelivered = rootView.findViewById(R.id.chip_delivered);
        chipRejected = rootView.findViewById(R.id.chip_rejected);
        chipCancelled = rootView.findViewById(R.id.chip_canceled);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.chip_all:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.ALL;
                        break;
                    case R.id.chip_pending:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.PENDING;
                        break;
                    case R.id.chip_accepted:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.ACCEPTED;
                        break;
                    case R.id.chip_ready:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.READY_TO_BE_PICKED;
                        break;
                    case R.id.chip_on_the_way:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.ON_THE_WAY;
                        break;
                    case R.id.chip_delivered:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.DELIVERED;
                        break;
                    case R.id.chip_rejected:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.REJECTED;
                        break;
                    case R.id.chip_canceled:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.CANCELED;
                        break;
                }
                currentPageNumber = 1;
                start = 0;
                ordersAdapter.clearPageMap();
                getAllOrdersMethod();
            }
        });
    }

    public boolean isLoading() {
        return ProgressDialogUtil.isProgressLoading();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setHasOptionsMenu(true);
        setUpAdapter();
        getAllOrdersMethod();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_orders, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showPopUpMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAllOrdersMethod() {

        if (!Util.checkIntenetConnection(getContext())) {
            ordersAdapter.setShowLoading(false);
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            return;
        }

        storeDashboard();
        getALLOrders();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefesh) {
            getALLOrdersWithPage(pageNumberToRefresh);
            needRefesh = false;
        }
    }

    public void getALLOrders() {
        Map<String, Object> param = new HashMap<>();
        param.put("order_type", orderTypeFilter.getSlug());
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);

        isLoading = true;
        NetworkAdaper.getNetworkServices().getStoreOrdersNew(param, new Callback<StoreOrdersReponse>() {
            @Override
            public void success(StoreOrdersReponse getValues, Response response) {
                if (!isAdded()) {
                    return;
                }
                isLoading = false;
                if (getValues.isSuccess()) {
                    horizontal.setVisibility(View.VISIBLE);
                    List<OrdersListModel> orderListModel = getValues.getData().getOrders();
                    totalOrders = getValues.getData().getOrdersTotal();

//                    ordersAdapter.addOrdersListModels(orderListModel, totalOrders);
                    for (OrdersListModel model : orderListModel) {
                        model.setPageNumber(currentPageNumber);
                    }

                    ordersAdapter.addUpdatePageWithOrders(currentPageNumber, orderListModel, totalOrders);
                    currentPageNumber++;
                    start += pageSize;
                    ordersAdapter.setShowLoading(!orderListModel.isEmpty());
                } else {
                    ordersAdapter.setShowLoading(false);
                    Toast.makeText(getContext(), getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                isLoading = false;
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
                ordersAdapter.setShowLoading(false);
            }
        });
    }

    public void getALLOrdersWithPage(final int pageNumber) {

        storeDashboard();

        Map<String, Object> param = new HashMap<>();
        param.put("order_type", orderTypeFilter.getSlug());
        param.put("page", pageNumber);
        param.put("pagelength", pageSize);

        isLoading = true;
        NetworkAdaper.getNetworkServices().getStoreOrdersNew(param, new Callback<StoreOrdersReponse>() {
            @Override
            public void success(StoreOrdersReponse getValues, Response response) {
                if (!isAdded()) {
                    return;
                }
                isLoading = false;
                if (getValues.isSuccess()) {
                    List<OrdersListModel> orderListModel = getValues.getData().getOrders();
                    totalOrders = getValues.getData().getOrdersTotal();

//                    ordersAdapter.addOrdersListModels(orderListModel, totalOrders);
                    for (OrdersListModel model : orderListModel) {
                        model.setPageNumber(pageNumber);
                    }

                    ordersAdapter.addUpdatePageWithOrders(pageNumber, orderListModel, totalOrders);
                } else {
                    Toast.makeText(getContext(), getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                isLoading = false;
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
                ordersAdapter.setShowLoading(false);
            }
        });
    }

    private void showPopUpMenu() {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popwindow_orders_filter, null, false);

        rightMenuPopUpWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        rightMenuPopUpWindow.setOutsideTouchable(true);
        rightMenuPopUpWindow.setBackgroundDrawable(new ColorDrawable());
        rightMenuPopUpWindow.setTouchInterceptor(new View.OnTouchListener() { // or whatever you want
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) // here I want to close the pw when clicking outside it but at all this is just an example of how it works and you can implement the onTouch() or the onKey() you want
                {
                    rightMenuPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }

        });

        float den = getActivity().getResources().getDisplayMetrics().density;
        int offsetY = (int) (den * 2);
        rightMenuPopUpWindow.showAsDropDown(hiddenView, 0, 0);

        final RadioGroup rdGroup = (RadioGroup) layout.findViewById(R.id.rdGroup);
        RadioButton radioAllOrders = (RadioButton) layout.findViewById(R.id.radioAllOrders);

        RadioButton radioPending = (RadioButton) layout.findViewById(R.id.radioPending);
        RadioButton radioAccepted = (RadioButton) layout.findViewById(R.id.radioAccepted);

        RadioButton radioShipped = (RadioButton) layout.findViewById(R.id.radioShipped);

        RadioButton radioDelivered = (RadioButton) layout.findViewById(R.id.radioDelivered);

        RadioButton radioRejected = (RadioButton) layout.findViewById(R.id.radioRejected);
        RadioButton radioCanceled = (RadioButton) layout.findViewById(R.id.radioCanceled);

        switch (checkedId) {
            case R.id.radioPending:
                radioPending.setChecked(true);
                break;
            case R.id.radioAccepted:
                radioAccepted.setChecked(true);
                break;
            case R.id.radioShipped:
                radioShipped.setChecked(true);
                break;
            case R.id.radioDelivered:
                radioDelivered.setChecked(true);
                break;
            case R.id.radioRejected:
                radioRejected.setChecked(true);
                break;
            case R.id.radioCanceled:
                radioCanceled.setChecked(true);
                break;
            case R.id.radioAllOrders:
            default:
                radioAllOrders.setChecked(true);
        }

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = rdGroup.findViewById(checkedId);
                int index = rdGroup.indexOfChild(radioButton);

                OrdersFragment.this.checkedId = checkedId;
                currentPageNumber = 1;
                start = 0;
                // Add logic here

                switch (index) {
                    case 0: // first button
                        orderTypeFilter = HomeOrdersAdapter.OrderType.ALL;
                        break;
                    case 1:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.PENDING;
                        break;
                    case 2:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.ACCEPTED;
                        break;
                    case 3:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.SHIPPED;
                        break;
                    case 4:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.DELIVERED;
                        break;
                    case 5:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.REJECTED;
                        break;
                    case 6:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.CANCELED;
                        break;
                }
                ordersAdapter.clearPageMap();
                getAllOrdersMethod();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rightMenuPopUpWindow.dismiss();
                    }
                }, 500);
            }
        });
    }

    private void updateOrderStatus(HomeOrdersAdapter.OrderType orderStatus, String orderId, final int pageNumber, String message, String customerId) {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", customerId);
        param.put("order_status", String.valueOf(orderStatus.getStatusId()));
        param.put("order_ids", orderId);
        if (!TextUtils.isEmpty(message)) {
            param.put("order_rejection_note", message);
        }

        NetworkAdaper.getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (getValues.getSuccess()) {
                    getALLOrdersWithPage(pageNumber);
                } else {
                    Toast.makeText(getContext(), getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClickOrder(int position, int pageNumber) {
        needRefesh = true;
        pageNumberToRefresh = pageNumber;
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(OrderDetailActivity.ORDER_OBJECT, order);
        startActivity(OrderDetailActivity.getStartIntent(getContext(), bundle));
        AnimUtil.slideFromRightAnim(getActivity());
    }

    @Override
    public void onRejectOrder(final int position, final int pageNumber) {
        if (ordersAdapter.getOrdersListModels().isEmpty()) {
            return;
        }
        RejectOrderDialog rejectOrderDialog = RejectOrderDialog.getInstance(null);
        rejectOrderDialog.setListener(new RejectOrderDialog.DialogListener() {
            @Override
            public void onSubmit(String message) {
                OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
                updateOrderStatus(HomeOrdersAdapter.OrderType.REJECTED, order.getOrderId(), pageNumber, message,order.getUserId());
            }
        });
        rejectOrderDialog.show(getChildFragmentManager(), NotificationDialog.TAG);
    }

    @Override
    public void onAcceptOrder(int position, int pageNumber) {
        if (ordersAdapter.getOrdersListModels().isEmpty()) {
            return;
        }
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ACCEPTED, order.getOrderId(), pageNumber, "", order.getUserId());
    }

    @Override
    public void onShipOrder(int position, int pageNumber) {
        if (ordersAdapter.getOrdersListModels().isEmpty()) {
            return;
        }
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.SHIPPED, order.getOrderId(), pageNumber, "", order.getUserId());
    }

    @Override
    public void onReadyToBePicked(int position, int pageNumber) {
        if (ordersAdapter.getOrdersListModels().isEmpty()) {
            return;
        }
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.READY_TO_BE_PICKED, order.getOrderId(), pageNumber, "",order.getUserId());
    }

    @Override
    public void onDeliverOrder(int position, int pageNumber) {
        if (ordersAdapter.getOrdersListModels().isEmpty()) {
            return;
        }
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.DELIVERED, order.getOrderId(), pageNumber, "",order.getUserId());
    }

    @Override
    public void onTheWayOrder(int position, int pageNumber) {
        if (ordersAdapter.getOrdersListModels().isEmpty()) {
            return;
        }
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ON_THE_WAY, order.getOrderId(), pageNumber, "",order.getUserId());
    }

    @Override
    public void onWhatsappMessage(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        String phone = order.getPhone();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        openWhatsapp(phone);
    }

    private void openWhatsapp(String phone) {
        try {
            boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
            if (isWhatsappInstalled) {

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                String data = String.format("%s%s@s.whatsapp.net", AppPreference.getInstance().getPhoneCode(), phone);
                sendIntent.putExtra("jid", data);//phone number without "+" prefix
                startActivity(sendIntent);
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getContext().getPackageManager();
        boolean appInstalled = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    @Override
    public void onCallCustomer(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        String phone = order.getPhone();

        if (TextUtils.isEmpty(phone)) {
            DialogUtils.showAlertDialog(getContext(), Constant.APP_TITLE, "Sorry! phone number is not available.");
        } else {
            callAlert(phone);
        }
    }

    @Override
    public void onAssignRunner(String runnerId, final int pageNumber, final String orderId, String areaId) {
        Bundle bundle = new Bundle();
        bundle.putString(ChooseRunnerDialog.RUNNER_ID, runnerId);
        bundle.putString(ChooseRunnerDialog.AREA_ID, areaId);
        ChooseRunnerDialog dialog = ChooseRunnerDialog.getInstance(bundle);
        dialog.setListener(new ChooseRunnerDialog.ChooseRunnerDialogListener() {
            @Override
            public void onSelectRunner(String id) {
                addRunner(id, orderId, pageNumber);
            }
        });
        dialog.show(getChildFragmentManager(), ChooseRunnerDialog.TAG);
    }

    private void addRunner(String id, String orderId, final int pageNumber) {

        Map<String, String> param = new HashMap<>();
        param.put("order_id", orderId);
        param.put("runner_id", id);

        ProgressDialogUtil.showProgressDialog(getContext());
        NetworkAdaper.getNetworkServices().assignRunner(param, new Callback<CommonResponse>() {
            @Override
            public void success(CommonResponse response, Response response2) {

                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (response.isSuccess()) {
                    getALLOrdersWithPage(pageNumber);
                }
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                if (isAdded()) {
                    ProgressDialogUtil.hideProgressDialog();
                }
            }
        });
    }

    private void callAlert(final String phone) {
        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        adb.setTitle("Call " + phone + " ?");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                actionCall(phone);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void actionCall(String phone) {
        try {
            PackageManager pm = getContext().getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
                AnimUtil.slideFromRightAnim(getActivity());
            } else {
                Toast.makeText(getContext(), "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
