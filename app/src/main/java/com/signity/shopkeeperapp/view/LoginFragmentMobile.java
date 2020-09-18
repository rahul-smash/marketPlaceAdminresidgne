package com.signity.shopkeeperapp.view;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.MobResponseDetails;
import com.signity.shopkeeperapp.model.StoresModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*import com.signity.shopkeeperapp.gcm.QuickstartPreferences;
import com.signity.shopkeeperapp.gcm.RegistrationIntentService;*/

/**
 * Created by Rajesh on 12/10/15.
 */
public class LoginFragmentMobile extends Fragment implements View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private static final int CREDENTIAL_PICKER_REQUEST = 1;
    Button btnNext, backButton;
    EditText edtPhone;
    String from;
    PrefManager prefManager;
    String deviceToken;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                LoginFragmentMobile.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getActivity());
    }

    private void requestHint() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HintRequest hintRequest = new HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build();
                PendingIntent intent = Credentials.getClient(getContext()).getHintPickerIntent(hintRequest);
                try {
                    startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, null);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }, 200);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        prefManager = new PrefManager(getActivity());

        if (Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN).equalsIgnoreCase("") || Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN).equalsIgnoreCase(null)) {
            //  processForDeviceToken();
            deviceToken = prefManager.getSharedValue(Constant.DEVICE_TOKEN);
            Log.i("@@onAttach", deviceToken);
        }

       /* mRegistrationBroadcastReceiver = new BroadcastReceiver() {
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
        };*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mobile_login_fragment, container, false);
        /*Log.i("@@onAttach", "");

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
        });*/

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
//                callNetworkServiceForOtp();
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
        // deviceToken = prefManager.getSharedValue(Constant.DEVICE_TOKEN);
        //  String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        String deviceToken = prefManager.getSharedValue(Constant.DEVICE_TOKEN);

        // deviceToken = Util.loadPreferenceValue(getActivity(), prefManager.getSharedValue(Constant.DEVICE_TOKEN));
        Log.i("@@getAdminStores", deviceToken);
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


    /*private void processForDeviceToken() {

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
    }*/


    @Override
    public void onResume() {
        super.onResume();
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    public void onPause() {
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
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
