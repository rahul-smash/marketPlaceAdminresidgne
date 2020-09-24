package com.signity.shopkeeperapp.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.signity.shopkeeperapp.R;


/**
 * Created by Rajinder on 5/10/15.
 */
public class ProgressDialogUtil {

    static ProgressDialog pDialog;

    public static void showProgressDialog(Context context) {
        if (pDialog == null) {
            pDialog = new ProgressDialog(context, R.style.ProgressDialog);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    public static void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public static boolean isProgressLoading() {
        return pDialog != null && pDialog.isShowing();
    }
}
