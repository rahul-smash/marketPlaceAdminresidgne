package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.creatives.CreativeModel;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ketan Tetry on 1/11/19.
 */
public class CreativeRecycleAdapter extends RecyclerView.Adapter<CreativeRecycleAdapter.ViewHolder> {

    private final Constant.MarketMode marketMode;
    private Context context;
    private List<CreativeModel> creativeModelList;
    private CreativeListener listener;

    public CreativeRecycleAdapter(Context context, Constant.MarketMode marketMode) {
        this.context = context;
        this.creativeModelList = new ArrayList<>();
        this.marketMode = marketMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_creative_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        CreativeModel creativeModel = creativeModelList.get(position);

        viewHolder.textViewCreativeTag.setText(creativeModel.getTitle());

        if (creativeModel.getCreatives() != null && creativeModel.getCreatives().size() != 0) {
            CreativeRecycleImagesAdapter creativeRecycleImagesAdapter = new CreativeRecycleImagesAdapter(context, marketMode);
            creativeRecycleImagesAdapter.setCreativeList(creativeModel.getCreatives());
            creativeRecycleImagesAdapter.setListener(new CreativeRecycleImagesAdapter.CreativeListener() {
                @Override
                public void onClickCreative(int childPosition) {
                    if (listener != null) {
                        listener.onClickCreative(viewHolder.getAdapterPosition(), childPosition);
                    }
                }
            });

            viewHolder.recyclerView.setAdapter(creativeRecycleImagesAdapter);
            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        }

//        viewHolder.textViewViewShared.setVisibility(marketMode == AppConstants.MarketMode.FRAME ? View.GONE : View.VISIBLE);

        viewHolder.textViewViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickViewAll(viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.textViewViewShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickViewShared(viewHolder.getAdapterPosition());
                }
            }
        });

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewHolder.recyclerView.getLayoutParams();
        int value = position != creativeModelList.size() - 1 ? 0 : 32;
        layoutParams.setMargins(0, 0, 0, (int) Util.pxFromDp(context, value));
        viewHolder.recyclerView.setLayoutParams(layoutParams);

    }

    public void setListener(CreativeListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return creativeModelList.size();
    }

    public List<CreativeModel> getCreativeModelList() {
        return creativeModelList;
    }

    public void setCreativeModelList(List<CreativeModel> creativeModelList) {
        this.creativeModelList = creativeModelList;
        notifyDataSetChanged();
    }

    public interface CreativeListener {
        void onClickViewAll(int position);

        void onClickViewShared(int position);

        void onClickCreative(int mainPosition, int childPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCreativeTag, textViewViewAll, textViewViewShared;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCreativeTag = itemView.findViewById(R.id.tv_creative_tag);
            recyclerView = itemView.findViewById(R.id.rv_creative_main);
            textViewViewAll = itemView.findViewById(R.id.tv_view_all);
            textViewViewShared = itemView.findViewById(R.id.tv_shared);
        }
    }
}
