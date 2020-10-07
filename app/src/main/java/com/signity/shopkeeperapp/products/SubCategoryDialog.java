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
import com.signity.shopkeeperapp.model.Categories.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryDialog extends DialogFragment implements SubCategoryDialogAdapter.SubCategoryDialogListener {

    public static final String TAG = "SubCategoryDialog";
    public static final String SUBCATEGORY_DATA = "SUBCATEGORY_DATA";
    private ImageView imageViewClose;
    private RecyclerView recycleViewCategory;
    private List<SubCategory> subCategoryList = new ArrayList<>();
    private SubCategoryDialogAdapter subCategoryDialogAdapter;
    private SearchView searchViewCategory;
    private TextView textViewDialogTitle;
    private SubCategoryListener listener;

    public static SubCategoryDialog getInstance(Bundle bundle) {
        SubCategoryDialog categoryDialog = new SubCategoryDialog();
        categoryDialog.setArguments(bundle);
        return categoryDialog;
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddProductActivity) {
            listener = (SubCategoryListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_category, container, true);
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

    private void setUpSearchView() {
        searchViewCategory.setIconifiedByDefault(false);
        searchViewCategory.setQueryHint("Search SubCategory");
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

        textViewDialogTitle.setText("Choose SubCategory");
    }

    private void filterCategory(String trim) {
        List<SubCategory> subCategoriesNew = new ArrayList<>();

        if (subCategoryList.size() == 0) {
            return;
        }

        for (SubCategory subCategory : subCategoryList) {
            if (subCategory.getTitle().toLowerCase().startsWith(trim.toLowerCase())) {
                subCategoriesNew.add(subCategory);
            }
        }
        subCategoryDialogAdapter.setSubCategoryList(subCategoriesNew);
    }

    private void setUpAdapter() {
        subCategoryDialogAdapter = new SubCategoryDialogAdapter(getContext(), subCategoryList);
        subCategoryDialogAdapter.setListener(this);
        recycleViewCategory.setAdapter(subCategoryDialogAdapter);
        recycleViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getExtra() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            subCategoryList = bundle.getParcelableArrayList(SUBCATEGORY_DATA);
        }
    }

    private void initView(View view) {
        recycleViewCategory = view.findViewById(R.id.rv_category);
        searchViewCategory = view.findViewById(R.id.search_view);
        textViewDialogTitle = view.findViewById(R.id.tv_dialog_title);
        imageViewClose = view.findViewById(R.id.iv_close);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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

    @Override
    public void onSelectSubCategory(String subCategoryId, String subCategoryName) {
        if (listener != null) {
            listener.onSelectSubCategory(subCategoryId, subCategoryName);
        }
        dismiss();
    }

    public interface SubCategoryListener {
        void onSelectSubCategory(String subCategoryId, String subCategoryName);
    }
}
