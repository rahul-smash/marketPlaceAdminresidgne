package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.image.MessageResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private Context context;
    private List<MessageResponse> imageList = new ArrayList<>();

    public ImagesAdapter(Context context) {
        this.context = context;
    }

    public List<MessageResponse> getImageList() {
        return imageList;
    }

    public void setImageList(List<MessageResponse> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    public void addImage(MessageResponse imageUrl) {
        imageList.add(imageUrl);
        notifyItemInserted(imageList.size() - 1);
    }

    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_image, parent, false);
        return new ImagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_product_image);
        }

        public void bind(int positon) {

            MessageResponse messageResponse = imageList.get(positon);
            String imageUrl = messageResponse.getImageUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(ResourcesCompat.getDrawable(context.getResources(), R.drawable.addimageicon, null))
                        .into(imageView);
            }
        }
    }
}
