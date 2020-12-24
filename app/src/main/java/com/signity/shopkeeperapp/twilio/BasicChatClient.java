/*
package com.signity.shopkeeperapp.twilio;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.signity.shopkeeperapp.BuildConfig;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.StatusListener;
import com.twilio.chat.internal.HandlerUtil;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BasicChatClient extends CallbackListener<ChatClient> {

    private static final String TAG = "BasicChatClient";
    private String accessToken;
    private String fcmToken;

    private ChatClient chatClient;

    private Context context;

    private LoginListener loginListener;
    private Handler loginListenerHandler;

    public BasicChatClient(Context context) {
        super();
        this.context = context;

        if (BuildConfig.DEBUG) {
            ChatClient.setLogLevel(Log.DEBUG);
        }
    }

    public void setFCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
        if (chatClient != null) {
            setupFcmToken();
        }
    }

    public void login(LoginListener listener) {

        loginListenerHandler = HandlerUtil.setupListenerHandler();
        loginListener = listener;

        notifyLoginStarted();

        getToken();
    }

    private void getToken() {
        //TODO - STORE _ ID USED FOR TWILIO LOGIN
        String email = AppPreference.getInstance().getUserEmail();
        final String identity = !TextUtils.isEmpty(email) ? email : AppPreference.getInstance().getUserMobile();

        NetworkAdaper.twilioServer().getTwilioToken(identity, new Callback<String>() {
            @Override
            public void success(String response, Response response2) {
                try {
                    accessToken = response;
                    createClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                notifyLoginError("Error in fetching token");
            }
        });
    }

    private void notifyLoginError(final String s) {
        loginListenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (loginListener != null) {
                    loginListener.onLoginError(s);
                }
            }
        });
    }

    private void notifyLoginStarted() {
        loginListenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (loginListener != null) {
                    loginListener.onLoginStarted();
                }
            }
        });
    }

    public ChatClient getChatClient() {
        return chatClient;
    }

    private void setupFcmToken() {
        chatClient.registerFCMToken(new ChatClient.FCMToken(fcmToken),
                new StatusListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Firebase token setup successful " + fcmToken);
                        AppPreference.getInstance().setTwilioFcmToken(true);
                    }
                });
    }

    public void unregisterFcmToken() {
        chatClient.unregisterFCMToken(new ChatClient.FCMToken(fcmToken),
                new StatusListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Firebase token unregistered " + fcmToken);
                        AppPreference.getInstance().setTwilioFcmToken(false);
                    }
                });
    }

    private void createClient() {

        if (chatClient == null) {
            ChatClient.Properties props = new ChatClient.Properties.Builder()
                    .setRegion("us1")
                    .createProperties();

            ChatClient.create(context.getApplicationContext(), accessToken, props, this);
        } else {
            chatClient.updateToken(accessToken, new StatusListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Client Update Token was successful");
                    notifyLoginFinish();
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    super.onError(errorInfo);
                    Log.d(TAG, "Client Update Token failed");
                    notifyLoginError(errorInfo.getMessage());
                }
            });
        }
    }

    public void shutdown() {

        if (chatClient == null) {
            return;
        }

        chatClient.shutdown();
        chatClient = null; // Client no longer usable after shutdown()
    }

    // Client created, remember the reference and set up UI
    @Override
    public void onSuccess(ChatClient client) {
        chatClient = client;

        notifyLoginFinish();
    }

    private void notifyLoginFinish() {
        loginListenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (loginListener != null) {
                    loginListener.onLoginFinished();
                }
            }
        });
    }

    // Client not created, fail
    @Override
    public void onError(final ErrorInfo errorInfo) {
        notifyLoginError(errorInfo.toString());
    }

    public void onTokenExpired() {
        accessToken = null;
        if (chatClient != null) {
            Log.d(TAG, "onTokenExpired: ");
            getToken();
        }
    }

    public interface LoginListener {
        public void onLoginStarted();

        public void onLoginFinished();

        public void onLoginError(String errorMessage);

        public void onLogoutFinished();
    }
}
*/
