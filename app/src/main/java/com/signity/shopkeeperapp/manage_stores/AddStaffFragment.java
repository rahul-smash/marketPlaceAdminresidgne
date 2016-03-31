package com.signity.shopkeeperapp.manage_stores;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.GetStaffDataModel;
import com.signity.shopkeeperapp.model.GetStaffResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 23/2/16.
 */
public class AddStaffFragment extends Fragment implements View.OnClickListener {

    private GetStaffDataModel getStaffDataModel;

    // Views
    private EditText editTextFullName, editTextNumber;
    private Button buttonBack, buttonSave, buttonDelete;
    private RelativeLayout relativeStatus;

    TextView textViewTitle;
    boolean isEdit;

    String status = "active";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        getStaffDataModel = (GetStaffDataModel) bundle.getSerializable("data");
        if (getStaffDataModel != null) {
            isEdit = true;
        } else {
            isEdit = false;
        }

    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                AddStaffFragment.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_add_staff, container, false);
        relativeStatus = (RelativeLayout) rootView.findViewById(R.id.relativeStatus);

        buttonBack = (Button) rootView.findViewById(R.id.backButton);
        buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
        buttonDelete = (Button) rootView.findViewById(R.id.buttonDelete);
        editTextFullName = (EditText) rootView.findViewById(R.id.editTextFullName);
        editTextNumber = (EditText) rootView.findViewById(R.id.editTextNumber);
        textViewTitle = (TextView) rootView.findViewById(R.id.textTitle);
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radioActive:
                        status = "active";
                        break;
                    case R.id.radioInActive:
                        status = "inactive";
                        break;

                }
            }
        });

        if (isEdit) {
            textViewTitle.setText("Edit Staff");
            relativeStatus.setVisibility(View.VISIBLE);
            setViewForEdit(rootView);
        } else {
            textViewTitle.setText("Add Staff");
            relativeStatus.setVisibility(View.GONE);
            status = "active";
            buttonDelete.setText("Cancel");
        }
        buttonBack.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        return rootView;
    }

    private void setViewForEdit(View view) {

        editTextFullName.setText(getStaffDataModel.getFullName() != null ? getStaffDataModel.getFullName() : "");
        editTextNumber.setText(getStaffDataModel.getPhone() != null ? getStaffDataModel.getPhone() : "");

        if (getStaffDataModel.getStatus() != null && getStaffDataModel.getStatus().equals("1")) {
            status = "active";
            ((RadioButton) view.findViewById(R.id.radioActive)).setChecked(true);
        } else {
            status = "inactive";
            ((RadioButton) view.findViewById(R.id.radioInActive)).setChecked(true);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                getActivity().onBackPressed();
                break;
            case R.id.buttonSave:
                onSaveButtonClickListener();
                break;
            case R.id.buttonDelete:
                onDeleteButtonListener();
                break;
        }

    }

    private void onDeleteButtonListener() {
        if (!isEdit) {
            getActivity().onBackPressed();
        } else {
            final DialogHandler dialogHandler = new DialogHandler(getActivity());
            dialogHandler.setDialog("Confirmation", "Are you sure to delete this staff?");
            dialogHandler.setNegativeButton("No", true).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogHandler.dismiss();
                }
            });
            dialogHandler.setPostiveButton("Yes", true).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogHandler.dismiss();
                    deleteStaff();
                }
            });


        }


    }

    private void deleteStaff() {
        ProgressDialogUtil.showProgressDialog(getActivity());

        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
        String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        String platform = Constant.PLATFORM;

        String storeId = Util.loadPreferenceValue(getActivity(), Constant.STORE_ID);
        String action = "delete";
        Map<String, String> requestParamAdd = new HashMap<String, String>();
        requestParamAdd.put("device_id", deviceId);
        requestParamAdd.put("device_token", deviceToken);
        requestParamAdd.put("platform", Constant.PLATFORM);
        requestParamAdd.put("action", action);
        requestParamAdd.put("user_id", getStaffDataModel.getId());
        requestParamAdd.put("store_id", storeId);

        NetworkAdaper.getInstance().getNetworkServices().getstoreStaff(requestParamAdd, new Callback<GetStaffResponse>() {
            @Override
            public void success(GetStaffResponse getStaffResponse, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (getStaffResponse.getSuccess() != null ? getStaffResponse.getSuccess() : false) {
                    final DialogHandler dialogHandler = new DialogHandler(getActivity());
                    dialogHandler.setDialog("Success", getStaffResponse.getMessage());
                    dialogHandler.setPostiveButton("Ok", true).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogHandler.dismiss();
                            getActivity().onBackPressed();
                        }
                    });
                } else {
                    final DialogHandler dialogHandler = new DialogHandler(getActivity());
                    dialogHandler.setDialog("Failed", getStaffResponse.getMessage());
                    dialogHandler.setPostiveButton("Ok", true).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogHandler.dismiss();
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error", error.getMessage());
                final DialogHandler dialogHandler = new DialogHandler(getActivity());
                dialogHandler.setDialog("Error", "Unable to Connect Server");
                dialogHandler.setPostiveButton("Ok", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogHandler.dismiss();
                    }
                });
            }
        });

    }

    private void onSaveButtonClickListener() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        String name = editTextFullName.getText().toString();
        if (name.isEmpty()) {
            editTextFullName.setError("Enter Staff Name");
            return;
        }
        String phone = editTextNumber.getText().toString();
        if (name.isEmpty()) {
            editTextNumber.setError("Enter Staff Number");
            return;
        }

        String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //String deviceToken = pushClientManager.getRegistrationId(getActivity());
        String deviceToken = Util.loadPreferenceValue(getActivity(), Constant.DEVICE_TOKEN);
        String platform = Constant.PLATFORM;

        String storeId = Util.loadPreferenceValue(getActivity(), Constant.STORE_ID);
        String action = "";
        Map<String, String> requestParamAdd = new HashMap<String, String>();
        requestParamAdd.put("device_id", deviceId);
        requestParamAdd.put("device_token", deviceToken);
        requestParamAdd.put("platform", platform);
        requestParamAdd.put("mobile", phone);
        requestParamAdd.put("name", name);
        requestParamAdd.put("status", status);
        requestParamAdd.put("store_id", storeId);
        if (isEdit) {
            action = "edit";
        } else {
            action = "add";
        }
        requestParamAdd.put("action", action);

        if (isEdit) {
            requestParamAdd.put("user_id", getStaffDataModel.getId());
        }

        NetworkAdaper.getInstance().getNetworkServices().getstoreStaff(requestParamAdd, new Callback<GetStaffResponse>() {
            @Override
            public void success(GetStaffResponse getStaffResponse, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (getStaffResponse.getSuccess() != null ? getStaffResponse.getSuccess() : false) {
                    final DialogHandler dialogHandler = new DialogHandler(getActivity());
                    dialogHandler.setDialog("Success", getStaffResponse.getMessage());
                    dialogHandler.setPostiveButton("Ok", true).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogHandler.dismiss();
                            getActivity().onBackPressed();
                        }
                    });
                } else {
                    final DialogHandler dialogHandler = new DialogHandler(getActivity());
                    dialogHandler.setDialog("Failed", getStaffResponse.getMessage());
                    dialogHandler.setPostiveButton("Ok", true).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogHandler.dismiss();
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error", error.getMessage());
                final DialogHandler dialogHandler = new DialogHandler(getActivity());
                dialogHandler.setDialog("Error", "Unable to Connect Server");
                dialogHandler.setPostiveButton("Ok", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogHandler.dismiss();
                    }
                });
            }
        });


    }

}
