package com.signity.shopkeeperapp.products;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.signity.shopkeeperapp.R;


/**
 * Created by Ketan Tetry on 10/12/19.
 */
public class ImageBottomDialog extends BottomSheetDialogFragment {

    public static final String TAG = "ImageBottomDialog";
    private ImageListener imageListener;
    private CustomerListener customerListener;
    private boolean isProductSelection;

    public ImageBottomDialog(ImageListener imageListener) {
        this.imageListener = imageListener;
    }

    public ImageBottomDialog(CustomerListener listener) {
        this.customerListener = listener;
        isProductSelection = true;
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
        MaterialCardView materialCardViewGallery = view.findViewById(R.id.cv_gallery);
        MaterialCardView materialCardViewCamera = view.findViewById(R.id.cv_camera);
        MaterialCardView materialCardViewProduct = view.findViewById(R.id.cv_product);

        materialCardViewProduct.setVisibility(isProductSelection ? View.VISIBLE : View.GONE);
        materialCardViewProduct.findViewById(R.id.cv_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (customerListener != null) {
                    customerListener.onClickProduct();
                }
            }
        });

        materialCardViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                imageListener.onClickGallery();
                if (customerListener != null) {
                    customerListener.onClickGallery();
                }
            }
        });

        materialCardViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                imageListener.onClickCamera();
                if (customerListener != null) {
                    customerListener.onClickCamera();
                }
            }
        });
    }

    public interface ImageListener {

        void onClickGallery();

        void onClickCamera();

    }

    public interface CustomerListener extends ImageListener {
        void onClickProduct();
    }
}
