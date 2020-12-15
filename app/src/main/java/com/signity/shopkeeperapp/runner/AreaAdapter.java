package com.signity.shopkeeperapp.runner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.runner.AreaResponseData;

import java.util.ArrayList;
import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<AreaResponseData> areaList = new ArrayList<>();

    public AreaAdapter(Context context) {
        this.context = context;
    }

    public List<AreaResponseData> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaResponseData> areaList) {
        this.areaList = areaList;
        notifyDataSetChanged();
    }

    public List<String> getSelectedArea() {
        List<String> area = new ArrayList<>();
        for (AreaResponseData areaResponseData : areaList) {
            if (areaResponseData.isChecked()) {
                area.add(areaResponseData.getAreaId());
            }
        }
        return area;
    }

    public void clearData() {
        this.areaList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_areas, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AreaViewHolder) {
            ((AreaViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    class AreaViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_area);
        }

        public void bind(int positon) {
            final AreaResponseData areaResponseData = areaList.get(positon);
            checkBox.setText(areaResponseData.getArea());
            checkBox.setChecked(areaResponseData.isChecked());

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    areaResponseData.setChecked(!areaResponseData.isChecked());
                    notifyDataSetChanged();
                }
            });
        }
    }
}