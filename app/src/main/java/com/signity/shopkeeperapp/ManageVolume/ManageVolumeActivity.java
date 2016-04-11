package com.signity.shopkeeperapp.ManageVolume;

import android.content.Context;
import android.media.AudioManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.PrefManager;

public class ManageVolumeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView volStatus;
    ImageButton btnBack;
    PrefManager prefManager;
    SeekBar seekBar;
    AudioManager am;
    TextView msgTxt;
    Button okBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_volume);
        prefManager=new PrefManager(ManageVolumeActivity.this);
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volStatus=(ImageView)findViewById(R.id.volStatus);
        btnBack=(ImageButton)findViewById(R.id.btnBack);
        msgTxt=(TextView)findViewById(R.id.msgTxt);
        okBtn=(Button)findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setEnabled(false);
        btnBack.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkVolStatus();
    }

    private void checkVolStatus() {
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_RING);
        seekBar.setProgress(currentVolume);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                volStatus.setBackgroundResource(R.drawable.no_sound);
                msgTxt.setText("Kindly Unmute your phone for the updates and set the sound to the maximum level.");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                volStatus.setBackgroundResource(R.drawable.no_sound);
                msgTxt.setText("Kindly Unmute your phone for the updates and set the sound to the maximum level.");
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                volStatus.setBackgroundResource(R.drawable.sound);

                if(currentVolume<7){
                    msgTxt.setText("Kindly set the volume to the maximum level to receive the Order Alerts.");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            int currentVolume = am.getStreamVolume(AudioManager.STREAM_RING);
            seekBar.setProgress(currentVolume);
            if(currentVolume>=1){
                volStatus.setBackgroundResource(R.drawable.sound);
                if(currentVolume<7){
                    msgTxt.setText("Kindly set the volume to the maximum level to receive the Order Alerts.");
                }
                else {
                    msgTxt.setText("Great!!");
                }
            }
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            int currentVolume = am.getStreamVolume(AudioManager.STREAM_RING);
            seekBar.setProgress(currentVolume);
            if(currentVolume==0){
                msgTxt.setText("Kindly Unmute your phone for the updates and set the sound to the maximum level.");
                volStatus.setBackgroundResource(R.drawable.no_sound);
            }

            return false;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                onBackPressed();
                break;

            case R.id.okBtn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(ManageVolumeActivity.this);
    }
}
