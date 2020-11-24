package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.signity.shopkeeperapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ketan Tetry on 6/11/19.
 */
public class FilterRecycleAdapter extends RecyclerView.Adapter<FilterRecycleAdapter.ViewHolder> {
    private static final String TAG = FilterRecycleAdapter.class.getSimpleName();
    private Context context;
    private List<String> allTags = new ArrayList<>();
    private List<String> choosenTags;

    public FilterRecycleAdapter(Context context) {
        this.context = context;
        choosenTags = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_filter_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String tag = allTags.get(position);

        holder.checkBox.setText(tag);

        holder.setIsRecyclable(false);

        for (String string : choosenTags){
            if (string.equals(tag)){
                holder.checkBox.setChecked(true);
            }
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    choosenTags.add(tag);
                } else {
                    choosenTags.remove(tag);
                }
            }
        });
    }

    public List<String> getIds() {
        return choosenTags;
    }

    @Override
    public int getItemCount() {
        return allTags.size();
    }

    public void setChoosen(ArrayList<String> choosenList) {
        choosenTags = choosenList;
        notifyDataSetChanged();
    }

    public void clearChoosen(){
        choosenTags.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_filter);
            checkBox = itemView.findViewById(R.id.cb_filter);
        }
    }

    public void setAllTags(List<String> allTags) {
        this.allTags = allTags;
    }
}
