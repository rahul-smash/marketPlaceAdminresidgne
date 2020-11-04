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

import java.util.ArrayList;
import java.util.List;

public class PaymentModeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> paymentModeList = new ArrayList<>();
    private int selectedId = -1;

    public PaymentModeAdapter(Context context) {
        this.context = context;
    }

    public void setPaymentModeList(List<String> paymentModeList) {
        this.paymentModeList = paymentModeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_payment_mode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return paymentModeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPaymentMode;
        LinearLayout linearLayoutPayment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPaymentMode = itemView.findViewById(R.id.tv_payment_mode);
            linearLayoutPayment = itemView.findViewById(R.id.ll_payment);
        }

        public void onBind(int position) {
            final String paymentModel = paymentModeList.get(position);
            textViewPaymentMode.setText(paymentModel);

            linearLayoutPayment.setBackground(context.getResources().getDrawable(selectedId == position ? R.drawable.item_background_rounded_stroke_primary :
                    R.drawable.item_background_rounded_stroke));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedId = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
