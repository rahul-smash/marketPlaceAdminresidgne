package com.signity.shopkeeperapp.dashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;


/**
 * Created by Ketan Tetry on 10/12/19.
 */
public class ShareBottomSheetDialog extends BaseDialogFragment {

    public static final String TAG = "ShareBottomSheetDialog";
    private ShareBottomSheetDialogListener listener;

    public static ShareBottomSheetDialog getInstance(Bundle args) {
        ShareBottomSheetDialog dialog = new ShareBottomSheetDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public void setListener(ShareBottomSheetDialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected int setLayout() {
        return R.layout.bottom_sheet_share_dialog;
    }

    @Override
    protected void setUp(View view) {
        initViews(view);
    }

    private void initViews(View view) {
        view.findViewById(R.id.const_website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onShareWebsite();
                }
                dismiss();
            }
        });
/*        view.findViewById(R.id.const_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onShareProduct();
                }
                dismiss();
            }
        });*/
        view.findViewById(R.id.const_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onShareLocation();
                }
                dismiss();
            }
        });
        view.findViewById(R.id.const_facebook_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onShareFacebookPage();
                }
                dismiss();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), this.getTheme());
    }

    public interface ShareBottomSheetDialogListener {
        void onShareWebsite();

        void onShareProduct();

        void onShareLocation();

        void onShareFacebookPage();
    }
}
