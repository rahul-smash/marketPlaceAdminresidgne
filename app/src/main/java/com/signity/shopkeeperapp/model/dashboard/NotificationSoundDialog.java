package com.signity.shopkeeperapp.model.dashboard;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.Locale;

/**
 * FeedbackDialog
 *
 * @blame Ketan Tetry
 */
public class NotificationSoundDialog extends BaseDialogFragment {

    public static final String TAG = "NotificationSoundDialog";
    private static final int SOUND_REQUEST = 987;

    private TextView textViewSoundName;
    private AppCompatSeekBar seekbar;
    private AudioManager audioManager;
    private OnCloseListener listener;

    public static NotificationSoundDialog getInstance(@Nullable Bundle bundle) {
        NotificationSoundDialog dialog = new NotificationSoundDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setListener(OnCloseListener listener) {
        this.listener = listener;
    }

    private void onClickClose() {
        if (listener != null) {
            listener.onClose();
        }
        dismiss();
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_notification_sound;
    }

    @Override
    protected void setUp(View view) {
        initView(view);
        setUp();
        checkVolStatus();
    }

    private void setUp() {
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (AppPreference.getInstance().getNotificationRing() != null) {
            defaultSoundUri = Uri.parse(AppPreference.getInstance().getNotificationRing());
        }
        setSoundName(defaultSoundUri);
    }

    private void setSoundName(Uri defaultSoundUri) {
        Ringtone ringtone = RingtoneManager.getRingtone(getContext(), defaultSoundUri);
        String ringName = ringtone.getTitle(getContext());
        textViewSoundName.setText(ringName);
    }

    private void initView(View view) {

        textViewSoundName = view.findViewById(R.id.tv_sound_name);
        seekbar = view.findViewById(R.id.seekBar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_RING, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        view.findViewById(R.id.iv_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickClose();
            }
        });

        view.findViewById(R.id.ll_change_sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSound();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SOUND_REQUEST) {

            if (data == null) {
                return;
            }

            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                setSoundName(uri);
                AppPreference.getInstance().setNotificationRing(uri.toString());
                NotificationManager notifManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                String DEFAULCHANNEL = String.format(Locale.getDefault(), "%s%d", Constant.NOTIFICATION_CHANNEL, AppPreference.getInstance().getChannelId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notifManager.deleteNotificationChannel(DEFAULCHANNEL);
                }
                AppPreference.getInstance().setChannelId(AppPreference.getInstance().getChannelId() + 1);
            }
        }
    }

    private void changeSound() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Notification Sound");
        if (AppPreference.getInstance().getNotificationRing() != null) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(AppPreference.getInstance().getNotificationRing()));
        }
        startActivityForResult(intent, SOUND_REQUEST);
    }

    private void checkVolStatus() {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);

        seekbar.setProgress(currentVolume);

        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
            case AudioManager.RINGER_MODE_VIBRATE:
                Toast.makeText(getContext(), "Kindly Unmute your phone for the updates and set the sound to the maximum level." + currentVolume, Toast.LENGTH_SHORT).show();
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                if (currentVolume < 7)
                    Toast.makeText(getContext(), "Kindly set the volume to the maximum level to receive the Order Alerts." + currentVolume, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public interface OnCloseListener {
        void onClose();
    }
}