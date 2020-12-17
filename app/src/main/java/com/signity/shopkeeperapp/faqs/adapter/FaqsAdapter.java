package com.signity.shopkeeperapp.faqs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.faq.FaqModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ketan Tetry on 31/10/19.
 */
public class FaqsAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "FaqsAdapter";
    private Context context;
    private List<FaqModel> faqList = new ArrayList<>();

    public FaqsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return faqList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return faqList.get(groupPosition).getQuestion();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return faqList.get(groupPosition).getAnswer();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String question = (String) getGroup(groupPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.itemview_faqs_question, parent, false);

        TextView textViewQuestion = convertView.findViewById(R.id.tv_faqs_que);
        textViewQuestion.setText(question);

        ImageView imageViewArrow = convertView.findViewById(R.id.iv_faqs_arrow);

        if (isExpanded) {
            textViewQuestion.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            imageViewArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.upward));
        } else {
            textViewQuestion.setTextColor(context.getResources().getColor(R.color.colorTextDark));
            imageViewArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.downward));
        }

        if (groupPosition == 0) {
            View view = convertView.findViewById(R.id.view_faqs);
            view.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String answer = (String) getChild(groupPosition, childPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.itemview_faqs_answer, parent, false);

        TextView textviewAnswer = convertView.findViewById(R.id.tv_faqs_ans);
        textviewAnswer.setText(answer);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setFaqList(List<FaqModel> faqList) {
        this.faqList = faqList;
        notifyDataSetChanged();
    }
}
