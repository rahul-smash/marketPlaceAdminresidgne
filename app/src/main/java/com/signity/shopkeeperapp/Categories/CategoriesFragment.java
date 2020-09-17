package com.signity.shopkeeperapp.Categories;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.Util;

/**
 * Created by root on 31/3/16.
 */
public class CategoriesFragment extends Fragment {

    View rootView;

    public static Fragment newInstance(Context context) {
        Bundle args = new Bundle();
        return Fragment.instantiate(context,
                CategoriesFragment.class.getName(), args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.category_fragment, container, false);


        initialization();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        return rootView;
    }

    private void initialization() {



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Util.checkIntenetConnection(getActivity())) {

        } else {
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
        }
    }


}
