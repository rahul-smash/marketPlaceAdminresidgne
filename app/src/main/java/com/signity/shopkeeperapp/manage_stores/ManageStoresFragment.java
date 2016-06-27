package com.signity.shopkeeperapp.manage_stores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.DashBoardModelStoreDetail;
import com.signity.shopkeeperapp.model.StoresListModel;
import com.signity.shopkeeperapp.model.StoresModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.network.NetworkConstant;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 7/1/16.
 */
public class ManageStoresFragment extends Fragment {

    View rootView;
    ListView mListView;
    TextView noDataFound;
    StoresListAdapter adapter;
    String phone_number = "";

    public static Fragment newInstance(Context context) {
        Bundle args = new Bundle();
        return Fragment.instantiate(context,
                ManageStoresFragment.class.getName(), args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_layout_manage_stores, container, false);

        initialization();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return rootView;
    }

    private void initialization() {

        mListView = (ListView) rootView.findViewById(R.id.listStores);
        noDataFound = (TextView) rootView.findViewById(R.id.noDataFound);

        phone_number = getPhonenumber();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Util.checkIntenetConnection(getActivity())) {
            getAdminStores();
        } else {
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
        }
    }


    private void getAdminStores() {
        ProgressDialogUtil.showProgressDialog(getActivity());

        String phone = phone_number;
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        String deviceToken = pushClientManager.getRegistrationId(getActivity());
        String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        Map<String, String> param = new HashMap<String, String>();
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);
        if (Util.loadPreferenceValue(getActivity(), Constant.LOGIN_TYPE).equalsIgnoreCase("email")) {
            param.put("type", "email");
            param.put("mobile", Util.loadPreferenceValue(getActivity(), Constant.EMAIL));
        } else {
            param.put("mobile", phone);
        }

        NetworkAdaper.getInstance().getNetworkServices().getAdminStores(param, new Callback<StoresModel>() {
            @Override
            public void success(StoresModel mobResponse, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (mobResponse.getSuccess()) {
                    if (mobResponse.getStoresList().size() > 0) {

                        adapter = new StoresListAdapter(getActivity(), mobResponse.getStoresList());
                        mListView.setAdapter(adapter);

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

    private String getPhonenumber() {

        String mob = Util.loadPreferenceValue(getActivity(), Constant.LOGIN_USER_MOBILE_NUMBER);
        return mob;
    }

    public DashBoardModelStoreDetail getStoreDataAsObject(String store) {
        DashBoardModelStoreDetail object;
        Gson gson = new Gson();
        Type type = new TypeToken<DashBoardModelStoreDetail>() {
        }.getType();
        object = gson.fromJson(store, type);
        return object;
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

                    String currentStoreId = Util.loadPreferenceValue(getActivity(), Constant.STORE_ID);

                    if (currentStoreId.equalsIgnoreCase(list.get(position).getId())) {
                        showAlertForCurrentStore(Constant.APP_TITLE, "You already in " + list.get(position).getStoreName() + ". Please select different store");
                    } else {
                        storeSwitchAlert(Constant.APP_TITLE, list.get(position).getId(), list.get(position).getStoreName());
                    }

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

    }

    private void storeSwitchAlert(String title, final String id, final String storeName) {

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());


        adb.setTitle(title);
        adb.setMessage("Are you sure switch to " + storeName + " ?");
        adb.setIcon(R.drawable.ic_launcher);


        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                storeSwitch(Constant.APP_TITLE, id, "You are successfully switch to " + storeName + ".");
            }
        });


        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }


    private void storeSwitch(String title, final String storeId, String msg) {

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        adb.setCancelable(false);
        adb.setTitle(title);
        adb.setMessage(msg);
        adb.setIcon(R.drawable.ic_launcher);


        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setStoreId(storeId);
                Intent intent_home = new Intent(getActivity(),
                        MainActivity.class);
                startActivity(intent_home);
                AnimUtil.slideFromRightAnim(getActivity());
                getActivity().finish();

            }
        });

        adb.show();
    }

    private void showAlertForCurrentStore(String title, String msg) {

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        adb.setCancelable(false);
        adb.setTitle(title);
        adb.setMessage(msg);
        adb.setIcon(R.drawable.ic_launcher);


        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        adb.show();
    }

}
