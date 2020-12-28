/*
package com.signity.shopkeeperapp.twilio.chat;

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

*/
/**
 * Created by Ketan Tetry on 1/11/19.
 *//*

public class ChannelRecycleAdapter extends RecyclerView.Adapter<ChannelRecycleAdapter.ViewHolder> {
    private static final String TAG = ChannelRecycleAdapter.class.getSimpleName();
    private Context context;
    private ChannelListener channelListener;
    private List<ChannelModel> channelModels = new ArrayList<>();

    public ChannelRecycleAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_channel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ChannelModel channelModel = channelModels.get(position);
        viewHolder.bind(channelModel, position);
    }

    @Override
    public int getItemCount() {
        return channelModels.size();
    }

    public void setChannelModels(List<ChannelModel> channelModels) {
        this.channelModels = channelModels;
        notifyDataSetChanged();
    }

    public void setChannelListener(ChannelListener channelListener) {
        this.channelListener = channelListener;
    }

    public interface ChannelListener {

        void onChannelClick(int position);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewChannelName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChannelName = itemView.findViewById(R.id.tv_channel_name);
        }

        public void bind(ChannelModel channelModel, int position) {
            textViewChannelName.setText(channelModel.getFriendlyName());
        }
    }
}*/
