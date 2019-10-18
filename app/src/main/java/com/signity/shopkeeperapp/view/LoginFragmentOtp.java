package com.signity.shopkeeperapp.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.model.OtpVerifyModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 2/11/15.
 */
public class LoginFragmentOtp extends Fragment implements View.OnClickListener {

    String id;
    String phone;
    String otp;
    String status;
    String role;
    String name;
    String email;
PrefManager prefManager;
    boolean isEmailExist = false, isNameExist = false;

    public static Button btnDone, backButton;
    Button resend;
    public static EditText edtOTp;


    IncomingSms incomingSms;
    private IntentFilter filter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager=new PrefManager(getActivity());
        Bundle bundle = getArguments();
        id = bundle.getString("id");
        phone = bundle.getString("phone");
        otp = bundle.getString("otp");
        status = bundle.getString("status");
        name = bundle.getString("name");
        email = bundle.getString("email");
        role = bundle.getString("role");


    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                LoginFragmentOtp.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_login_otp, container, false);
        btnDone = (Button) rootView.findViewById(R.id.btnDone);
        resend = (Button) rootView.findViewById(R.id.btnResend);
        backButton = (Button) rootView.findViewById(R.id.backButton);
        edtOTp = (EditText) rootView.findViewById(R.id.edtOTp);
        btnDone.setOnClickListener(this);
        backButton.setOnClickListener(this);
        backButton.setVisibility(View.INVISIBLE);
        resend.setOnClickListener(this);

        incomingSms = new IncomingSms();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                if (vallidOtp()) {
                    callNetworkServiceOtpVerify();
                }
                break;
            case R.id.backButton:
                getActivity().onBackPressed();
                break;
            case R.id.btnResend:
                callNetworkServiceForOtp();
                break;
        }
    }

    private boolean vallidOtp() {
        String otpValue = edtOTp.getText().toString();
        if (otpValue.isEmpty()) {
            Toast.makeText(getActivity(), " Please enter your One Time Password", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (!otpValue.equals(otp)) {
//            Toast.makeText(getActivity(), "Wrong OTP code", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    private void callNetworkServiceForOtp() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        edtOTp.setText("");
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
      //  String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        String deviceToken=prefManager.getSharedValue(Constant.DEVICE_TOKEN);
        Map<String, String> param = new HashMap<String, String>();
        param.put("phone", phone);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);
        NetworkAdaper.getInstance().getNetworkServices().moblieVerification(param, new Callback<MobResponseLogin>() {

            @Override
            public void success(MobResponseLogin mobResponse, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (mobResponse.getSuccess()) {
                    id = mobResponse.getData().getId();
                    otp = mobResponse.getData().getOtp();
                    status = mobResponse.getData().getStatus();
                    role = mobResponse.getData().getRole();
                    Toast.makeText(getActivity(), "OTP sent to your registered mobile", Toast.LENGTH_SHORT).show();
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

    private void callNetworkServiceOtpVerify() {
        ProgressDialogUtil.showProgressDialog(getActivity());

        String otpValue = edtOTp.getText().toString();
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
        String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        Map<String, String> param = new HashMap<String, String>();
        param.put("phone", phone);
        param.put("otp", otpValue);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);
        NetworkAdaper.getInstance().getNetworkServices().otpVerify(param, new Callback<OtpVerifyModel>() {

            @Override
            public void success(OtpVerifyModel mobResponse, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (mobResponse.getSuccess()) {
                    proceedFutherForHomeScreen();
                    Toast.makeText(getActivity(), mobResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void proceedFutherForHomeScreen() {
        Util.savePreferenceValue(getActivity(), Constant.LOGIN_CHECK, "1");
        Util.savePreferenceValue(getActivity(), Constant.IS_ADMIN, (role != null && !(role.isEmpty())) ? role : "");
        Util.savePreferenceValue(getActivity(), Constant.STAFF_ADMIN_ID, id);
        Util.savePreferenceValue(getActivity(), Constant.PHONE, phone);
        Util.savePreferenceValue(getActivity(), Constant.LOGIN_TYPE, "phone");
        Util.savePreferenceValue(getActivity(), Constant.EMAIL, "");

        Intent intent_home = new Intent(getActivity(), MainActivity.class);
//        intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent_home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent_home);
        getActivity().finish();
        AnimUtil.slideFromRightAnim(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            getActivity().registerReceiver(incomingSms, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(incomingSms);
    }

    public static class IncomingSms extends BroadcastReceiver {
        // Get the object of SmsManager
        final SmsManager sms = SmsManager.getDefault();
        int verificationCode = -1;


        public void IncomingSms() {

        }

        public void onReceive(Context context, Intent intent) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
                        message = message.replaceAll("[a-z.A-Z]", "").trim();
                        try {
                            verificationCode = Integer.parseInt(message);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String msg = "";
                        if (senderNum.contains("-")) {
                            if (verificationCode != -1) {
                                msg = "Success! your verification code is:" + verificationCode;
                                actionPerformOnOTPReceived(context, verificationCode, msg);
                            } else {
                                msg = "Failed verification. Please enter verification code manually.";
                                actionPerformOnOTPReceived(context, verificationCode, msg);
                            }
                        } else {

                        }
                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }
        }
    }

    public static void actionPerformOnOTPReceived(Context context, int verificationCode, String msg) {

        if (verificationCode != -1) {
            edtOTp.setText("" + verificationCode);
            btnDone.performClick();
        } else {
            // DialogUtils.showAlertDialog(context, "Error!", msg);
        }
    }


}