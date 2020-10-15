package com.signity.shopkeeperapp.dashboard.home;

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.customer.CustomerFragment;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.dashboard.orders.HomeOrdersAdapter;
import com.signity.shopkeeperapp.dashboard.orders.OrderDetailActivity;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.dashboard.StoreDashboardResponse;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.notifications.NotificationActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.ACTION_DIAL;

public class HomeFragment extends Fragment implements HomeContentAdapter.HomeContentAdapterListener, HomeOrdersAdapter.OrdersListener {

    public static final String TAG = "HomeFragment";
    private TextView textViewNotificationCount;
    private TextView textViewOverView;
    private TextView textViewStoreUrl;
    private TextView textViewStoreName;
    private RecyclerView recyclerViewContent;
    private RecyclerView recyclerViewOrders;
    private HomeContentAdapter homeContentAdapter;
    private HomeOrdersAdapter homeOrdersAdapter;
    private LinearLayout linearLayoutViewAllOrders;
    private ChipGroup chipGroup;
    private LinearLayout linearLayoutOverview, linearLayoutShare;

    private HomeFragmentListener listener;
    private List<OrdersListModel> ordersListModels = new ArrayList<>();
    private int notificationCount;
    @IdRes
    private int checkedId;
    private HomeOrdersAdapter.OrderType orderType = HomeOrdersAdapter.OrderType.ALL;
    private Constant.StoreDashboard typeofDay = Constant.StoreDashboard.TODAY;

