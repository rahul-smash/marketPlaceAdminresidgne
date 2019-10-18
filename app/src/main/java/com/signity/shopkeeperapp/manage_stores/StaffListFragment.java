package com.signity.shopkeeperapp.manage_stores;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
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

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.GetStaffDataModel;
import com.signity.shopkeeperapp.model.GetStaffResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 23/2/16.
 */
public class StaffListFragment extends Fragment implements View.OnClickListener {

    ListView listViewStaff;
    Button buttonBack;
    Button buttonAddStaff;
    TextView textViewTitle, textViewNoStaff;
    ListStaffAdapter adapter;
    List<GetStaffDataModel> listData;
    PrefManager prefManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                StaffListFragment.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_staff_list, container, false);
        prefManager = new PrefManager(getActivity());
        listViewStaff = (ListView) rootView.findViewById(R.id.listStaff);
        buttonBack = (Button) rootView.findViewById(R.id.backButton);
        buttonAddStaff = (Button) rootView.findViewById(R.id.addUser);
        textViewTitle = (TextView) rootView.findViewById(R.id.textTitle);
        textViewNoStaff = (TextView) rootView.findViewById(R.id.textViewNoStaff);
        buttonBack.setOnClickListener(this);
        buttonAddStaff.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getStaffListResponse();
    }

    private void getStaffListResponse() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
        //  String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        String deviceToken = prefManager.getSharedValue(Constant.DEVICE_TOKEN);
        String storeId = Util.loadPreferenceValue(getActivity(), Constant.STORE_ID);
        String mobile = Util.loadPreferenceValue(getActivity(), Constant.LOGIN_USER_MOBILE_NUMBER);
        Map<String, String> param = new HashMap<String, String>();
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);
        param.put("store_id", storeId);
        param.put("action", "get");
        param.put("mobile", mobile);

        NetworkAdaper.getInstance().getNetworkServices().getstoreStaff(param, new Callback<GetStaffResponse>() {
            @Override
            public void success(GetStaffResponse getStaffResponse, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (getStaffResponse.getSuccess() != null ? getStaffResponse.getSuccess() : false) {
                    listData = getStaffResponse.getData();
                    if (listData != null && listData.size() != 0) {
                        adapter = new ListStaffAdapter(getActivity(), listData);
                        listViewStaff.setAdapter(adapter);
                        listViewStaff.setVisibility(View.VISIBLE);
                        textViewNoStaff.setVisibility(View.GONE);
                    } else {
                        listViewStaff.setVisibility(View.GONE);
                        textViewNoStaff.setVisibility(View.VISIBLE);
                    }
                } else {
                    listViewStaff.setVisibility(View.GONE);
                    textViewNoStaff.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error", error.getMessage());
                listViewStaff.setVisibility(View.GONE);
                textViewNoStaff.setVisibility(View.VISIBLE);
            }
        });


    }

    public class ListStaffAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        List<GetStaffDataModel> list;
        ColorStateList colorStateList;
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected}, // enabled
                new int[]{-android.R.attr.state_selected}, // disabled
        };
        int[] colors = new int[]{
                Color.GREEN,
                Color.RED
        };

        public ListStaffAdapter(Context context, List<GetStaffDataModel> list) {

            this.context = context;
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            colorStateList = new ColorStateList(states, colors);
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
                        .inflate(R.layout.row_list_staff, null);
                holder = new ViewHolder();
                holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
                holder.textViewName.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));

                holder.textViewNumber = (TextView) convertView.findViewById(R.id.textViewNumber);
                holder.textViewNumber.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));

                holder.textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatus);
                holder.textViewStatus.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));
                holder.textViewStatus.setTextColor(colorStateList);
                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GetStaffDataModel dataModel = list.get(position);

            holder.textViewName.setText(dataModel.getFullName());
            holder.textViewNumber.setText(dataModel.getPhone());

            if (dataModel.getStatus().equalsIgnoreCase("1")) {
                holder.textViewStatus.setText("Active");
                holder.textViewStatus.setSelected(true);
            } else {
                holder.textViewStatus.setText("InActive");
                holder.textViewStatus.setSelected(false);
            }

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editStaff(dataModel);
                }
            });

            return convertView;
        }

        public class ViewHolder {
            TextView textViewName, textViewNumber, textViewStatus;
            RelativeLayout parent;
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                getActivity().onBackPressed();
                break;
            case R.id.addUser:
                addNewStaff();
                break;
        }
    }

    public void addNewStaff() {
        Fragment fragment = AddStaffFragment.newInstance(getActivity());
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_center_slide,
                R.anim.center_to_left_slide,
                R.anim.left_to_center_slide,
                R.anim.center_to_right_slide);
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void editStaff(GetStaffDataModel getStaffDataModel) {
        Fragment fragment = AddStaffFragment.newInstance(getActivity());
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", getStaffDataModel);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_to_center_slide,
                R.anim.center_to_left_slide,
                R.anim.left_to_center_slide,
                R.anim.center_to_right_slide);
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
