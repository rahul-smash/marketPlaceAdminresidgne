package com.signity.shopkeeperapp.stores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.verify.StoreResponse;

import java.util.ArrayList;
import java.util.List;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.ViewHolder> {

    private Context context;
    private StoresAdapterListener listener;
    private List<StoreResponse> storeResponseList = new ArrayList<>();

    public StoresAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_stores, parent, false);
        return new StoresAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return storeResponseList.size();
    }

    public void setListener(StoresAdapterListener listener) {
        this.listener = listener;
    }

    public void setStoreResponseList(List<StoreResponse> dataResponses) {
        this.storeResponseList = dataResponses;
        notifyDataSetChanged();
    }

    public interface StoresAdapterListener {
        void onClickStore(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStoreName, textViewStoreAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStoreName = itemView.findViewById(R.id.tv_store_name);
            textViewStoreAddress = itemView.findViewById(R.id.tv_order_id_items_time);
        }

        public void bind(int positon) {

            StoreResponse storeResponse = storeResponseList.get(positon);

            textViewStoreName.setText(storeResponse.getStoreName());
            textViewStoreAddress.setText(storeResponse.getLocation());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickStore(getAdapterPosition());
                    }
                }
            });
        }
    }
}