package com.signity.shopkeeperapp.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.model.CustomersModel;
import com.signity.shopkeeperapp.model.UserModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajesh on 28/9/15.
 */
public class CustomerFragment extends Fragment {

    View rootView;
    ListView listCustomer;

    EditText searchEdit;
    CustomerAdapter adapter;

    AppDatabase appDatabase;

    TextView noDataFound, txtAreaa, txtName;
    public boolean filterStatusArea = false, isFilterStatusName = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = DbAdapter.getInstance().getDb();
    }

    public static Fragment newInstance(Context context) {
        Bundle args = new Bundle();
        return Fragment.instantiate(context,
                CustomerFragment.class.getName(), args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_customer, container, false);

        initialization();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        return rootView;
    }


    private void initialization() {

        listCustomer = (ListView) rootView.findViewById(R.id.listCustomer);
        noDataFound = (TextView) rootView.findViewById(R.id.noDataFound);
        searchEdit = (EditText) rootView.findViewById(R.id.searchEdit);
        txtAreaa = (TextView) rootView.findViewById(R.id.txtAreaa);
        txtName = (TextView) rootView.findViewById(R.id.txtName);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Util.checkIntenetConnection(getActivity())) {
            getCustomers();
        } else {
            List<UserModel> listCustomers = appDatabase.getAllCustomer();
            if (listCustomers != null && listCustomers.size() != 0) {
                adapter = new CustomerAdapter(getActivity(), listCustomers);
                listCustomer.setAdapter(adapter);
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
                listCustomer.setVisibility(View.GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }
        }
    }

    public void getCustomers() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
     //   param.put("api_key", "123");

        NetworkAdaper.getInstance().getNetworkServices().getCustomers(param, new Callback<CustomersModel>() {
            @Override
            public void success(CustomersModel getCustomers, Response response) {
                Log.e("Tab", getCustomers.toString());
                if (getCustomers.getSuccess()) {

                    ProgressDialogUtil.hideProgressDialog();
                    if (getCustomers != null) {
                        if (getCustomers.getData().getCustomers().size() > 0) {
                            appDatabase.setAllCustomers(getCustomers.getData().getCustomers());
                            listCustomer.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.GONE);
                            adapter = new CustomerAdapter(getActivity(), getCustomers.getData().getCustomers());
                            listCustomer.setAdapter(adapter);
                        } else {
                            listCustomer.setVisibility(View.GONE);
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    listCustomer.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getCustomers.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }


    public class CustomerAdapter extends BaseAdapter implements Filterable {


        Context context;
        LayoutInflater inflater;
        private List<UserModel> filteredData = null;
        List<UserModel> list;
        private ItemFilter mFilter = new ItemFilter();

        public CustomerAdapter() {
            super();
        }

        public CustomerAdapter(Context context, List<UserModel> list) {
            this.context = context;
            this.filteredData = list;
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getCount() {
            return this.filteredData.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredData.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater
                        .inflate(R.layout.row_list_customer, null);
                holder = new ViewHolder();
                holder.txtCustName = (TextView) convertView.findViewById(R.id.txtCustName);
                holder.txtCustNumber = (TextView) convertView.findViewById(R.id.txtCustNumber);
                holder.parent = (LinearLayout) convertView.findViewById(R.id.parent);
                holder.txtArea = (TextView) convertView.findViewById(R.id.txtArea);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (filteredData.get(position).getFullName().equalsIgnoreCase("") || filteredData.get(position).getFullName().equals(null)) {
                holder.txtCustName.setText("Guest User");
            } else {
                holder.txtCustName.setText(filteredData.get(position).getFullName());
            }

            if (filteredData.get(position).getArea().equalsIgnoreCase("") || filteredData.get(position).getArea().equals(null)) {
                holder.txtArea.setText("");
            } else {
                holder.txtArea.setText(filteredData.get(position).getArea());
            }


            holder.txtCustNumber.setText(filteredData.get(position).getPhone());

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent custDetailIntent = new Intent(context, CustomerActivity.class);
                    custDetailIntent.putExtra("name", filteredData.get(position).getFullName());
                    custDetailIntent.putExtra("id", filteredData.get(position).getId());
                    custDetailIntent.putExtra("email", filteredData.get(position).getEmail());
                    custDetailIntent.putExtra("address", filteredData.get(position).getArea());
                    custDetailIntent.putExtra("phone", filteredData.get(position).getPhone());

                    context.startActivity(custDetailIntent);
                    AnimUtil.slideFromRightAnim((Activity) context);
                }
            });


            searchEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    adapter.getFilter().filter(s);
                }
            });


            txtAreaa.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {

                    if (filterStatusArea == false) {
                        Collections.sort(filteredData, new Comparator<UserModel>() {
                            @Override
                            public int compare(UserModel userModel, UserModel t1) {

                                return userModel.getArea().compareToIgnoreCase(t1.getArea());

                            }

                        });
                        Drawable img = getContext().getResources().getDrawable(R.drawable.downward);
                        txtAreaa.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                        adapter.notifyDataSetChanged();
                        filterStatusArea = true;
                    } else {

                        Drawable img = getContext().getResources().getDrawable(R.drawable.upward);
                        txtAreaa.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                        Collections.reverse(filteredData);
                        adapter.notifyDataSetChanged();
                        filterStatusArea = false;

                    }
                }
            });

            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFilterStatusName == false) {
                        Collections.sort(filteredData, new Comparator<UserModel>() {
                            @Override
                            public int compare(UserModel userModel, UserModel t1) {

                                return userModel.getFullName().compareToIgnoreCase(t1.getFullName());

                            }

                        });
                        Drawable img = getContext().getResources().getDrawable(R.drawable.downward);
                        txtName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                        adapter.notifyDataSetChanged();
                        isFilterStatusName = true;
                    } else {

                        Drawable img = getContext().getResources().getDrawable(R.drawable.upward);
                        txtName.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                        Collections.reverse(filteredData);
                        adapter.notifyDataSetChanged();
                        isFilterStatusName = false;

                    }
                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new ItemFilter();
            }
            return mFilter;
        }


        private class ItemFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                List<UserModel> list1 = list;

                int count = list1.size();
                final List<UserModel> nlist = new ArrayList<UserModel>(count);
                UserModel obj = null;

                for (int i = 0; i < count; i++) {
                    obj = new UserModel();
                    obj = list1.get(i);
                    if (obj.getFullName().toLowerCase().startsWith(filterString)) {
                        nlist.add(obj);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<UserModel>) results.values;
                adapter.notifyDataSetChanged();
            }
        }


        public class ViewHolder {
            TextView txtCustName, txtCustNumber, txtArea;
            LinearLayout parent;

        }


    }
}
