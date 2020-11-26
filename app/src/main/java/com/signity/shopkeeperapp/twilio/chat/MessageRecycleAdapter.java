package com.signity.shopkeeperapp.twilio.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.FileUtils;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.twilio.chat.Message;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ketan Tetry on 1/11/19.
 */
public class MessageRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MessageRecycleAdapter.class.getSimpleName();
    private Context context;
    private List<MessageItem> messageItems = new ArrayList<>();
    private MessageListener listener;

    public MessageRecycleAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == ViewType.SENDER_TEXT.ordinal() || viewType == ViewType.RECEIVER_TEXT.ordinal()) {
            view = LayoutInflater.from(context).inflate(viewType == ViewType.SENDER_TEXT.ordinal() ? R.layout.itemview_sender : R.layout.itemview_receiver, parent, false);
            return new ViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(viewType == ViewType.SENDER_MEDIA.ordinal() ? R.layout.itemview_sender_media : R.layout.itemview_receiver_media, parent, false);
            return new ViewHolderMedia(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MessageItem messageItem = messageItems.get(position);

        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).bind(messageItem, position);
        } else if (viewHolder instanceof ViewHolderMedia) {
            ((ViewHolderMedia) viewHolder).bind(messageItem, position);
        }
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    public void setMessageItems(List<MessageItem> messageItems) {
        this.messageItems = messageItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MessageItem messageItem = messageItems.get(position);

        if (messageItem.getMessage().getType() == Message.Type.TEXT) {
            if (messageItem.getMessage().getAuthor().equals(AppPreference.getInstance().getUserEmail()) ||
                    messageItem.getMessage().getAuthor().equals(AppPreference.getInstance().getUserMobile())) {
                return ViewType.SENDER_TEXT.ordinal();
            } else {
                return ViewType.RECEIVER_TEXT.ordinal();
            }
        } else {
            if (messageItem.getMessage().getAuthor().equals(AppPreference.getInstance().getUserEmail()) ||
                    messageItem.getMessage().getAuthor().equals(AppPreference.getInstance().getUserMobile())) {
                return ViewType.SENDER_MEDIA.ordinal();
            } else {
                return ViewType.RECEIVER_MEDIA.ordinal();
            }
        }
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    enum ViewType {
        SENDER_TEXT, RECEIVER_TEXT, SENDER_MEDIA, RECEIVER_MEDIA
    }

    public interface MessageListener {
        void onLongPressMessage(MessageItem messageItem);

        void onDownloadMedia(MessageItem messageItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewChannelName, textViewMessageTime;
        MaterialCardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChannelName = itemView.findViewById(R.id.tv_channel_name);
            textViewMessageTime = itemView.findViewById(R.id.tv_time);
            materialCardView = itemView.findViewById(R.id.material_card);
        }

        public void bind(final MessageItem messageItem, int position) {
            textViewChannelName.setText(messageItem.getMessage().getMessageBody());

            Date messageDate = messageItem.getMessage().getDateCreatedAsDate();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String messageTime = formatter.format(messageDate);

            textViewMessageTime.setText(messageTime);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onLongPressMessage(messageItem);
                    }

                    return false;
                }
            });

            if (position == 0) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) materialCardView.getLayoutParams();
                int value = position == 0 ? 8 : 0;
                layoutParams.setMargins((int) Util.pxFromDp(context, value), (int) Util.pxFromDp(context, value), (int) Util.pxFromDp(context, value), (int) Util.pxFromDp(context, 8));
                materialCardView.setLayoutParams(layoutParams);
            }
        }
    }

    public class ViewHolderMedia extends RecyclerView.ViewHolder {

        ImageView imageViewMedia, imageViewDownload;
        TextView textViewMessageTime;
        MaterialCardView materialCardView;

        public ViewHolderMedia(@NonNull View itemView) {
            super(itemView);
            imageViewMedia = itemView.findViewById(R.id.iv_media);
            imageViewDownload = itemView.findViewById(R.id.iv_download);
            textViewMessageTime = itemView.findViewById(R.id.tv_time);
            materialCardView = itemView.findViewById(R.id.material_card);
        }

        public void bind(final MessageItem messageItem, int position) {

            Date messageDate = messageItem.getMessage().getDateCreatedAsDate();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String messageTime = formatter.format(messageDate);

            Log.d(TAG, "bind: " + messageItem.getMessage().getMedia().getSid());
            File imageFile = Util.getUserImage("Twilio", messageItem.getMessage().getSid());

            if (imageFile.exists()) {
                imageViewDownload.setVisibility(View.GONE);
                imageViewMedia.setVisibility(View.VISIBLE);
                imageViewMedia.setImageURI(FileUtils.getUri(imageFile));
            } else {
                imageViewDownload.setVisibility(View.VISIBLE);
                imageViewMedia.setVisibility(View.GONE);
            }

            textViewMessageTime.setText(messageTime);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        listener.onLongPressMessage(messageItem);
                    }

                    return false;
                }
            });

            imageViewDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onDownloadMedia(messageItem);
                    }
                }
            });

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) materialCardView.getLayoutParams();
            int value = position == 0 ? 8 : 0;
            layoutParams.setMargins((int) Util.pxFromDp(context, value), (int) Util.pxFromDp(context, value), (int) Util.pxFromDp(context, value), (int) Util.pxFromDp(context, 8));
            materialCardView.setLayoutParams(layoutParams);
        }
    }
}