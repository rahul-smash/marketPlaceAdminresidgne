/*
package com.signity.shopkeeperapp.twilio.chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.MyApplication;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.twilio.BasicChatClient;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.ChannelListener;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ChatClientListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Member;
import com.twilio.chat.Members;
import com.twilio.chat.Message;
import com.twilio.chat.Messages;
import com.twilio.chat.StatusListener;
import com.twilio.chat.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * StaffActivity
 *
 * @blame Android Team
 *//*

public class CustomerSupportActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BasicChatClient.LoginListener, ChannelRecycleAdapter.ChannelListener, ChannelListener, ChatClientListener, MessageRecycleAdapter.MessageListener {
    private static final String TAG = CustomerSupportActivity.class.getSimpleName();
    private static final int REQUEST_READ_PERMISSION = 1002;
    private static final int REQUEST_WRITE_PERMISSION = 1003;
    private static final int CHECK_PERMISSION = 1004;
    private static final int REQUEST_IMAGE_GET = 1001;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayoutEmpty;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private EditText editText;
    private MessageRecycleAdapter messageRecycleAdapter;
    private Channel privateChannel;
    private ArrayList<MessageItem> messageItemList = new ArrayList<>();
    private BasicChatClient basicChatClient;
    private Message mediaMessage;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CustomerSupportActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        initViews();
        setUp();
    }

    protected void setUp() {
        setUpToolbar();
        setUpMessageAdapter();
        setUpSwipe();

        checkStoragePermission(CHECK_PERMISSION);

        basicChatClient = MyApplication.getInstance().getBasicChatClient();
        basicChatClient.login(this);
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.rv_customer_support);
        contentLoadingProgressBar = findViewById(R.id.pb_customer);
        editText = findViewById(R.id.editText);

        findViewById(R.id.ib_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (privateChannel != null) {
                    privateChannel.typing();
                }
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (messageRecycleAdapter.getItemCount() > 0)
                                recyclerView.smoothScrollToPosition(messageRecycleAdapter.getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpSwipe() {
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark
                ));
    }

    private void setUpMessageAdapter() {
        messageRecycleAdapter = new MessageRecycleAdapter(this);
        messageRecycleAdapter.setListener(this);
        recyclerView.setAdapter(messageRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customer_support, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_image:
                if (checkStoragePermission(REQUEST_READ_PERMISSION)) {
                    openPhoto();
                }
                break;
            case android.R.id.home:
                super.onBackPressed();
                hideKeyboard();
                return true;
        }
        return true;
    }

    private boolean checkStoragePermission(int requestCode) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_READ_PERMISSION:
                    openPhoto();
                    break;
                case REQUEST_WRITE_PERMISSION:
                    downloadMedia(mediaMessage);
                    break;
                case CHECK_PERMISSION:
                    break;
                default:
                    throw new RuntimeException("Invalid request code");
            }
        } else {
            Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {

            if (data == null) {
                return;
            }

            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

            Uri fullPhotoUri = data.getData();

            Intent intent = new Intent(CustomerSupportActivity.this, TwilioImageService.class);
            intent.putExtra(TwilioImageService.IMAGE_ACTION, TwilioImageService.ImageAction.UPLOAD);
            intent.putExtra(TwilioImageService.MEDIA_URI, fullPhotoUri.toString());
            intent.putExtra(TwilioImageService.CHANNEL, privateChannel);
            startService(intent);

        }
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        fetchAllMessages();
    }

    @Override
    public void onLoginStarted() {
        ProgressDialogUtil.showProgressDialog(this);
    }

    @Override
    public void onLoginFinished() {

        if (isDestroyed()) {
            return;
        }

        ProgressDialogUtil.hideProgressDialog();
        setFcmToken();
        basicChatClient.getChatClient().setListener(this);
        fetchJoinedPrivateChannel();
    }

    private void setFcmToken() {
        final String firebaseToken = AppPreference.getInstance().getDeviceToken();

        if (firebaseToken != null && !AppPreference.getInstance().isSetTwilioFcmToken()) {
            basicChatClient.setFCMToken(firebaseToken);
        }
    }

    private void fetchJoinedPrivateChannel() {
        contentLoadingProgressBar.show();

        if (basicChatClient.getChatClient() == null) {
            return;
        }
        String email = AppPreference.getInstance().getUserEmail();
        final String uniqueName = !TextUtils.isEmpty(email) ? email : AppPreference.getInstance().getUserMobile();

        basicChatClient.getChatClient()
                .getChannels()
                .getChannel(uniqueName, new CallbackListener<Channel>() {
                    @Override
                    public void onSuccess(Channel channel) {

                        if (isDestroyed()) {
                            return;
                        }

                        contentLoadingProgressBar.hide();
                        privateChannel = channel;
                        privateChannel.addListener(CustomerSupportActivity.this);
                        setNotification(Channel.NotificationLevel.MUTED);
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        super.onError(errorInfo);
                        contentLoadingProgressBar.hide();
                        Toast.makeText(CustomerSupportActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setNotification(final Channel.NotificationLevel notificationLevel) {
        privateChannel.setNotificationLevel(notificationLevel, new StatusListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Notification " + notificationLevel.name());
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
                Log.d(TAG, "onSuccess: Notification " + errorInfo.getMessage());
            }
        });
    }

    private void fetchAllMessages() {

        if (privateChannel == null) {
            fetchJoinedPrivateChannel();
            return;
        }

        Messages messagesObject = privateChannel.getMessages();
        if (messagesObject != null) {
            contentLoadingProgressBar.show();
            messagesObject.getLastMessages(100, new CallbackListener<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messagesArray) {

                    if (isDestroyed()) {
                        return;
                    }

                    contentLoadingProgressBar.hide();
                    messageItemList.clear();
                    Members members = privateChannel.getMembers();
                    if (messagesArray.size() > 0) {
                        for (int i = 0; i < messagesArray.size(); i++) {
                            messageItemList.add(new MessageItem(messagesArray.get(i), members));
                        }
                    }
                    messageRecycleAdapter.setMessageItems(messageItemList);
                    if (messageRecycleAdapter.getItemCount() > 0)
                        recyclerView.scrollToPosition(messageRecycleAdapter.getItemCount() - 1);
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    super.onError(errorInfo);
                    if (isDestroyed()) {
                        return;
                    }
                    contentLoadingProgressBar.hide();
                    Toast.makeText(CustomerSupportActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onLoginError(String errorMessage) {
        if (isDestroyed()) {
            return;
        }
        ProgressDialogUtil.hideProgressDialog();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogoutFinished() {

    }

    @Override
    public void onChannelClick(int position) {

    }

    @Override
    public void onMessageAdded(Message message) {
        fetchAllMessages();

        if (message.getType() == Message.Type.MEDIA) {
            mediaMessage = message;
            if (checkStoragePermission(REQUEST_WRITE_PERMISSION)) {
                downloadMedia(mediaMessage);
            }
        }
    }

    private void downloadMedia(Message message) {

        if (privateChannel == null) {
            return;
        }

        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();

        privateChannel.getMessages().getMessageByIndex(message.getMessageIndex(), new CallbackListener<Message>() {
            @Override
            public void onSuccess(Message message) {

                if (message.getMedia() != null) {

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(Util.getUserImage("Twilio", message.getSid()));

                        message.getMedia().download(fileOutputStream, new StatusListener() {
                            @Override
                            public void onSuccess() {

                                if (isDestroyed()) {
                                    return;
                                }

                                fetchAllMessages();
                            }
                        }, null);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onMessageUpdated(Message message, Message.UpdateReason updateReason) {

    }

    @Override
    public void onMessageDeleted(Message message) {

    }

    @Override
    public void onMemberAdded(Member member) {
        Toast.makeText(this, member.getIdentity() + " joined", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMemberUpdated(Member member, Member.UpdateReason updateReason) {

    }

    @Override
    public void onMemberDeleted(Member member) {

    }

    @Override
    public void onTypingStarted(Channel channel, Member member) {
        Toast.makeText(this, member.getIdentity() + " is typing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTypingEnded(Channel channel, Member member) {

    }

    @Override
    public void onSynchronizationChanged(Channel channel) {
        if (channel.getSynchronizationStatus() == Channel.SynchronizationStatus.ALL) {
            fetchAllMessages();
        }
    }

    public void sendMessage() {

        final String message = editText.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Invalid message", Toast.LENGTH_SHORT).show();
            return;
        }

        clearEditText();
        sendMessage(message);
    }

    private void clearEditText() {
        editText.setText("");
        hideKeyboard();
    }

    private void sendMessage(final String text) {

        if (privateChannel == null) {
            return;
        }

        final Messages messagesObject = privateChannel.getMessages();

        messagesObject.sendMessage(Message.options().withBody(text), new CallbackListener<Message>() {
            @Override
            public void onSuccess(Message message) {

                if (isDestroyed()) {
                    return;
                }

                fetchAllMessages();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (privateChannel != null) {
            privateChannel.removeListener(this);
            setNotification(Channel.NotificationLevel.DEFAULT);
        }
    }

    @Override
    public void onChannelJoined(Channel channel) {

    }

    @Override
    public void onChannelInvited(Channel channel) {

    }

    @Override
    public void onChannelAdded(Channel channel) {

    }

    @Override
    public void onChannelUpdated(Channel channel, Channel.UpdateReason updateReason) {

    }

    @Override
    public void onChannelDeleted(Channel channel) {

    }

    @Override
    public void onChannelSynchronizationChange(Channel channel) {
    }

    @Override
    public void onError(ErrorInfo errorInfo) {

    }

    @Override
    public void onUserUpdated(User user, User.UpdateReason updateReason) {

    }

    @Override
    public void onUserSubscribed(User user) {

    }

    @Override
    public void onUserUnsubscribed(User user) {

    }

    @Override
    public void onClientSynchronization(ChatClient.SynchronizationStatus synchronizationStatus) {
    }

    @Override
    public void onNewMessageNotification(String s, String s1, long l) {

    }

    @Override
    public void onAddedToChannelNotification(String s) {

    }

    @Override
    public void onInvitedToChannelNotification(String s) {

    }

    @Override
    public void onRemovedFromChannelNotification(String s) {

    }

    @Override
    public void onNotificationSubscribed() {

    }

    @Override
    public void onNotificationFailed(ErrorInfo errorInfo) {

    }

    @Override
    public void onConnectionStateChange(ChatClient.ConnectionState connectionState) {

    }

    @Override
    public void onTokenExpired() {
        basicChatClient.onTokenExpired();
    }

    @Override
    public void onTokenAboutToExpire() {
        basicChatClient.onTokenExpired();
    }

    @Override
    public void onLongPressMessage(final MessageItem messageItem) {

        MessageOptionDialog dialog = MessageOptionDialog.getInstance(null);
        dialog.setListener(new MessageOptionDialog.MessageOptionListener() {
            @Override
            public void onEdit() {
                editMessage(messageItem);
            }

            @Override
            public void onRemove() {
                removeMessage(messageItem);
            }
        });
        dialog.show(getSupportFragmentManager(), MessageOptionDialog.TAG);

    }

    @Override
    public void onDownloadMedia(MessageItem messageItem) {
        mediaMessage = messageItem.getMessage();

        if (checkStoragePermission(REQUEST_WRITE_PERMISSION)) {
            downloadMedia(mediaMessage);
        }
    }

    private void editMessage(final MessageItem messageItem) {

        if (privateChannel == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(EditMessageDialog.MESSAGE, messageItem.getMessage().getMessageBody());

        EditMessageDialog messageDialog = EditMessageDialog.getInstance(bundle);
        messageDialog.setCallback(new EditMessageDialog.Callback() {
            @Override
            public void onUpdate(String updateMessage) {
                messageItem.getMessage().updateMessageBody(updateMessage, new StatusListener() {
                    @Override
                    public void onSuccess() {
                        fetchAllMessages();
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        super.onError(errorInfo);
                        Toast.makeText(CustomerSupportActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        messageDialog.show(getSupportFragmentManager(), EditMessageDialog.TAG);

    }

    private void removeMessage(MessageItem messageItem) {

        if (privateChannel == null) {
            return;
        }

        privateChannel.getMessages().removeMessage(messageItem.getMessage(), new StatusListener() {
            @Override
            public void onSuccess() {
                fetchAllMessages();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                super.onError(errorInfo);
                Toast.makeText(CustomerSupportActivity.this, errorInfo.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/
