package com.signity.shopkeeperapp.login;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.LoginModel;
import com.signity.shopkeeperapp.model.verify.EmailVerifyResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajesh on 12/10/15.
 * Updated by ketan on 16/09/20.
 */
public class LoginEmailFragment extends Fragment {

    public static final String TAG = "LoginEmailFragment";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private String email, password;
    private TextView textViewForgot;
    private LoginEmailListener listener;

    public static LoginEmailFragment getInstance(Bundle bundle) {
        LoginEmailFragment fragment = new LoginEmailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.email_login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        editTextEmail = view.findViewById(R.id.edt_email);
        editTextPassword = view.findViewById(R.id.edt_password);
        textViewForgot = view.findViewById(R.id.tv_forgot);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.iv_next_click);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEmailVerification();
            }
        });

        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotDailogModule();
            }
        });
    }

    private void openForgotDailogModule() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.fragment_forgot);

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        final EditText getEmail = (EditText) dialog.findViewById(R.id.getEmail);
        getEmail.setText(editTextEmail.getText().toString().trim());


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (Util.checkValidEmail(getEmail.getText().toString().trim())) {
                    dialog.dismiss();
                    callNetworkForForgotPassword(getEmail.getText().toString().trim());
                } else {
                    Toast.makeText(getActivity(), "Please enter valid email id.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void requestEmailVerification() {

        if (validate()) {
            return;
        }

        if (!Util.checkIntenetConnection(requireContext())) {
            DialogUtils.showAlertDialog(getContext(), "Internet", "Please check your Internet Connection.");
            return;
        }

        AppPreference.getInstance().setUserEmail(email);
        callNetworkServiceForPasswrod();
    }

    private void callNetworkServiceForPasswrod() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceToken = AppPreference.getInstance().getDeviceToken();

        Map<String, String> param = new HashMap<String, String>();
        param.put("email", email);
        param.put("password", password);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);

        NetworkAdaper.getNetworkServices().loginVerificationNew(param, new Callback<EmailVerifyResponse>() {

            @Override
            public void success(EmailVerifyResponse otpVerifyResponse, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (otpVerifyResponse.isSuccess()) {
                    AppPreference.getInstance().setLoginType("email");
                    if (otpVerifyResponse.getStore().size() > 1) {
                        AppPreference.getInstance().saveUser(otpVerifyResponse.getUser());
                        listener.onEmailVerified(true);
                    } else {
                        AppPreference.getInstance().setLoggedIn(Constant.Mode.LOGGED_IN);
                        AppPreference.getInstance().saveUser(otpVerifyResponse.getUser());
                        AppPreference.getInstance().saveStore(otpVerifyResponse.getStore().get(0));
                        NetworkAdaper.setupRetrofitClient(NetworkAdaper.setBaseUrl(AppPreference.getInstance().getStoreId(), AppPreference.getInstance().getBrandId()));
                        listener.onEmailVerified(false);
                    }
                } else {
                    Toast.makeText(getActivity(), otpVerifyResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    private void callNetworkForForgotPassword(String email) {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("email_id", email);
        NetworkAdaper.getNetworkServices().forgetPassword(param, new Callback<LoginModel>() {

            @Override
            public void success(LoginModel loginModel, Response response) {

                if (loginModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogHandler dialogHandler = new DialogHandler(getActivity());
                    dialogHandler.setdialogForFinish("Success", "" + loginModel.getMessage(), false);
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogHandler dialogHandler = new DialogHandler(getActivity());
                    dialogHandler.setdialogForFinish("Error", "" + loginModel.getMessage(), false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });

    }

    private boolean validate() {

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        if (!Util.checkValidEmail(email)) {
            editTextEmail.requestFocus();
            Toast.makeText(getContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(email)) {
            editTextPassword.requestFocus();
            Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginEmailListener) {
            listener = (LoginEmailListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface LoginEmailListener {
        void onEmailVerified(boolean verified);
    }

}