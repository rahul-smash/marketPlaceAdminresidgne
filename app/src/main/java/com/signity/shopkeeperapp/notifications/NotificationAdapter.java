package com.signity.shopkeeperapp.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private NotificationAdapterListener listener;
    private List<String> notificationList = new ArrayList<>();

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_notification, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void setListener(NotificationAdapterListener listener) {
        this.listener = listener;
    }

    public void setNotificationList(List<String> dataResponses) {
        this.notificationList = dataResponses;
        notifyDataSetChanged();
    }

    public interface NotificationAdapterListener {
        void onClickNotification(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStoreName, textViewStoreAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStoreName = itemView.findViewById(R.id.tv_store_name);
            textViewStoreAddress = itemView.findViewById(R.id.tv_order_id);
        }

        public void bind(int positon) {

        }
    }
}