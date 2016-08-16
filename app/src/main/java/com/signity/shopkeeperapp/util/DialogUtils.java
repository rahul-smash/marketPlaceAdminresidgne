package com.signity.shopkeeperapp.util;

import android.content.Context;

/**
 * Created by rajesh on 16/10/15.
 */
public class DialogUtils {

    public static void showAlertDialog(Context context, String title,
                                       String message) {

        final DialogHandler dialogHandler = new DialogHandler(context);
        dialogHandler.setdialogForFinish(title, message, false);

    }
}
