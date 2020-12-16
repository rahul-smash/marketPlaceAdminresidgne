package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.runner.areas.AreaDTO;

import java.util.ArrayList;
import java.util.List;

public class AreaSelectionDialogAdapter extends RecyclerView.Adapter<AreaSelectionDialogAdapter.ViewHolder> {

    private Context context;
    private List<AreaDTO> dtoList = new ArrayList<>();
    private List<AreaDTO> selectedList = new ArrayList<>();

    public AreaSelectionDialogAdapter(Context context, List<AreaDTO> categoryDataList) {
        this.context = context;
        this.dtoList = categoryDataList;
    }

    public void setCategoryDataList(List<AreaDTO> categoryDataList) {
        this.dtoList = categoryDataList;
        notifyDataSetChanged();
    }

    public List<AreaDTO> getDtoList() {
        return dtoList;
    }

    @NonNull
    @Override
    public AreaSelectionDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_areas, parent, false);
        return new AreaSelectionDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AreaSelectionDialogAdapter.ViewHolder holder, final int position) {
        final AreaDTO areaDTO = dtoList.get(position);
        holder.checkBox.setText(areaDTO.getAreaName());
        holder.checkBox.setChecked(areaDTO.isChecked());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dtoList.get(position).setChecked(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dtoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_area);
        }
    }
}
