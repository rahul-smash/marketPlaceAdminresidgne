package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.market.facebook.PageList;

import java.util.ArrayList;
import java.util.List;

/**
 * FeedbackServiceDialogAdapter
 *
 * @blame Android Team
 */
public class FacebookDialogAdapter extends RecyclerView.Adapter<FacebookDialogAdapter.ViewHolder> {
    private static final String TAG = FacebookDialogAdapter.class.getSimpleName();
    private List<PageList.DataBean> list = new ArrayList<>();
    private Context context;
    private int selectedPosition = -1;


    public FacebookDialogAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FacebookDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_facebook_pages, parent, false);
        return new FacebookDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacebookDialogAdapter.ViewHolder viewHolder, final int position) {
        final PageList.DataBean dta = list.get(position);

        viewHolder.radioButtonPages.setText(dta.getName());

        viewHolder.radioButtonPages.setChecked(selectedPosition == position);

        viewHolder.radioButtonPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });

    }

    public void setList(List<PageList.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButtonPages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButtonPages = itemView.findViewById(R.id.rb_pages);
        }
    }
}
