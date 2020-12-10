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
public class MarketBottomSheetDialog extends BaseDialogFragment {

    public static final String TAG = "MarketBottomSheetDialog";
    private ShareBottomSheetDialogListener listener;

    public static MarketBottomSheetDialog getInstance(Bundle args) {
        MarketBottomSheetDialog dialog = new MarketBottomSheetDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public void setListener(ShareBottomSheetDialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected int setLayout() {
        return R.layout.bottom_sheet_market_dialog;
    }

    @Override
    protected void setUp(View view) {
        initViews(view);
    }

    private void initViews(View view) {
        view.findViewById(R.id.const_gallery_creative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onShareGallery();
                }
                dismiss();
            }
        });
        view.findViewById(R.id.const_premium_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSharePremiumVideo();
                }
                dismiss();
            }
        });
        view.findViewById(R.id.const_premium_creative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSharePremiumCreative();
                }
                dismiss();
            }
        });
        view.findViewById(R.id.const_your_creative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onShareYourCreative();
                }
                dismiss();
            }
        });
        view.findViewById(R.id.const_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onShareProducts();
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
        void onShareGallery();

        void onSharePremiumVideo();

        void onSharePremiumCreative();

        void onShareYourCreative();

        void onShareProducts();
    }
}
