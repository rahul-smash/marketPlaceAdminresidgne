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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ketan Tetry on 1/11/19.
 */
public class SharedPostAdapter extends RecyclerView.Adapter<SharedPostAdapter.ViewHolder> {
    private static final String TAG = SharedPostAdapter.class.getSimpleName();
    private CreativeListener listener;
    private Context context;
    private List<Creative> creativeList = new ArrayList<>();

    public SharedPostAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_creative_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        Creative creative = creativeList.get(position);
        final String imageUrl = creative.getImageThumbnail();
        final boolean isShared = creative.isShared();

        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(context.getResources().getDrawable(R.drawable.defaulticonimg))
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolder.imageViewBorder.setVisibility(isShared ? View.VISIBLE : View.GONE);
                            return false;
                        }
                    })
                    .into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.defaulticonimg));
        }

        viewHolder.textViewCreativeTitle.setText(creative.getTitle());
        viewHolder.textViewDesc.setText(creative.getDescription());

        viewHolder.textViewEngage.setVisibility(View.GONE);

        if (creative.getTags() != null && creative.getTags().size() != 0) {
            viewHolder.textViewTag.setVisibility(View.VISIBLE);
            viewHolder.textViewTag.setText(creative.getTags().get(0).getTitle());
        } else {
            viewHolder.textViewTag.setVisibility(View.GONE);
        }

        viewHolder.linearLayoutEngagement.setVisibility(TextUtils.isEmpty(creative.getPostId()) ? View.GONE : View.VISIBLE);

        viewHolder.textViewLikeCount.setText(TextUtils.isEmpty(creative.getPostId()) ? "NA" : String.valueOf(creative.getLikeCount()));
        viewHolder.textViewShareCount.setText(TextUtils.isEmpty(creative.getPostId()) ? "NA" : String.valueOf(creative.getShareCount()));
        viewHolder.textViewCommentCount.setText(TextUtils.isEmpty(creative.getPostId()) ? "NA" : String.valueOf(creative.getCommentCount()));

        viewHolder.linearLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickShare(viewHolder.getAdapterPosition());
                }
            }
        });

    }

    public List<Creative> getCreativeList() {
        return creativeList;
    }

    public void setCreativeList(List<Creative> creativeList) {
        this.creativeList = creativeList;
        notifyDataSetChanged();
    }

    public void addCreativeData(List<Creative> creativeList) {
        this.creativeList.addAll(creativeList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return creativeList.size();
    }

    public void setListener(CreativeListener listener) {
        this.listener = listener;
    }

    public interface CreativeListener {
        void onClickShare(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCreativeTitle, textViewShare, textViewDesc, textViewEngage, textViewLikeCount, textViewCommentCount, textViewShareCount, textViewTag;
        ImageView imageView, imageViewBorder, imageViewTop;
        ProgressBar progressBar;
        LinearLayout linearLayoutShare, linearLayoutEngagement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCreativeTitle = itemView.findViewById(R.id.tv_title);
            textViewDesc = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.iv_creative);
            textViewShare = itemView.findViewById(R.id.tv_share);
            imageViewBorder = itemView.findViewById(R.id.iv_creative_border);
            textViewEngage = itemView.findViewById(R.id.tv_engagement);
            progressBar = itemView.findViewById(R.id.progress_image);
            linearLayoutShare = itemView.findViewById(R.id.ll_share);
            imageViewTop = itemView.findViewById(R.id.iv_creative_image_top);
            linearLayoutEngagement = itemView.findViewById(R.id.ll_engagement);
            textViewLikeCount = itemView.findViewById(R.id.tv_likes);
            textViewShareCount = itemView.findViewById(R.id.tv_shares);
            textViewCommentCount = itemView.findViewById(R.id.tv_comments);
            textViewTag = itemView.findViewById(R.id.tv_tag);
        }
    }
}
