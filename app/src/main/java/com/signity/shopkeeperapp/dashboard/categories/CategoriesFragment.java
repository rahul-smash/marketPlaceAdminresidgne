package com.signity.shopkeeperapp.dashboard.categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.dashboard.home.HomeFragment;

public class CategoriesFragment extends Fragment implements View.OnClickListener {
    private HomeFragment.HomeFragmentListener listener;

    public static final String TAG = "CategoriesFragment";
    private TextView txtAddCategory;


    public static CategoriesFragment getInstance(Bundle bundle) {
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DashboardActivity) {
            listener = (HomeFragment.HomeFragmentListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        Log.i("@@CategoriesFragment", "CategoriesFragment");
        txtAddCategory = (TextView) rootView.findViewById(R.id.txtAddCategory);
        txtAddCategory.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View view) {
        if (txtAddCategory == view) {
            Intent in = new Intent(getActivity(), AddCategories.class);
            startActivity(in);
        }
    }
}

