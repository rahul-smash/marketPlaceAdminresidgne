package com.signity.shopkeeperapp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.LoginModel;
import com.signity.shopkeeperapp.model.MobResponseDetails;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.model.StoresListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.network.NetworkConstant;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 28/12/15.
 */
public class LoginFragmentStoresListing extends Fragment implements View.OnClickListener {

    public static List<StoresListModel> mStoresList = new ArrayList<>();
    ListView mListView;
    Button backButton;

    StoresListAdapter adapter;

    String phone;
    String type;

    PrefManager prefManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getActivity());
        Bundle bundle = getArguments();
        phone = bundle.getString("phone");
        type = bundle.getString("type");
        if (type == null) {
            type = "";
        }
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                LoginFragmentStoresListing.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_login_stores_listing, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listStores);
        backButton = (Button) rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        adapter = new StoresListAdapter(getActivity(), mStoresList);
        mListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.backButton:
                getActivity().onBackPressed();
                break;
        }

    }

    public class StoresListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        List<StoresListModel> list;


        public StoresListAdapter() {
            super();
        }

        public StoresListAdapter(Context context, List<StoresListModel> list) {

            this.context = context;
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater
                        .inflate(R.layout.row_list_stores, null);
                holder = new ViewHolder();
                holder.txtStoreName = (TextView) convertView.findViewById(R.id.txtStoreName);
                holder.txtStoreName.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));

                holder.txtStoreAddress = (TextView) convertView.findViewById(R.id.txtStoreAddress);
                holder.txtStoreAddress.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));


                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.txtStoreName.setText(list.get(position).getStoreName());
            holder.txtStoreAddress.setText(list.get(position).getLocation());


            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setStoreId(list.get(position).getId());
                }
            });

            return convertView;
        }

        public class ViewHolder {
            TextView txtStoreName, txtStoreAddress;
            RelativeLayout parent;
        }

    }

    private void setStoreId(String id) {
        Util.savePreferenceValue(getActivity(), Constant.STORE_ID, id);
        String url = NetworkConstant.BASE + "/" + id + NetworkConstant.APISTORE;
        NetworkAdaper.setupRetrofitClient(url);
        if (type.equalsIgnoreCase("email")) {
            openDialogForPassword();
        } else {
            callNetworkServiceForOtp();
        }

    }

    private void proceedFutherForHomeScreen() {
        Intent intent_home = new Intent(getActivity(), MainActivity.class);
        intent_home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent_home);
        getActivity().finish();
        AnimUtil.slideFromRightAnim(getActivity());
    }

    private void openDialogForPassword() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.fragment_email_password);

        Button forgot = (Button) dialog.findViewById(R.id.btnForgot);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        final EditText password = (EditText) dialog.findViewById(R.id.password);


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openForgotDailogModule();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (password.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter an email id.", Toast.LENGTH_SHORT).show();
                } else {
                    callNetworkServiceForPasswrod(password.getText().toString());
                }
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    private void openForgotDailogModule() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.fragment_forgot);
        String email = prefManager.getSharedValue(Constant.LOGIN_USER_EMAIL_ID);

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        final EditText getEmail = (EditText) dialog.findViewById(R.id.getEmail);
        getEmail.setText(email);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (getEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter an email id.", Toast.LENGTH_SHORT).show();
                } else if (checkValidEmail(getEmail.getText().toString().trim())) {
                    callNetworkForForgotPassword(getEmail.getText().toString().trim());
                } else {
                    Toast.makeText(getActivity(), "Please enter valid email id.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void callNetworkServiceForPasswrod(String password) {
        ProgressDialogUtil.showProgressDialog(getActivity());
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
      //  String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        String deviceToken=prefManager.getSharedValue(Constant.DEVICE_TOKEN);
        Log.i("@@deviceToken",deviceToken);
        String email = prefManager.getSharedValue(Constant.LOGIN_USER_EMAIL_ID);
        String pass = password;
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", email);
        param.put("password", pass);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);
        NetworkAdaper.getInstance().getNetworkServices().loginVerification(param, new Callback<LoginModel>() {

            @Override
            public void success(LoginModel loginModel, Response response) {

                if (loginModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (loginModel.getData() != null) {
                        String storeId = loginModel.getData().getStoreId();
                        String userId = loginModel.getData().getId();
                        String email = loginModel.getData().getEmail();
                        String role = loginModel.getData().getRole();
                        prefManager.storeSharedValue(Constant.STAFF_ADMIN_ID, userId);
                        prefManager.storeSharedValue(Constant.LOGIN_TYPE, "email");
                        prefManager.storeSharedValue(Constant.EMAIL, email);
                        prefManager.storeSharedValue(Constant.IS_ADMIN, (role != null &&
                                !(role.isEmpty())) ? role : "");
                        prefManager.storeSharedValue(Constant.LOGIN_CHECK, "1");
                        proceedFutherForHomeScreen();
                    }

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

    private void callNetworkForForgotPassword(String email) {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("email_id", email);
        NetworkAdaper.getInstance().getNetworkServices().forgetPassword(param, new Callback<LoginModel>() {


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

    private void callNetworkServiceForOtp() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
     //   String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        String deviceToken =prefManager.getSharedValue(Constant.DEVICE_TOKEN);
        Log.i("@@deviceToken",deviceToken);

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
                    checkUserExistModule(mobResponse);

                } else {
                    Toast.makeText(getActivity(), "" + mobResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkUserExistModule(MobResponseLogin response) {

        if (response.getUserExists().toString().equalsIgnoreCase("1")) {
            Util.savePreferenceValue(getActivity(), Constant.LOGIN_CHECK, "1");
            prefManager.storeSharedValue(Constant.IS_ADMIN, response.getData().getRole());
            prefManager.storeSharedValue(Constant.STAFF_ADMIN_ID, response.getData().getId());
            Util.savePreferenceValue(getActivity(), Constant.PHONE, phone);
            Util.savePreferenceValue(getActivity(), Constant.LOGIN_TYPE, "phone");
            Util.savePreferenceValue(getActivity(), Constant.EMAIL, "");
            Intent intent_home = new Intent(getActivity(), MainActivity.class);
            intent_home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent_home);
            getActivity().finish();
            AnimUtil.slideFromRightAnim(getActivity());

        } else {
            MobResponseDetails data = response.getData();
            proceedToMobileOtpGeneration(data);
        }
    }

    private void saveUserIdToPref() {

    }

    private void proceedToMobileOtpGeneration(MobResponseDetails data) {

        Fragment fragment = LoginFragmentOtp.newInstance(getActivity());
        Bundle bundle = new Bundle();
        bundle.putString("id", data.getId());
        bundle.putString("phone", data.getPhone());
        bundle.putString("name", data.getFullName());
        bundle.putString("email", data.getEmail());
        bundle.putString("otp", data.getOtp());
        bundle.putString("status", data.getStatus());
        bundle.putString("role", data.getRole());
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_center_slide,
                R.anim.center_to_left_slide,
                R.anim.left_to_center_slide,
                R.anim.center_to_right_slide);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    public boolean checkValidEmail(String email) {
        boolean isValid = false;
        String PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(email);
        isValid = matcher.matches();
        return isValid;
    }

}
