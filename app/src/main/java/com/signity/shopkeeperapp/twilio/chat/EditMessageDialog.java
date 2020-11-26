package com.signity.shopkeeperapp.twilio.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;

/**
 * CustomerDialog
 *
 * @blame Android Team
 */
public class EditMessageDialog extends BaseDialogFragment {
    public static final String TAG = EditMessageDialog.class.getSimpleName();
    public static final String MESSAGE = "MESSAGE";

    private AppCompatEditText appCompatEditText;

    private EditMessageDialog.Callback callback;

    public static EditMessageDialog getInstance(Bundle bundle) {
        EditMessageDialog dialog = new EditMessageDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_edit_message;
    }

    @Override
    protected void setUp(View view) {
        initViews(view);
        if (getArguments() != null) {
            final String message = getArguments().getString(MESSAGE, "");
            appCompatEditText.setText(message);
        }
    }

    private void initViews(View view) {
        appCompatEditText = view.findViewById(R.id.appCompatEditText);

        view.findViewById(R.id.iv_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClose();
            }
        });

        view.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdate();
            }
        });
    }

    void onClickClose() {
        hideKeyboard(getView());
        dismiss();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void onClickUpdate() {

        final String channelName = appCompatEditText.getEditableText().toString().trim();

        if (TextUtils.isEmpty(channelName)) {
            Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (callback != null) {
            callback.onUpdate(channelName);
        }

        onClickClose();
    }

    public interface Callback {

        void onUpdate(String updateMessage);

    }
}

