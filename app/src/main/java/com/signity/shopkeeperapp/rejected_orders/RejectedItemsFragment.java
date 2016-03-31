package com.signity.shopkeeperapp.rejected_orders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.DataAdapter;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by root on 4/11/15.
 */
public class RejectedItemsFragment extends Fragment {

    View fragmentView;
    ListView listRejectedOrder;
    CustomerAdapter adapter;
    TextView noDataFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                RejectedItemsFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.rejected_order_fragment, container, false);
        listRejectedOrder = (ListView) fragmentView.findViewById(R.id.listRejectedOrder);
        noDataFound = (TextView) fragmentView.findViewById(R.id.noDataFound);
        getRejectedOrder();

        return fragmentView;
    }

    private void getRejectedOrder() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("api_key", "");
        param.put("order_type", "rejected");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {

            @Override
            public void success(GetOrdersModel getOrdersModel, Response response) {
                Log.e("Tab", getOrdersModel.toString());
                if (getOrdersModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getOrdersModel != null) {
                        if (getOrdersModel.getData().getOrders().size() > 0) {
                            listRejectedOrder.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.GONE);
                            adapter = new CustomerAdapter(getActivity(), getOrdersModel.getData().getOrders());
                            listRejectedOrder.setAdapter(adapter);
                        } else {
                            listRejectedOrder.setVisibility(View.GONE);
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    listRejectedOrder.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getOrdersModel.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }


    public class CustomerAdapter extends BaseAdapter {


        Context context;
        LayoutInflater inflater;
        List<OrdersListModel> list;

        public CustomerAdapter() {
            super();
        }

        public CustomerAdapter(Context context, List<OrdersListModel> list) {
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
                        .inflate(R.layout.rejected_order_items, null);
                holder = new ViewHolder();
                holder.valOdrId = (TextView) convertView.findViewById(R.id.valOdrId);
                holder.valCustName = (TextView) convertView.findViewById(R.id.valCustName);
                holder.valTotalAmt = (TextView) convertView.findViewById(R.id.valTotalAmt);
                holder.valTime = (TextView) convertView.findViewById(R.id.valTime);
                holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (list.get(position).getOrderId().equalsIgnoreCase("") || list.get(position).getOrderId().equals(null)) {
                holder.valOdrId.setText("");
            } else {
                holder.valOdrId.setText(list.get(position).getOrderId());
            }

            if (list.get(position).getCustomerName().equalsIgnoreCase("") || list.get(position).getCustomerName().equals(null)) {
                holder.valCustName.setText("");
            } else {
                holder.valCustName.setText(list.get(position).getCustomerName());
            }

            if (list.get(position).getTotalAmount().toString().equalsIgnoreCase("") || list.get(position).getTotalAmount().toString().equals(null)) {
                holder.valTotalAmt.setText("");
            } else {
                holder.valTotalAmt.setText(((Activity) context).getString(R.string.text_rs) + " " + list.get(position).getTotalAmount().toString());
            }
            if (list.get(position).getTime().equalsIgnoreCase("") || list.get(position).getTime().equals(null)) {
                holder.valTime.setText("");
            } else {
                holder.valTime.setText(list.get(position).getTime());
            }


            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DataAdapter.getInstance().setListItem(list.get(position).getItems());
                    Intent rejectedOrderIntent = new Intent(getActivity(), RejectedItemsListActivity.class);
                    rejectedOrderIntent.putExtra("name", list.get(position).getCustomerName());
                    rejectedOrderIntent.putExtra("phone", list.get(position).getPhone());
                    rejectedOrderIntent.putExtra("orderID", list.get(position).getOrderId());
                    rejectedOrderIntent.putExtra("userID", list.get(position).getUserId());
                    context.startActivity(rejectedOrderIntent);
                    AnimUtil.slideFromRightAnim((Activity) context);
                }
            });


            return convertView;
        }

        public class ViewHolder {
            TextView valOdrId, valCustName, valTotalAmt, valTime;
            RelativeLayout parent;
        }


    }
}



