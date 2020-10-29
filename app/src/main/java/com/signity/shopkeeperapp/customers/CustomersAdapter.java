package com.signity.shopkeeperapp.customers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.customers.CustomersResponse;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private CustomerAdapterListener listener;
    private int totalCustomers;
    private boolean showLoading = true;
    private List<CustomersResponse> customersList = new ArrayList<>();

    public CustomersAdapter(Context context) {
        this.context = context;
    }

    public List<CustomersResponse> getCustomersList() {
        return customersList;
    }

    public void setCustomersList(List<CustomersResponse> customersResponseList, int totalCustomers, boolean showLoading) {
        this.showLoading = showLoading;
        this.totalCustomers = totalCustomers;
        this.customersList = customersResponseList;
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.customersList.clear();
        notifyDataSetChanged();
    }

    public void addCustomersList(List<CustomersResponse> customersResponseList, int totalCustomers) {
        this.showLoading = true;
        this.totalCustomers = totalCustomers;
        this.customersList.addAll(customersResponseList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.itemview_customers, parent, false);
            return new CustomerViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_loading, parent, false);
        return new ViewHolderLoading(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomerViewHolder) {
            ((CustomerViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        if (showLoading) {
            if (customersList.isEmpty()) {
                return 1;
            }
            if (customersList.size() < totalCustomers) {
                return customersList.size() + 1;
            }
        }
        return customersList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < customersList.size()) {
            return 1;
        }
        return 101;
    }

    public void setListener(CustomerAdapterListener listener) {
        this.listener = listener;
    }

    public interface CustomerAdapterListener {
        void onClickCustomer(CustomersResponse customersResponse);
    }

    static class ViewHolderLoading extends RecyclerView.ViewHolder {

        public ViewHolderLoading(final View convertView) {
            super(convertView);
        }
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {

        TextView textViewActiveCount, textViewTotalCount, textViewAmountPaid;
        TextView textViewCustomerName, textViewCustomerNumber, textViewCustomerCity;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.tv_customer_name);
            textViewCustomerNumber = itemView.findViewById(R.id.tv_customer_number);
            textViewCustomerCity = itemView.findViewById(R.id.tv_customer_city);
            textViewActiveCount = itemView.findViewById(R.id.tv_active_count);
            textViewTotalCount = itemView.findViewById(R.id.tv_total_count);
            textViewAmountPaid = itemView.findViewById(R.id.tv_amount_paid);
        }

        public void bind(int positon) {

            final CustomersResponse customersResponse = customersList.get(positon);

            textViewCustomerName.setText(customersResponse.getFullName());
            textViewCustomerNumber.setText(customersResponse.getPhone());
            textViewCustomerCity.setText(customersResponse.getArea());
            textViewActiveCount.setText(String.valueOf(customersResponse.getActiveOrders()));
            textViewTotalCount.setText(String.valueOf(customersResponse.getTotalOrders()));
            textViewAmountPaid.setText(Util.getPriceWithCurrency(Double.parseDouble(customersResponse.getPaidAmount()), AppPreference.getInstance().getCurrency()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickCustomer(customersList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}