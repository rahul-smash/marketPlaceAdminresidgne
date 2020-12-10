package com.signity.shopkeeperapp.runner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.runner.RunnerDetail;

import java.util.ArrayList;
import java.util.List;

public class RunnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private RunnerAdapterListener listener;
    private boolean showLoading = true;
    private List<RunnerDetail> runnerList = new ArrayList<>();

    public RunnerAdapter(Context context) {
        this.context = context;
    }

    public List<RunnerDetail> getRunnerList() {
        return runnerList;
    }

    public void setRunnerList(List<RunnerDetail> dtoList) {
        this.runnerList = dtoList;
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.runnerList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.itemview_runner, parent, false);
            return new RunnerViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_loading, parent, false);
        return new ViewHolderLoading(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RunnerViewHolder) {
            ((RunnerViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        if (showLoading) {
            if (runnerList.isEmpty()) {
                return 1;
            }
        }
        return runnerList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < runnerList.size()) {
            return 1;
        }
        return 101;
    }

    public void setListener(RunnerAdapterListener listener) {
        this.listener = listener;
    }

    private void popmenu(ImageView view, final String id, final int position) {
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_runner_option, null, false);
        final PopupWindow popupWindowOverView = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindowOverView.setOutsideTouchable(true);
        popupWindowOverView.setBackgroundDrawable(new ColorDrawable());
        popupWindowOverView.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindowOverView.dismiss();
                    return true;
                }
                return false;
            }

        });

        float den = context.getResources().getDisplayMetrics().density;
        int offsetY = (int) (den * 8);
        popupWindowOverView.showAsDropDown(view, 0, -offsetY);

        LinearLayout edit = layout.findViewById(R.id.ll_share);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditRunner(id);
                }
                popupWindowOverView.dismiss();
            }
        });

        LinearLayout delete = layout.findViewById(R.id.ll_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeleteRunner(runnerList.get(position).getFullName(),runnerList.get(position).getActieOrder(),id, runnerList.get(position).getActieOrder() == 0);
                }
                popupWindowOverView.dismiss();
            }
        });
    }

    public interface RunnerAdapterListener {
        void onClickSwitch(String id, String status);

        void onDeleteRunner(String fullName, int actieOrder, String id, boolean canDelete);

        void onEditRunner(String id);

        void onViewRunnerDetail(String id);

        void onOpenWhatsapp(String phone);

        void onOpenCall(String phone);

        void onOpenMessage(String phone);
    }

    static class ViewHolderLoading extends RecyclerView.ViewHolder {

        public ViewHolderLoading(final View convertView) {
            super(convertView);
        }
    }

    class RunnerViewHolder extends RecyclerView.ViewHolder {

        Switch switchRunner;
        TextView textViewActiveCount, textViewRunnerStatus;
        TextView textViewCustomerName, textViewCustomerNumber, textViewCustomerCity, textViewRunnerEmail;
        LinearLayout linearLayoutArea, linearLayoutEmail;
        ImageView imageViewWhatsapp, imageViewCall, imageViewSms, imageViewMore;

        public RunnerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.tv_runner_name);
            textViewCustomerNumber = itemView.findViewById(R.id.tv_runner_number);
            textViewCustomerCity = itemView.findViewById(R.id.tv_runner_city);
            textViewRunnerEmail = itemView.findViewById(R.id.tv_runner_email);
            linearLayoutEmail = itemView.findViewById(R.id.ll_email);
            textViewActiveCount = itemView.findViewById(R.id.tv_active_count);
            textViewRunnerStatus = itemView.findViewById(R.id.tv_runner_status);
            linearLayoutArea = itemView.findViewById(R.id.ll_area);
            imageViewWhatsapp = itemView.findViewById(R.id.iv_whatsapp);
            imageViewCall = itemView.findViewById(R.id.iv_phone_call);
            imageViewSms = itemView.findViewById(R.id.iv_message);
            imageViewMore = itemView.findViewById(R.id.iv_more);
            switchRunner = itemView.findViewById(R.id.switch_runner);
        }

        public void bind(int positon) {

            final RunnerDetail runnerDetail = runnerList.get(positon);

            textViewCustomerName.setText(runnerDetail.getFullName());
            textViewCustomerNumber.setText(runnerDetail.getPhone());
            textViewActiveCount.setText(String.valueOf(runnerDetail.getActieOrder()));
            textViewRunnerStatus.setText(runnerDetail.getStatus().equals("1") ? "Enabled" : "Disabled");
            switchRunner.setChecked(runnerDetail.getStatus().equals("1"));

            if (!TextUtils.isEmpty(runnerDetail.getEmail())) {
                textViewRunnerEmail.setText(runnerDetail.getEmail());
            } else {
                linearLayoutEmail.setVisibility(View.GONE);
            }

            if (runnerDetail.getArea() != null && !runnerDetail.getArea().isEmpty()) {
                textViewCustomerCity.setText(runnerDetail.getArea().get(0).getName());
            } else {
                linearLayoutArea.setVisibility(View.GONE);
            }

            switchRunner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClickSwitch(runnerDetail.getId(), runnerDetail.getStatus().equals("1") ? "0" : "1");
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onViewRunnerDetail(runnerDetail.getId());
                    }
                }
            });

            imageViewWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onOpenWhatsapp(runnerDetail.getPhone());
                    }
                }
            });

            imageViewSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onOpenMessage(runnerDetail.getPhone());
                    }
                }
            });

            imageViewCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onOpenCall(runnerDetail.getPhone());
                    }
                }
            });

            imageViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popmenu(imageViewMore, runnerDetail.getId(), getAdapterPosition());
                }
            });

        }
    }
}