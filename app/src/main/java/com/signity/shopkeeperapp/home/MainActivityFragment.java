package com.signity.shopkeeperapp.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.BuildConfig;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.customer.CustomerFragment;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.model.DashBoardModel;
import com.signity.shopkeeperapp.model.DashBoardModelDetail;
import com.signity.shopkeeperapp.model.DashBoardModelStoreDetail;
import com.signity.shopkeeperapp.model.ModelForceUpdate;
import com.signity.shopkeeperapp.model.ResponseForceUpdate;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.orders.AllOrderFragment;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    View fragmentView;
    TextView mOutstandingPayment, mDueOrders, mActiveOrders, mAllCustomers;
    TextView labelOutstandingPayment, labelDueOrders, labelActiveOrders, labelAllCutomers;
    ImageView imageDueOrders, imageActiveOrders, customerImageView;
    String[] titleText = {"Due Orders", "Active Orders", "Customers"};

    AppDatabase appDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = DbAdapter.getInstance().getDb();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                MainActivityFragment.class.getName());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initialize();

        callForceUpdatApi();
        return fragmentView;
    }

    private void initialize() {

        mOutstandingPayment = (TextView) fragmentView.findViewById(R.id.outstandingPayment);
        mOutstandingPayment.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_THIN));
        mDueOrders = (TextView) fragmentView.findViewById(R.id.dueOrders);
        mDueOrders.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_THIN));
        mActiveOrders = (TextView) fragmentView.findViewById(R.id.activeOrder);
        mActiveOrders.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_THIN));
        mAllCustomers = (TextView) fragmentView.findViewById(R.id.allCustomers);
        mAllCustomers.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_THIN));

        labelOutstandingPayment = (TextView) fragmentView.findViewById(R.id.labelOutStanding);
        labelOutstandingPayment.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_REGULAR));
        labelDueOrders = (TextView) fragmentView.findViewById(R.id.labelDueOrder);
        labelDueOrders.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_REGULAR));
        labelActiveOrders = (TextView) fragmentView.findViewById(R.id.labelActiveOrder);
        labelActiveOrders.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_REGULAR));
        labelAllCutomers = (TextView) fragmentView.findViewById(R.id.labelAllCust);
        labelAllCutomers.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_REGULAR));

        customerImageView = (ImageView) fragmentView.findViewById(R.id.imageView2);
        customerImageView.setOnClickListener(this);
        imageDueOrders = (ImageView) fragmentView.findViewById(R.id.imageDueOrders);
        imageDueOrders.setOnClickListener(this);
        imageActiveOrders = (ImageView) fragmentView.findViewById(R.id.imageActiveOrders);
        imageActiveOrders.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Util.checkIntenetConnection(getActivity())) {
            getDashBoardValues();
        } else {
            DashBoardModelDetail dashBoardModelDetail = appDatabase.getDashBoard();
//            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            setDashBoardValues(dashBoardModelDetail);
        }
    }

    public void getDashBoardValues() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("api_key", "123");

        NetworkAdaper.getInstance().getNetworkServices().getDashBoard(param, new Callback<DashBoardModel>() {
            @Override
            public void success(DashBoardModel getDashBoard, Response response) {
                Log.e("Tab", getDashBoard.toString());
                if (getDashBoard.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    appDatabase.setDashBoard(getDashBoard.getData());
                    saveStoreDetails(getDashBoard.getData().getStore());
                    setDashBoardValues(getDashBoard.getData());
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getDashBoard.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }


    private void saveStoreDetails(DashBoardModelStoreDetail store) {

        try {
            Util.savePreferenceValue(getActivity(), Constant.STORE_DETAILS, getStoreDataAsString(store));
            Util.savePreferenceValue(getActivity(), Constant.STORE_STATUS_MESSAGE, store.getStoreMessage());


            String cur = store.getCurrency();
            if (cur.contains("\\")) {
                Util.saveCurrency(getActivity(), Util.unescapeJavaString(cur));
            } else {
                Util.saveCurrency(getActivity(), String.valueOf(Html.fromHtml(store.getCurrency())));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDashBoardValues(DashBoardModelDetail dashBoardValues) {

        String currency = Util.getCurrency(getActivity());
        Log.i("@@setDashBoardValues", "" + currency + "" + dashBoardValues.getOutstanding());
        mOutstandingPayment.setText(currency + "" + dashBoardValues.getOutstanding());
        mDueOrders.setText("" + dashBoardValues.getDueOrders());
        mActiveOrders.setText("" + dashBoardValues.getActiveOrders());
        mAllCustomers.setText("" + dashBoardValues.getCustomers());

    }


    public String getStoreDataAsString(DashBoardModelStoreDetail store) {
        Gson gson = new Gson();
        Type type = new TypeToken<DashBoardModelStoreDetail>() {
        }.getType();
        return gson.toJson(store, type);
    }

    public void replace(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        Bundle bundle;
        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        int id = view.getId();

        switch (id) {

            case R.id.imageDueOrders:
                com.signity.shopkeeperapp.home.MainActivity.fragmentName = titleText[0];
                title.setText(titleText[0]);

                fragment = AllOrderFragment.newInstance(getActivity());
                bundle = new Bundle();
                bundle.putString("type", Constant.TYPE_ALL_ORDER);
                fragment.setArguments(bundle);

                replace(fragment);

                break;

            case R.id.imageActiveOrders:
                com.signity.shopkeeperapp.home.MainActivity.fragmentName = titleText[1];
                title.setText(titleText[1]);
                fragment = AllOrderFragment.newInstance(getActivity());
                bundle = new Bundle();
                bundle.putString("type", Constant.TYPE_ACTIVE_ORDER);
                fragment.setArguments(bundle);
                replace(fragment);
                break;

            case R.id.imageView2:
                MainActivity.fragmentName = titleText[2];
                title.setText(titleText[2]);
                replace(CustomerFragment.newInstance(getActivity()));
                break;
        }
    }


    /*Version update Module*/
    private void callForceUpdatApi() {

        NetworkAdaper.getInstance().getNetworkServices().forceDownload(new Callback<ResponseForceUpdate>() {
            @Override
            public void success(ResponseForceUpdate responseForceUpdate, Response response) {
                Log.i("@@callForceUpdatApi",""+responseForceUpdate);
                if (responseForceUpdate != null && responseForceUpdate.getSuccess()) {
                    try {
                        ModelForceUpdate forceUpdate = responseForceUpdate.getData().get(0);
                        checkForceUpdate(forceUpdate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Log", "err:" + error.getMessage());
            }
        });
    }

    private void checkForceUpdate(ModelForceUpdate forceUpdate) {

        String currentVersion = null;
        String playStoreVersion = null;
        if (forceUpdate != null) {
            currentVersion = BuildConfig.VERSION_NAME;
            playStoreVersion = forceUpdate.getAndroidAppVerison();

            if (playStoreVersion != null && !playStoreVersion.isEmpty()) {
                try {
                    double playVersion = Double.parseDouble(playStoreVersion);

                    double appVersion = Double.parseDouble(currentVersion);
                    Log.i("@@playversion",""+playVersion);
                    Log.i("@@appVersion",""+appVersion);

                    if (playVersion > appVersion) {
                        openDialogForVersion(forceUpdate);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void openDialogForVersion(ModelForceUpdate forceUpdate) {
        if (forceUpdate.getForceDownload() != null) {
            final DialogHandler dialogHandler = new DialogHandler(getActivity());
            dialogHandler.setDialog("APPLICATION UPDATE", forceUpdate.getForceDownloadMessage());
            if (forceUpdate.getForceDownload().equalsIgnoreCase("1")) {
                dialogHandler.setCancelable(false);
                dialogHandler.setPostiveButton("Update", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPlayStoreLink();
                        dialogHandler.dismiss();
                    }

                });
            } else {
                dialogHandler.setNegativeButton("Cancel", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogHandler.dismiss();
                    }
                });
                dialogHandler.setPostiveButton("Update", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPlayStoreLink();
                        dialogHandler.dismiss();
                    }
                });
            }
        }
    }

    private void openPlayStoreLink() {
        final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
        Log.i("@@openPlayStoreLink",""+appPackageName);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
