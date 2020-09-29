package com.signity.shopkeeperapp.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.notification.NotificationResponse;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private NotificationAdapterListener listener;
    private List<NotificationResponse> notificationList = new ArrayList<>();

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
        return notificationList.size();
    }

    public void setListener(NotificationAdapterListener listener) {
        this.listener = listener;
    }

    public List<NotificationResponse> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationResponse> dataResponses) {
        this.notificationList.addAll(dataResponses);
        notifyDataSetChanged();
    }

    public interface NotificationAdapterListener {
        void onClickNotification(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTag, textViewTitle, textViewMessage, textViewDate, textViewTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTag = itemView.findViewById(R.id.tv_notification_tag);
            textViewTitle = itemView.findViewById(R.id.tv_notification_title);
            textViewMessage = itemView.findViewById(R.id.tv_notification_message);
            textViewDate = itemView.findViewById(R.id.tv_notification_date);
            textViewTime = itemView.findViewById(R.id.tv_notification_time);
        }

        public void bind(int positon) {

            NotificationResponse notificationResponse = notificationList.get(positon);

            textViewTag.setText(notificationResponse.getType());
            textViewTitle.setText(notificationResponse.getType());
            textViewMessage.setText(notificationResponse.getMessage());
            textViewDate.setText(Util.getOrderDate(notificationResponse.getCreated()));
            textViewTime.setText(Util.getOrderTime(notificationResponse.getCreated()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickNotification(getAdapterPosition());
                    }
                }
            });
        }
    }
}