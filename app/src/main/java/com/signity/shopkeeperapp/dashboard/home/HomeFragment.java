package com.signity.shopkeeperapp.dashboard.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.signity.shopkeeperapp.R;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    TextView textViewNotificationCount;
    private int notificationCount = 12;

    public static HomeFragment getInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
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
        init(view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void init(View view) {

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_notification);

        View actionView = menuItem.getActionView();

        if (actionView == null) {
            return;
        }

        textViewNotificationCount = (TextView) actionView.findViewById(R.id.notification_count);

        setUpBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
    }

    private void setUpBadge() {
        if (textViewNotificationCount != null) {
            if (notificationCount == 0) {
                textViewNotificationCount.setVisibility(View.GONE);
            } else {
                textViewNotificationCount.setText(String.valueOf(Math.min(notificationCount, 99)));
                textViewNotificationCount.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_notification) {
            Toast.makeText(getContext(), "Notification", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
