package com.signity.shopkeeperapp.notifications;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.DataAdapter;


public class NotificationsDetailActivity extends Activity implements View.OnClickListener {


    private TextView positveButton, negativeButton, titleTxt, messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notifications_detail);
        //android O fix bug orientation
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.i("@@NotificatDetailActi","AddressSelectActivity");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        this.setFinishOnTouchOutside(false);


        init();
        initListner();
        showNotificationPopUp();




    }

    private void initListner() {
        positveButton.setOnClickListener(this);
    }


    private void init() {

        positveButton = (TextView) findViewById(R.id.yesBtn);
        positveButton.setText(getString(R.string.str_lbl_ok));
        negativeButton = (TextView) findViewById(R.id.noBtn);
        titleTxt = (TextView) findViewById(R.id.title);
        messageText = (TextView) findViewById(R.id.message);
    }

    private void showNotificationPopUp() {
        String message=null;
        try {
            message= DataAdapter.getInstance().getNotificationMessage();
        } catch (Exception e) {
            message=null;
            e.printStackTrace();
        }

        if(message!=null && !message.isEmpty()){
            titleTxt.setText(getResources().getString(R.string.dialog_title));
            messageText.setText(message);
            DataAdapter.getInstance().setNotificationMessage(null);
        }else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yesBtn:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
