package com.signity.shopkeeperapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;


/**
 * Created by rajesh on 11/1/16.
 */
public class DialogHandler {


    private Context context;

    private Dialog dialog;
    private TextView positveButton, negativeButton, titleTxt, messageText;

    /*Click Handler on positive and negitive button*/
    OnPostiveButtonClick onPostiveButtonClick;
    OnNegativeButtonClick onNegativeButtonClick;

    public interface OnPostiveButtonClick {
        void Onclick();

    }

    public interface OnNegativeButtonClick {
        void Onclick();
    }

    public void setOnPositiveButtonClickListener(String text, OnPostiveButtonClick onPostiveButtonClick) {
        positveButton.setText(text);
        positveButton.setVisibility(View.VISIBLE);
        this.onPostiveButtonClick = onPostiveButtonClick;
    }

    public void setOnNegativeButtonClickListener(String text, OnNegativeButtonClick onNegativeButtonClick) {
        negativeButton.setText(text);
        negativeButton.setVisibility(View.VISIBLE);
        this.onNegativeButtonClick = onNegativeButtonClick;
    }


    /* Static 'instance' method */

    public DialogHandler(Context context) {
        this.context = context;
        setUpDialog(context);
    }

    public void setUpDialog(Context context) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog);
        positveButton = (TextView) dialog.findViewById(R.id.yesBtn);
        negativeButton = (TextView) dialog.findViewById(R.id.noBtn);
        titleTxt = (TextView) dialog.findViewById(R.id.title);
        messageText = (TextView) dialog.findViewById(R.id.message);
        positveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPostiveButtonClick.Onclick();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNegativeButtonClick.Onclick();
            }
        });
    }

    public void setDialog(String title, String message) {
        titleTxt.setText(title);
        messageText.setText(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

    }
    public void setDialogDummy(String title) {
        titleTxt.setText(title);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

    }
    public void setCancelable(boolean status) {
        dialog.setCancelable(status);
    }

    public View setPostiveButton(String text, boolean isShow) {
        positveButton.setText(text);
        if (isShow) {
            positveButton.setVisibility(View.VISIBLE);
        }
        return positveButton;
    }

    public View setNegativeButton(String text, boolean isShow) {
        negativeButton.setText(text);
        if (isShow) {
            negativeButton.setVisibility(View.VISIBLE);
        }
        return negativeButton;
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void setdialogForFinish(String title, String message, final boolean isfinish) {
        titleTxt.setText(title);
        messageText.setText(message);
        dialog.setCanceledOnTouchOutside(false);
        positveButton.setText("Ok");
        positveButton.setVisibility(View.VISIBLE);
        positveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isfinish) {
                    ((Activity) context).finish();
                }
                dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

    }


}
