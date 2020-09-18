package com.signity.shopkeeperapp.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsRetrieverBroadcast extends BroadcastReceiver {

    private static final String TAG = "SmsRetrieverBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Log.d(TAG, "onReceive: ");

            Bundle extras = intent.getExtras();

            if (extras == null) {
                return;
            }

            Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            if (smsRetrieverStatus == null) {
                return;
            }

            switch (smsRetrieverStatus.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get consent intent
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d(TAG, "onReceive: " + message);

                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Time out occurred, handle the error.
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + smsRetrieverStatus.getStatusCode());
            }
        }
    }
}