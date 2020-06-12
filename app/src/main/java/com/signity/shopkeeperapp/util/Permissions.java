package com.signity.shopkeeperapp.util;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/*
 * This class contain all Permission related to App
 *
 * */


public class Permissions {
    public List<String> permissions;
    public static final int READ_WRITE = 101;
    public static final int CALENDAR_PERMISSION = 102;
    public static final int CALL_PERMISSION = 103;
    //  public static final int CAMERA = 104;
    public static final int READ_PHONE_STATE = 105;

    private Permissions() {
        permissions = new ArrayList<>();
    }

    public static Permissions getInstance() {
        return new Permissions();
    }

    public Permissions hasStorageReadPermission(Activity activity) {

        if (ActivityCompat.checkSelfPermission(activity, Constant.READ_SDCARD) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Constant.READ_SDCARD);
        }
        return this;
    }

    public Permissions hasStorageWritePermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Constant.WRITE_SDCARD) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Constant.WRITE_SDCARD);
        }
        return this;
    }

    public void askForPermissions(Activity activity, int requestcode) {
        if (permissions.size() > 0 && isMarshMallow()) {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), requestcode);
        }
    }

    public Permissions checkReadWritePermissions(Activity activity) {
        hasStorageReadPermission(activity);
        hasStorageWritePermission(activity);
        return this;
    }

    public Permissions hasCalendarPermission(Activity mActivity) {
        if (ActivityCompat.checkSelfPermission(mActivity, Constant.CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Constant.CALENDAR);
        }
        return this;
    }

    public Permissions hasPhoneCallPermission(Activity mActivity) {
        if (ActivityCompat.checkSelfPermission(mActivity, Constant.CALL) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Constant.CALL);
        }
        return this;
    }

    public Permissions hasCameraPermission(Context context) {

     /*   if (!hasPermission(context, Manifest.permission.CAMERA)) {
            permissions.add(Constants.CAMERA);
        }*/
        if (!hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissions.add(Constant.READ_SDCARD);
        }
        if (!hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissions.add(Constant.WRITE_SDCARD);
        }
        //checkReadWritePermissions((Activity)context);

        return this;
    }

    public boolean hasPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isMarshMallow() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public Permissions hasRequiredPermissions(Context context) {

/*        if (!hasPermission(context, Manifest.permission.CAMERA)) {
            permissions.add(Constants.CAMERA);
        }
        if (!hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissions.add(Constants.READ_SDCARD);
        }
        if (!hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissions.add(Constants.WRITE_SDCARD);
        }
        if (!hasPermission(context, Manifest.permission.CALL_PHONE)) {
            permissions.add(Constants.CALL);
        }

        if (!hasPermission(context, Manifest.permission.RECORD_AUDIO)) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }*/
        if (!hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }


        return this;
    }

    public boolean hasPermissionsToAsk() {
        if (permissions.size() > 0)
            return true;
        else
            return false;

    }
}
