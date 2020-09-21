package com.signity.shopkeeperapp.login;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.classes.CustomTextWatcher;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.model.verify.OtpVerifyResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.stores.StoresActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ketan on 18/09/20.
 */
public class VerifyOtpFragment extends Fragment {

    public static final String TAG = "VerifyOtpFragment";
    private static final int SMS_CONSENT_REQUEST = 1021;
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();

                if (extras == null) {
                    return;
                }

                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                if (smsRetrieverStatus == null) {
                    return;
                }

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + smsRetrieverStatus.getStatusCode());
                }
            }
        }
    };
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private TextView textViewTimerMessage;
    private TextView textViewResendOtp;
    private TextView textViewOtpMessage;
    private ProgressBar progressbar;
    private CountDownTimer countDownTimer;
    private boolean shouldClear;
    private VerifyOtpListener listener;
    private StringBuilder newOtp;

    public static VerifyOtpFragment getInstance(Bundle bundle) {
        VerifyOtpFragment fragment = new VerifyOtpFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readOtpMessage(String message) {

        if (TextUtils.isEmpty(message)) {
            return;
        }

        try {
            String oneTimeCode = parseOneTimeCode(message);
            char[] otp = oneTimeCode.toCharArray();

            editText1.setText(String.valueOf(otp[0]));
            editText2.setText(String.valueOf(otp[1]));
            editText3.setText(String.valueOf(otp[2]));
            editText4.setText(String.valueOf(otp[3]));

            onVerifyOtp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mobile_otp_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {

        editText1 = view.findViewById(R.id.edt_otp1);
        editText1.requestFocus();

        editText2 = view.findViewById(R.id.edt_otp2);
        editText3 = view.findViewById(R.id.edt_otp3);
        editText4 = view.findViewById(R.id.edt_otp4);
        textViewTimerMessage = view.findViewById(R.id.tv_timertext);
        textViewOtpMessage = view.findViewById(R.id.tv_otp_message);
        progressbar = view.findViewById(R.id.progressbar);

        textViewResendOtp = view.findViewById(R.id.tv_resend_otp);
        textViewResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText1.setText(null);
                editText2.setText(null);
                editText3.setText(null);
                editText4.setText(null);

                editText1.requestFocus();
                requestOtp();
            }
        });

        FloatingActionButton floatingActionButton = view.findViewById(R.id.iv_next_click);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVerifyOtp();
            }
        });

        setUpEditor();
    }

    private void onVerifyOtp() {

        if (validate()) {
            Toast.makeText(getContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        verifyOtp(newOtp.toString());
    }

    private boolean validate() {

        String edtOtp1 = editText1.getText().toString().trim();
        String edtOtp2 = editText2.getText().toString().trim();
        String edtOtp3 = editText3.getText().toString().trim();
        String edtOtp4 = editText4.getText().toString().trim();

        if (TextUtils.isEmpty(edtOtp1)) {
            editText1.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(edtOtp2)) {
            editText2.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(edtOtp3)) {
            editText3.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(edtOtp4)) {
            editText4.requestFocus();
            return true;
        }

        newOtp = new StringBuilder();
        newOtp.append(edtOtp1);
        newOtp.append(edtOtp2);
        newOtp.append(edtOtp3);
        newOtp.append(edtOtp4);

        return false;
    }

    private void setUpEditor() {
        editText1.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    editText2.requestFocus();
                    countDownTimer.cancel();
                    textViewTimerMessage.setText(null);
                    progressbar.setVisibility(View.GONE);
                    shouldClear = false;
                }
            }
        });

        editText2.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    editText3.requestFocus();
                    shouldClear = false;
                }
            }
        });

        editText2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (i == KeyEvent.KEYCODE_DEL && TextUtils.isEmpty(editText2.getText())) {

                    if (shouldClear) {
                        editText1.requestFocus();
                        shouldClear = false;
                    } else {
                        shouldClear = true;
                    }
                }

                return false;
            }
        });

        editText3.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    editText4.requestFocus();
                    shouldClear = false;
                }
            }
        });

        editText3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && TextUtils.isEmpty(editText3.getText())) {
                    if (shouldClear) {
                        editText2.requestFocus();
                        shouldClear = false;
                    } else {
                        shouldClear = true;
                    }
                }
                return false;
            }
        });

        editText4.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    shouldClear = false;
                }
            }
        });

        editText4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && TextUtils.isEmpty(editText4.getText())) {
                    if (shouldClear) {
                        editText3.requestFocus();
                        shouldClear = false;
                    } else {
                        shouldClear = true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof VerifyOtpListener) {
            listener = (VerifyOtpListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpData();
        setUpSmsRetreiver();
        setUpTimer();
    }

    private void setUpData() {
        final String mobile = Util.loadPreferenceValue(getContext(), Constant.LOGIN_USER_MOBILE_NUMBER);
        textViewOtpMessage.setText(String.format("%s %s", "OTP has been sent to", mobile));
    }

    private void setUpTimer() {

        progressbar.setVisibility(View.VISIBLE);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    textViewResendOtp.setEnabled(false);
                    textViewResendOtp.setTextColor(getResources().getColor(R.color.colorTextGrey));
                    textViewTimerMessage.setText(String.format(Locale.getDefault(), "Trying to auto capture in 00:%02d", millisUntilFinished / 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                try {
                    textViewTimerMessage.setText(null);
                    textViewResendOtp.setEnabled(true);
                    textViewResendOtp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    progressbar.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    private void setUpSmsRetreiver() {
        SmsRetrieverClient client = SmsRetriever.getClient(getContext());
        Task<Void> task = client.startSmsUserConsent(null);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        requireActivity().registerReceiver(smsVerificationReceiver, intentFilter);
    }

    private void requestOtp() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String mobile = Util.loadPreferenceValue(getContext(), Constant.LOGIN_USER_MOBILE_NUMBER);

        PrefManager prefManager = new PrefManager(getContext());
        String deviceToken = prefManager.getSharedValue(Constant.DEVICE_TOKEN);

        Map<String, String> param = new HashMap<String, String>();
        param.put("phone", mobile);
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
                    setUpTimer();
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

    private void verifyOtp(String otp) {
        ProgressDialogUtil.showProgressDialog(getActivity());

        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String mobile = Util.loadPreferenceValue(getContext(), Constant.LOGIN_USER_MOBILE_NUMBER);
        String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);

        Map<String, String> param = new HashMap<String, String>();
        param.put("phone", mobile);
        param.put("otp", otp);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);

        NetworkAdaper.getNetworkServices().otpVerifyNew(param, new Callback<OtpVerifyResponse>() {

            @Override
            public void success(OtpVerifyResponse otpVerifyResponse, Response response) {

                if (!isAdded()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (otpVerifyResponse.isSuccess()) {
                    Toast.makeText(getActivity(), otpVerifyResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    // TODO - Write re-direct login in activity
                    /*if (listener != null) {
                        listener.onOtpVerified();
                    }*/

                    if (otpVerifyResponse.getStore() == null) {
                        return;
                    }

                    if (otpVerifyResponse.getStore().size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(StoresActivity.STORES_LIST, (ArrayList<? extends Parcelable>) otpVerifyResponse.getStore());
                        startActivity(StoresActivity.getStartIntent(getContext(), bundle));
                        AnimUtil.slideFromRightAnim(getActivity());
                        getActivity().finish();
                    } else {
                        AppPreference.getInstance().setLoggedIn(Constant.Mode.LOGGED_IN);
                        AppPreference.getInstance().saveUser(otpVerifyResponse.getStore().get(0).getUserResponse());
                        AppPreference.getInstance().saveStore(otpVerifyResponse.getStore().get(0).getStoreResponse());
                        NetworkAdaper.setupRetrofitClient(NetworkAdaper.setBaseUrl(AppPreference.getInstance().getStoreId()));
                        startActivity(DashboardActivity.getStartIntent(getContext()));
                        AnimUtil.slideFromRightAnim(getActivity());
                        getActivity().finish();
                    }

                } else {
                    Toast.makeText(getActivity(), otpVerifyResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    textViewResendOtp.setEnabled(true);
                    textViewResendOtp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        requireActivity().unregisterReceiver(smsVerificationReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SMS_CONSENT_REQUEST && resultCode == Activity.RESULT_OK) {
            String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
            readOtpMessage(message);
        }
    }

    private String parseOneTimeCode(String message) {
        return message.trim().substring(message.length() - 4, message.length());
    }

    public interface VerifyOtpListener {
        void onOtpVerified();
    }

}