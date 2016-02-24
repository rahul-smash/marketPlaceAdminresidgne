package com.signity.shopkeeperapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.MobResponseDetails;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.model.StoresListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.network.NetworkConstant;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        phone = bundle.getString("phone");

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

    public class StoresListAdapter extends BaseAdapter

    {
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

        callNetworkServiceForOtp();
//        saveUserIdToPref();
//        Intent intent_home = new Intent(getActivity(), MainActivity.class);
//        intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent_home);
//
//        getActivity().finish();
//        AnimUtil.slideFromRightAnim(getActivity());
    }

    private void callNetworkServiceForOtp() {
        ProgressDialogUtil.showProgressDialog(getActivity());

        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
        String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
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
//                    checkUserExistModule(mobResponse);
                    MobResponseDetails details = mobResponse.getData();
                    Util.savePreferenceValue(getActivity(), Constant.IS_ADMIN, details.getRole());
                    saveUserIdToPref();
                    Intent intent_home = new Intent(getActivity(), MainActivity.class);
                    intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_home);
                    getActivity().finish();
                    AnimUtil.slideFromRightAnim(getActivity());
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

            saveUserIdToPref();
            Intent intent_home = new Intent(getActivity(), MainActivity.class);
            intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_home);

            getActivity().finish();
            AnimUtil.slideFromRightAnim(getActivity());

        } else {
            MobResponseDetails data = response.getData();
            proceedToMobileOtpGeneration(data);
        }
    }

    private void saveUserIdToPref() {
        Util.savePreferenceValue(getActivity(), Constant.LOGIN_CHECK, "1");
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

}
