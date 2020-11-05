package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.orders.offers.DataResponse;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DataResponse> couponsList = new ArrayList<>();
    private CouponsAdapterListener listener;

    public CouponsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_coupons, parent, false);
        return new CouponsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CouponsViewHolder) {
            ((CouponsViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }

    public void setListener(CouponsAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(List<DataResponse> data) {
        this.couponsList = data;
        notifyDataSetChanged();
    }

    public interface CouponsAdapterListener {
        void onClickApply();
    }

    class CouponsViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDiscount, textViewCouponCode, textViewMinOrder, textViewValidDate;
        LinearLayout linearLayoutApplyCoupon;

        public CouponsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDiscount = itemView.findViewById(R.id.tv_coupon_discount);
            textViewCouponCode = itemView.findViewById(R.id.tv_coupon_code);
            textViewMinOrder = itemView.findViewById(R.id.tv_min_order);
            textViewValidDate = itemView.findViewById(R.id.tv_valid_date);

            linearLayoutApplyCoupon = itemView.findViewById(R.id.ll_apply_coupon);
        }

        public void bind(int positon) {
            DataResponse response = couponsList.get(positon);

            String uptoPrice = Util.getCurrencySymbol(AppPreference.getInstance().getCurrency()).concat(response.getDiscountUpto());
            String minPrice = Util.getCurrencySymbol(AppPreference.getInstance().getCurrency()).concat(response.getMinimumOrderAmount());

            String discount = response.getDiscount().concat("%");

            if (Integer.parseInt(response.getDiscount()) == 0) {
                discount = "";
            }

            textViewDiscount.setText(String.format("%s OFF Upto %s", discount, uptoPrice));
            textViewCouponCode.setText(response.getCouponCode());
            textViewMinOrder.setText(String.format("Min Order - %s", minPrice));
            textViewValidDate.setText(String.format("Valid Till - %s", response.getValidTo()));

            linearLayoutApplyCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickApply();
                    }
                }
            });
        }
    }
}