package com.signity.shopkeeperapp.LogInModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.login.MobileLoginActivity;

/**
 * Created by root on 25/4/16.
 */
public class LogInOptionsFragment extends Fragment implements View.OnClickListener {

    Button numberBtn, emailBtn;
    PrefManager prefManager;

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                LogInOptionsFragment.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_options, container, false);
        numberBtn = (Button) rootView.findViewById(R.id.numberBtn);
        emailBtn = (Button) rootView.findViewById(R.id.emailBtn);

        numberBtn.setOnClickListener(this);
        emailBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.numberBtn:
                prefManager.storeSharedValue(Constant.LOG_IN_TYPE, "mobile");
                Intent intent_home = new Intent(getActivity(),
                        MobileLoginActivity.class);
                startActivity(intent_home);
                AnimUtil.slideFromRightAnim(getActivity());
//                getActivity().finish();
                break;
            case R.id.emailBtn:
                prefManager.storeSharedValue(Constant.LOG_IN_TYPE, "email");
                Intent intent = new Intent(getActivity(),
                        MobileLoginActivity.class);
                intent.putExtra("type", "email");
                startActivity(intent);
                AnimUtil.slideFromRightAnim(getActivity());
//                getActivity().finish();
                break;
        }
    }
}
