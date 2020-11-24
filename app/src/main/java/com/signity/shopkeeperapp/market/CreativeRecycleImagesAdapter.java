package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.creatives.Creative;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ketan Tetry on 1/11/19.
 */
public class CreativeRecycleImagesAdapter extends RecyclerView.Adapter<CreativeRecycleImagesAdapter.ViewHolder> {
    private static final String TAG = CreativeRecycleImagesAdapter.class.getSimpleName();
    private final Constant.MarketMode marketMode;
    private Context context;
    private List<Creative> creativeList;
    private CreativeListener listener;

    public CreativeRecycleImagesAdapter(Context context, Constant.MarketMode marketMode) {
        this.context = context;
        this.creativeList = new ArrayList<>();
        this.marketMode = marketMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_creative, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        Creative creative = creativeList.get(position);
        final boolean isShared = creative.isShared();

        final String imageUrl = creative.getImageThumbnail();
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(context.getResources().getDrawable(R.mipmap.ic_launcher))
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolder.imageViewBorder.setVisibility(isShared ? View.VISIBLE : View.GONE);
                            viewHolder.textViewShared.setVisibility(isShared ? View.VISIBLE : View.GONE);
                            switch (marketMode) {
                                case CREATIVE:
                                    viewHolder.imageViewTop.setVisibility(View.GONE);
                                    break;
                                case FRAME:
                                    viewHolder.imageViewTop.setVisibility(View.VISIBLE);
                                    break;
                            }
                            return false;
                        }
                    })
                    .into(viewHolder.imageView);
        }

        viewHolder.textViewCreativeTitle.setText(creative.getTitle());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickCreative(viewHolder.getAdapterPosition());
                }
            }
        });

        if (position == 0) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewHolder.cardView.getLayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginStart((int) Util.pxFromDp(context, 16));
            }
            viewHolder.cardView.setLayoutParams(layoutParams);
        }

        if (position == creativeList.size() - 1) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewHolder.cardView.getLayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.setMarginEnd((int) Util.pxFromDp(context, 16));
            }
            viewHolder.cardView.setLayoutParams(layoutParams);
        }
    }

    public void setListener(CreativeListener listener) {
        this.listener = listener;
    }

    public void setCreativeList(List<Creative> creativeList) {
        this.creativeList = creativeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return creativeList.size();
    }

    public interface CreativeListener {

        void onClickCreative(int childPosition);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCreativeTitle, textViewShared;
        ImageView imageView, imageViewBorder, imageViewTop;
        ProgressBar progressBar;
        MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCreativeTitle = itemView.findViewById(R.id.tv_creative_title);
            imageView = itemView.findViewById(R.id.iv_creative_image);
            imageViewTop = itemView.findViewById(R.id.iv_creative_image_top);
            imageViewBorder = itemView.findViewById(R.id.iv_creative_border);
            cardView = itemView.findViewById(R.id.rl_content1);
            textViewShared = itemView.findViewById(R.id.tv_shared_option);
            progressBar = itemView.findViewById(R.id.progress_image);
        }
    }
}
