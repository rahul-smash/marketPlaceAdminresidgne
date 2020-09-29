package com.signity.shopkeeperapp.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.notification.NotificationResponse;
import com.signity.shopkeeperapp.util.Util;

public class NotificationDialog extends DialogFragment {

    public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";
    public static final String TAG = "NotificationDialog";
    private TextView textViewTag, textViewTitle, textViewMessage, textViewDate, textViewTime;
    private ImageView imageViewClose;
    private NotificationResponse notificationResponse;

    public static NotificationDialog getInstance(Bundle bundle) {
        NotificationDialog notificationDialog = new NotificationDialog();
        notificationDialog.setArguments(bundle);
        return notificationDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_notification, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getExtra();
        setUpData();
    }

    private void getExtra() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            notificationResponse = bundle.getParcelable(NOTIFICATION_DATA);
        }
    }

    private void setUpData() {
        textViewTag.setText(notificationResponse.getType());
        textViewTitle.setText(notificationResponse.getType());
        textViewMessage.setText(notificationResponse.getMessage());
        textViewDate.setText(Util.getOrderDate(notificationResponse.getCreated()));
        textViewTime.setText(Util.getOrderTime(notificationResponse.getCreated()));
    }

    private void initView(View view) {
        textViewTag = view.findViewById(R.id.tv_notification_tag);
        textViewTitle = view.findViewById(R.id.tv_notification_title);
        textViewMessage = view.findViewById(R.id.tv_notification_message);
        textViewDate = view.findViewById(R.id.tv_notification_date);
        textViewTime = view.findViewById(R.id.tv_notification_time);
        imageViewClose = view.findViewById(R.id.iv_close);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
        if (prevFragment != null) {
            transaction.remove(prevFragment);
        }
        transaction.addToBackStack(null);
        show(transaction, tag);
    }
}
