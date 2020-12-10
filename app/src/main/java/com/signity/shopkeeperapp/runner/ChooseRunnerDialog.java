package com.signity.shopkeeperapp.runner;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseDialogFragment;
import com.signity.shopkeeperapp.model.runner.RunnerDetail;
import com.signity.shopkeeperapp.model.runner.RunnersResponseDTO;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Ketan Tetry on 10/12/19.
 */
public class ChooseRunnerDialog extends BaseDialogFragment {

    public static final String TAG = "PreviewDialog";
    public static final String RUNNER_ID = "RUNNER_ID";
    private ChooseRunnerDialogListener listener;
    private RecyclerView recyclerView;
    private ChooseRunnerAdapter chooseRunnerAdapter;
    private String runnerId;

    public static ChooseRunnerDialog getInstance(Bundle args) {
        ChooseRunnerDialog dialog = new ChooseRunnerDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public void setListener(ChooseRunnerDialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected int setLayout() {
        return R.layout.bottom_sheet_choose_runner;
    }

    @Override
    protected void setUp(View view) {
        getExtra();
        initViews(view);
        setUpAdapter();
        getRunners();
    }

    private void getExtra() {
        if (getArguments() != null) {
            runnerId = getArguments().getString(RUNNER_ID);
        }
    }

    public void getRunners() {
        NetworkAdaper.getNetworkServices().getRunners(new Callback<RunnersResponseDTO>() {
            @Override
            public void success(RunnersResponseDTO responseDTO, Response response) {

                if (!isAdded()) {
                    return;
                }

                if (responseDTO.isSuccess()) {
                    if (responseDTO.getData() != null) {
                        List<RunnerDetail> runnerDetailList = new ArrayList<>();
                        for (RunnerDetail runnerDetail : responseDTO.getData()) {
                            if (runnerDetail.getStatus().equals("1")) {
                                runnerDetailList.add(runnerDetail);
                            }
                        }
                        chooseRunnerAdapter.setCustomersList(runnerDetailList);
                    } else {
                        Toast.makeText(getContext(), "Data not Found!", Toast.LENGTH_SHORT).show();
                        chooseRunnerAdapter.setShowLoading(false);
                    }

                } else {
                    Toast.makeText(getContext(), "Data not Found!", Toast.LENGTH_SHORT).show();
                    chooseRunnerAdapter.setShowLoading(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
                    chooseRunnerAdapter.setShowLoading(false);
                }
            }
        });
    }


    private void setUpAdapter() {
        chooseRunnerAdapter = new ChooseRunnerAdapter(getContext());
        chooseRunnerAdapter.setListener(new ChooseRunnerAdapter.ChooseRunnerAdapterListener() {
            @Override
            public void onSelectRunner(String id) {
                if (listener != null) {
                    listener.onSelectRunner(id);
                }
                dismiss();
            }
        });
        chooseRunnerAdapter.setSelectedRunnerId(runnerId);
        recyclerView.setAdapter(chooseRunnerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(getContext(), 8)));
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_runner);

        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), this.getTheme());
    }

    public interface ChooseRunnerDialogListener {
        void onSelectRunner(String id);
    }
}
