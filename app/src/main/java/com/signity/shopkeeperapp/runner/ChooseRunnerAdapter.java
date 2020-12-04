package com.signity.shopkeeperapp.runner;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.runner.RunnerDetail;

import java.util.ArrayList;
import java.util.List;

public class ChooseRunnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ChooseRunnerAdapterListener listener;
    private boolean showLoading = true;
    private List<RunnerDetail> customersList = new ArrayList<>();
    private String selectedRunnerId;

    public ChooseRunnerAdapter(Context context) {
        this.context = context;
    }

    public List<RunnerDetail> getCustomersList() {
        return customersList;
    }

    public void setCustomersList(List<RunnerDetail> customersResponseList) {
        this.customersList = customersResponseList;
        notifyDataSetChanged();
    }

    public void setSelectedRunnerId(String selectedRunnerId) {
        this.selectedRunnerId = selectedRunnerId;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.customersList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.itemview_choose_runner, parent, false);
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

    public void setListener(ChooseRunnerAdapterListener listener) {
        this.listener = listener;
    }

    public interface ChooseRunnerAdapterListener {
        void onSelectRunner(String id);
    }

    static class ViewHolderLoading extends RecyclerView.ViewHolder {

        public ViewHolderLoading(final View convertView) {
            super(convertView);
        }
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView textViewActiveCount;
        TextView textViewCustomerName, textViewCustomerNumber, textViewCustomerCity;
        LinearLayout linearLayoutArea;
        ImageView imageViewWhatsapp, imageViewCall, imageViewSms;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.tv_runner_name);
            textViewCustomerNumber = itemView.findViewById(R.id.tv_runner_number);
            textViewCustomerCity = itemView.findViewById(R.id.tv_runner_city);
            textViewActiveCount = itemView.findViewById(R.id.tv_active_count);
            linearLayoutArea = itemView.findViewById(R.id.ll_area);
            imageViewWhatsapp = itemView.findViewById(R.id.iv_whatsapp);
            imageViewCall = itemView.findViewById(R.id.iv_phone_call);
            imageViewSms = itemView.findViewById(R.id.iv_message);
            radioButton = itemView.findViewById(R.id.rbtn_choose_runner);
        }

        public void bind(int positon) {

            final RunnerDetail customersResponse = customersList.get(positon);

            textViewCustomerName.setText(customersResponse.getFullName());
            textViewCustomerNumber.setText(customersResponse.getPhone());
            textViewActiveCount.setText(String.valueOf(customersResponse.getActieOrder()));

            if (customersResponse.getArea() != null && !customersResponse.getArea().isEmpty()) {
                textViewCustomerCity.setText(customersResponse.getArea().get(0).getName());
            } else {
                linearLayoutArea.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(selectedRunnerId))
                radioButton.setChecked(customersResponse.getId().equals(selectedRunnerId));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onSelectRunner(customersResponse.getId());
                    }
                }
            });

            imageViewWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            imageViewSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            imageViewCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}