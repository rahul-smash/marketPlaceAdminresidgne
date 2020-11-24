package com.signity.shopkeeperapp.market;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;
import com.signity.shopkeeperapp.classes.FacebookManager;
import com.signity.shopkeeperapp.model.market.facebook.FacebookPageRequest;
import com.signity.shopkeeperapp.model.market.facebook.FacebookPageResponse;
import com.signity.shopkeeperapp.model.market.facebook.PageList;

import java.util.ArrayList;
import java.util.List;

/**
 * FeedbackDialog
 *
 * @blame Ketan Tetry
 */
public class FacebookPagesDialog extends BaseDialogFragment {
    public static final String TAG = FacebookPagesDialog.class.getSimpleName();

    private RecyclerView recyclerView;

    private ProgressBar pbLoading;

    private FacebookDialogAdapter facebookDialogAdapter;
    private List<PageList.DataBean> data = new ArrayList<>();
    private PageCallback listener;
    private FacebookManager facebookManager;

    public static FacebookPagesDialog getInstance(Bundle bundle) {
        FacebookPagesDialog dialog = new FacebookPagesDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_facebook_pages;
    }

    @Override
    protected void setUp() {

        facebookManager = new FacebookManager(getActivity());
        setUpAdapter();

        getFacebookPages();
    }

    private void getFacebookPages() {
        pbLoading.setVisibility(View.VISIBLE);
        facebookManager.getFacebookPages(new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {

                if (!isVisible()) {
                    return;
                }

                try {
                    pbLoading.setVisibility(View.GONE);

                    PageList pageList = new Gson().fromJson(response.getRawResponse(), PageList.class);

                    if (pageList != null) {
                        data = pageList.getData();
                    }

                    if (data != null && data.size() != 0) {
                        facebookDialogAdapter.setList(data);
                    } else {
                        if (listener != null) {
                            dismiss();
                            listener.onNoPageFound();
                            Toast.makeText(getBaseActivity(), "No facebook page found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpAdapter() {
        facebookDialogAdapter = new FacebookDialogAdapter(getContext());
        recyclerView.setAdapter(facebookDialogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void onClickClose() {
        dismissDialog(FacebookPagesDialog.TAG);
    }

    void doUpdateClick() {
        int selected = facebookDialogAdapter.getSelectedPosition();

        if (selected == -1) {
            Toast.makeText(getContext(), "Please select page", Toast.LENGTH_SHORT).show();
            return;
        }

        PageList.DataBean dataBean = data.get(selected);
        FacebookPageRequest request = new FacebookPageRequest();
//        request.setBrand(String.valueOf(AppPreferenceHelper.getInstance().getBrandId()));
        request.setPageId(dataBean.getId());
        request.setPageName(dataBean.getName());
        request.setPageAccessToken(dataBean.getAccess_token());


/*        AppApiHelper.getApiHelper()
                .updateFacebookPage(request)
                .enqueue(new DigiCallback<FacebookPageResponse>(getContext()) {
                    @Override
                    public void onSuccess(FacebookPageResponse response) {
                        if (response.getStatus()) {
                            AppPreferenceHelper.getInstance().setFacebookPage(response.getFacebookPage());
                        } else {
                            showMessage(response.getMessage());
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });*/

//        AppPreferenceHelper.getInstance().setFacebookPageId(dataBean.getId());
//        AppPreferenceHelper.getInstance().setFacebookPageAccessToken(dataBean.getAccess_token());
        onClickClose();
        if (listener != null) {
            listener.onPageSelected();
        }
    }

    public void setListener(PageCallback listener) {
        this.listener = listener;
    }

    public interface PageCallback {
        void onPageSelected();

        void onNoPageFound();
    }
}
