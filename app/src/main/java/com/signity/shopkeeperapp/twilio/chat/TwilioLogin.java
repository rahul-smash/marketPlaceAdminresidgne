package com.signity.shopkeeperapp.twilio.chat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.signity.shopkeeperapp.app.MyApplication;
import com.signity.shopkeeperapp.twilio.BasicChatClient;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.twilio.chat.Attributes;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.StatusListener;

import org.json.JSONException;
import org.json.JSONObject;

public class TwilioLogin implements BasicChatClient.LoginListener {

    private static final String TAG = TwilioLogin.class.getSimpleName();
    private Context context;
    private BasicChatClient basicChatClient;

    public TwilioLogin(Context context) {
        this.context = context;
        basicChatClient = MyApplication.getInstance().getBasicChatClient();
    }

    public void performLoginCreatePrivateChannel() {
        basicChatClient.login(this);
    }

    @Override
    public void onLoginStarted() {
        Log.d(TAG, "onLoginStarted: ");
    }

    @Override
    public void onLoginFinished() {
        Log.d(TAG, "onLoginFinished: ");

        final String firebaseToken = AppPreference.getInstance().getDeviceToken();

        if (firebaseToken != null && !AppPreference.getInstance().isSetTwilioFcmToken()) {
            basicChatClient.setFCMToken(firebaseToken);
        }

        createNewPrivateChannel();
    }

    @Override
    public void onLoginError(String errorMessage) {
        Log.d(TAG, "onLoginError: " + errorMessage);
    }

    @Override
    public void onLogoutFinished() {
        Log.d(TAG, "onLogoutFinished: ");
    }

    private void createNewPrivateChannel() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", AppPreference.getInstance().getUserName());
            jsonObject.put("userNumber", AppPreference.getInstance().getUserMobile());
            jsonObject.put("userEmail", AppPreference.getInstance().getUserEmail());
            jsonObject.put("storeName", AppPreference.getInstance().getStoreName());
            jsonObject.put("storeId", AppPreference.getInstance().getStoreId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String email = AppPreference.getInstance().getUserEmail();
        final String uniqueName = !TextUtils.isEmpty(email) ? email : AppPreference.getInstance().getUserMobile();

        basicChatClient.getChatClient().getChannels().channelBuilder()
                .withFriendlyName(AppPreference.getInstance().getStoreName())
                .withUniqueName(uniqueName)
                .withAttributes(new Attributes(jsonObject))
                .withType(Channel.ChannelType.PRIVATE)
                .build(new CallbackListener<Channel>() {
                    @Override
                    public void onSuccess(Channel channel) {
                        AppPreference.getInstance().setPrivateChannelCreated(true);
                        joinPrivateChannel(channel);
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        super.onError(errorInfo);
                        if (errorInfo.getCode() == 50307) {
                            AppPreference.getInstance().setPrivateChannelCreated(true);
                        }
                    }
                });
    }

    private void joinPrivateChannel(final Channel channel) {
        channel.join(new StatusListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Channel Joined");
                AppPreference.getInstance().setPrivateChannelJoined(true);
                addCustomerExecutive(channel);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
                Log.d(TAG, "onError: " + errorInfo.getMessage());
            }
        });
    }

    private void addCustomerExecutive(Channel channel) {
        // TODO - Customer_Support id
        channel.getMembers().addByIdentity("Customer_Support", new StatusListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Customer Support Added.");
            }
        });
    }
}
