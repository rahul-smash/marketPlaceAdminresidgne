package com.signity.shopkeeperapp.setting;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.Locale;

/**
 * Created by rajesh on 4/8/16.
 */
public class StoreSettingActivity extends AppCompatActivity {

    TextView pickSound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_setting);
        pickSound = (TextView) findViewById(R.id.pickSound);


        pickSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("audio/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Audio "), 1);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                if (AppPreference.getInstance().getNotificationRing() != null) {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(AppPreference.getInstance().getNotificationRing()));
                }
                startActivityForResult(intent, 5);
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {

            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            Log.e("onActivityResult: ", intent.getExtras().toString());

            if (uri != null) {
                Log.e("URI", uri.toString());
                AppPreference.getInstance().setNotificationRing(uri.toString());

                NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                String DEFAULCHANNEL = String.format(Locale.getDefault(), "%s%d", Constant.NOTIFICATION_CHANNEL, AppPreference.getInstance().getChannelId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notifManager.deleteNotificationChannel(DEFAULCHANNEL);
                }
                AppPreference.getInstance().setChannelId(AppPreference.getInstance().getChannelId() + 1);
//                this.chosenRingtone = uri.toString();
            } else {
//                this.chosenRingtone = null;
            }
        }
    }
}
