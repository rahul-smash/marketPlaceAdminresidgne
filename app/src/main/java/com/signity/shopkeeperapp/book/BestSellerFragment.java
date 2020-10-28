package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;

public class BestSellerFragment extends Fragment {

    public static final String TAG = "BestSellerFragment";
    private RecyclerView recyclerView;
    private BestSellerAdapter bestSellerAdapter;

    public static BestSellerFragment getInstance(Bundle bundle) {
        BestSellerFragment fragment = new BestSellerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        bestSellerAdapter = new BestSellerAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bestSellerAdapter);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_best_seller);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_best_seller, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
