package com.signity.shopkeeperapp.LogInModule;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.LoginModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {


    EditText oldPass, newPass, confirmPass;
    Button btnSave, backButton;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        prefManager = new PrefManager(ChangePasswordActivity.this);
        oldPass = (EditText) findViewById(R.id.oldPass);
        oldPass.setTransformationMethod(new PasswordTransformationMethod());
        newPass = (EditText) findViewById(R.id.newPass);
        confirmPass = (EditText) findViewById(R.id.confirmPass);
        btnSave = (Button) findViewById(R.id.btnSave);
        backButton = (Button) findViewById(R.id.backButton);
        btnSave.setOnClickListener(this);
        backButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (!oldPass.getText().toString().isEmpty() && !newPass.getText().toString().isEmpty()
                        && !confirmPass.getText().toString().isEmpty()) {
                    callNetwrokForChangePassword();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backButton:
                onBackPressed();
                break;
        }
    }

    private void callNetwrokForChangePassword() {

        ProgressDialogUtil.showProgressDialog(ChangePasswordActivity.this);
        String old_pass = oldPass.getText().toString().trim();
        String new_pass = newPass.getText().toString().trim();
        String confirm_pass = confirmPass.getText().toString().trim();
        String userId = prefManager.getSharedValue(Constant.STAFF_ADMIN_ID);
        Map<String, String> param = new HashMap<String, String>();
        param.put("old_password", old_pass);
        param.put("new_password", new_pass);
        param.put("confirm_password", confirm_pass);
        param.put("user_id", userId);

        NetworkAdaper.getInstance().getNetworkServices().changePassword(param, new Callback<LoginModel>() {
            @Override
            public void success(LoginModel loginModel, Response response) {
                if (loginModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    oldPass.setText("");
                    newPass.setText("");
                    confirmPass.setText("");
                    DialogUtils.showAlertDialog(ChangePasswordActivity.this, Constant.APP_TITLE, "" + loginModel.getMessage());
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogUtils.showAlertDialog(ChangePasswordActivity.this, Constant.APP_TITLE, "" + loginModel.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(ChangePasswordActivity.this, Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(ChangePasswordActivity.this);
    }
}
