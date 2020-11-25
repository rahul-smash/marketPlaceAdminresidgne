package com.signity.shopkeeperapp.market;

import android.os.Bundle;
import android.view.View;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;

/**
 * FeedbackDialog
 *
 * @blame Ketan Tetry
 */
public class FacebookPostDialog extends BaseDialogFragment {
    public static final String TAG = FacebookPostDialog.class.getSimpleName();
    private ProfileShareListener listener;

    public static FacebookPostDialog getInstance(Bundle bundle) {
        FacebookPostDialog dialog = new FacebookPostDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.facebook_post_dialog;
    }

    @Override
    protected void setUp(View view) {
        view.findViewById(R.id.iv_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClose();
            }
        });

        view.findViewById(R.id.btn_update_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdateClick();
            }
        });
    }

    void onClickClose() {
        dismiss();
    }

    void doUpdateClick() {
        onClickClose();
        if (listener != null) {
            listener.postOnProfile();
        }
    }

    public void setListener(ProfileShareListener listener) {
        this.listener = listener;
    }

    public interface ProfileShareListener {
        void postOnProfile();
    }
}