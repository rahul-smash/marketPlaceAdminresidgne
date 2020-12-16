package com.signity.shopkeeperapp.products;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.runner.areas.AreaDTO;

import java.util.ArrayList;
import java.util.List;

public class AreaSelectionDialog extends DialogFragment {

    public static final String TAG = "AreaSelectionDialog";
    public static final String AREA_DATA = "AREA_DATA";
    public static final String CITY_NAME = "CITY_NAME";
    private ImageView imageViewClose;
    private RecyclerView recycleViewCategory;
    private List<AreaDTO> areaDataList = new ArrayList<>();
    private AreaSelectionDialogAdapter dialogAdapter;
    private SearchView searchViewCategory;
    private TextView textViewDialogTitle;
    private AreaSelectionListener listener;
    private String cityName;

    public static AreaSelectionDialog getInstance(Bundle bundle) {
        AreaSelectionDialog categoryDialog = new AreaSelectionDialog();
        categoryDialog.setArguments(bundle);
        return categoryDialog;
    }

    public void setListenerCategory(AreaSelectionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_area_selection, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getExtra();
        setUpAdapter();
        setUpSearchView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void setUpSearchView() {
        searchViewCategory.setIconifiedByDefault(false);
        searchViewCategory.setQueryHint("Search Category");
        searchViewCategory.setFocusable(false);
        searchViewCategory.setIconified(false);
        searchViewCategory.clearFocus();

        searchViewCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCategory(newText.trim());
                return false;
            }
        });

        textViewDialogTitle.setText(cityName);
    }

    private void filterCategory(String trim) {
        List<AreaDTO> categoryDataListNew = new ArrayList<>();

        if (areaDataList.size() == 0) {
            return;
        }

        for (AreaDTO categoryData : areaDataList) {
            if (categoryData.getAreaName().toLowerCase().startsWith(trim.toLowerCase())) {
                categoryDataListNew.add(categoryData);
            }
        }
        dialogAdapter.setCategoryDataList(categoryDataListNew);
    }

    private void setUpAdapter() {
        dialogAdapter = new AreaSelectionDialogAdapter(getContext(), areaDataList);
        recycleViewCategory.setAdapter(dialogAdapter);
        recycleViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getExtra() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            areaDataList = bundle.getParcelableArrayList(AREA_DATA);
            cityName = bundle.getString(CITY_NAME);
        }
    }

    private void initView(View view) {
        recycleViewCategory = view.findViewById(R.id.rv_category);
        searchViewCategory = view.findViewById(R.id.search_view);
        textViewDialogTitle = view.findViewById(R.id.tv_city);
        imageViewClose = view.findViewById(R.id.iv_close);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        view.findViewById(R.id.ll_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    List<AreaDTO> areaDTOS = new ArrayList<>();
                    for (AreaDTO dto : dialogAdapter.getDtoList()) {
                        if (dto.isChecked()) {
                            areaDTOS.add(dto);
                        }
                    }

                    if (areaDTOS.isEmpty()) {
                        Toast.makeText(getContext(), "Please add area", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    listener.onSubmit(areaDTOS);
                }
            }
        });
    }

    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
        if (prevFragment != null) {
            transaction.remove(prevFragment);
        }
        transaction.addToBackStack(null);
        show(transaction, tag);
    }

    public interface AreaSelectionListener {
        void onSubmit(List<AreaDTO> selectedAreas);
    }
}