    public static HomeFragment getInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        init(view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DashboardActivity) {
            listener = (HomeFragmentListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
    }

    private void setUpStoreData() {
        String storeUrl = AppPreference.getInstance().getStoreUrl();

        if (!TextUtils.isEmpty(storeUrl)) {
            String[] arr = storeUrl.split("//");
            textViewStoreUrl.setText(arr[1]);
        }

        String storeName = AppPreference.getInstance().getStoreName();
        textViewStoreName.setText(storeName);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpStoreData();
        storeDashboard();
        getOrders();
    }

    public void getOrders() {

        if (!Util.checkIntenetConnection(getContext())) {
            homeOrdersAdapter.setShowLoading(false);
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("order_type", orderType.getSlug());
        param.put("page", 1);
        param.put("pagelength", 10);

        NetworkAdaper.getNetworkServices().getDashbaordStoreOrders(param, new Callback<StoreOrdersReponse>() {
            @Override
            public void success(StoreOrdersReponse ordersReponse, Response response) {

                if (!isAdded()) {
                    return;
                }

                if (ordersReponse.isSuccess()) {
                    ordersListModels = ordersReponse.getData().getOrders();

                    for (OrdersListModel model : ordersListModels) {
                        model.setPageNumber(1);
                    }

                    Map<Integer, List<OrdersListModel>> newMapData = new HashMap<>();
                    newMapData.put(1, ordersListModels);

                    homeOrdersAdapter.setPageOrdersMap(newMapData);

                } else {
                    Toast.makeText(getContext(), ordersReponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
                homeOrdersAdapter.setShowLoading(false);
            }
        });
    }

    public void storeDashboard() {
        Map<String, Integer> param = new HashMap<>();
        param.put("days_filder", typeofDay.getDays());

        NetworkAdaper.getNetworkServices().storeDashboard(param, new Callback<StoreDashboardResponse>() {
            @Override
            public void success(StoreDashboardResponse storeDashboardResponse, Response response) {

                if (!isAdded()) {
                    return;
                }

                if (storeDashboardResponse.isSuccess()) {

                    if (textViewOverView != null) {
                        textViewOverView.setText(typeofDay.getTitle());
                    }

                    if (storeDashboardResponse.getData() == null) {
                        return;
                    }

                    homeContentAdapter.setUpData(storeDashboardResponse.getData());
                    if (listener != null) {
                        listener.onUpdateOrdersCount(storeDashboardResponse.getData().getTotalOrders());
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

    private void setUpAdapter() {
        homeContentAdapter = new HomeContentAdapter(getContext());
        homeContentAdapter.setListener(this);
        recyclerViewContent.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewContent.setAdapter(homeContentAdapter);

        homeOrdersAdapter = new HomeOrdersAdapter(getContext());
        homeOrdersAdapter.setListener(this);
        recyclerViewOrders.setAdapter(homeOrdersAdapter);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewOrders.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(getContext(), 16)));
    }

    private void init(View view) {
        linearLayoutOverview = view.findViewById(R.id.ll_overview);
        linearLayoutShare = view.findViewById(R.id.ll_share);
        recyclerViewContent = view.findViewById(R.id.rv_content);
        recyclerViewOrders = view.findViewById(R.id.rv_orders);
        textViewStoreUrl = view.findViewById(R.id.tv_store_url);
        textViewStoreName = view.findViewById(R.id.tv_store_name);
        linearLayoutViewAllOrders = view.findViewById(R.id.ll_view_all_orders);
        chipGroup = view.findViewById(R.id.chip_group);
        textViewOverView = view.findViewById(R.id.tv_overview);
        Chip allChip = view.findViewById(R.id.chip_all);
        allChip.setChecked(true);

        linearLayoutOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverViewPopMenu();
            }
        });

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.chip_all:
                        orderType = HomeOrdersAdapter.OrderType.ALL;
                        break;
                    case R.id.chip_pending:
                        orderType = HomeOrdersAdapter.OrderType.PENDING;
                        break;
                    case R.id.chip_accepted:
                        orderType = HomeOrdersAdapter.OrderType.ACCEPTED;
                        break;
                    case R.id.chip_shipped:
                        orderType = HomeOrdersAdapter.OrderType.SHIPPED;
                        break;
                    case R.id.chip_delivered:
                        orderType = HomeOrdersAdapter.OrderType.DELIVERED;
                        break;
                    case R.id.chip_rejected:
                        orderType = HomeOrdersAdapter.OrderType.REJECTED;
                        break;
                }
                homeOrdersAdapter.clearPageMap();
                getOrders();
            }
        });

        linearLayoutViewAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickViewAllOrders();
                }
            }
        });

        textViewStoreUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite();
            }
        });

        linearLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsiteShare();
            }
        });
    }

    private void openWebsite() {
        String website = AppPreference.getInstance().getStoreUrl();
        if (TextUtils.isEmpty(website)) {
            Toast.makeText(getContext(), "Url not available", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        startActivity(i);
    }

    private void openWebsiteShare() {
        String website = AppPreference.getInstance().getStoreUrl();
        if (TextUtils.isEmpty(website)) {
            Toast.makeText(getContext(), "Url not available", Toast.LENGTH_SHORT).show();
            return;
        }
        String text = String.format("Enjoy your favorite food in the comfort of your home by ordering from %s, online. Click %s. Order now!",
                AppPreference.getInstance().getStoreName(),
                website);
        shareIntent(text, "Share website");
    }

    private void showOverViewPopMenu() {

        View layout = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_overview, null, false);
        final PopupWindow popupWindowOverView = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindowOverView.setOutsideTouchable(true);
        popupWindowOverView.setBackgroundDrawable(new ColorDrawable());
        popupWindowOverView.setTouchInterceptor(new View.OnTouchListener() { // or whatever you want
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) // here I want to close the pw when clicking outside it but at all this is just an example of how it works and you can implement the onTouch() or the onKey() you want
                {
                    popupWindowOverView.dismiss();
                    return true;
                }
                return false;
            }

        });

        float den = getActivity().getResources().getDisplayMetrics().density;
        int offsetY = (int) (den * 2);
        popupWindowOverView.showAsDropDown(linearLayoutOverview, 0, 0);

        final RadioGroup rdGroup = (RadioGroup) layout.findViewById(R.id.rdGroup);

        RadioButton radioToday = (RadioButton) layout.findViewById(R.id.radioToday);

        RadioButton radioYesterday = (RadioButton) layout.findViewById(R.id.radioYesterday);

        RadioButton radioDays = (RadioButton) layout.findViewById(R.id.radioDays);

        switch (checkedId) {
            case R.id.radioYesterday:
                radioYesterday.setChecked(true);
                break;
            case R.id.radioDays:
                radioDays.setChecked(true);
                break;
            case R.id.radioToday:
            default:
                radioToday.setChecked(true);
        }

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = rdGroup.findViewById(checkedId);
                int index = rdGroup.indexOfChild(radioButton);
                HomeFragment.this.checkedId = checkedId;
                switch (index) {
                    case 0:
                        typeofDay = Constant.StoreDashboard.TODAY;
                        break;
                    case 1:
                        typeofDay = Constant.StoreDashboard.YESTERDAY;
                        break;
                    case 2:
                        typeofDay = Constant.StoreDashboard.LAST_WEEK;
                        break;
                }
                storeDashboard();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupWindowOverView.dismiss();
                    }
                }, 500);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_notification);

        View actionView = menuItem.getActionView();

        if (actionView == null) {
            return;
        }

        textViewNotificationCount = (TextView) actionView.findViewById(R.id.notification_count);

        setUpBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
    }

    private void setUpBadge() {
        if (textViewNotificationCount != null) {
            if (notificationCount == 0) {
                textViewNotificationCount.setVisibility(View.GONE);
            } else {
                textViewNotificationCount.setText(String.valueOf(Math.min(notificationCount, 99)));
                textViewNotificationCount.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_notification) {
            startActivity(NotificationActivity.getStartIntent(getContext()));
            AnimUtil.slideFromRightAnim(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClickItem(HomeContentAdapter.HomeItems homeItems) {
        switch (homeItems) {
            case ORDERS:
            case TOTAL_SALES:
                if (listener != null) {
                    listener.onClickViewAllOrders();
                }
                break;
            case ALL_CUSTOMERS:
                showFragment(CustomerFragment.getInstance(null), CustomerFragment.TAG);
                break;
            case TOTAL_PRODUCT:
                if (listener != null) {
                    listener.onClickViewProducts();
                }
                break;
        }
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            fragmentTransaction.replace(R.id.fl_dashboard, fragment, tag);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }
    }

    private void updateOrderStatus(HomeOrdersAdapter.OrderType orderStatus, String orderId, final int position) {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", AppPreference.getInstance().getUserId());
        param.put("order_status", String.valueOf(orderStatus.getStatusId()));
        param.put("order_ids", orderId);

        NetworkAdaper.getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {

                if (!isAdded()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (getValues.getSuccess()) {
                    getOrders();
                } else {
                    Toast.makeText(getContext(), getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClickOrder(int position, int pageNumber) {
        OrdersListModel ordersListModel = ordersListModels.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(OrderDetailActivity.ORDER_OBJECT, ordersListModel);
        startActivity(OrderDetailActivity.getStartIntent(getContext(), bundle));
        AnimUtil.slideFromRightAnim(getActivity());
    }

    @Override
    public void onRejectOrder(int position, int pageNumber) {
        OrdersListModel order = ordersListModels.get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.REJECTED, order.getOrderId(), position);
    }

    @Override
    public void onAcceptOrder(int position, int pageNumber) {
        OrdersListModel order = ordersListModels.get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ACCEPTED, order.getOrderId(), position);
    }

    @Override
    public void onShipOrder(int position, int pageNumber) {
        OrdersListModel order = ordersListModels.get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.SHIPPED, order.getOrderId(), position);
    }

    @Override
    public void onDeliverOrder(int position, int pageNumber) {
        OrdersListModel order = ordersListModels.get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.DELIVERED, order.getOrderId(), position);
    }

    @Override
    public void onWhatsappMessage(int position) {
        OrdersListModel order = ordersListModels.get(position);
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
                String data = String.format("%s%s@s.whatsapp.net", "91", phone);
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
        OrdersListModel order = ordersListModels.get(position);
        String phone = order.getPhone();

        if (TextUtils.isEmpty(phone)) {
            DialogUtils.showAlertDialog(getContext(), Constant.APP_TITLE, "Sorry! phone number is not available.");
        } else {
            callAlert(phone);
        }
    }

    private void callAlert(final String phone) {
        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        adb.setTitle("Call " + phone + " ?");
        adb.setIcon(R.drawable.ic_launcher);
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

    private void shareIntent(String extra, String shareTitle) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, extra);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, shareTitle);
        startActivity(shareIntent);
    }

    public interface HomeFragmentListener {
        void onClickViewAllOrders();

        void onClickViewProducts();

        void onClickViewCustomers();

        void onUpdateOrdersCount(int count);
    }
}
