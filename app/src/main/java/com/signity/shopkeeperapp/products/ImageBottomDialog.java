package com.signity.shopkeeperapp.products;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.signity.shopkeeperapp.R;


/**
 * Created by Ketan Tetry on 10/12/19.
 */
public class ImageBottomDialog extends BottomSheetDialogFragment {

    public static final String TAG = "ImageBottomDialog";
    private final ImageListener imageListener;
    private MaterialCardView materialCardViewGallery, materialCardViewCamera;

    public ImageBottomDialog(ImageListener imageListener) {
        this.imageListener = imageListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), this.getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_image_chooser, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        materialCardViewGallery = view.findViewById(R.id.cv_gallery);
        materialCardViewCamera = view.findViewById(R.id.cv_camera);

        materialCardViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                imageListener.onClickGallery();
            }
        });

        materialCardViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                imageListener.onClickCamera();
            }
        });
    }

    public interface ImageListener {

        void onClickGallery();

        void onClickCamera();

    }
}
