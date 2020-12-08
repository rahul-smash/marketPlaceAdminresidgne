package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.market.videoCreative.VideoCreative;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ketan Tetry on 1/11/19.
 */
public class VideoRecycleAdapter extends RecyclerView.Adapter<VideoRecycleAdapter.ViewHolder> {

    private Context context;
    private VideoListener listener;
    private List<VideoCreative> videoCreativeList;

    public VideoRecycleAdapter(Context context) {
        this.context = context;
        this.videoCreativeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_video_creatives, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        final VideoCreative videoCreative = videoCreativeList.get(position);
        final String videoThumb = videoCreative.getVideoThumb();

        if (!TextUtils.isEmpty(videoThumb)) {
            Glide.with(context)
                    .load(videoThumb)
                    .placeholder(context.getResources().getDrawable(R.drawable.video_default_image))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolder.imageViewPlayButton.setVisibility(View.VISIBLE);
                            if (videoCreative.getIsShared()) {
                                viewHolder.textViewShared.setVisibility(View.VISIBLE);
                            }
                            return false;
                        }
                    })
                    .into(viewHolder.imageViewVideoThumb);
        } else {
            Bitmap bitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.video_default_image, 400, 300, false);
            viewHolder.imageViewVideoThumb.setImageBitmap(bitmap);
//            viewHolder.imageViewVideoThumb.setImageDrawable(context.getResources().getDrawable(R.drawable.video_default_image));
        }

        viewHolder.textViewVideoTitle.setText(videoCreative.getDescription());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickVideo(viewHolder.getAdapterPosition());
                }
            }
        });

        if (videoCreativeList.size() - 1 == position) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewHolder.constraintLayoutVideo.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, (int) Util.pxFromDp(context, 32));
            viewHolder.constraintLayoutVideo.setLayoutParams(layoutParams);
        }
    }

    public void setListener(VideoListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return videoCreativeList.size();
    }

    public List<VideoCreative> getVideoCreativeList() {
        return videoCreativeList;
    }

    public void setVideoCreativeList(List<VideoCreative> videoCreativeList) {
        this.videoCreativeList = videoCreativeList;
        notifyDataSetChanged();
    }

    public interface VideoListener {

        void onClickVideo(int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVideoTitle, textViewShared;
        ImageView imageViewVideoThumb, imageViewPlayButton;
        ConstraintLayout constraintLayoutVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVideoTitle = itemView.findViewById(R.id.tv_video_title);
            textViewShared = itemView.findViewById(R.id.tv_shared_option);
            imageViewVideoThumb = itemView.findViewById(R.id.iv_video);
            imageViewPlayButton = itemView.findViewById(R.id.btnPlay);
            constraintLayoutVideo = itemView.findViewById(R.id.const_video);
        }
    }
}
