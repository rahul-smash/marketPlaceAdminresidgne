package com.signity.shopkeeperapp.classes;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Created by Ketan Tetry on 7/1/20.
 */
public class FacebookManager {

    private static final String ME_ACCOUNT = "/me/accounts";
    private CallbackManager callbackManager;
    private Activity activity;

    public FacebookManager(Activity activity) {
        this.activity = activity;
    }

    public static void uploadPhoto(String message, @Nullable Bundle bundle, File file, GraphRequest.Callback callback) throws FileNotFoundException {
        String path = String.format("%s/photos", AppPreference.getInstance().getFacebookPageId());
        GraphRequest.newUploadPhotoRequest(getPageAccessToken(), path, file, message, bundle, callback).executeAsync();
    }

    @NonNull
    private static AccessToken getPageAccessToken() {
        return new AccessToken(AppPreference.getInstance().getFacebookPageAccessToken(),
                AccessToken.getCurrentAccessToken().getApplicationId(),
                AccessToken.getCurrentAccessToken().getUserId(),
                Arrays.asList("pages_show_list,pages_manage_posts,pages_read_engagement,pages_read_user_content"),
                null,
                null,
                AccessTokenSource.NONE,
                null,
                null,
                null);
    }

    public boolean isFacebookLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public AccessToken getFacebookAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public void performFacebookLogin(FacebookCallback<LoginResult> callback) {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callback);
        LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList("manage_pages", "publish_pages"));
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void getFacebookPages(GraphRequest.Callback callback) {
        GraphRequest graphRequest = new GraphRequest();
        graphRequest.setGraphPath(ME_ACCOUNT);
        graphRequest.setHttpMethod(HttpMethod.GET);
        graphRequest.setAccessToken(getFacebookAccessToken());
        graphRequest.setCallback(callback);
        graphRequest.executeAsync();
    }

}
