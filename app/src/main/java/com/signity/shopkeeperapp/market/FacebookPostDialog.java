package com.signity.shopkeeperapp.market;

import android.os.Bundle;
import android.view.View;

import com.signitysolutions.digisalon.R;
import com.signitysolutions.digisalon.ui.base.BaseDialogFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    protected Unbinder setUnBinder(View view) {
        return ButterKnife.bind(this, view);
    }

    @Override
    protected void setUp() {
    }

    @OnClick(R.id.iv_close_dialog)
    void onClickClose() {
        dismiss();
    }

    @OnClick(R.id.btn_update_dialog)
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