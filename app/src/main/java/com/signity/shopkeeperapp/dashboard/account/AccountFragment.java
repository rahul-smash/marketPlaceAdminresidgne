package com.signity.shopkeeperapp.dashboard.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

public class AccountFragment extends Fragment {

    public static final String TAG = "AccountFragment";
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextNumber;

    public static AccountFragment getInstance(Bundle bundle) {
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        textInputEditTextName = view.findViewById(R.id.edt_user_name);
        textInputEditTextEmail = view.findViewById(R.id.edt_user_email);
        textInputEditTextNumber = view.findViewById(R.id.edt_user_number);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpData();
    }

    private void setUpData() {
        textInputEditTextName.setText(AppPreference.getInstance().getUserName());
        textInputEditTextEmail.setText(AppPreference.getInstance().getStoreEmail());
        textInputEditTextNumber.setText(AppPreference.getInstance().getStoreMobile());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
