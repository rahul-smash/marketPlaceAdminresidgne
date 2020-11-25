package com.signity.shopkeeperapp.market;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;
import com.signity.shopkeeperapp.util.Constant;


/**
 * Created by Ketan Tetry on 10/12/19.
 */
public class ShareCreativeChooserDialog extends BaseDialogFragment {

    public static final String TAG = "PreviewDialog";
    private final ShareListener shareListener;

    private TextView textViewSchedule;

    private Constant.ShareMode type;

    public ShareCreativeChooserDialog(ShareListener shareListener) {
        this.shareListener = shareListener;
    }

    @Override
    protected int setLayout() {
        return R.layout.bottom_sheet_share_creative;
    }

    @Override
    protected void setUp(View view) {
        initViews(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = (Constant.ShareMode) bundle.getSerializable("Type");
        }

        textViewSchedule.setText(type == Constant.ShareMode.SCHEDULE ? "Schedule Later" : "Re-Schedule");
    }

    private void initViews(View view) {
        textViewSchedule = view.findViewById(R.id.btn_post_schedule);
        textViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSchedule();
            }
        });

        view.findViewById(R.id.btn_post_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPost();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), this.getTheme());
    }

    public void onClickSchedule() {
        dismiss();
        shareListener.onClickSchedule();
    }

    public void onClickPost() {
        dismiss();
        shareListener.onClickPost();
    }

    public interface ShareListener {

        void onClickSchedule();

        void onClickPost();

    }
}
