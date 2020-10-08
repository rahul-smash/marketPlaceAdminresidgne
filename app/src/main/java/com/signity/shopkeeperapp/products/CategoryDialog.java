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
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;

import java.util.ArrayList;
import java.util.List;

public class CategoryDialog extends DialogFragment implements CategoryDialogAdapter.CategoryDialogListener {

    public static final String TAG = "CategoryDialog";
    public static final String CATEGORY_DATA = "CATEGORY_DATA";
    private ImageView imageViewClose;
    private RecyclerView recycleViewCategory;
    private List<GetCategoryData> categoryDataList = new ArrayList<>();
    private CategoryDialogAdapter categoryDialogAdapter;
    private SearchView searchViewCategory;
    private TextView textViewDialogTitle;
    private CategoryListener listener;

    public void setListenerCategory(CategoryListener listener) {
        this.listener = listener;
    }

    public static CategoryDialog getInstance(Bundle bundle) {
        CategoryDialog categoryDialog = new CategoryDialog();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddProductActivity) {
            listener = (CategoryListener) context;
        }
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

        textViewDialogTitle.setText("Choose Category");
    }

    private void filterCategory(String trim) {
        List<GetCategoryData> categoryDataListNew = new ArrayList<>();

        if (categoryDataList.size() == 0) {
            return;
        }

        for (GetCategoryData categoryData : categoryDataList) {
            if (categoryData.getTitle().toLowerCase().startsWith(trim.toLowerCase())) {
                categoryDataListNew.add(categoryData);
            }
        }
        categoryDialogAdapter.setCategoryDataList(categoryDataListNew);
    }

    private void setUpAdapter() {
        categoryDialogAdapter = new CategoryDialogAdapter(getContext(), categoryDataList);
        categoryDialogAdapter.setCategoryDataList(categoryDataList);
        categoryDialogAdapter.setListener(this);
        recycleViewCategory.setAdapter(categoryDialogAdapter);
        recycleViewCategory.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getExtra() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryDataList = bundle.getParcelableArrayList(CATEGORY_DATA);
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
    public void onSelectCategory(String categoryId, String categoryName) {
        if (listener != null) {
            listener.onSelectCategory(categoryId, categoryName);
        }
        dismiss();
    }

    public interface CategoryListener {
        void onSelectCategory(String categoryId, String categoryName);
    }
}
