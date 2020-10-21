package com.signity.shopkeeperapp.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.signity.shopkeeperapp.R;

/**
 * Created by Rajesh on 12/10/15.
 * Updated by ketan on 16/09/20.
 */
public class LoginChooserFragment extends Fragment {

    public static final String TAG = "LoginChooserFragment";

    private Button buttonMobile, buttonEmail;
    private LoginChooserListener listener;

    public static LoginChooserFragment getInstance(Bundle bundle) {
        LoginChooserFragment fragment = new LoginChooserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        buttonMobile = view.findViewById(R.id.btn_mobile);
        buttonEmail = view.findViewById(R.id.btn_email);

        buttonMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickMobile();
                }
            }
        });

        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickEmail();
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginChooserListener) {
            listener = (LoginChooserListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface LoginChooserListener {
        void onClickMobile();

        void onClickEmail();
    }

}
