package com.signity.shopkeeperapp.helpMedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.helpMediaModel.HelpVideos;

import java.util.ArrayList;
import java.util.List;

/**
 * YoutubeVideoAdapter
 *
 * @blame Android Team
 */
public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.ViewHolder> {
    private static final String TAG = YoutubeVideoAdapter.class.getSimpleName();
    private Context context;
    private List<HelpVideos> helpVideosArrayList;

    public YoutubeVideoAdapter(Context context) {
        this.context = context;
        helpVideosArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public YoutubeVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_youtube, parent, false);
        return new YoutubeVideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final YoutubeVideoAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.tvYoutubeDecs.setText(helpVideosArrayList.get(position).getTitle());

        viewHolder.setIsRecyclable(false);

        final String[] videoid = helpVideosArrayList.get(position).getVideoUrl().split("=");

        final String videoImage = helpVideosArrayList.get(position).getVideoImage();
        if (!TextUtils.isEmpty(videoImage)) {
            Glide.with(context)
                    .load(videoImage)
                    .placeholder(context.getResources().getDrawable(R.drawable.video_default_image))
                    .fitCenter()
                    .into(viewHolder.imageViewItem);
        }
        viewHolder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.imageViewItem.setVisibility(View.GONE);
                viewHolder.btnPlay.setVisibility(View.GONE);
                viewHolder.youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
                viewHolder.youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
                    @Override
                    public void onYouTubePlayer(YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(videoid[1], 0);
                    }
                });
            }
        });

        viewHolder.imgYoutubeShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, helpVideosArrayList.get(position).getVideoUrl());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Share");
                context.startActivity(shareIntent);
            }
        });

    }

    public void setHelpVideosArrayList(List<HelpVideos> helpVideosArrayList) {
        this.helpVideosArrayList = helpVideosArrayList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return helpVideosArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView;
        TextView tvYoutubeDecs;
        ImageView imageViewItem, btnPlay, imgYoutubeShare;

        public ViewHolder(View itemView) {
            super(itemView);

            tvYoutubeDecs = itemView.findViewById(R.id.tv_youtube_decs);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            imgYoutubeShare = itemView.findViewById(R.id.img_youtube_share);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            youTubePlayerView = itemView.findViewById(R.id.youtube_view);
        }
    }
}