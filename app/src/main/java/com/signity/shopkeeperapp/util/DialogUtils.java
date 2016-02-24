package com.signity.shopkeeperapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by rajesh on 16/10/15.
 */
public class DialogUtils {

    public static void showAlertDialog(Context context, String title,
                                       String message) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                context);
//
//        // set title
//        alertDialogBuilder.setTitle(title);
//
//        // set dialog message
//        alertDialogBuilder.setMessage(message).setCancelable(false)
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // if this button is clicked, close
//                        // current activity
//                        dialog.cancel();
//
//                    }
//                });
//
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//
//        // show it
//        alertDialog.show();


        final DialogHandler dialogHandler = new DialogHandler(context);
        dialogHandler.setdialogForFinish(title, message, false);

    }
}
