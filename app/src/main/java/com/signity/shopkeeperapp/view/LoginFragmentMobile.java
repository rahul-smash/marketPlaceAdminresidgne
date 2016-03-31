package com.signity.shopkeeperapp.view;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.gcm.QuickstartPreferences;
import com.signity.shopkeeperapp.gcm.RegistrationIntentService;
import com.signity.shopkeeperapp.model.MobResponseDetails;
import com.signity.shopkeeperapp.model.StoresModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajesh on 12/10/15.
 */
public class LoginFragmentMobile extends Fragment implements View.OnClickListener {

    Button btnNext, backButton;
    EditText edtPhone;
    //   private GCMClientManager pushClientManager;
    String from;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //     pushClientManager = new GCMClientManager(getActivity(), Constant.PROJECT_NUMBER);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN).equalsIgnoreCase("") || Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN).equalsIgnoreCase(null)) {
            processForDeviceToken();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                } else {
                    alert(getActivity(), Constant.APP_TITLE, "This device is not registered on server, please try again.");
                }
            }
        };
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                LoginFragmentMobile.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_login_mobile, container, false);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        edtPhone = (EditText) rootView.findViewById(R.id.edtPhone);
        backButton = (Button) rootView.findViewById(R.id.backButton);
        btnNext.setOnClickListener(this);
        backButton.setOnClickListener(this);

        edtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    processStart();
                }

                return false;
            }
        });

        return rootView;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnNext:
                processStart();
                break;
            case R.id.backButton:
                getActivity().onBackPressed();
                break;
        }

    }

    private void processStart() {

        if (vallidPhone()) {
            if (Util.checkIntenetConnection(getActivity())) {
                // callNetworkServiceForOtp();
                String phone = edtPhone.getText().toString();
                Util.savePreferenceValue(getActivity(), Constant.LOGIN_USER_MOBILE_NUMBER, phone);
                getAdminStores();
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }

        }
    }


    private void getAdminStores() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        String phone = edtPhone.getText().toString();
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
        String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        Map<String, String> param = new HashMap<String, String>();
        param.put("mobile", phone);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);
        NetworkAdaper.getInstance().getNetworkServices().getAdminStores(param, new Callback<StoresModel>() {
            @Override
            public void success(StoresModel mobResponse, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (mobResponse.getSuccess()) {
                    if (mobResponse.getStoresList().size() > 0) {
                        proceedToStoresListing(mobResponse);
                    } else {
                        Toast.makeText(getActivity(), "No store found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), mobResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void proceedToMobileOtpGeneration(MobResponseDetails data) {
        Log.e("OTP", data.getOtp());
        Fragment fragment = LoginFragmentOtp.newInstance(getActivity());
        Bundle bundle = new Bundle();
        bundle.putString("id", data.getId());
        bundle.putString("phone", data.getPhone());
        bundle.putString("name", data.getFullName());
        bundle.putString("email", data.getEmail());
        bundle.putString("otp", data.getOtp());
        bundle.putString("status", data.getStatus());
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_center_slide,
                R.anim.center_to_left_slide,
                R.anim.left_to_center_slide,
                R.anim.center_to_right_slide);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    private void proceedToStoresListing(StoresModel data) {

        Fragment fragment = LoginFragmentStoresListing.newInstance(getActivity());

        LoginFragmentStoresListing.mStoresList = data.getStoresList();

        String phone = edtPhone.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_center_slide,
                R.anim.center_to_left_slide,
                R.anim.left_to_center_slide,
                R.anim.center_to_right_slide);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    private boolean vallidPhone() {
        String phone = edtPhone.getText().toString();
        if (phone.isEmpty()) {
            Toast.makeText(getActivity(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void processForDeviceToken() {

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            try {
                Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
                getActivity().startService(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            alert(getActivity(), Constant.APP_TITLE, "This device is not supported.");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");

            }
            return false;
        }
        return true;
    }

    private void alert(Context context, String title,
                       String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder.setMessage(message).
                setCancelable(false).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                dialog.cancel();
                                getActivity().finish();
                            }
                        }

                );

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}
