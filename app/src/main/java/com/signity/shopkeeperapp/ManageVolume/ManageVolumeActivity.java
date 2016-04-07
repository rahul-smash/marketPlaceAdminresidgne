package com.signity.shopkeeperapp.ManageVolume;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.PrefManager;

public class ManageVolumeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView volStatus;
    ImageButton btnBack;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_volume);
        prefManager=new PrefManager(ManageVolumeActivity.this);
        volStatus=(ImageView)findViewById(R.id.volStatus);
        btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkVolStatus();
    }

    private void checkVolStatus() {
        if(prefManager.getSharedValue(Constant.VOLUME_STATUS).equalsIgnoreCase("ring")){
            volStatus.setBackgroundResource(R.drawable.sound);
        }else {
            volStatus.setBackgroundResource(R.drawable.no_sound);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            volStatus.setBackgroundResource(R.drawable.sound);
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            volStatus.setBackgroundResource(R.drawable.no_sound);
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
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(ManageVolumeActivity.this);
    }
}
