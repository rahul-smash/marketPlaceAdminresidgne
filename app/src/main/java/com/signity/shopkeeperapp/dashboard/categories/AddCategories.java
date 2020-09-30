package com.signity.shopkeeperapp.dashboard.categories;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.signity.shopkeeperapp.R;

import com.signity.shopkeeperapp.util.AnimUtil;

import com.signity.shopkeeperapp.util.PrefManager;


public class AddCategories extends AppCompatActivity implements View.OnClickListener {


    private PrefManager prefManager;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category);
        initview();
        setUpToolbar();
        prefManager = new PrefManager(getApplicationContext());

    }


    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                AnimUtil.slideFromLeftAnim(AddCategories.this);
            }
        });
    }


    private void initview() {
        toolbar = findViewById(R.id.toolbar);


    }

    @Override
    public void onClick(View view) {


    }


}
