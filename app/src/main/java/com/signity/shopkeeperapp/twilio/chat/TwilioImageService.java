/*
package com.signity.shopkeeperapp.twilio.chat;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.signity.shopkeeperapp.util.FileUtils;
import com.signity.shopkeeperapp.util.Util;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Message;
import com.twilio.chat.StatusListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;

public class TwilioImageService extends IntentService {

    public final static String IMAGE_ACTION = "IMAGE_ACTION";
    public final static String CHANNEL = "CHANNEL";
    public final static String MEDIA_URI = "MEDIA_URI";
    public final static String MESSAGE_INDEX = "MESSAGE_INDEX";
    private static final String TAG = "TwilioImageService";

    public TwilioImageService() {
        super(TwilioImageService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ImageAction imageAction = (ImageAction) intent.getSerializableExtra(IMAGE_ACTION);

            if (imageAction != null) {
                switch (imageAction) {
                    case UPLOAD:
                        uploadService(intent);
                        break;
                    case DOWNLOAD:
                        downloadService(intent);
                        break;
                    default:
                        throw new RuntimeException("Invalid Action");
                }
            }

        }
    }

    private void downloadService(Intent intent) {
        final Channel channel = intent.getParcelableExtra(CHANNEL);
        final long messageIndex = intent.getLongExtra(MESSAGE_INDEX, -1L);

        if (channel == null) {
            return;
        }

        channel.getMessages().getMessageByIndex(messageIndex, new CallbackListener<Message>() {
            @Override
            public void onSuccess(final Message message) {

                if (message.getMedia() != null) {

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(Util.getUserImage("Twilio", message.getSid()));

                        message.getMedia().download(fileOutputStream, new StatusListener() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "onSuccess: download: " + message.getSid());
                                LocalBroadcastManager.getInstance(TwilioImageService.this).sendBroadcast(new Intent("download"));
                            }
                        }, null);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void uploadService(Intent intent) {

        final String imageStringUri = intent.getStringExtra(MEDIA_URI);
        final Channel channel = intent.getParcelableExtra(CHANNEL);

        Uri imageUri = Uri.parse(imageStringUri);

        File file = FileUtils.getFile(this, imageUri);

        try {
            Bitmap bitmap = Util.decodeBitmap(file, 500, 500);
            if (bitmap != null) {
                file = Util.saveImageFile(bitmap, "TwilioImage", getExternalFilesDir("Digi"), 90);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file == null) {
            return;
        }

        if (channel == null) {
            return;
        }

        try {

            FileInputStream fileInputStream = new FileInputStream(file);
            String imageName = String.format(Locale.getDefault(), "TwilioImage_%d", new Date().getTime());

            Message.Options options = Message.options()
                    .withMediaFileName(imageName)
                    .withMedia(fileInputStream, FileUtils.getMimeType(file));

            channel.getMessages().sendMessage(options, new CallbackListener<Message>() {
                @Override
                public void onSuccess(Message message) {


                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    super.onError(errorInfo);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public enum ImageAction {
        UPLOAD, DOWNLOAD
    }

    public static File saveImageFile(Bitmap bitmap, String filename, File storage, int quality) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bytes);
        File file = new File(storage, filename.concat(".jpg"));
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
*/
