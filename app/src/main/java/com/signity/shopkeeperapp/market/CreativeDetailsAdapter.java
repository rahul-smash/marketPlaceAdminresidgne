package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.creatives.Creative;
import com.signity.shopkeeperapp.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ketan Tetry on 1/11/19.
 */
public class CreativeDetailsAdapter extends RecyclerView.Adapter<CreativeDetailsAdapter.ViewHolder> {
    private static final String TAG = CreativeDetailsAdapter.class.getSimpleName();
    private final Constant.MarketMode marketMode;
    private Context context;
    private List<Creative> creativeList;
    private CreativeListener listener;

    public CreativeDetailsAdapter(Context context, Constant.MarketMode marketMode) {
        this.context = context;
        this.creativeList = new ArrayList<>();
        this.marketMode = marketMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_creative_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        final Creative creative = creativeList.get(position);
        final String imageUrl = creative.getImageThumbnail();
        final boolean isShared = creative.isShared();

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
        } else {
            viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
        }

        viewHolder.textViewCreativeTitle.setText(creative.getTitle());
        viewHolder.textViewDesc.setText(creative.getDescription());

        viewHolder.textViewFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickCreative(creative.getId());
                }
            }
        });

        viewHolder.linearLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickShare(viewHolder.getAdapterPosition());
                }
            }
        });

    }

    public void setCreativeList(List<Creative> creativeList) {
        this.creativeList = creativeList;
        notifyDataSetChanged();
    }

    public void setListener(CreativeListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return creativeList.size();
    }

    public interface CreativeListener {

        void onClickCreative(int id);

        void onClickShare(int position);

//        void onCheckEngagement(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCreativeTitle, textViewShare, textViewDesc, textViewFacebook, textViewShared;
        ImageView imageView, imageViewBorder, imageViewTop;
        ProgressBar progressBar;
        LinearLayout linearLayoutShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCreativeTitle = itemView.findViewById(R.id.tv_title);
            textViewDesc = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.iv_creative);
            textViewShare = itemView.findViewById(R.id.tv_share);
            imageViewBorder = itemView.findViewById(R.id.iv_creative_border);
            textViewFacebook = itemView.findViewById(R.id.tv_share_facebook);
            textViewShared = itemView.findViewById(R.id.tv_shared_option);
            progressBar = itemView.findViewById(R.id.progress_image);
            linearLayoutShare = itemView.findViewById(R.id.ll_share);
            imageViewTop = itemView.findViewById(R.id.iv_creative_image_top);
        }
    }
}
