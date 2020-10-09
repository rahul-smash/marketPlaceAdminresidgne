package com.signity.shopkeeperapp.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by rajesh on 26/4/16.
 */
public class SpacesItemImageDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemImageDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = space;
        outRect.bottom = 0;
        outRect.top = 0;
    }
}