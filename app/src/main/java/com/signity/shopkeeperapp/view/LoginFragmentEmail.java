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
import android.support.annotation.Nullable;
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
/*import com.signity.shopkeeperapp.gcm.RegistrationIntentService;*/
import com.signity.shopkeeperapp.model.StoresModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 6/7/16.
 */
public class LoginFragmentEmail extends Fragment implements View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Button btnNext;
    private EditText edtEmail;
    private Button backButton;
    PrefManager prefManager;
    String deviceToken;

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                LoginFragmentEmail.class.getName());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_login_email, container, false);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        edtEmail = (EditText) rootView.findViewById(R.id.edtEmail);
        backButton = (Button) rootView.findViewById(R.id.backButton);
        btnNext.setOnClickListener(this);
        backButton.setOnClickListener(this);

        edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        prefManager=new PrefManager(getActivity());
       // if (Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN).equalsIgnoreCase("") || Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN).equalsIgnoreCase(null)) {
            if (prefManager.getSharedValue(Constant.DEVICE_TOKEN).equalsIgnoreCase("") || prefManager.getSharedValue(Constant.DEVICE_TOKEN).equalsIgnoreCase(null)) {


                //   processForDeviceToken();
            deviceToken = prefManager.getSharedValue(Constant.DEVICE_TOKEN);
        }

    }

   /* private void processForDeviceToken() {

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
*/

    private void processStart() {
        String email = edtEmail.getText().toString();
        if (checkValidEmail(email)) {
            if (Util.checkIntenetConnection(getActivity())) {
//                callNetworkServiceForOtp();
                Util.savePreferenceValue(getActivity(), Constant.LOGIN_USER_EMAIL_ID, email);
                getAdminStores();
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }
        } else {
            edtEmail.setError("Invalid Email");
        }
    }

    public boolean checkValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        boolean isValid = false;
        String PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(email);
        isValid = matcher.matches();
        return isValid;
    }

    private void getAdminStores() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        String phone = edtEmail.getText().toString();
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
        String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        Log.i("@@Device_token_Email", "" + deviceToken);
        Map<String, String> param = new HashMap<String, String>();
        param.put("mobile", phone);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);
        param.put("type", "email");


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

    private void proceedToStoresListing(StoresModel data) {

        Fragment fragment = LoginFragmentStoresListing.newInstance(getActivity());
        LoginFragmentStoresListing.mStoresList = data.getStoresList();
        String email = edtEmail.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("phone", email);
        bundle.putString("type", "email");
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_center_slide,
                R.anim.center_to_left_slide,
                R.anim.left_to_center_slide,
                R.anim.center_to_right_slide);
        ft.replace(R.id.container, fragment);
        ft.commit();
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


    /*Popup*/
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
                Log.i("TAG", "This device is not supported.");

            }
            return false;
        }
        return true;
    }
}
