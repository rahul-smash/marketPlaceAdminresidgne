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
import com.signity.shopkeeperapp.model.orders.loyalty.DataResponse;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class LoyaltyPointsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DataResponse> pointsResponses = new ArrayList<>();
    private LoyaltyPointsAdapterListener listener;

    public LoyaltyPointsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_loyalty_points, parent, false);
        return new LoyaltyPointsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoyaltyPointsViewHolder) {
            ((LoyaltyPointsViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return pointsResponses.size();
    }

    public void setListener(LoyaltyPointsAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(List<DataResponse> data) {
        this.pointsResponses = data;
        notifyDataSetChanged();
    }

    public interface LoyaltyPointsAdapterListener {
        void onClickApply();
    }

    class LoyaltyPointsViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDiscount, textViewPoints, textViewCode;
        LinearLayout linearLayoutRedeem;

        public LoyaltyPointsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDiscount = itemView.findViewById(R.id.tv_loyalty_discount);
            textViewPoints = itemView.findViewById(R.id.tv_loyalty_points);
            textViewCode = itemView.findViewById(R.id.tv_loyalty_code);

            linearLayoutRedeem = itemView.findViewById(R.id.ll_redeem);
        }

        public void bind(int positon) {

            DataResponse response = pointsResponses.get(positon);

            String discount = Util.getCurrencySymbol(AppPreference.getInstance().getCurrency()).concat(response.getAmount());

            textViewDiscount.setText(String.format("%s OFF for %s Points", discount, response.getPoints()));
            textViewPoints.setText(String.format("Redeem for %s Points", response.getPoints()));
            textViewCode.setText(response.getCouponCode());

            linearLayoutRedeem.setOnClickListener(new View.OnClickListener() {
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