package com.signity.shopkeeperapp.manage_stores;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.GetStaffDataModel;

/**
 * Created by rajesh on 23/2/16.
 */
public class AddStaffFragment extends Fragment implements View.OnClickListener {

    private GetStaffDataModel getStaffDataModel;
    Button buttonBack;
    TextView textViewTitle;
    boolean isEdit;

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
        buttonBack = (Button) rootView.findViewById(R.id.backButton);
        textViewTitle = (TextView) rootView.findViewById(R.id.textTitle);
        if (isEdit) {
            textViewTitle.setText("Edit Staff");
        } else {
            textViewTitle.setText("Add Staff");
        }

        buttonBack.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                getActivity().onBackPressed();
                break;
        }

    }
}
