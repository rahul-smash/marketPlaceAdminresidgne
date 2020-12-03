package com.signity.shopkeeperapp.runner;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.runner.CommonResponse;
import com.signity.shopkeeperapp.model.runner.RunnersResponseDTO;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.ACTION_DIAL;

public class RunnerActivity extends BaseActivity implements RunnerAdapter.RunnerAdapterListener {

    private static final String TAG = "RunnerActivity";
    private Toolbar toolbar;
    private RunnerAdapter runnerAdapter;
    private RecyclerView recyclerView;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, RunnerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner);
        initViews();
        setUpToolbar();
        setUpAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRunners();
    }

    public void getRunners() {
        NetworkAdaper.getNetworkServices().getRunners(new Callback<RunnersResponseDTO>() {
            @Override
            public void success(RunnersResponseDTO responseDTO, Response response) {

                if (isDestroyed()) {
                    return;
                }

                if (responseDTO.isSuccess()) {
                    if (responseDTO.getData() != null) {
                        runnerAdapter.setRunnerList(responseDTO.getData());
                    } else {
                        Toast.makeText(RunnerActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                        runnerAdapter.setShowLoading(false);
                    }

                } else {
                    Toast.makeText(RunnerActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                    runnerAdapter.setShowLoading(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    Toast.makeText(RunnerActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
                    runnerAdapter.setShowLoading(false);
                }
            }
        });
    }

    private void setUpAdapter() {
        runnerAdapter = new RunnerAdapter(this);
        runnerAdapter.setListener(this);
        recyclerView.setAdapter(runnerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 12)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_runner);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void runAnimation() {
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        runAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_runner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            runAnimation();
        }

        if (item.getItemId() == R.id.action_add_runner) {
            startActivity(AddRunnerActivity.getStartIntent(this));
            AnimUtil.slideFromRightAnim(this);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClickSwitch(String id, String status) {
        // TODO - Enable Disable Runner
        Toast.makeText(this, "Change Status", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteRunner(String id, boolean canDelete) {

        if (!canDelete) {
            Toast.makeText(this, "Can't Delete", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialogUtil.showProgressDialog(this);

        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        NetworkAdaper.getNetworkServices().deleteRunner(params, new Callback<CommonResponse>() {
            @Override
            public void success(CommonResponse res, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (res.isSuccess()) {
                    getRunners();
                }
                Toast.makeText(RunnerActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    ProgressDialogUtil.hideProgressDialog();
                }
            }
        });

    }

    @Override
    public void onEditRunner(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(AddRunnerActivity.RUNNER_ID, id);
        startActivity(AddRunnerActivity.getStartIntent(this, bundle));
        AnimUtil.slideFromRightAnim(this);
    }

    @Override
    public void onViewRunnerDetail(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(RunnerDetailActivity.RUNNER_ID, id);
        startActivity(RunnerDetailActivity.getStartIntent(this, bundle));
        AnimUtil.slideFromRightAnim(this);
    }

    @Override
    public void onOpenWhatsapp(String phone) {

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        openWhatsapp(phone);
    }

    @Override
    public void onOpenCall(String phone) {
        if (TextUtils.isEmpty(phone)) {
            DialogUtils.showAlertDialog(this, Constant.APP_TITLE, "Sorry! phone number is not available.");
        } else {
            callAlert(phone);
        }
    }

    @Override
    public void onOpenMessage(String phone) {

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse(String.format("smsto:%s", phone));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(intent);
    }

    private void callAlert(final String phone) {
        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(this);
        adb.setTitle("Call " + phone + " ?");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                actionCall(phone);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void actionCall(String phone) {
        try {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
                AnimUtil.slideFromRightAnim(this);
            } else {
                Toast.makeText(this, "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openWhatsapp(String phone) {
        try {
            boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
            if (isWhatsappInstalled) {

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                String data = String.format("%s%s@s.whatsapp.net", AppPreference.getInstance().getPhoneCode(), phone);
                sendIntent.putExtra("jid", data);//phone number without "+" prefix
                startActivity(sendIntent);
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean appInstalled = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }
}
