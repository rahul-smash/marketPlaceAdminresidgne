package com.signity.shopkeeperapp.LogInModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.gcm.GCMClientManager;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.LoginModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.network.NetworkConstant;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogInWithEmailActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnNext, backButton,signUpBtn,forgotPassBtn;
    EditText edtPass,edtEmail;
    private GCMClientManager pushClientManager;
    String from;

    String id;
    boolean isEmailExist = false, isNameExist = false;

    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_with_email);
        prefManager = new PrefManager(LogInWithEmailActivity.this);
        btnNext = (Button) findViewById(R.id.btnNext);
        signUpBtn=(Button)findViewById(R.id.signUpBtn);
        forgotPassBtn=(Button)findViewById(R.id.forgotPassBtn);
        edtEmail=(EditText)findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
        backButton = (Button) findViewById(R.id.backButton);
        btnNext.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        forgotPassBtn.setOnClickListener(this);
        addActionDoneEvet(edtPass);
    }

    private void addActionDoneEvet(EditText edtPhone) {

        edtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnNext.performClick();
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnNext:
                if(edtEmail.getText().toString().isEmpty() && edtPass.getText().toString().isEmpty()){
                    Toast.makeText(LogInWithEmailActivity.this,"Email and password are required to continue.",Toast.LENGTH_SHORT).show();
//                    textLayout1.setError("You need to enter your email");
//                    textLayout2.setError("You need to enter your password");
                }
                else {

                    if(edtPass.getText().toString().isEmpty()){
                        Toast.makeText(LogInWithEmailActivity.this,"Your password is required .",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(checkValidEmail(edtEmail.getText().toString())){
                            callNetworkServiceForLogin();
                        }
                        else {
                            Toast.makeText(LogInWithEmailActivity.this,"Your email is required and must be in a valid email format.",Toast.LENGTH_SHORT).show();
                        }

                    }



                }
                break;

            case R.id.backButton:
                onBackPressed();
                break;

            case R.id.forgotPassBtn:

                final Dialog dialog = new Dialog(LogInWithEmailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.fragment_forgot);

                Button cancelBtn=(Button)dialog.findViewById(R.id.cancelBtn);
                Button okBtn=(Button)dialog.findViewById(R.id.okBtn);
                final EditText getEmail=(EditText)dialog.findViewById(R.id.getEmail);


                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(getEmail.getText().toString().trim().isEmpty()){
                            Toast.makeText(LogInWithEmailActivity.this, "Please enter an email id.", Toast.LENGTH_SHORT).show();
                        }else if(checkValidEmail(getEmail.getText().toString().trim())){
                            callNetworkForForgotPassword(getEmail.getText().toString().trim());
                        }else {
                            Toast.makeText(LogInWithEmailActivity.this,"Please enter valid email id.",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
        }
    }


    private void callNetworkServiceForLogin() {
        ProgressDialogUtil.showProgressDialog(LogInWithEmailActivity.this);

        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();
        Map<String, String> param = new HashMap<String, String>();
        param.put("email", email);
        param.put("password", pass);
        NetworkAdaper.getInstance().getNetworkServices().loginVerification(param, new Callback<LoginModel>() {


            @Override
            public void success(LoginModel loginModel, Response response) {

                if (loginModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if(loginModel.getData()!=null){
                        String storeId=loginModel.getData().getStoreId();
                        String userId=loginModel.getData().getId();
                        prefManager.storeSharedValue(Constant.USER_ID,userId);
                        setStoreId(storeId);
                    }


                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogHandler dialogHandler = new DialogHandler(LogInWithEmailActivity.this);
                    dialogHandler.setdialogForFinish("Error",""+loginModel.getMessage(),false);
                }


            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }


    private void setStoreId(String id) {

        Util.savePreferenceValue(LogInWithEmailActivity.this, Constant.STORE_ID, id);
        String url = NetworkConstant.BASE + "/" + id + NetworkConstant.APISTORE;
        NetworkAdaper.setupRetrofitClient(url);

        proceedFutherForHomeScreen();
    }


    private void proceedFutherForHomeScreen() {

        saveUserIdToPref();

        Intent intent_home = new Intent(LogInWithEmailActivity.this, MainActivity.class);
        intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_home);

        this.finish();
        AnimUtil.slideFromRightAnim(LogInWithEmailActivity.this);
    }

    private void saveUserIdToPref() {

        Util.savePreferenceValue(LogInWithEmailActivity.this, Constant.LOGIN_CHECK, "1");
    }

    private void callNetworkForForgotPassword(String email) {

        ProgressDialogUtil.showProgressDialog(LogInWithEmailActivity.this);

        Map<String, String> param = new HashMap<String, String>();
        param.put("email_id", email);
        NetworkAdaper.getInstance().getNetworkServices().forgetPassword(param, new Callback<LoginModel>() {


            @Override
            public void success(LoginModel loginModel, Response response) {

                if (loginModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogHandler dialogHandler = new DialogHandler(LogInWithEmailActivity.this);
                    dialogHandler.setdialogForFinish("Success", "" + loginModel.getMessage(), false);
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogHandler dialogHandler = new DialogHandler(LogInWithEmailActivity.this);
                    dialogHandler.setdialogForFinish("Error", "" + loginModel.getMessage(), false);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });

    }

    public boolean checkValidEmail(String email) {
        boolean isValid = false;
        String PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(email);
        isValid = matcher.matches();
        return isValid;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(LogInWithEmailActivity.this);
    }
}
