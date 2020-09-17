package com.signity.shopkeeperapp.enquiries;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.EnquiriesListModel;
import com.signity.shopkeeperapp.model.EnquiriesModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 23/12/15.
 */
public class EnquiriesFragment extends Fragment {

    View rootView;
    ListView listEnquiries;

    EnquiriesAdapter adapter;

    TextView noDataFound;

    public static Fragment newInstance(Context context) {
        Bundle args = new Bundle();
        return Fragment.instantiate(context,
                EnquiriesFragment.class.getName(), args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_enquiries, container, false);

        initialization();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return rootView;
    }

    private void initialization() {

        listEnquiries = (ListView) rootView.findViewById(R.id.listEnquiries);
        noDataFound = (TextView) rootView.findViewById(R.id.noDataFound);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Util.checkIntenetConnection(getActivity())) {
            getEnquiries();
        } else {
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
        }
    }

    public void getEnquiries() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("api_key", "123"); //optional

        NetworkAdaper.getInstance().getNetworkServices().getStoreEnquiries(param, new Callback<EnquiriesModel>() {
            @Override
            public void success(EnquiriesModel getEnquiries, Response response) {
                Log.e("Tab", getEnquiries.toString());
                if (getEnquiries.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getEnquiries != null) {
                        if (getEnquiries.getEnquiriesList().size() > 0) {
                            listEnquiries.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.GONE);
                            adapter = new EnquiriesAdapter(getActivity(), getEnquiries.getEnquiriesList());
                            listEnquiries.setAdapter(adapter);
                        } else {
                            listEnquiries.setVisibility(View.GONE);
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    listEnquiries.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getEnquiries.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }


    public class EnquiriesAdapter extends BaseAdapter
    {
        Context context;
        LayoutInflater inflater;
        List<EnquiriesListModel> list;


        public EnquiriesAdapter() {
            super();
        }

        public EnquiriesAdapter(Context context, List<EnquiriesListModel> list) {

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
                        .inflate(R.layout.row_list_enquiries, null);
                holder = new ViewHolder();
                holder.txtCustName = (TextView) convertView.findViewById(R.id.valCustName);
                holder.txtCustName.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));

                holder.txtMessage = (TextView) convertView.findViewById(R.id.valMessage);
                holder.txtMessage.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));

                holder.txtDateTime = (TextView) convertView.findViewById(R.id.valTime);
                holder.txtDateTime.setTypeface(FontUtil.getTypeface(context, FontUtil.FONT_ROBOTO_REGULAR));

                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (list.get(position).getName().equalsIgnoreCase("") || list.get(position).getName().equals(null)) {
                holder.txtCustName.setText("Guest User");
            } else {
                holder.txtCustName.setText(list.get(position).getName());
            }

            holder.txtMessage.setText(list.get(position).getMessage());
            holder.txtDateTime.setText(list.get(position).getCreated());


            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent custDetailIntent = new Intent(context, EnquiriesActivity.class);
                    custDetailIntent.putExtra("name", list.get(position).getName());
                    custDetailIntent.putExtra("message", list.get(position).getMessage());
                    custDetailIntent.putExtra("email", list.get(position).getEmail());
                    custDetailIntent.putExtra("city", list.get(position).getCity());
                    custDetailIntent.putExtra("phone", list.get(position).getPhone());
                    custDetailIntent.putExtra("booking", list.get(position).getDatetime());

                    context.startActivity(custDetailIntent);
                    AnimUtil.slideFromRightAnim((Activity) context);

                }
            });

            return convertView;
        }

        public class ViewHolder {
            TextView txtCustName, txtMessage, txtDateTime;
            RelativeLayout parent;
        }


    }
}
