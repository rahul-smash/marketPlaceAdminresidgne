package com.signity.shopkeeperapp.orders;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

/**
 * Created by rajesh on 28/9/15.
 */
public class DueOrderActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_due_order);
        Bundle bundle = getIntent().getExtras();
        Fragment fragment = OrderDetailFragment.newInstance(this);
        fragment.setArguments(bundle);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(DueOrderActivity.this);
    }

}
