package com.signity.shopkeeperapp.model.dashboard;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;

/**
 * FeedbackDialog
 *
 * @blame Ketan Tetry
 */
public class InfoDialog extends BaseDialogFragment {

    public static final String TAG = InfoDialog.class.getSimpleName();

    TextView textViewTitle;
    TextView textViewMessage;

    private String title, message;

    public static InfoDialog getInstance(@Nullable Bundle bundle) {
        InfoDialog dialog = new InfoDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_info;
    }

    @Override
    protected void setUp(View view) {
        initView(view);
        getExtra();
    }

    private void initView(View view) {
        textViewMessage = view.findViewById(R.id.tv_message);
        textViewTitle = view.findViewById(R.id.tv_title);

        view.findViewById(R.id.iv_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickClose();
            }
        });
    }

    private void getExtra() {
        if (getArguments() != null) {
            title = getArguments().getString("title");
            message = getArguments().getString("message");

            textViewTitle.setText(!TextUtils.isEmpty(title) ? title.trim() : "");
            textViewMessage.setText(!TextUtils.isEmpty(message) ? message.trim() : "");
        }
    }

    void onClickClose() {
        dismiss();
    }
}