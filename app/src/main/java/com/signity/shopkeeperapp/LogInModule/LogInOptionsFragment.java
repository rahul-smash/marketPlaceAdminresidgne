package com.signity.shopkeeperapp.LogInModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.view.LoginScreenActivity;

/**
 * Created by root on 25/4/16.
 */
public class LogInOptionsFragment extends Fragment implements View.OnClickListener {

    Button numberBtn,emailBtn;


    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                LogInOptionsFragment.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //     pushClientManager = new GCMClientManager(getActivity(), Constant.PROJECT_NUMBER);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_options, container, false);
        numberBtn=(Button)rootView.findViewById(R.id.numberBtn);
        emailBtn=(Button) rootView.findViewById(R.id.emailBtn);


        numberBtn.setOnClickListener(this);
        emailBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.numberBtn:
                Intent intent_home = new Intent(getActivity(),
                        LoginScreenActivity.class);
                startActivity(intent_home);
                AnimUtil.slideFromRightAnim(getActivity());
                getActivity().finish();
                break;

            case R.id.emailBtn:

                Intent intent = new Intent(getActivity(),
                        LogInWithEmailActivity.class);
                startActivity(intent);
                AnimUtil.slideFromRightAnim(getActivity());
                getActivity().finish();

                break;

        }
    }
}
