package com.signity.shopkeeperapp.twilio.chat;

import android.os.Bundle;
import android.view.View;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;

/**
 * CustomerDialog
 *
 * @blame Android Team
 */
public class MessageOptionDialog extends BaseDialogFragment {

    public static final String TAG = "MessageOptionDialog";
    private MessageOptionListener listener;

    public static MessageOptionDialog getInstance(Bundle bundle) {
        MessageOptionDialog dialog = new MessageOptionDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_message_option;
    }

    @Override
    protected void setUp(View view) {
        view.findViewById(R.id.iv_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClose();
            }
        });

        view.findViewById(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEdit();
            }
        });

        view.findViewById(R.id.tv_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRemove();
            }
        });
    }

    void onClickClose() {
        dismiss();
    }

    void onClickEdit() {
        onClickClose();
        if (listener != null) {
            listener.onEdit();
        }
    }

    void onClickRemove() {
        onClickClose();

        if (listener != null) {
            listener.onRemove();
        }
    }

    public void setListener(MessageOptionListener listener) {
        this.listener = listener;
    }

    public interface MessageOptionListener {
        void onEdit();

        void onRemove();
    }

}

