package com.signity.shopkeeperapp.login;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.MobResponseDetails;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.view.LoginFragmentOtp;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Rajesh on 12/10/15.
 */
public class LoginMobileFragment extends Fragment {

    public static final String TAG = "LoginFragmentMobile";
    private static final int CREDENTIAL_PICKER_REQUEST = 1020;

    private EditText editTextMobile;
    private String mobileNumber;
    private LoginMobileListener listener;

    public static LoginMobileFragment getInstance(Bundle bundle) {
        LoginMobileFragment fragment = new LoginMobileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mobile_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        editTextMobile = view.findViewById(R.id.edt_mobile);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.iv_next_click);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMobileOtp();
            }
        });
    }

    private void requestMobileOtp() {

        if (validate()) {
            return;
        }

        if (!Util.checkIntenetConnection(getContext())) {
            DialogUtils.showAlertDialog(getContext(), "Internet", "Please check your Internet Connection.");
            return;
        }

        Util.savePreferenceValue(getContext(), Constant.LOGIN_USER_MOBILE_NUMBER, mobileNumber);
        requestOtp();
    }

    private boolean validate() {

        mobileNumber = editTextMobile.getText().toString().trim();

        if (TextUtils.isEmpty(mobileNumber)) {
            editTextMobile.requestFocus();
            Toast.makeText(getContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginMobileListener) {
            listener = (LoginMobileListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestHint();
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
        }, 500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);

                if (credential == null) {
                    return;
                }

                String number = credential.getId();
                number = number.substring(number.length() - 10);
                editTextMobile.setText(number);
            }
        }
    }

//    private void getAdminStores() {
//
//        ProgressDialogUtil.showProgressDialog(getContext());
//        String deviceId = Settings.Secure.getString(getContext().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        PrefManager prefManager = new PrefManager(getContext());
//        String deviceToken = prefManager.getSharedValue(Constant.DEVICE_TOKEN);
//
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("mobile", mobileNumber);
//        param.put("device_id", deviceId);
//        param.put("device_token", deviceToken);
//        param.put("platform", Constant.PLATFORM);
//
//        NetworkAdaper.getNetworkServices().getAdminStores(param, new Callback<StoresModel>() {
//            @Override
//            public void success(StoresModel mobResponse, Response response) {
//
//                if (!isVisible()) {
//                    return;
//                }
//
//                ProgressDialogUtil.hideProgressDialog();
//
//                if (mobResponse.getSuccess()) {
//                    if (mobResponse.getStoresList().size() > 0) {
//                        proceedToStoresListing(mobResponse);
//                    } else {
//                        Toast.makeText(getActivity(), "No store found", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), mobResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                ProgressDialogUtil.hideProgressDialog();
//                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    private void requestOtp() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        PrefManager prefManager = new PrefManager(getContext());
        String deviceToken = prefManager.getSharedValue(Constant.DEVICE_TOKEN);

        Map<String, String> param = new HashMap<String, String>();
        param.put("phone", mobileNumber);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);

        NetworkAdaper.getNetworkServices().moblieVerification(param, new Callback<MobResponseLogin>() {

            @Override
            public void success(MobResponseLogin mobResponse, Response response) {

                if (!isAdded()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (mobResponse.getSuccess()) {
                    Toast.makeText(getActivity(), "OTP sent to your registered mobile", Toast.LENGTH_SHORT).show();
                    Util.savePreferenceValue(getContext(), Constant.LOGIN_USER_MOBILE_NUMBER, mobileNumber);
                    if (listener != null) {
                        listener.onOtpRequested();
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

//    private void proceedToStoresListing(StoresModel data) {
//        Fragment fragment = LoginFragmentStoresListing.newInstance(getActivity());
//        LoginFragmentStoresListing.mStoresList = data.getStoresList();
//        String phone = edtPhone.getText().toString();
//        Bundle bundle = new Bundle();
//        bundle.putString("phone", phone);
//        fragment.setArguments(bundle);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.right_to_center_slide,
//                R.anim.center_to_left_slide,
//                R.anim.left_to_center_slide,
//                R.anim.center_to_right_slide);
//        ft.replace(R.id.container, fragment);
//        ft.commit();
//    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface LoginMobileListener {
        void onOtpRequested();
    }

}
